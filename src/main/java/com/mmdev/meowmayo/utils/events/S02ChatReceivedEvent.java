package com.mmdev.meowmayo.utils.events;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.network.play.server.S02PacketChat;

public class S02ChatReceivedEvent extends Event {
    private final S02PacketChat packet;

    public S02ChatReceivedEvent(S02PacketChat packet) {
        this.packet = packet;
    }

    public S02PacketChat getPacket() {
        return packet;
    }

    public String getMessage() {
        return packet.getChatComponent().getUnformattedText();
    }

    public String getUnformattedMessage() {
        return EnumChatFormatting.getTextWithoutFormattingCodes(packet.getChatComponent().getUnformattedText());
    }
}
