package com.mmdev.meowmayo.features.kuudra.tracker;

import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class KuudraTimer {
    public boolean enabled = false;
    public long serverMs = 0;
    public long clientMs = 0;

    public void enable() {
        this.enabled = true;
    }
    public void disable() {
        this.enabled = false;
    }

    public void flush() {
        disable();
        clientMs = 0;
        serverMs = 0;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (enabled) {
            if (event.phase != TickEvent.Phase.END) return;
            clientMs += 50;
        }
    }

    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (enabled) {
            serverMs += 50;
        }
    }

    public void split(List<Long> rt, List<Long> lt, int index) {
        lt.set(index, Math.max(0, clientMs - serverMs));
        rt.set(index, Math.max(0, System.currentTimeMillis()));
        clientMs = 0;
        serverMs = 0;
    }

    public long getCurrentSplit() {
        return clientMs;
    }

    public long getCurrentLaglessSplit() {
        return serverMs;
    }
}
