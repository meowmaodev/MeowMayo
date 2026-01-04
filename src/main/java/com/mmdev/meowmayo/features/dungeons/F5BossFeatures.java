package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTracker;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.RenderShapeUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S23BlockChangeEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class F5BossFeatures {
    private ToggleSetting highlightLivid = (ToggleSetting) ConfigSettings.getSetting("Livid Solver");

    private static boolean active = false;

    private EntityOtherPlayerMP livid = null;

    private static HashMap<Integer, String> names = new HashMap<>();

    static {
        names.put(0, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzMyNzkxMiwKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmEwNGM4Yzg4N2UzOThkMzMyMGQzOTUwNTdjODdiMWUwMmI3OTViMTBiYmIzOGY3ZTJhOGNmYmZjMDc4YTE2OCIKICAgIH0KICB9Cn0=");
        names.put(2, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzM0ODg4MSwKICAicHJvZmlsZUlkIiA6ICIxNzhmMTJkYWMzNTQ0ZjRhYjExNzkyZDc1MDkzY2JmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJzaWxlbnRkZXRydWN0aW9uIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdmMWFiZmQwNzE3NTExMTVmYTEwMDBjOWQ3NmQxMDk3M2ZmMzI3NzMxNDZjZDE0MDY4NjRiYWFmMzc4MTZlOWEiCiAgICB9CiAgfQp9");
        names.put(4, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzMwMjU4MSwKICAicHJvZmlsZUlkIiA6ICJiYWE1Yjg0YzA2NGM0NTBlYjU2NTU4ZDQxOWVmYTkzMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYW1lbGxpYWFkYW1zIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxY2M2NDA4ZTVhMjY4ZTZjZWIyZjhiOWFmYjZlZWZkNGE5NGI3ZWI0Nzg4MzgyNmJkNmMzNTRmYzNkY2E5NzMiCiAgICB9CiAgfQp9");
        names.put(5, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzM5MTM5NCwKICAicHJvZmlsZUlkIiA6ICI2ZmQyNGJlNDk4ZjA0MDJlOTZhYWQ2MWUzY2VmYjZmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbmdlbGFsbHhfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRjM2MwMjQ4OGU2M2I1ZTY3NTg0YWE5Nzc2ZDVlYTU2YmFhNjk2NWE3MzNhNjhmNzAwY2E4YjA4ODkxMWEyYjciCiAgICB9CiAgfQp9");
        names.put(8, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzQzNjUwMSwKICAicHJvZmlsZUlkIiA6ICI3ZGEyYWIzYTkzY2E0OGVlODMwNDhhZmMzYjgwZTY4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHb2xkYXBmZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ4ZTQzZjg0MmYzMTY2NTFlNTFhNTc5N2NhYTMyYmZhOWRlODFhOGMyMzg0YmQ2YzBkMWM0N2M0NDgwM2M5MSIKICAgIH0KICB9Cn0=");
        names.put(10, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzQxNTQyNSwKICAicHJvZmlsZUlkIiA6ICJmYThiNGRmYWMxZTg0Mzg5YmFkZTIzYTE0Zjk1ZTRkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJkZXZ2YXJhcmdzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJmMDVmYjRiZGI1NzgwNzc4ZmU0NDYxMjgzZWRkZmFiNzI3M2I5NmQ0Njc1NDdlOGJjYTdlYzEwMTM1N2U2NmYiCiAgICB9CiAgfQp9");
        names.put(11, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzM2ODIxMiwKICAicHJvZmlsZUlkIiA6ICJmNWQwYjFhZTQxNmU0YTE5ODEyMTRmZGQzMWU3MzA1YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYXRjaFRoZVdhdmUxMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82MTA4ODU0Mzk0YzgwZmVkNDE4OTU4Mjg3ZGU1ODEyMDlmZDY5ZmZmM2U2M2NiM2M4ODFjMzRiZmE4MThjOWUiCiAgICB9CiAgfQp9");
        names.put(13, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzIzNjA1MiwKICAicHJvZmlsZUlkIiA6ICIyNmM1MmQzZjgxMzQ0ZjUzYmNhYzA0Mjc4ODBiZDVjNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbWJpZ3VvdXNCaXZhbHZlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzliZmY0ZDY1OWQ5ODVlNTFmNDIxOTU1YWM4NzcwNGE5YjYxMjJjYjZhMTY5ZDliMDQ4Y2RkNmFiMWUxYjBiNTciCiAgICB9CiAgfQp9");
        names.put(14, "ewogICJ0aW1lc3RhbXAiIDogMTU5ODk3NzI4MzQ1NiwKICAicHJvZmlsZUlkIiA6ICI5MWYwNGZlOTBmMzY0M2I1OGYyMGUzMzc1Zjg2ZDM5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdG9ybVN0b3JteSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hMTE2ZGJhYmQ3Njk1N2E1MDBkYjhmMzQ2NDcwZDc5NjQ3M2YyNDU1N2Y3ZjlkM2Y0ZTJhYzNmN2M4NDM5ZWEzIgogICAgfQogIH0KfQ==");
    }

    private static BlockPos blockPos = new BlockPos(5, 108, 59);
    private static int blockState = -1;
    // 4 - yellow - &e
    // 11 - blue - &9
    // 13 - green - &2
    // 5 - lime - &a
    // 14 - red - &c
    // 10 - purple - &5
    // 2 - magenta - &d
    // 8 - gray - &7
    // 0 - white - &f

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        active = false;
        livid = null;
        blockState = 14;
    }

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (!(DungeonTracker.getTier().equals("M5") || DungeonTracker.getTier().equals("F5"))) return;

        if (DungeonTracker.getPhase() == 3) {
            if (msg.equalsIgnoreCase("[BOSS] Livid: I respect you for making it to here, but I'll be your undoing.")) {
                active = true;
            }
        }
    }

    // change to this eventually
//    @SubscribeEvent
//    public void onBlockChange(S23BlockChangeEvent event) {
//        if (!(DungeonTracker.getTier().equals("M5") || DungeonTracker.getTier().equals("F5"))) return;
//
//        if (DungeonTracker.getPhase() == 2) {
//            if (event.getPacket().getBlockPosition().equals(blockPos)) {
//                int nbs = event.getPacket().getBlockState().getBlock().getMetaFromState(event.getPacket().getBlockState());
//                ChatUtils.system("Searching for livid " + nbs);
//
//                if (nbs != blockState) {
//                    blockState = nbs;
//                }
//            }
//        }
//    }

    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (active) {
            WorldClient world = Minecraft.getMinecraft().theWorld;

            Block block = world.getBlockState(blockPos).getBlock();

            int nbs = block.getMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(blockPos));
            if (nbs != blockState) {
                blockState = nbs;

                livid = null;
            }

            if (livid == null) {
                for (Entity e : world.getLoadedEntityList()) {
                    if (e instanceof EntityOtherPlayerMP) {
                        EntityOtherPlayerMP fake = (EntityOtherPlayerMP) e;
                        GameProfile profile = fake.getGameProfile();
                        if (profile.getProperties().containsKey("textures")) {
                            Property textureProp = profile.getProperties().get("textures").iterator().next();
                            String base64 = textureProp.getValue();
                            if (base64.equals(names.get(blockState))) {
                                livid = fake;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!highlightLivid.getValue()) return;
        if (!active) return;
        if (livid == null) return;
        if (!livid.isEntityAlive()) {
            livid = null;
            return;
        }
        RenderShapeUtils.drawBox(livid.posX, livid.posY+1, livid.posZ, 0.8, 2.0, 0.8, 0F, 1F, 1F, 1F, false, event.partialTicks);
    }
}
