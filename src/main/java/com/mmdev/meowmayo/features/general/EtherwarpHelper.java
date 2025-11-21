package com.mmdev.meowmayo.features.general;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.FloatSliderSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.RenderShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;

public class EtherwarpHelper {
    private static ToggleSetting etherwarpHelper = (ToggleSetting) ConfigSettings.getSetting("Etherwarp Helper");
    private static ToggleSetting fakePlayer = (ToggleSetting) ConfigSettings.getSetting("Fake Player Etherwarp");
    private static FloatSliderSetting fakePlayerTrans = (FloatSliderSetting) ConfigSettings.getSetting("Fake Player Transparency");

    private static Set<Block> special = new HashSet<Block>();

    static { // this might need to be expanded
        special.add(Blocks.torch);
        special.add(Blocks.tripwire);
        special.add(Blocks.carpet);
        special.add(Blocks.skull);
        special.add(Blocks.tripwire_hook);
        special.add(Blocks.lever);
        special.add(Blocks.stone_button);
        special.add(Blocks.flower_pot);
        special.add(Blocks.wooden_button);
        special.add(Blocks.sapling);
        special.add(Blocks.tallgrass);
        special.add(Blocks.fire);
        special.add(Blocks.rail);
        special.add(Blocks.activator_rail);
        special.add(Blocks.detector_rail);
        special.add(Blocks.golden_rail);
        special.add(Blocks.redstone_torch);
        special.add(Blocks.redstone_wire);
        special.add(Blocks.ladder);
        special.add(Blocks.stone_pressure_plate);
        special.add(Blocks.wooden_pressure_plate);
        special.add(Blocks.heavy_weighted_pressure_plate);
        special.add(Blocks.light_weighted_pressure_plate);
    }

    public static final double range = 61.0;
    public static final double step = 0.1;

    BlockPos hitBlock = null;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null) return;

        EntityPlayerSP player = mc.thePlayer;
        World world = mc.theWorld;

        if (world == null || player == null) return;

        if (etherwarpHelper.getValue()) {
            ItemStack held = player.getHeldItem();
            if (held == null) return;

            boolean isAotv = (held.getDisplayName().toLowerCase().contains("aspect of the void") && held.getItem() == Items.diamond_shovel);

            if (!player.isSneaking() || !isAotv) {
                hitBlock = null;
                return;
            }

            Vec3 eye = player.getPositionEyes(1.0F);
            Vec3 look = player.getLook(1.0F);

            double x = eye.xCoord;
            double y = eye.yCoord;
            double z = eye.zCoord;

            hitBlock = null;

            for (double d = 0; d <= range; d += step) {
                // Move along look vector
                x += look.xCoord * step;
                y += look.yCoord * step;
                z += look.zCoord * step;

                BlockPos pos = new BlockPos(x, y, z);
                Block block = world.getBlockState(pos).getBlock();

                if (block != Blocks.air) {
                    if (special.contains(block)) continue;

                    BlockPos above1 = pos.up(1);
                    BlockPos above2 = pos.up(2);

                    Block blockAbove1 = world.getBlockState(above1).getBlock();
                    Block blockAbove2 = world.getBlockState(above2).getBlock();

                    boolean allowed = (
                                    (blockAbove1 == Blocks.air || special.contains(blockAbove1)) &&
                                    (blockAbove2 == Blocks.air || special.contains(blockAbove2))
                            );

                    if (!allowed) return;

                    hitBlock = pos;
                    break; // first solid block found
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (etherwarpHelper.getValue() && hitBlock != null) {
            Minecraft mc = Minecraft.getMinecraft();
            float partialTicks = event.partialTicks;

            RenderShapeUtils.drawFilledBox(hitBlock.getX() + 0.5, hitBlock.getY() + 0.5, hitBlock.getZ() + 0.5, 1.01, 1.01, 1.01, 0f, 0f, 1f, 1f, false, partialTicks);

            if (fakePlayer.getValue()) {
                EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(
                        mc.theWorld,
                        mc.thePlayer.getGameProfile()
                );

                fakePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                fakePlayer.prevRotationYaw = mc.thePlayer.rotationYaw;

                fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
                fakePlayer.prevRotationYawHead = mc.thePlayer.rotationYawHead;

                fakePlayer.rotationPitch = mc.thePlayer.rotationPitch;
                fakePlayer.prevRotationPitch = mc.thePlayer.rotationPitch;

                fakePlayer.renderYawOffset = mc.thePlayer.renderYawOffset;
                fakePlayer.prevRenderYawOffset = mc.thePlayer.renderYawOffset;

                fakePlayer.limbSwing = mc.thePlayer.limbSwing;

                fakePlayer.limbSwingAmount = mc.thePlayer.limbSwingAmount;
                fakePlayer.prevLimbSwingAmount = mc.thePlayer.prevLimbSwingAmount;

                fakePlayer.swingProgress = mc.thePlayer.swingProgress;

                // Position the fake player above the block
                fakePlayer.setPosition(
                        hitBlock.getX() + 0.5,         // center X
                        hitBlock.getY() + 1.0,         // 1 block above the block
                        hitBlock.getZ() + 0.5          // center Z
                );

                // Copy rotation from the real player
                fakePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
                fakePlayer.rotationPitch = mc.thePlayer.rotationPitch;

                // Interpolated camera position for smooth rendering
                Entity viewer = mc.getRenderViewEntity();
                double camX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
                double camY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
                double camZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

                GlStateManager.pushMatrix();
                GlStateManager.translate(-camX, -camY, -camZ);

                // Optional: make the fake player semi-transparent
                GlStateManager.enableBlend();
                GlStateManager.disableLighting();
                GlStateManager.color(1f, 1f, 1f, fakePlayerTrans.getValue()); // 50% alpha

                RenderManager renderManager = mc.getRenderManager();
                renderManager.renderEntityWithPosYaw(
                        fakePlayer,
                        fakePlayer.posX,
                        fakePlayer.posY,
                        fakePlayer.posZ,
                        fakePlayer.rotationYaw,
                        partialTicks
                );

                // Restore GL state
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
}
