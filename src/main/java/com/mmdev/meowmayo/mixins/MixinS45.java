package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S45ReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS45 {
    @Inject(method = "handleTitle", at = @At("RETURN"), cancellable = true)
    private void onHandleTitle(S45PacketTitle packet, CallbackInfo ci) {
        S45ReceivedEvent event = new S45ReceivedEvent(packet);

        if (event.hasText()) {
            MinecraftForge.EVENT_BUS.post(event);
        }
    }
}

