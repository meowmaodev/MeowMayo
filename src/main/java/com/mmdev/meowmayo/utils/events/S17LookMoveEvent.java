package com.mmdev.meowmayo.utils.events;

import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class S17LookMoveEvent extends Event {
    private final S14PacketEntity.S17PacketEntityLookMove packet;

    public S17LookMoveEvent(S14PacketEntity.S17PacketEntityLookMove packet) {
        this.packet = packet;
    }

    public S14PacketEntity.S17PacketEntityLookMove getPacket() {
        return packet;
    }
}
