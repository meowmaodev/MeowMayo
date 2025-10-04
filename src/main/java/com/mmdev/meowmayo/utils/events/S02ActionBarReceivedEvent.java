package com.mmdev.meowmayo.utils.events;

import net.minecraft.network.play.server.S02PacketChat;
import net.minecraftforge.fml.common.eventhandler.Event;

public class S02ActionBarReceivedEvent extends Event {
    private final S02PacketChat packet;

    public S02ActionBarReceivedEvent(S02PacketChat packet) {
        this.packet = packet;
    }

    public S02PacketChat getPacket() {
        return packet;
    }

    public String getMessage() {
        return packet.getChatComponent().getUnformattedTextForChat();
    }
}