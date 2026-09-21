package useless.moonsteel.api;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import useless.moonsteel.MoonSteelConstants;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MoonSteelMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {
		/* not need */
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.startsWith("useless.moonsteel.mixin.backpack")) {
			return MoonSteelConstants.BACKPACKS.getAsBoolean();
		}
		if (mixinClassName.startsWith("useless.moonsteel.mixin.commandly")) {
			return MoonSteelConstants.COMMANDLY.getAsBoolean();
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
		/* not need */
	}

	@Override
	public List<String> getMixins() {
		return Collections.emptyList();
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		/* not need */
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		/* not need */
	}
}
