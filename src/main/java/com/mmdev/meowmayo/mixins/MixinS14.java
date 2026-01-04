package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S17LookMoveEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS14 {
    @Inject(method = "handleEntityMovement", at = @At("RETURN"))
    private void onEntityMovement(S14PacketEntity packet, CallbackInfo ci) {
        if (packet instanceof S14PacketEntity.S17PacketEntityLookMove) {
            MinecraftForge.EVENT_BUS.post(new S17LookMoveEvent((S14PacketEntity.S17PacketEntityLookMove) packet));
        }
    }
}
