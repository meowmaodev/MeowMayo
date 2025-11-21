package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S3EReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS3E {
    @Inject(method = "handleTeams", at = @At("RETURN"))
    private void onHandleTeams(S3EPacketTeams packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S3EReceivedEvent(packet));
    }
}
