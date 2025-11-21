package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.utils.DungeonUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DungeonPhases {
    // 1 is dungeon started
    // 2 is blood opened
    // 3 is blood clear
    // 4 is in boss (use per boss class)
    private static boolean inDungeons = false;

    private static int phase = 0;

    //[NPC] Mort: Here, I found this map when I first entered the dungeon.

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (msg.equals("[NPC] Mort: Here, I found this map when I first entered the dungeon.")) {
            phase = 1;
        }
    }
}
