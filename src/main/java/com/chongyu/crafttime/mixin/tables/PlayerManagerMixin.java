package com.chongyu.crafttime.mixin.tables;

import com.chongyu.crafttime.CraftTime;
import com.mojang.logging.LogUtils;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.WorldSaveHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow @Final private MinecraftServer server;
    @Mutable
    @Final
    @Shadow  private final WorldSaveHandler saveHandler;
    @Shadow  private static final Logger LOGGER = LogUtils.getLogger();

    protected PlayerManagerMixin(WorldSaveHandler saveHandler) {
        this.saveHandler = saveHandler;
    }

    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ca){
        player.sendMessage(Text.of(CraftTime.MOD_ID).copy().append(Text.translatable("crafttime.join_info").formatted(Formatting.YELLOW)));
    }
}