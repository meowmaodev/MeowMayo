package com.mmdev.meowmayo.mixins;

import com.mmdev.meowmayo.utils.events.S02ActionBarReceivedEvent;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinS02 {
    @Inject(method = "handleChat", at = @At("RETURN"))
    private void onHandleChat(S02PacketChat packet, CallbackInfo ci) {
        // not
        if (packet.getType() == 2) { // Chat menu
            MinecraftForge.EVENT_BUS.post(new S02ActionBarReceivedEvent(packet));
        } else {
            MinecraftForge.EVENT_BUS.post(new S02ChatReceivedEvent(packet));
        }
    }
}
