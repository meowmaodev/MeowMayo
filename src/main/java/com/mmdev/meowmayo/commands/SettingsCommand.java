package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.gui.MainGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import java.util.List;
import java.util.Arrays;

import com.mmdev.meowmayo.gui.SettingsGui;
import com.mmdev.meowmayo.gui.GuiHandler;

public class SettingsCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "meowmayo";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("meow", "mm");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/MeowMayo - opens settings menu";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        GuiHandler.setGuiToOpen(new MainGui());
    }
}
