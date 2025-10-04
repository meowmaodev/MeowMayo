package com.mmdev.meowmayo.features.kuudra;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.RendUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S29ReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RendFeatures {
    private ToggleSetting rendDamage = (ToggleSetting) ConfigSettings.getSetting("Rend Pull Damage");
    private ToggleSetting rendAlert = (ToggleSetting) ConfigSettings.getSetting("Rend Alert");
    private ToggleSetting rendBone = (ToggleSetting) ConfigSettings.getSetting("Rend Bone Info");

    private static double currHp = 0.0;
    private static double hp = 24999.0;
    private static int pull = 1;

    private static int pulls = 0;

    private static boolean playerPull = false;
    private static boolean rendCooldown = false;

    private static boolean throwingBone = false;
    private static int boneSlot = 0;
    private static int boneHit = 0;

    private static boolean waitingBone = false;
    private static int waitingBoneTicks = 0;

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (msg.equals("[NPC] Elle: POW! SURELY THAT'S IT! I don't think he has any more in him!")) {
            currHp = 26000.0;
            hp = 24999.0;
            pull = 1;
        }
    }

    @SubscribeEvent
    public void onSoundPacket(S29ReceivedEvent event) {
        if (KuudraPhases.getCurrPhase() < 7) return;

        String soundName = event.getSoundName();

        if (soundName.equals("mob.zombie.woodbreak")) {
            if (rendDamage.getValue()) {
                pulls++;
                DelayUtils.scheduleTask(() -> pulls = Math.max(pulls - 1, 0), 100);
            }
        }

        if (soundName.equals("tile.piston.out")) { // terror armor 10 stack bone tracker, this should go off bone pos tbh
            if (rendBone.getValue() && throwingBone) {
                ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();

                if (heldItem == null) return;

                String itemName = heldItem.getDisplayName().toLowerCase();

                if (itemName.contains("breath") || itemName.contains("juju") || itemName.contains("terminator")) return;

                String helm = Minecraft.getMinecraft().thePlayer.getCurrentArmor(3) == null ? "N/A" : Minecraft.getMinecraft().thePlayer.getCurrentArmor(3).getDisplayName();

                if (boneHit == 0) {
                    boneHit = 1;
                    ChatUtils.system("§7Front bone hit at §b" + KuudraPhases.getFormattedSplitTime() + " §r| §7Holding - " + heldItem.getDisplayName() + " §r| §7Wearing - " + helm);
                } else if (boneHit == 1) {
                    boneHit = 0;
                    ChatUtils.system("§7Back bone hit at §b" + KuudraPhases.getFormattedSplitTime() + " §r| §7Holding - " + heldItem.getDisplayName() + " §r| §7Wearing - " + helm);
                    throwingBone = false;

                    if (rendAlert.getValue()) {
                        PlayerUtils.makeTextAlert("-=-=-=-=- Rend NOW! -=-=-=-=-", "random.anvil_land", 500);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (rendBone.getValue()) {
            throwingBone = false;
            boneSlot = 0;
            waitingBoneTicks = 0;
        }
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event) {
        if (KuudraPhases.getCurrPhase() != 7) return;
        if (event.button == 0 && event.buttonstate) {
            if (rendDamage.getValue()) {
                ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (heldItem == null) return;

                if (!heldItem.getDisplayName().toLowerCase().contains("bonemerang")) return;
                if (!rendCooldown) {
                    ChatUtils.system("§7Pulled at §b" + KuudraPhases.getFormattedSplitTime());
                    rendCooldown = true;
                    DelayUtils.scheduleTask(() -> rendCooldown = false, 2000);
                }
                playerPull = true;
                DelayUtils.scheduleTask(() -> playerPull = false, 100);
            }
        } else if (event.button == 1 && event.buttonstate) {
            if (rendBone.getValue() && !throwingBone) {
                ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (heldItem == null) return;

                if (!heldItem.getDisplayName().toLowerCase().contains("bonemerang")) return;

                boneSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                boneHit = 0;
                throwingBone = true;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (KuudraPhases.getCurrPhase() != 7) return;

        if (rendDamage.getValue()) {
            EntityMagmaCube kuudra = KuudraPhases.getKuudra();
            if (kuudra == null) {
                currHp = 26000.0;
            } else {
                currHp = kuudra.getHealth();
            }

            double diff = hp - currHp;
            if (diff > 2100) {
                String rendMessage = (
                        "§7Pull number §6" + pull + " §7hit at §b" + KuudraPhases.getFormattedSplitTime() +
                        " §7 for " + RendUtils.getDamageColor(diff) + RendUtils.formatHealth(diff * 9600)
                );

                hp = currHp;
                rendMessage += RendUtils.pullConfidence(pulls);
                if (playerPull) {
                    playerPull = false;
                    rendMessage += "§r | §a§lYOUR PULL";
                }

                pull++;
            }
            hp = currHp;
        }

        if (waitingBone) {
            ItemStack boneItem = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(boneSlot);
            if (boneItem == null) return;
            if (boneItem.getDisplayName().toLowerCase().contains("bonemerang") || waitingBoneTicks > 100) {
                throwingBone = false;
                waitingBoneTicks = 0;
            } else {
                waitingBoneTicks++;
            }
        }
    }
}