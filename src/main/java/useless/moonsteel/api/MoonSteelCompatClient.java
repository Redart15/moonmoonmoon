package useless.moonsteel.api;

import net.fabricmc.loader.api.FabricLoader;
import turniplabs.halplibe.event.defs.ClientEvents;
import useless.moonsteel.api.paxel.MoonSteelPaxel;

import static useless.moonsteel.MoonSteel.KEY;

public class MoonSteelCompatClient {

	private MoonSteelCompatClient(){

	}

	public static void init(){
		if(FabricLoader.getInstance().isModLoaded("paxels")){
			ClientEvents.ITEM_MODEL_RELOAD.listen(KEY, MoonSteelPaxel::initItemModels);
		}
	}
}
