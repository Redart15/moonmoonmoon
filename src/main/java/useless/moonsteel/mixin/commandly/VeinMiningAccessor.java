package useless.moonsteel.mixin.commandly;

import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import redart15.commandly.veincapitator.VeinMining;

@Mixin(VeinMining.class)
public interface VeinMiningAccessor {
	@Accessor
	ItemStack getTool();
}
