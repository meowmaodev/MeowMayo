package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DungeonsExtras {
    private ToggleSetting announceMimic = (ToggleSetting) ConfigSettings.getSetting("Mimic Dead Message");
    private ToggleSetting announcePrince = (ToggleSetting) ConfigSettings.getSetting("Prince Dead Message");
    private ToggleSetting assumePrinceShard = (ToggleSetting) ConfigSettings.getSetting("Prince Shard");

    private static final String princeTexture = "ewogICJ0aW1lc3RhbXAiIDogMTU5MDE3Njk2NjUxNywKICAicHJvZmlsZUlkIiA6ICJhMmY4MzQ1OTVjODk0YTI3YWRkMzA0OTcxNmNhOTEwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiUHVuY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk5OTEyM2NmMGE0MTQ3N2Y3MDZmYzZhNTA4OTE0NjNlOGNiNTBkY2JkZDJjODFjNjUyZmNlZjZmZGJkZTIxYyIKICAgIH0KICB9Cn0=";
    private static final String mimicTexture = "ewogICJ0aW1lc3RhbXAiIDogMTY3Mjc2NTM1NTU0MCwKICAicHJvZmlsZUlkIiA6ICJhNWVmNzE3YWI0MjA0MTQ4ODlhOTI5ZDA5OTA0MzcwMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJXaW5zdHJlYWtlcnoiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTE5YzEyNTQzYmM3NzkyNjA1ZWY2OGUxZjg3NDlhZThmMmEzODFkOTA4NWQ0ZDRiNzgwYmExMjgyZDM1OTdhMCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";

    private static boolean princeDead = false;
    private static boolean mimicDead = false;

    @SubscribeEvent
    public void onChatReceived(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (!princeDead && announcePrince.getValue() && msg.equals("A Prince falls. +1 Bonus Score")) {
            princeDead = true;
            ChatUtils.partyChat("Prince Dead!");
        }
    }

    @SubscribeEvent
    public void onMobDeath(LivingDeathEvent event) {
        EntityLivingBase mob = event.entityLiving;

        if (!princeDead && announcePrince.getValue() && assumePrinceShard.getValue()) {
            if (event.entityLiving instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP fake = (EntityOtherPlayerMP) event.entityLiving;
                GameProfile profile = fake.getGameProfile();
                if (profile.getProperties().containsKey("textures")) {
                    Property textureProp = profile.getProperties().get("textures").iterator().next();
                    String base64 = textureProp.getValue();
                    if (base64.equals(princeTexture)) {
                        ChatUtils.partyChat("Prince Dead!");
                        princeDead = true;
                    }
                }
            }
        }

        if (!mimicDead && announceMimic.getValue()) {
            if (event.entityLiving instanceof EntityZombie) {
                EntityZombie zmb = (EntityZombie) event.entityLiving;
                ItemStack head = zmb.getEquipmentInSlot(4);
                if (head != null && head.getItem() == Items.skull) {
                    NBTTagCompound tag = head.getTagCompound();
                    if (tag != null && tag.hasKey("SkullOwner")) {
                        NBTTagCompound skullOwner = tag.getCompoundTag("SkullOwner");
                        if (skullOwner.hasKey("Properties")) {
                            NBTTagCompound properties = skullOwner.getCompoundTag("Properties");
                            if (properties.hasKey("textures")) {
                                NBTTagList textures = properties.getTagList("textures", 10);
                                if (textures.tagCount() > 0) {
                                    String value = textures.getCompoundTagAt(0).getString("Value");

                                    if (value.equals(mimicTexture)) {
                                        ChatUtils.partyChat("Mimic killed");
                                        mimicDead = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        princeDead = false;
        mimicDead = false;
    }
}
