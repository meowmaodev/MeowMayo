package com.mmdev.meowmayo.utils.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

public class S2DOpenWindowReceivedEvent extends Event {
    private final S2DPacketOpenWindow packet;

    public S2DOpenWindowReceivedEvent(S2DPacketOpenWindow packet) {
        this.packet = packet;
    }

    public S2DPacketOpenWindow getPacket() {
        return packet;
    }
}