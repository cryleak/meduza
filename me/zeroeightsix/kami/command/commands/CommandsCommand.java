/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.command.commands;

import java.util.Comparator;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;

public class CommandsCommand
extends Command {
    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY);
    }

    @Override
    public void call(String[] args) {
        KamiMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing(command -> command.getLabel())).forEach(command -> Command.sendChatMessage("&2" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &2" + command.getDescription()));
    }
}

