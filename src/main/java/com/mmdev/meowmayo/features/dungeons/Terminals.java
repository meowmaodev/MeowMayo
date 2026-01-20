package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.TextSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTracker;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.MathUtils;
import com.mmdev.meowmayo.utils.events.C0DCloseWindowSentEvent;
import com.mmdev.meowmayo.utils.events.S2DOpenWindowReceivedEvent;
import com.mmdev.meowmayo.utils.events.S2ECloseWindowReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import scala.Int;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Terminals {
    private long startTime = 0;
    private long lastClickTime = 0;
    private List<Long> clickIntervals = new ArrayList<>();
    private int initialPathSize = 0;
    private double totalDistance = 0;

    private final Minecraft mc = Minecraft.getMinecraft();

    private ToggleSetting startsWithSolver = (ToggleSetting) ConfigSettings.getSetting("Starts With Solver");
    private ToggleSetting allColorSolver = (ToggleSetting) ConfigSettings.getSetting("All Colors Solver");
    private ToggleSetting clickInOrderSolver = (ToggleSetting) ConfigSettings.getSetting("Click In Order Solver");
    private ToggleSetting announceMelody = (ToggleSetting) ConfigSettings.getSetting("Announce Melody");
    private TextSetting melodyMessage = (TextSetting) ConfigSettings.getSetting("Announce Melody Message");
    private ToggleSetting drawLine = (ToggleSetting) ConfigSettings.getSetting("Draw Line on Solvers");

    private TerminalType.MatchResult terminal = null;
    private String searchLetter = null;
    private String searchColor = null;

    private boolean found = false;
    private boolean renderLong = false;

    private Queue<Integer> pathQueue = new LinkedList<>();

    int guiLeft = 0;
    int guiTop = 0;

    public enum TerminalType {
        Numbers("Click in order!", false),
        Melody("Click the button on time!", false),
        Rubix("Change all to same color!", false),
        Panes("Correct all the panes!", false),
        Prefix("^What starts with: '(.)'\\?$", true),
        Color("^Select all the ([\\w\\s]+) items!$",  true),

        NONE(null,  false);

        private final String titlePart;
        private Pattern titleRegex = null;
        private final boolean regex;

        TerminalType(String titlePart, boolean regex) {
            this.titlePart = titlePart;
            this.regex = regex;

            if (regex) {
                titleRegex = Pattern.compile(titlePart);
            }
        }

        public static class MatchResult {
            public final TerminalType type;
            public final String extraData;

            public MatchResult(TerminalType type, String data) {
                this.type = type;
                this.extraData = data;
            }
        }

        public static MatchResult fromTitle(String title) {
            for (TerminalType type : values()) {
                if (type == NONE) continue;

                if (type.regex) {
                    Matcher m = type.titleRegex.matcher(title);
                    if (m.find()) return new TerminalType.MatchResult(type, m.group(1));
                } else {
                    if (title.equals(type.titlePart)) return new TerminalType.MatchResult(type, null);
                }
            }
            return new TerminalType.MatchResult(TerminalType.NONE, null);
        }
    }

    @SubscribeEvent
    public void onOpenWindow(S2DOpenWindowReceivedEvent event) {
        if (!(DungeonTracker.getTier().equals("M7") || DungeonTracker.getTier().equals("F7"))) return;
        if (DungeonTracker.getPhase() != 5) return;

        if (terminal == null) {
            String windowName = event.getPacket().getWindowTitle().getUnformattedText();

            terminal = TerminalType.fromTitle(windowName);

            if (terminal.type == TerminalType.NONE) {
                terminal = null;
                return;
            }

            if (terminal.type == TerminalType.Prefix) {
                searchLetter = terminal.extraData;
                renderLong = true;
            }

            if (terminal.type == TerminalType.Color) {
                searchColor = terminal.extraData;
                renderLong = true;
            }

            if (terminal.type == TerminalType.Melody && announceMelody.getValue()) {
                ChatUtils.partyChat(melodyMessage.getValue());
            }

            this.startTime = System.currentTimeMillis();
            this.lastClickTime = this.startTime;
        }
    }

    private void reset() {
        // terminal info
        terminal = null;
        searchLetter = null;
        searchColor = null;
        found = false;
        renderLong = false;

        // solver info
        pathQueue.clear();
        guiLeft = 0;
        guiTop = 0;

        // timing data
        startTime = 0;
        lastClickTime = 0;
        initialPathSize = 0;
        totalDistance = 0;
        clickIntervals.clear();
    }

    @SubscribeEvent
    public void onServerCloseWindow(S2ECloseWindowReceivedEvent event) {
        reset();
    }

    @SubscribeEvent
    public void onClientCloseWindow(C0DCloseWindowSentEvent event) {
        reset();
    }

    @SubscribeEvent
    public void onRenderContainer(GuiScreenEvent.DrawScreenEvent event) {
        if (!(event.gui instanceof GuiChest)) return;

        if (!pathQueue.isEmpty()) {
            renderPath();
        }
    }

    @SubscribeEvent // this will change on a packet set slot when i can be asked to rewrite it to fix clicking out of order
    public void onServerTick(S32ReceivedEvent event) { // we are checking every tick for items in queue because uhhh this wont break if the user clicks out of order uh huh
        if (terminal == null) return;
        if (!(mc.currentScreen instanceof GuiChest)) return;

        GuiChest gui = (GuiChest) mc.currentScreen;
        ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
        if (!found) { // only resolve when the gui is closed
            guiLeft = (gui.width - 176) / 2;
            guiTop = (gui.height - (114 + (chest.getLowerChestInventory().getSizeInventory() / 9) * 18)) / 2;

            if (terminal.type == TerminalType.Prefix && startsWithSolver.getValue()) {
                generateStartsWithPath(chest);
            }

            if (terminal.type == TerminalType.Color && allColorSolver.getValue()) {
                generateAllColorsPath(chest);
            }

            if (terminal.type == TerminalType.Numbers && clickInOrderSolver.getValue()) {
                generateNumbersPath(chest);
            }

            return;
        }

        if (!pathQueue.isEmpty()) {
            int head = pathQueue.peek();

            if (terminal.type == TerminalType.Numbers) {
                if (isSlotClickedGreen(chest, head) && (System.currentTimeMillis() - lastClickTime > 10)) {
                    long now = System.currentTimeMillis();
                    long diff = now - lastClickTime;

                    clickIntervals.add(diff);
                    lastClickTime = now;
                    pathQueue.poll();

                    if (pathQueue.isEmpty()) {
                        sendSummary(now);
                    }
                }
            } else {
                if (isSlotClickedEnch(chest, head) && (System.currentTimeMillis() - lastClickTime > 10)) {
                    long now = System.currentTimeMillis();
                    long diff = now - lastClickTime;

                    clickIntervals.add(diff);
                    lastClickTime = now;
                    pathQueue.poll();

                    if (pathQueue.isEmpty()) {
                        sendSummary(now);
                    }
                }
            }
        }
    }

    private void renderPath() {
        if (pathQueue.isEmpty() || !(mc.thePlayer.openContainer instanceof ContainerChest)) return;
        ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

        GlStateManager.pushMatrix();

        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        int node = 0;

        Slot s1 = null;
        Slot s2 = null;
        Slot s3 = null;

        for (Integer idx : pathQueue) {
            Slot s = chest.getSlot(idx);
            int color = 0xff4affe7;
            if (node == 0) {
                s1 = s;
                color = 0xff22ff00;
            } else if (node == 1) {
                s2 = s;
                color = 0xff8ff57f;
            } else if (node == 2) {
                s3 = s;
                color = 0xffc4f7bc;
            }

            Gui.drawRect(guiLeft + s.xDisplayPosition, guiTop + s.yDisplayPosition,
                    guiLeft + s.xDisplayPosition + 16, guiTop + s.yDisplayPosition + 16, color);

            node++;

            if (!renderLong && node == 3) break;
        }

        GlStateManager.disableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (drawLine.getValue()) {
            GL11.glLineWidth(3.0f);
            GL11.glBegin(GL11.GL_LINES);

            if (s1 != null && s2 != null) {
                GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
                GL11.glVertex2f(guiLeft + s1.xDisplayPosition + 8, guiTop + s1.yDisplayPosition + 8);
                GL11.glVertex2f(guiLeft + s2.xDisplayPosition + 8, guiTop + s2.yDisplayPosition + 8);

                if (s3 != null) {
                    GL11.glColor4f(0.8f, 0.0f, 0.1f, 0.6f);
                    GL11.glVertex2f(guiLeft + s2.xDisplayPosition + 8, guiTop + s2.yDisplayPosition + 8);
                    GL11.glVertex2f(guiLeft + s3.xDisplayPosition + 8, guiTop + s3.yDisplayPosition + 8);
                }
            }

            GL11.glEnd();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private void generateNumbersPath(ContainerChest chest) {
        int chestSlotsCount = chest.getLowerChestInventory().getSizeInventory();
        if (!chest.getSlot(chestSlotsCount - 1).getHasStack()) return;

        List<Integer> unvisited = new ArrayList<>();
        for (int i = 0; i < chestSlotsCount; i++) {
            Slot s = chest.getSlot(i);
            if (!s.getHasStack()) continue;

            ItemStack stack = s.getStack();
            if (stack.getItemDamage() == 14 && stack.stackSize <= 14) {
                unvisited.add(i);
            }
        }

        if (unvisited.size() < 14) return;

        unvisited.sort(Comparator.comparingInt(i -> chest.getSlot(i).getStack().stackSize));

        pathQueue.clear();
        pathQueue.addAll(unvisited);

        found = true;
        initialPathSize = 14;

        totalDistance = 0;
        for (int i = 0; i < unvisited.size() - 1; i++) {
            Slot current = chest.getSlot(unvisited.get(i));
            Slot next = chest.getSlot(unvisited.get(i + 1));

            totalDistance += MathUtils.get2dDistance(
                    current.xDisplayPosition, current.yDisplayPosition,
                    next.xDisplayPosition, next.yDisplayPosition
            );
        }

        this.startTime = System.currentTimeMillis();
        this.lastClickTime = this.startTime;

        double distInSlots = totalDistance / 18.0;
        double avgDistSlots = distInSlots / 13.0;

        ChatUtils.system(String.format("§aNumbers Terminal Opened! §7Total Dist: §f%.0f slots §7| §aAvg Jump: §f%.2f slots",
                distInSlots, avgDistSlots));
    }

    private void calculateOptimalPath(List<Integer> unvisited, ContainerChest chest) {
        if (unvisited.isEmpty()) return;

        int chestSlotsCount = chest.getLowerChestInventory().getSizeInventory();
        Slot startSlot = chest.getSlot(chestSlotsCount - 5);

        float curX = startSlot.xDisplayPosition + 8;
        float curY = startSlot.yDisplayPosition + 8;

        while (!unvisited.isEmpty()) {
            Integer closestIDX = null;
            double minSqDist = Double.MAX_VALUE;

            for (Integer index : unvisited) {
                Slot s = chest.getSlot(index);

                float sx = s.xDisplayPosition + 8;
                float sy = s.yDisplayPosition + 8;

                double dSq = MathUtils.get2dDistanceSq(curX, curY, sx, sy);

                if (dSq < minSqDist) {
                    minSqDist = dSq;
                    closestIDX = index;
                }
            }

            if (closestIDX != null) {
                Slot closestSlot = chest.getSlot(closestIDX);
                float sx = closestSlot.xDisplayPosition + 8;
                float sy = closestSlot.yDisplayPosition + 8;

                totalDistance += MathUtils.get2dDistance(curX, curY, sx, sy);

                pathQueue.add(closestIDX);
                unvisited.remove(closestIDX);

                curX = sx;
                curY = sy;
            }
        }

        if (pathQueue.size() > 3) {
            optimize2Opt(chest);
        }
    }

    private void optimize2Opt(ContainerChest chest) {
        List<Integer> list = new ArrayList<>(pathQueue);
        boolean improved = true;

        while (improved) {
            improved = false;
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = i + 2; j < list.size() - 1; j++) {
                    Slot a = chest.getSlot(list.get(i));
                    Slot b = chest.getSlot(list.get(i + 1));
                    Slot c = chest.getSlot(list.get(j));
                    Slot d = chest.getSlot(list.get(j + 1));

                    double currentDist = MathUtils.get2dDistance(a.xDisplayPosition, a.yDisplayPosition, b.xDisplayPosition, b.yDisplayPosition)
                            + MathUtils.get2dDistance(c.xDisplayPosition, c.yDisplayPosition, d.xDisplayPosition, d.yDisplayPosition);

                    double newDist = MathUtils.get2dDistance(a.xDisplayPosition, a.yDisplayPosition, c.xDisplayPosition, c.yDisplayPosition)
                            + MathUtils.get2dDistance(b.xDisplayPosition, b.yDisplayPosition, d.xDisplayPosition, d.yDisplayPosition);

                    if (newDist < currentDist) {
                        Collections.reverse(list.subList(i + 1, j + 1));
                        improved = true;
                    }
                }
            }
        }

        pathQueue.clear();
        pathQueue.addAll(list);
    }

    private void generateStartsWithPath(ContainerChest chest) {
        List<Integer> unvisited = new ArrayList<>();
        int chestSlotsCount = chest.inventorySlots.size() - 36;

        if (!chest.getSlot(chestSlotsCount - 1).getHasStack()) return;

        for (int i = 0; i < chestSlotsCount; i++) {
            Slot s = chest.getSlot(i);
            if (!s.getHasStack()) continue;

            String name = EnumChatFormatting.getTextWithoutFormattingCodes(s.getStack().getDisplayName().toLowerCase());
            if (name.startsWith(searchLetter.toLowerCase())) {
                unvisited.add(i);
            }
        }

        calculateOptimalPath(unvisited, chest);

        if (!pathQueue.isEmpty()) {
            found = true;

            initialPathSize = pathQueue.size();
            startTime = System.currentTimeMillis();
            lastClickTime = startTime;

            double avgDist = totalDistance / initialPathSize / 18;
            ChatUtils.system(String.format("§aStarts With Terminal Opened! Items: §f%d §7| §aAvg Distance: §f%.2f slots",
                    initialPathSize, avgDist));
        }
    }

    private boolean isItemTargetColor(ItemStack stack) {
        if (stack == null) return false;

        String itemName = EnumChatFormatting.getTextWithoutFormattingCodes(stack.getDisplayName().toLowerCase());
        String target = searchColor.toLowerCase().trim();

        // exceptions?
        if (target.equals("white") && (itemName.contains("bone meal") || itemName.equals("wool"))) return true;
        if (target.equals("black") && itemName.contains("ink sac")) return true;
        if (target.equals("blue") && itemName.contains("lapis lazuli")) return true;
        if (target.equals("yellow") && itemName.contains("dandelion yellow")) return true;
        if (target.equals("silver") && itemName.contains("light gray dye")) return true;
        if (target.equals("red") && itemName.contains("rose red")) return true;
        if (target.equals("brown") && itemName.contains("cocoa bean")) return true;
        if (target.equals("green") && (itemName.contains("cactus green"))) return true;

        return itemName.startsWith(target);
    }

    private void generateAllColorsPath(ContainerChest chest) {
        List<Integer> unvisited = new ArrayList<>();
        int chestSlotsCount = chest.inventorySlots.size() - 36;

        if (!chest.getSlot(chestSlotsCount - 1).getHasStack()) return; // this can be hard coded but idc

        for (int i = 0; i < chestSlotsCount; i++) {
            Slot s = chest.getSlot(i);
            if (!s.getHasStack()) continue;

            if (isItemTargetColor(s.getStack())) {
                unvisited.add(i);
            }
        }

        calculateOptimalPath(unvisited, chest);

        if (!pathQueue.isEmpty()) {
            found = true;

            initialPathSize = pathQueue.size();
            startTime = System.currentTimeMillis();
            lastClickTime = startTime;

            double avgDist = totalDistance / initialPathSize / 18;
            ChatUtils.system(String.format("§aAll Colors Terminal Opened! Items: §f%d §7| §aAvg Distance: §f%.2f slots",
                    initialPathSize, avgDist));
        }
    }

    private void sendSummary(long endTime) {
        long totalTime = endTime - startTime;
        long firstItem = clickIntervals.get(0);

        double avgBetween = 0;
        if (clickIntervals.size() > 1) {
            long sum = 0;
            for (int i = 1; i < clickIntervals.size(); i++) sum += clickIntervals.get(i);
            avgBetween = (double) sum / (clickIntervals.size() - 1);
        }

        ChatUtils.system("Terminal Finished!\n" +
                        "§eReaction Time: §f" + firstItem + "ms\n" +
                        "§eAvg Interval: §f" + avgBetween + "ms\n" +
                        "§6Time Taken: §f" + (totalTime / 1000.0) + "s"
                );
    }

    private boolean isSlotClickedEnch(ContainerChest chest, int idx) { // slop of the finest quality!
        Slot slot = chest.getSlot(idx);
        if (slot == null || !slot.getHasStack()) {
            return true;
        }

        ItemStack stack = slot.getStack();

        return stack.isItemEnchanted();
    }

    private boolean isSlotClickedGreen(ContainerChest chest, int idx) {
        Slot slot = chest.getSlot(idx);
        if (slot == null || !slot.getHasStack()) {
            return true;
        }

        ItemStack stack = slot.getStack();

        return stack.getItemDamage() == 5;
    }
}