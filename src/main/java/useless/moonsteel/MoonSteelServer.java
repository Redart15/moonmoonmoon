package useless.moonsteel;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.core.net.command.CommandManager;
import useless.moonsteel.command.CommandScore;

public class MoonSteelServer implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		CommandManager.registerCommand(new CommandScore());
	}
}
