package com.mmdev.meowmayo.utils.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class C0DCloseWindowSentEvent extends Event {
    private final C0DPacketCloseWindow packet;

    public C0DCloseWindowSentEvent(C0DPacketCloseWindow packet) {
        this.packet = packet;
    }

    public C0DPacketCloseWindow getPacket() {
        return packet;
    }
}