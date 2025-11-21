package com.mmdev.meowmayo.utils.events;

import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraftforge.fml.common.eventhandler.Event;

public class S2AParticleReceivedEvent extends Event {
    private final S2APacketParticles packet;

    public S2AParticleReceivedEvent(S2APacketParticles packet) {
        this.packet = packet;
    }

    public S2APacketParticles getPacket() {
        return packet;
    }

    public String getParticleName() {
        return packet.getParticleType().getParticleName();
    }

    public double getParticleX() {
        return packet.getXCoordinate();
    }

    public double getParticleY() {
        return packet.getYCoordinate();
    }

    public double getParticleZ() {
        return packet.getZCoordinate();
    }
}
