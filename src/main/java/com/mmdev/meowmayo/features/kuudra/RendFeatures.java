package com.mmdev.meowmayo.features.kuudra;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraPhases;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.RendUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S29ReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

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
    private static int boneHit = 0;

    private static boolean waitFlag = false;
    private static boolean boneListener = false;

    private static int arbitraryWaitingCounterThatProbablyHasWayyyyyTooLongOfANameToBeFollowingGoodCodeStandards = 0;

    private double lastPos = -6767.0;
    private float lastYaw = -420.0F;
    private float secondYaw = -421.0F;

    private boolean turned = false;
    private boolean firstHit = false;
    private boolean secondHit = false;

    private static EntityArmorStand bone = null;

    private static FacingDir dir = FacingDir.NORTH;

    public enum FacingDir {
        NORTH(-1, 'z'),
        SOUTH(1, 'z'),
        WEST(-1, 'x'),
        EAST(1, 'x');

        public final int sign;
        public final char axis;

        FacingDir(int sign, char axis) {
            this.sign = sign;
            this.axis = axis;
        }
    }

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
            boneHit = 0;
            bone = null;
            boneListener = false;
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
        } else if (event.button == 1 && event.buttonstate) { // exp tracker goes here when made
            if (rendBone.getValue() && !waitFlag) {
                ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (heldItem == null) return;

                if (!heldItem.getDisplayName().toLowerCase().contains("bonemerang") || heldItem.getItem() != Items.bone) return;

                waitFlag = true;
                if (boneListener) return;

                boneListener = true;
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (boneListener) {
            if (arbitraryWaitingCounterThatProbablyHasWayyyyyTooLongOfANameToBeFollowingGoodCodeStandards >= 2) {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                List<EntityArmorStand> stands = Minecraft.getMinecraft().theWorld.getEntities(EntityArmorStand.class, entity -> true);

                for (EntityArmorStand stand : stands) {
                    double dist = stand.getDistanceToEntity(player);
                    if (dist <= 3) {
                        ItemStack armorStandItem = stand.getHeldItem();

                        if (armorStandItem != null && armorStandItem.getItem() == Items.bone) {
                            bone = stand;
                            dir = getDirection(player);
                        }
                    }
                }

                throwingBone = true;
                waitFlag = false;

                boneListener = false;
            }
            arbitraryWaitingCounterThatProbablyHasWayyyyyTooLongOfANameToBeFollowingGoodCodeStandards++;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
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

                ChatUtils.system(rendMessage);

                pull++;
            }
            hp = currHp;
        }

        if (throwingBone) {
            if (bone != null) {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

                EntityMagmaCube kuudra = KuudraPhases.getKuudra();
                if (kuudra == null) {
                    bone = null;
                    return;
                }

                AxisAlignedBB box = kuudra.getEntityBoundingBox();
                double widthX = (box.maxX - box.minX) / 2.0;
                double widthZ = (box.maxZ - box.minZ) / 2.0;

                double boneCoord = dir.axis == 'x' ? bone.posX : bone.posZ;
                double playerCoord = dir.axis == 'x' ? player.posX : player.posZ;
                double width = dir.axis == 'x' ? widthX : widthZ;
                double base = dir.axis == 'x' ? kuudra.posY : kuudra.posZ;

                double newPos = dir.sign * boneCoord;
                double kPos = base + (-dir.sign * width);
                if (firstHit) kPos += dir.sign;

                double relBone = (boneCoord - kPos) * dir.sign;
                double relPlayer = (playerCoord - kPos) * dir.sign;

                if (!turned) {
                    if (newPos < lastPos) {
                        turned = true;
                    }

                    if (!firstHit && relBone > 0 && relPlayer < 0) {
                        ItemStack heldItem = player.getHeldItem();
                        String itemName = (heldItem != null) ? heldItem.getDisplayName() : "Nothing";
                        String armorName = player.getCurrentArmor(3) != null ? player.getCurrentArmor(3).getDisplayName() : "None";
                        ChatUtils.system(
                                "§7Front bone hit at §b" + KuudraPhases.getFormattedSplitTime() + " §r| §7Holding - " + itemName + " §r| §7Wearing - " + armorName
                        );
                        firstHit = true;
                    }
                } else {
                    if (!secondHit && firstHit && relBone < 0 && relPlayer < 0) {
                        ItemStack heldItem = player.getHeldItem();
                        String itemName = (heldItem != null) ? heldItem.getDisplayName() : "Nothing";
                        String armorName = player.getCurrentArmor(3) != null ? player.getCurrentArmor(3).getDisplayName() : "None";
                        ChatUtils.system(
                                "§7Back bone hit at §b" + KuudraPhases.getFormattedSplitTime() + " §r| §7Holding - " + itemName + " §r| §7Wearing - " + armorName
                        );
                        secondHit = true;
                    }
                }
                if (bone.rotationYaw == lastYaw && lastYaw == secondYaw) {
                    bone = null;
                } else {
                    lastPos = newPos;
                    secondYaw = lastYaw;
                    lastYaw = bone.rotationYaw;
                }
            } else {
                throwingBone = false;
                turned = false;
                lastPos = -6767.0;
                lastYaw = -420.0F;
                secondYaw = -421.0F;
                firstHit = false;
                secondHit = false;
            }
        }
    }

    public static FacingDir getDirection(EntityPlayerSP player) {
        float yaw = player.rotationYaw;

        if (yaw > -135.0F && yaw <= -45.0F) {
            return FacingDir.EAST;
        } else if (yaw > -45.0F && yaw <= 45.0F) {
            return FacingDir.SOUTH;
        } else if (yaw > 45.0F && yaw <= 135.0F) {
            return FacingDir.WEST;
        } else {
            return FacingDir.NORTH;
        }
    }
}