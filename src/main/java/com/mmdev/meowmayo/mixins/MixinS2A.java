package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S2AParticleReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS2A {
    @Inject(method = "handleParticles", at = @At("RETURN"))
    private void onHandleParticle(S2APacketParticles packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S2AParticleReceivedEvent(packet));
    }
}
