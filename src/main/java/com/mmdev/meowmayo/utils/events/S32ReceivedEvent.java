package com.mmdev.meowmayo.utils.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class S32ReceivedEvent extends Event {
    private final S32PacketConfirmTransaction packet;

    public S32ReceivedEvent(S32PacketConfirmTransaction packet) {
        this.packet = packet;
    }

    public S32PacketConfirmTransaction getPacket() {
        return packet;
    }
}
