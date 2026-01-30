package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S2DOpenWindowReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS2D {
    @Inject(method = "handleOpenWindow", at = @At("RETURN"))
    private void onHandleOpenWindow(S2DPacketOpenWindow packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S2DOpenWindowReceivedEvent(packet));
    }
}