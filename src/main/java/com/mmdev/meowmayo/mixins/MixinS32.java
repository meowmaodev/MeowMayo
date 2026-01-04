package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS32 {
    @Inject(method = "handleConfirmTransaction", at = @At("RETURN"))
    private void onHandleTransaction(S32PacketConfirmTransaction packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new S32ReceivedEvent(packet));
    }
}
