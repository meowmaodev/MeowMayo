package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.C0DCloseWindowSentEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinC0D {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"))
    private void onSendPacket(Packet<?> packetIn, CallbackInfo ci) {
        if (packetIn instanceof C0DPacketCloseWindow) {
            MinecraftForge.EVENT_BUS.post(new C0DCloseWindowSentEvent((C0DPacketCloseWindow) packetIn));
        }
    }
}