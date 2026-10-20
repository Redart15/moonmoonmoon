package useless.moonsteel.block.lamp;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.support.ISupport;
import net.minecraft.core.block.support.ISupportable;
import net.minecraft.core.block.support.PartialSupport;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.PackedField;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import static net.minecraft.core.item.tag.ItemTags.PREVENT_LEFT_CLICK_INTERACTIONS;

public class BlockLogicStarLamp extends BlockLogic implements ISupportable {
	public static final PackedField FLIP = new PackedField(4, 1);
	public static final float MIN_LENGTH = 0.0F;
	public static final float MAX_LENGTH = 1.0F;
	public static final float THINNESS = 2.0f / 16.0f;
	public static final float HEIGHT = 5.0f / 16.0F;
	public static final float START = 5.5F / 16.0F;

	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;

	public BlockLogicStarLamp(@NotNull Block<?> block, @NotNull Material material) {
		super(block, material);
	}

	public static boolean isFlipped(int blockData) {
		return FLIP.get(blockData) == 1;
	}

	@Override
	public void onPlacedOnSide(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		int meta = world.getBlockData(tilePos);
		side = side.opposite();
		if (!this.isSupported(world, tilePos, side)) {
			side = this.getDefaultSide(world, tilePos);
		}

		if (side != Side.NONE) {
			world.setBlockData(tilePos, setSide(meta, side));
		} else {
			this.dropWithCause(world, EnumDropCause.WORLD, tilePos, meta, null, null);
			world.setBlockTypeNotify(tilePos, Blocks.AIR);
		}
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (player.getHeldItem() == null) {
			int blockData = world.getBlockData(tilePos);
			return world.setBlockDataNotify(tilePos, FLIP.set(blockData, FLIP.get(blockData) == 1 ? 0 : 1));
		}
		return false;

	}

	@Override
	public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		int meta = source.getBlockData(tilePos);
		boolean flipped = BlockLogicStarLamp.isFlipped(meta);
		return switch (sideFromMeta(meta)) {
			case BOTTOM -> BlockLogicStarLamp.plate(Y, false, flipped);
			case TOP    -> BlockLogicStarLamp.plate(Y, true,  flipped);
			case NORTH  -> BlockLogicStarLamp.plate(Z, false, flipped);
			case SOUTH  -> BlockLogicStarLamp.plate(Z, true,  flipped);
			case WEST   -> BlockLogicStarLamp.plate(X, false, flipped);
			default     -> BlockLogicStarLamp.plate(X, true,  flipped);
		};
	}

	/**
	 * @param thinAxis axis along which the plate is thin (X, Y or Z)
	 * @param highSide true if the plate sits at the max end of that axis
	 * @param flipped  swaps which of the two remaining axes is full length
	 */
	private static AABBd plate(int thinAxis, boolean highSide, boolean flipped) {
		int full;
		int part;
		switch (thinAxis) {
			case X  -> { full = Z; part = Y; }
			case Y  -> { full = X; part = Z; }
			default -> { full = X; part = Y; } // Z
		}
		if (flipped) {
			int tmp = full; full = part; part = tmp;
		}
		double[] min = new double[3];
		double[] max = new double[3];
		min[thinAxis] = highSide ? MAX_LENGTH - THINNESS : MIN_LENGTH;
		max[thinAxis] = highSide ? MAX_LENGTH : THINNESS;
		min[full] = MIN_LENGTH;
		max[full] = MAX_LENGTH;
		min[part] = START;
		max[part] = START + HEIGHT;
		return new AABBd(min[X], min[Y], min[Z], max[X], max[Y], max[Z]);
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		if (!this.isSupported(world, tilePos, sideFromMeta(world.getBlockData(tilePos)))) {
			this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
			world.setBlockTypeNotify(tilePos, Blocks.AIR);
		}

	}

	public static final PackedField SIDES = new PackedField(0, 3);
	public static int setSide(int meta, @NotNull Side side) {
		return SIDES.set(meta, side.id);
	}

	public static @NotNull Side sideFromMeta(int meta) {
		return Side.fromId(SIDES.get(meta));
	}

	private @NotNull Side getDefaultSide(@NotNull World world, @NotNull TilePosc tilePos) {
		for (int i = 0; i < Side.sides.length; ++i) {
			Side side = Side.sides[i];
			if (this.isSupported(world, tilePos, side)) {
				return side;
			}
		}
		return Side.NONE;
	}

	@Override
	public int getPistonPushReaction(@NotNull World world, @NotNull TilePosc tilePos) {
		return 1;
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		return this.isSupported(world, tilePos, Side.BOTTOM)
			|| this.isSupported(world, tilePos, Side.TOP)
			|| this.isSupported(world, tilePos, Side.NORTH)
			|| this.isSupported(world, tilePos, Side.SOUTH)
			|| this.isSupported(world, tilePos, Side.WEST)
			|| this.isSupported(world, tilePos, Side.EAST);
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
	public @NotNull ISupport getSupportConstraint(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		return PartialSupport.INSTANCE.center();
	}

}
