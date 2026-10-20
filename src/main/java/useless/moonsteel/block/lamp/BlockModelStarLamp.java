package useless.moonsteel.block.lamp;

import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicPressurePlate;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.data.block.BlockModelData;

public class BlockModelStarLamp<T extends BlockLogic> extends BlockModelGeneric<T> {

	public BlockModelStarLamp(@NotNull Block<T> block, @NotNull BlockModelData staticModel) {
		super(block, staticModel);
	}

	@Override
	public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
		int blockData = worldSource.getBlockData(tilePos);
		Side side = BlockLogicStarLamp.sideFromMeta(blockData);
		int add = BlockLogicStarLamp.isFlipped(blockData) ? 1 : 0;
		boolean result = false;
		switch (side) {
			case BOTTOM -> 	result = this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 1, 	add, 			0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
			case TOP 	-> 	result = this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 3, 	add, 			0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
			case NORTH 	-> 	result = this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 0, 	2, 				add, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
			case SOUTH 	-> 	result = this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 0, 	0, 				add, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
			case WEST 	-> 	result = this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 3, 	(add + 1) & 3, 	1, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
			default 	-> 	result = this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 1, 	(add + 1) & 3, 	1, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
		}
		return result;
	}

}
