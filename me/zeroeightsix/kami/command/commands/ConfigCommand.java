/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.command.commands;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.DependantParser;
import me.zeroeightsix.kami.command.syntax.parsers.EnumParser;
import me.zeroeightsix.kami.gui.kami.KamiGUI;

public class ConfigCommand
extends Command {
    public ConfigCommand() {
        super("config", new ChunkBuilder().append("mode", true, new EnumParser(new String[]{"reload", "save", "path"})).append("path", true, new DependantParser(0, new DependantParser.Dependency(new String[][]{{"path", "path"}}, ""))).build());
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Missing argument &amode&r: Choose from reload, save or path");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "reload": {
                this.reload();
                break;
            }
            case "save": {
                try {
                    KamiMod.saveConfigurationUnsafe();
                    Command.sendChatMessage("Saved configuration!");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Command.sendChatMessage("Failed to save! " + e.getMessage());
                }
                break;
            }
            case "path": {
                if (args[1] == null) {
                    Path file = Paths.get(KamiMod.getConfigName(), new String[0]);
                    Command.sendChatMessage("Path to configuration: &a" + file.toAbsolutePath().toString());
                    break;
                }
                String newPath = args[1];
                if (!KamiMod.isFilenameValid(newPath)) {
                    Command.sendChatMessage("&a" + newPath + "&r is not a valid path");
                    break;
                }
                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("MeduzaLastConfig.txt", new String[0]), new OpenOption[0]);){
                    writer.write(newPath);
                    this.reload();
                    Command.sendChatMessage("Configuration path set to &a" + newPath + "&r!");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Command.sendChatMessage("Couldn't set path: " + e.getMessage());
                }
                break;
            }
            default: {
                Command.sendChatMessage("Incorrect mode, please choose from: reload, save or path");
            }
        }
    }

    private void reload() {
        KamiMod.getInstance().kamiGUI = new KamiGUI();
        KamiMod.getInstance().kamiGUI.initializeGUI();
        KamiMod.loadConfiguration();
        Command.sendChatMessage("Configuration reloaded!");
    }
}

