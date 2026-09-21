package useless.moonsteel.api;

import net.fabricmc.loader.api.FabricLoader;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.dependency.Key;
import useless.moonsteel.MoonSteel;
import useless.moonsteel.api.paxel.MoonSteelPaxel;
import useless.moonsteel.MoonSteelConstants;

public class MoonSteelCompat {
	private MoonSteelCompat() {
	}

	public static void init() {
		if(FabricLoader.getInstance().isModLoaded("aether")){
			/* gona work on it later */
		}
		if(MoonSteelConstants.PAXEL.getAsBoolean()){
			CommonEvents.AFTER_ITEM_INIT.listen(Key.of(MoonSteel.MOD_ID ,"paxels"), MoonSteelPaxel::initItem);
			CommonEvents.RECIPES_READY.listen(Key.of(MoonSteel.MOD_ID ,"paxels"), MoonSteelPaxel::recipeReady);
		}
	}
}
