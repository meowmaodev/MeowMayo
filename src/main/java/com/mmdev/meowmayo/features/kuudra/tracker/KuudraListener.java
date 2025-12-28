package com.mmdev.meowmayo.features.kuudra.tracker;

import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraChatListener;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraRegexListener;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraRegexListenerCI;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraTickListener;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import com.mmdev.meowmayo.utils.tracker.PhaseListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

public class KuudraListener {
    private Set<PhaseListener> activeListeners = new HashSet<>();

    private KuudraRegexListener runEnd;

    public void setActiveListeners(Set<PhaseListener> listeners) {
        activeListeners = listeners;
    }

    public void setRunEnd(KuudraRegexListener listener) {
        this.runEnd = listener;
    }

    @SubscribeEvent
    public void onChat(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();
        for (PhaseListener listener : activeListeners) {
            if (listener instanceof KuudraChatListener) {
                ((KuudraChatListener) listener).onChatMessage(msg);
            }
            if (listener instanceof KuudraRegexListenerCI) {
                ((KuudraRegexListenerCI) listener).onChatMessage(msg);
            }
            if (listener instanceof KuudraRegexListener) {
                ((KuudraRegexListener) listener).onChatMessage(msg);
            }
        }

        runEnd.onChatMessage(msg);
    }

    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        for (PhaseListener listener : activeListeners) {
            if (listener instanceof KuudraTickListener) {
                ((KuudraTickListener) listener).tick();
            }
        }
    }
}
