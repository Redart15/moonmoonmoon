package useless.moonsteel.mixin.accessor;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.provider.ChunkProviderThreaded;
import net.minecraft.core.world.pos.ChunkPosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkProviderThreaded.class)
public interface ChunkProviderThreadedAccessor {
	@Invoker
	Chunk callLoadChunk(@NotNull ChunkPosc chunkPos);
}
