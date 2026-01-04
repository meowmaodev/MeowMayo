package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S0FMobSpawnEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS0F {
    @Inject(method = "handleSpawnMob", at = @At("RETURN"))
    private void onMobSpawn(S0FPacketSpawnMob packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S0FMobSpawnEvent(packet));
    }
}
