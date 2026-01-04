package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S23BlockChangeEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS23 {
    @Inject(method = "handleBlockChange", at = @At("RETURN"))
    private void onEntityMovement(S23PacketBlockChange packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S23BlockChangeEvent(packet));
    }
}
