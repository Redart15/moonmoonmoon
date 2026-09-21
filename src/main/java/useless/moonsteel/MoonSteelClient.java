package useless.moonsteel;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.particle.ParticleDispatcher;
import net.minecraft.client.render.texture.stitcher.AtlasStitcher;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.entity.EntityPainting;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.event.defs.ClientEvents;
import useless.moonsteel.api.MoonSteelCompatClient;
import useless.moonsteel.command.CommandScore;
import useless.moonsteel.fx.ParticleMagicSmoke;
import useless.moonsteel.fx.ParticleStar;
import useless.moonsteel.item.MoonSteelItems;

import java.io.IOException;
import java.net.URISyntaxException;

import static useless.moonsteel.MoonSteel.*;

public class MoonSteelClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientEvents.BEFORE_CLIENT_START.listen(KEY, this::beforeClientStart);
		ClientEvents.AFTER_CLIENT_START.listen(KEY, this::afterClientStart);
		ClientEvents.ITEM_MODEL_RELOAD.listen(KEY, MoonSteelModels::initItemModels);
		ClientEvents.BLOCK_MODEL_RELOAD.listen(KEY, MoonSteelModels::initBlockModels);
		CommandManager.registerCommand(new CommandScore());
		MoonSteelCompatClient.init();
	}


	public void beforeClientStart() {
		for (final AtlasStitcher stitcher : TextureRegistry.stitcherMap.values()) {
			try {
				TextureRegistry.initializeAllFiles(MOD_ID, stitcher, true);
			} catch (URISyntaxException | IOException e) {
				MoonSteelConstants.LOGGER.error("Failed to initialize texture files!", e);
			}
		}
		SoundRepository.namespaceAdded(MOD_ID);
	}


	public void afterClientStart() {
		ParticleDispatcher.getInstance().addDispatch(STAR, (world, x, y, z, motionX, motionY, motionZ, data) -> new ParticleStar(world, x, y, z, motionX, motionY, motionX));
		ParticleDispatcher.getInstance().addDispatch(SMOKE, (world, x, y, z, motionX, motionY, motionZ, data) -> new ParticleMagicSmoke(world, x, y, z, motionX, motionY, motionX));
		EntityPainting.addBorder(MoonSteelItems.INGOT_MOONSTEEL.getDefaultStack(), NamespaceID.fromPool(MOD_ID, "border_moonsteel"));

	}
}
