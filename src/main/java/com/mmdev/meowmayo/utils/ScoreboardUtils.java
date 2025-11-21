package com.mmdev.meowmayo.utils;

import com.mmdev.meowmayo.utils.events.S3EReceivedEvent;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ScoreboardUtils {
    static String location = "";

     public static String getLocation() {
        return location;
    }

    @SubscribeEvent
    public void onHandleTeam(S3EReceivedEvent event) {
        S3EPacketTeams packet = event.getPacket();

        if (packet.getAction() != 2) return;

        String line = EnumChatFormatting.getTextWithoutFormattingCodes(
                (packet.getPrefix() != null ? packet.getPrefix() : "") + (packet.getSuffix() != null ? packet.getSuffix() : "")
        ).trim();

        if (line.startsWith("⏣")) {
            location = line;
        }
    }
}
