package useless.moonsteel.block.jar;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.support.ISupport;
import net.minecraft.core.block.support.ISupportable;
import net.minecraft.core.block.support.PartialSupport;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.animal.MobFireflyCluster;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.function.Supplier;

public class BlockLogicLantern extends BlockLogic implements ISupportable {
	public static final int MASK_HANGING = 1;
	public static final int MASK_ANGLED = 2;
	private final Supplier<Item> itemSupplier;

	public BlockLogicLantern(@NotNull Block<?> block, @NotNull Supplier<Item> itemSupplier) {
		super(block, Materials.GLASS);
		this.itemSupplier = itemSupplier;
		this.setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.5F, 0.6875F);
	}

	@Override
	public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		int metadata = source.getBlockData(tilePos);
		float offset = 0.0F;
		if ((metadata & 1) != 0) {
			offset = 0.3875F;
		}

		return new AABBd(0.3125F, offset, 0.3125F, 0.6875F, 0.5F + offset, 0.6875F);
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}

	@Override
	public @NotNull ISupport getSupport(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		return PartialSupport.INSTANCE;
	}


	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		world.setBlockTypeRaw(tilePos, Blocks.AIR);
		world.playSoundAtEntity(player, player, "item.pickup", 1.0F, 1.0F);
		if (!world.isClientSide) {
			world.dropItem(tilePos, new ItemStack(this.itemSupplier.get(), 1, 0));
		}

		world.markBlockDirty(tilePos);
		return true;
	}

	@Override
	public void onPlacedOnSide(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		if (side != Side.BOTTOM && (side == Side.TOP || yHit < 0.5F) || !this.isSupported(world, tilePos, Side.TOP) && !world.getBlockType(tilePos.up(new TilePos())).hasTag(BlockTags.CAN_HANG_OFF)) {
			if ((side == Side.TOP || side != Side.BOTTOM && yHit < 0.5F) && this.isSupported(world, tilePos, Side.BOTTOM)) {
				world.setBlockDataNotify(tilePos, 0);
			} else {
				if (!this.isSupported(world, tilePos, Side.TOP) && !world.getBlockType(tilePos.up(new TilePos())).hasTag(BlockTags.CAN_HANG_OFF)) {
					world.setBlockDataNotify(tilePos, 0);
				} else {
					world.setBlockDataNotify(tilePos, MASK_HANGING);
				}

			}
		} else {
			world.setBlockDataNotify(tilePos, MASK_HANGING);
		}
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		return this.isSupported(world, tilePos, Side.TOP) || world.getBlockType(tilePos.up(new TilePos())).hasTag(BlockTags.CAN_HANG_OFF) || this.isSupported(world, tilePos, Side.BOTTOM);
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		if (!this.canStay(world, tilePos)) {
			this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
			world.setBlockTypeNotify(tilePos, Blocks.AIR);
		}

	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data, @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case PICK_BLOCK, SILK_TOUCH, WORLD -> new ItemStack[]{new ItemStack(this.itemSupplier.get())};
			default -> null;
		};
	}

	@Override
	public boolean canStay(@NotNull World world, @NotNull TilePosc tilePos) {
		int meta = world.getBlockData(tilePos);
		if ((meta & 1) == 0) {
			return this.isSupported(world, tilePos, Side.BOTTOM);
		} else {
			return this.isSupported(world, tilePos, Side.TOP) || world.getBlockType(tilePos.up(new TilePos())).hasTag(BlockTags.CAN_HANG_OFF);
		}
	}

	public int getPistonPushReaction(@NotNull World world, @NotNull TilePosc tilePos) {
		return 1;
	}

	public @NotNull ISupport getSupportConstraint(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		return side.isVertical() ? PartialSupport.INSTANCE.center() : PartialSupport.INSTANCE;
	}
}
