package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S29ReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS29 {
    @Inject(method = "handleSoundEffect", at = @At("RETURN"))
    private void onHandleTransaction(S29PacketSoundEffect packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S29ReceivedEvent(packet));
    }
}