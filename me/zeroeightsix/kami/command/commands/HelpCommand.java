/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.command.commands;

import java.util.Arrays;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.module.ModuleManager;

public class HelpCommand
extends Command {
    private static final Subject[] subjects = new Subject[]{new Subject(new String[]{"type", "int", "boolean", "double", "float"}, new String[]{"Every module has a value, and that value is always of a certain &atype. ", "These types are displayed in here as the ones java use. They mean the following:", "&aboolean&r: Enabled or not. Values &3true/false", "&afloat&r: A number with a decimal point", "&adouble&r: Like a float, but a more accurate decimal point", "&aint&r: A number with no decimal point"})};
    private static String subjectsList = "";

    public HelpCommand() {
        super("help", new SyntaxChunk[0]);
        this.setDescription("Delivers help on certain subjects. Use &a-help subjects&r for a list.");
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendStringChatMessage(new String[]{"commands&2 to view all available commands", "bind <module> <key>&2 to bind mods", "&2Press &r" + ModuleManager.getModuleByName("ClickGUI").getBindName() + "&2 to open GUI", "prefix <prefix>&r to change the command prefix.", "help <subjects:[subject]> &r for more help."});
        } else {
            String subject = args[0];
            if (subject.equals("subjects")) {
                Command.sendChatMessage("Subjects: " + subjectsList);
            } else {
                Subject subject1 = Arrays.stream(subjects).filter(subject2 -> {
                    for (String name : subject2.names) {
                        if (!name.equalsIgnoreCase(subject)) continue;
                        return true;
                    }
                    return false;
                }).findFirst().orElse(null);
                if (subject1 == null) {
                    Command.sendChatMessage("No help found for &a" + args[0]);
                    return;
                }
                Command.sendStringChatMessage(subject1.info);
            }
        }
    }

    static {
        for (Subject subject : subjects) {
            subjectsList = subjectsList + subject.names[0] + ", ";
        }
        subjectsList = subjectsList.substring(0, subjectsList.length() - 2);
    }

    private static class Subject {
        String[] names;
        String[] info;

        public Subject(String[] names, String[] info) {
            this.names = names;
            this.info = info;
        }
    }
}

