package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S2ECloseWindowReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS2E {
    @Inject(method = "handleCloseWindow", at = @At("RETURN"))
    private void onHandleCloseWindow(S2EPacketCloseWindow packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S2ECloseWindowReceivedEvent(packet));
    }
}