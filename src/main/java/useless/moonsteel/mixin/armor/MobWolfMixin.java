package useless.moonsteel.mixin.armor;

import net.minecraft.core.entity.animal.MobWolf;
import net.minecraft.core.item.IArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import useless.moonsteel.interfaces.IMoonGrav;

import static useless.moonsteel.item.MoonSteelItems.ARMOR_WOLF_MOONSTEEL;
import static useless.moonsteel.item.MoonSteelItems.MOON_STEEL_ARMOR;

@Mixin(MobWolf.class)
public class MobWolfMixin implements IMoonGrav {

	static {
		MobWolf.ARMOR_MATERIALS.put(MOON_STEEL_ARMOR, (IArmorItem) ARMOR_WOLF_MOONSTEEL);
	}

	@Override
	public double moonsteel$getGravScalar() {
		return 0.5d;
	}
}
