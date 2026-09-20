package useless.moonsteel.mixin.accessor;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkPosc;
import net.minecraft.server.world.chunk.provider.ChunkProviderServer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkProviderServer.class)
public interface ChunkProviderServerAccessor {
	@Invoker
	Chunk callLoadChunkFromFile(@NotNull ChunkPosc chunkPos);
}
