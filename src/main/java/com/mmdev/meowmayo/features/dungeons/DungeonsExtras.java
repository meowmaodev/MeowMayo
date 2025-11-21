package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonsExtras {
    private ToggleSetting announceLeap = (ToggleSetting) ConfigSettings.getSetting("Announce Spirit Leap");

    private Pattern spiritLeap = Pattern.compile("You have teleported to (.+)!");

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String message = event.getUnformattedMessage();

        if (announceLeap.getValue()) {
            Matcher matcher = spiritLeap.matcher(message);
            if (matcher.matches()) {
                ChatUtils.partyChat("Leaped to " + matcher.group(1));
            }
        }
    }

    private final String princeTexture = "ewogICJ0aW1lc3RhbXAiIDogMTU5MDE3Njk2NjUxNywKICAicHJvZmlsZUlkIiA6ICJhMmY4MzQ1OTVjODk0YTI3YWRkMzA0OTcxNmNhOTEwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiUHVuY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk5OTEyM2NmMGE0MTQ3N2Y3MDZmYzZhNTA4OTE0NjNlOGNiNTBkY2JkZDJjODFjNjUyZmNlZjZmZGJkZTIxYyIKICAgIH0KICB9Cn0=";

    @SubscribeEvent
    public void onMobDeath(LivingDeathEvent event) {
        EntityLivingBase mob = event.entityLiving;

        if (event.entity.isEntityAlive()) return;

        if (event.entityLiving instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP fake = (EntityOtherPlayerMP) event.entityLiving;
            GameProfile profile = fake.getGameProfile();
            if (profile.getProperties().containsKey("textures")) {
                Property textureProp = profile.getProperties().get("textures").iterator().next();
                String base64 = textureProp.getValue();
                if (base64.equals(princeTexture)) {
                    ChatUtils.system("Prince killed");
                }
            }
        }
    }
}
