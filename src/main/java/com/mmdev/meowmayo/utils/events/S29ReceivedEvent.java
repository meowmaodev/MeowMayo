package com.mmdev.meowmayo.utils.events;

import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.Event;

public class S29ReceivedEvent extends Event {
    private final S29PacketSoundEffect packet;

    public S29ReceivedEvent(S29PacketSoundEffect packet) {
        this.packet = packet;
    }

    public S29PacketSoundEffect getPacket() {
        return packet;
    }

    public String getSoundName() {
        return packet.getSoundName();
    }
}
