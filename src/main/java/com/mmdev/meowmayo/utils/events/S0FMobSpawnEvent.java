package com.mmdev.meowmayo.utils.events;

import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraftforge.fml.common.eventhandler.Event;

public class S0FMobSpawnEvent extends Event {
    private final S0FPacketSpawnMob packet;

    public S0FMobSpawnEvent(S0FPacketSpawnMob packet) {
        this.packet = packet;
    }

    public S0FPacketSpawnMob getPacket() {
        return packet;
    }

    public int getEntityId() {
        return packet.getEntityID();
    }

    public double getMobX() {
        return packet.getX();
    }

    public double getMobY() {
        return packet.getY();
    }

    public double getMobZ() {
        return packet.getZ();
    }
}
