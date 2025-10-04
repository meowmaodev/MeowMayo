package com.mmdev.meowmayo.features.kuudra;

import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.regex.*;

import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;

public class KuudraExtras {
    private ToggleSetting announceFresh = (ToggleSetting) ConfigSettings.getSetting("Announce Fresh");
    private ToggleSetting freshTitle = (ToggleSetting) ConfigSettings.getSetting("Fresh Alert");
    private ToggleSetting announceMana = (ToggleSetting) ConfigSettings.getSetting("Announce Mana");
    private ToggleSetting dangerBlocks = (ToggleSetting) ConfigSettings.getSetting("Danger Block Warning");
    private ToggleSetting lastAlive = (ToggleSetting) ConfigSettings.getSetting("Last Alive");

    private Pattern focusPattern = Pattern.compile("^Used Extreme Focus! \\((\\d+) Mana\\)$");
    private Pattern killPattern = Pattern.compile("^(.+) was FINAL KILLED by Kuudra!$");

    private int finalDeaths = 0;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        finalDeaths = 0;
    }

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (Minecraft.getMinecraft().thePlayer == null) return;

        if (announceFresh.getValue() && msg.equals("Your Fresh Tools Perk bonus doubles your building speed for the next 10 seconds!")) {
            ChatUtils.partyChat("FRESH!");

            if (freshTitle.getValue()) {
                PlayerUtils.makeTextTitle("Fresh Tools Active!", 1000);
            }
        }


        if (announceMana.getValue()) {
            Matcher matcher = focusPattern.matcher(msg);

            if (matcher.matches()) {
                String mana = matcher.group(1);
                ChatUtils.partyChat("Used " + mana + " Mana!");
            }
        }

        if (!lastAlive.getValue() && KuudraPhases.getCurrPhase() >= 6) {
            Matcher matcher = killPattern.matcher(msg);

            if (matcher.matches()) {
                finalDeaths++;
                if (finalDeaths >= 3) {
                    PlayerUtils.makeTextAlert("Last Alive!", "random.bowhit", 500);
                    ChatUtils.partyChat("You are the last person standing - Solo!");
                }
            }
        }
    }

    private int tickCounter = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!(dangerBlocks.getValue() && KuudraPhases.getCurrPhase() == 7)) return;

        tickCounter++;
        if (tickCounter >= 5) { // every quarter second so we dont spam
            tickCounter = 0;

            if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;

            BlockPos posBelow = new BlockPos(
                    Minecraft.getMinecraft().thePlayer.posX,
                    Minecraft.getMinecraft().thePlayer.posY - 1,
                    Minecraft.getMinecraft().thePlayer.posZ
            );

            Block block = Minecraft.getMinecraft().theWorld.getBlockState(posBelow).getBlock();

            if (block == Blocks.stained_hardened_clay && Minecraft.getMinecraft().theWorld.getBlockState(posBelow).getBlock().getMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(posBelow)) == 14) {
                PlayerUtils.makeTextAlert("JUMP!", "note.pling", 250);
            }
        }
    }
}

//               _..----.._    _
//             .'  .--.    "-.(0)_
// '-.__.-'"'=:|   ,  _)_ \__ . c\'-..
//              '''------'---''---'-"