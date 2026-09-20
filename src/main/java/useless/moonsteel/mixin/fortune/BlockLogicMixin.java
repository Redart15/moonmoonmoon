package useless.moonsteel.mixin.fortune;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.moonsteel.MoonSteel;
import useless.moonsteel.block.MoonSteelBlocks;
import useless.moonsteel.item.MoonSteelItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = BlockLogic.class, remap = false)
public abstract class BlockLogicMixin {

	@Shadow
	@Final
	@NotNull
	public Block<?> block;

	@Shadow
	public abstract void dropWithCause(World world, EnumDropCause cause, TilePosc tilePosc, int meta, TileEntity tileEntity, Player player);

	@Inject(method = "onHarvest",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogic;dropWithCause(Lnet/minecraft/core/world/World;Lnet/minecraft/core/enums/EnumDropCause;Lnet/minecraft/core/world/pos/TilePosc;ILnet/minecraft/core/block/entity/TileEntity;Lnet/minecraft/core/entity/player/Player;)V")
	)
	private void multiplyHarvest(
		World world, Player entityplayer,
		TilePosc tilePos, int data, TileEntity tileEntity,
		CallbackInfo ci,
		@Share("player") LocalRef<Player> sharedPlayer
	) {
		sharedPlayer.set(entityplayer);
		final ItemStack heldItemStack = entityplayer.inventory.getCurrentItem();
		if (heldItemStack != null && heldItemStack.getItem() instanceof ItemTool itemTool && itemTool.getMaterial() == MoonSteelItems.MOON_STEEL_TOOL && MoonSteelBlocks.canBeFortuned(this.block)) {
			for (int i = 0; i < world.rand.nextInt(MoonSteel.FORTUNE_AMOUNT); i++) {
				dropWithCause(world, EnumDropCause.PROPER_TOOL, tilePos, data, tileEntity, entityplayer);
			}
		}
	}

//	@WrapMethod(method = "getBreakResult(Lnet/minecraft/core/world/World;Lnet/minecraft/core/enums/EnumDropCause;Lnet/minecraft/core/world/pos/TilePosc;ILnet/minecraft/core/block/entity/TileEntity;)[Lnet/minecraft/core/item/ItemStack;")
//	private ItemStack[] multiplyBreakResults(
//		World world, EnumDropCause dropCause,
//		TilePosc tilePos, int data,
//		TileEntity tileEntity, Operation<ItemStack[]> original,
//		@Share("player") LocalRef<Player> sharedPlayer
//	) {
//		Player player = sharedPlayer.get();
//		ItemStack[] breakResults = original.call(world, dropCause, tilePos, data, tileEntity);
//		if(player != null){
//			final ItemStack heldItemStack = player.inventory.getCurrentItem();
//			if (heldItemStack != null
//				&& heldItemStack.getItem() instanceof ItemTool itemTool
//				&& itemTool.getMaterial() == MoonSteelItems.MOON_STEEL_TOOL
//				&& MoonSteelBlocks.canBeFortuned(this.block)
//			) {
//				List<ItemStack> extraStacks = new ArrayList<>();
//				for (ItemStack stack : breakResults) {
//					int extra = world.rand.nextInt(MoonSteel.FORTUNE_AMOUNT);
//					if (stack.stackSize + extra <= stack.getMaxStackSize()) {
//						stack.stackSize += extra;
//					} else {
//						int remaining = stack.stackSize + extra - stack.getMaxStackSize();
//						stack.stackSize = stack.getMaxStackSize();
//						ItemStack extraStack = stack.copy();
//						extraStack.stackSize = remaining;
//						extraStacks.add(extraStack);
//					}
//				}
//				ItemStack[] returnValue = Arrays.copyOf(breakResults, breakResults.length + extraStacks.size());
//				for (int i = 0; i < extraStacks.size(); i++) {
//					returnValue[breakResults.length + i] = extraStacks.get(i);
//				}
//				return returnValue;
//			}
//		}
//		return breakResults;
//	}


}
