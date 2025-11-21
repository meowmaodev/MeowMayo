package com.mmdev.meowmayo.utils.events;

import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraftforge.fml.common.eventhandler.Event;

public class S3EReceivedEvent extends Event {
    private final S3EPacketTeams packet;

    public S3EReceivedEvent(S3EPacketTeams packet) {
        this.packet = packet;
    }

    public S3EPacketTeams getPacket() {
        return packet;
    }

    public String getName() {
        return packet.getDisplayName();
    }
}

