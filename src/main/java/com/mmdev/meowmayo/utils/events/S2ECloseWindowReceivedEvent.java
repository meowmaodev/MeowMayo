package com.mmdev.meowmayo.utils.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

public class S2ECloseWindowReceivedEvent extends Event {
    private final S2EPacketCloseWindow packet;

    public S2ECloseWindowReceivedEvent(S2EPacketCloseWindow packet) {
        this.packet = packet;
    }

    public S2EPacketCloseWindow getPacket() {
        return packet;
    }
}