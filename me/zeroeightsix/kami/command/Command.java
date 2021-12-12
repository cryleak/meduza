/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentBase
 */
package me.zeroeightsix.kami.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public abstract class Command {
    public static Setting<String> commandPrefix = Settings.s("commandPrefix", ".");
    protected String label;
    protected String syntax;
    protected String description;
    protected SyntaxChunk[] syntaxChunks;

    public Command(String label, SyntaxChunk[] syntaxChunks) {
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
    }

    public static void sendChatMessage(String message) {
        Command.sendRawChatMessage(KamiMod.getInstance().guiManager.getTextColor() + "&2\u722a\u4e47\u15ea\u3129\u4e59\u5342" + ChatFormatting.DARK_GRAY.toString() + " &a\u27ab " + ChatFormatting.RESET.toString() + message);
    }

    public static void sendStringChatMessage(String[] messages) {
        Command.sendChatMessage("");
        for (String s : messages) {
            Command.sendRawChatMessage(s);
        }
    }

    public static void sendRawChatMessage(String message) {
        if (Wrapper.getPlayer() == null) {
            return;
        }
        Wrapper.getPlayer().func_145747_a((ITextComponent)new ChatMessage(message));
    }

    public static String getCommandPrefix() {
        return commandPrefix.getValue();
    }

    public static char SECTIONSIGN() {
        return '\u00a7';
    }

    public String getDescription() {
        return this.description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return this.label;
    }

    public abstract void call(String[] var1);

    public SyntaxChunk[] getSyntaxChunks() {
        return this.syntaxChunks;
    }

    protected SyntaxChunk getSyntaxChunk(String name) {
        for (SyntaxChunk c : this.syntaxChunks) {
            if (!c.getType().equals(name)) continue;
            return c;
        }
        return null;
    }

    public static class ChatMessage
    extends TextComponentBase {
        String text;

        public ChatMessage(String text) {
            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(text);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String replacement = "\u00a7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            this.text = sb.toString();
        }

        public String func_150261_e() {
            return this.text;
        }

        public ITextComponent func_150259_f() {
            return new ChatMessage(this.text);
        }
    }
}

