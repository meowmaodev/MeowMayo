package com.mmdev.meowmayo.utils.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.network.play.server.S45PacketTitle;

public class S45ReceivedEvent extends Event {
    private final S45PacketTitle packet;

    public S45ReceivedEvent(S45PacketTitle packet) {
        this.packet = packet;
    }

    public S45PacketTitle getPacket() {
        return packet;
    }

    public String getUnformattedTitle() {
        if (packet.getMessage() != null) {
            return packet.getMessage().getUnformattedText().replaceAll("§.", "");
        }
        return "";
    }

    public String getTitle() {
        return packet.getMessage() != null ? packet.getMessage().getUnformattedText() : "";
    }

    public boolean hasText() {
        return packet.getMessage() != null && !packet.getMessage().getUnformattedText().isEmpty();
    }
}
