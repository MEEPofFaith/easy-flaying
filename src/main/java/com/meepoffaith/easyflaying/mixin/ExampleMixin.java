package com.meepoffaith.easyflaying.mixin;

import com.meepoffaith.easyflaying.*;
import net.minecraft.commands.*;
import net.minecraft.server.*;
import net.minecraft.util.thread.*;
import net.minecraft.world.level.chunk.storage.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(MinecraftServer.class)
abstract class ExampleMixin extends ReentrantBlockableEventLoop<TickTask> implements ServerInfo, ChunkIOErrorReporter, CommandSource, AutoCloseable {
    public ExampleMixin(String string) {
        super(string);
    }

    // common mixins will show errors in the IDE. see https://github.com/terrarium-earth/jvm-multiplatform/issues/10
    @Inject(method = "loadLevel", at = @At(value = "HEAD"))
    private void logOnWorldLoad(CallbackInfo ci) {
        EasyFlaying.LOGGER.info("MinecraftServer$loadLevel has started!");
    }
}
