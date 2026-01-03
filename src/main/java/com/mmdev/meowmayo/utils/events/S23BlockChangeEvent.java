package com.mmdev.meowmayo.utils.events;

import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraftforge.fml.common.eventhandler.Event;

public class S23BlockChangeEvent extends Event {
    private final S23PacketBlockChange packet;

    public S23BlockChangeEvent(S23PacketBlockChange packet) {
        this.packet = packet;
    }

    public S23PacketBlockChange getPacket() {
        return packet;
    }
}
