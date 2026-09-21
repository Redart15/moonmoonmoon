package useless.moonsteel;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BooleanSupplier;

public class MoonSteelConstants {
	private	MoonSteelConstants(){}
	// called to be seperate from game classes to allow use in configs as well as mixin configurations
	public static final String MOD_ID = "moonsteel";
	public static final BooleanSupplier BACKPACKS = () -> FabricLoader.getInstance().isModLoaded("betterwithbackpacks");
	public static final BooleanSupplier PAXEL = () -> FabricLoader.getInstance().isModLoaded("paxels");
	public static final BooleanSupplier COMMANDLY = () -> FabricLoader.getInstance().isModLoaded("commandly");
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
}
