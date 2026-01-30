package com.mmdev.meowmayo;

import com.mmdev.meowmayo.features.dungeons.tracker.DungeonStats;
import com.mmdev.meowmayo.features.general.farming.PestAlert;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraStats;
import com.mmdev.meowmayo.keybinds.KeyInputHandler;
import com.mmdev.meowmayo.keybinds.ModKeybinds;
import com.mmdev.meowmayo.utils.PartyUtils;
import com.mmdev.meowmayo.utils.ScoreboardUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ClientCommandHandler;

import com.mmdev.meowmayo.config.ModConfig;
import com.mmdev.meowmayo.gui.GuiHandler;
import com.mmdev.meowmayo.commands.*;
import com.mmdev.meowmayo.features.dungeons.*;
import com.mmdev.meowmayo.features.general.*;
import com.mmdev.meowmayo.features.kuudra.*;
import com.mmdev.meowmayo.features.party.*;
import com.mmdev.meowmayo.utils.TextOverlayUtils;
import com.mmdev.meowmayo.utils.PartyCommandListUtils;

@Mod(modid = MeowMayo.MODID, version = MeowMayo.VERSION, clientSideOnly = true)
public class MeowMayo {
    public static final String MODID = "meowmayo";
    public static final String VERSION = "1.0.2";

    // Initialization
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MixinBootstrap.init();

        org.lwjgl.opengl.Display.setTitle("MeowMayo");
        ModConfig.init(event.getSuggestedConfigurationFile().getParentFile());
        PartyCommandListUtils.init(event.getSuggestedConfigurationFile().getParentFile());
        DungeonStats.init(event.getSuggestedConfigurationFile().getParentFile());
        KuudraStats.init(event.getSuggestedConfigurationFile().getParentFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModKeybinds.init();

        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());

        // Register commands
        ClientCommandHandler.instance.registerCommand(new SettingsCommand());
        ClientCommandHandler.instance.registerCommand(new GetPearlsCommand());
        ClientCommandHandler.instance.registerCommand(new StorageCommand());
        ClientCommandHandler.instance.registerCommand(new KuudraTimeCommand());
        ClientCommandHandler.instance.registerCommand(new ResetKuudraTimeCommand());
        ClientCommandHandler.instance.registerCommand(new DungeonTimeCommand());
        ClientCommandHandler.instance.registerCommand(new ResetDungeonTimeCommand());
        ClientCommandHandler.instance.registerCommand(new PartyCommandsWhitelistCommand());
        ClientCommandHandler.instance.registerCommand(new PartyCommandsBlacklistCommand());

        DungeonTrack.init();
        KuudraTrack.init();

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new TextOverlayUtils());
        MinecraftForge.EVENT_BUS.register(new PartyUtils());
        MinecraftForge.EVENT_BUS.register(new ScoreboardUtils());

        MinecraftForge.EVENT_BUS.register(new Terminals());

        MinecraftForge.EVENT_BUS.register(new PestAlert());

        MinecraftForge.EVENT_BUS.register(new DungeonsExtras());
        MinecraftForge.EVENT_BUS.register(new LeapExtras());
        MinecraftForge.EVENT_BUS.register(new CampHelper());
        MinecraftForge.EVENT_BUS.register(new F5BossFeatures());
        MinecraftForge.EVENT_BUS.register(new F7BossFeatures());

        MinecraftForge.EVENT_BUS.register(new BackpackTracker());
        MinecraftForge.EVENT_BUS.register(new InvulnerabilityItems());
        MinecraftForge.EVENT_BUS.register(new BuffItems());
        MinecraftForge.EVENT_BUS.register(new ToggleSprint());
        MinecraftForge.EVENT_BUS.register(new EtherwarpHelper());
        MinecraftForge.EVENT_BUS.register(new CoordinateWaypoints());

        MinecraftForge.EVENT_BUS.register(new PartyCommands());

        MinecraftForge.EVENT_BUS.register(new KuudraExtras());
        MinecraftForge.EVENT_BUS.register(new SupplyFeatures());
        MinecraftForge.EVENT_BUS.register(new RendFeatures());
        MinecraftForge.EVENT_BUS.register(new StunFeatures());
    }
}
