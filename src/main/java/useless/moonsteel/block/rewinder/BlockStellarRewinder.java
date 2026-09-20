package useless.moonsteel.block.rewinder;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import useless.moonsteel.item.MoonSteelItems;

import java.util.Random;

public class BlockStellarRewinder extends BlockLogicRotatable {
	private static final Random random = new Random();

	//Uses BlockTileEntityRotatable for its rotation properties not because its a tileEntity
	public BlockStellarRewinder(Block<?> block, Material material) {
		super(block, material);
		block.withEntity(TileEntityStellarRewinder::new);
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		ItemStack heldItem = player.getHeldItem();
		TileEntity tileEntity = world.getTileEntity(tilePos);
		if(tileEntity instanceof TileEntityStellarRewinder rewinder
			&& heldItem != null
			&& heldItem.getItem() == MoonSteelItems.STAR_CONNECTED
		){
				rewinder.linkStar(heldItem);
				world.playSoundAtEntity(null, player, "ui.ui_click", 5, 1f + random.nextFloat() * 0.1f);
				return true;
			}
		return false;
	}

	@Override
	public int getPistonPushReaction(@NotNull World world, @NotNull TilePosc tilePos) {
		return 2;
	}
}
