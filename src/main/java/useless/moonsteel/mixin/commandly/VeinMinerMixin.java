package useless.moonsteel.mixin.commandly;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import redart15.commandly.veincapitator.VeinMining;
import useless.moonsteel.MoonSteel;
import useless.moonsteel.block.MoonSteelBlocks;
import useless.moonsteel.item.MoonSteelItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(VeinMining.class)
public abstract class VeinMinerMixin {

	@WrapOperation(method = "breakBlock", at = @At(value = "INVOKE", target = "Lredart15/commandly/veincapitator/VeinMining;getBreakResult(Lnet/minecraft/core/block/Block;Lnet/minecraft/core/world/World;Lnet/minecraft/core/enums/EnumDropCause;Lnet/minecraft/core/world/pos/TilePosc;ILnet/minecraft/core/block/entity/TileEntity;)[Lnet/minecraft/core/item/ItemStack;"))
	private ItemStack[] withItemClumping(
		VeinMining veinmining,
		@NotNull Block<?> block,
		World world,
		EnumDropCause dropCause,
		TilePosc tilePos, int meta,
		TileEntity tileEntity, Operation<ItemStack[]> original
	) {
		ItemStack heldItemStack = ((VeinMiningAccessor) veinmining).getTool();
		ItemStack[] result = original.call(veinmining, block, world, dropCause, tilePos, meta, tileEntity);
		if (heldItemStack != null
			&& heldItemStack.getItem() instanceof ItemTool itemTool
			&& itemTool.getMaterial() == MoonSteelItems.MOON_STEEL_TOOL
			&& MoonSteelBlocks.canBeFortuned(block)
		) {
			List<ItemStack> drops = new ArrayList<>(Arrays.asList(result));
			for (int i = 0; i < world.rand.nextInt(MoonSteel.FORTUNE_AMOUNT); i++) {
				drops.addAll(Arrays.asList(original.call(veinmining, block, world, dropCause, tilePos, meta, tileEntity)));
			}
			return drops.toArray(ItemStack[]::new);
		}
		return result;
	}



	@WrapOperation(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;dropWithCause(Lnet/minecraft/core/world/World;Lnet/minecraft/core/enums/EnumDropCause;Lnet/minecraft/core/world/pos/TilePosc;ILnet/minecraft/core/block/entity/TileEntity;Lnet/minecraft/core/entity/player/Player;)V"))
	private void withOutItemClumping(
		Block<?> block,
		World world,
		EnumDropCause dropCause,
		TilePosc tilePos, int data,
		TileEntity tileEntity,
		Player player, Operation<Void> original
	){
		original.call(block, world, dropCause, tilePos, data, tileEntity, player);
		ItemStack heldItemStack = ((VeinMiningAccessor)this).getTool();
		if (heldItemStack != null
			&& heldItemStack.getItem() instanceof ItemTool itemTool
			&& itemTool.getMaterial() == MoonSteelItems.MOON_STEEL_TOOL
			&& MoonSteelBlocks.canBeFortuned(block)
		) {
			for (int i = 0; i < world.rand.nextInt(MoonSteel.FORTUNE_AMOUNT); i++) {
				block.dropWithCause(world, EnumDropCause.PROPER_TOOL, tilePos, data, tileEntity, player);
			}
		}
	}
}
