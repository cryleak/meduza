/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.zeroeightsix.kami.module.modules.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.ChatTextUtils;
import me.zeroeightsix.kami.util.FileHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Info(name="Spammer", category=Module.Category.CHAT, description="SPAM")
public class Spammer
extends Module {
    private static final String fileName = "Hephaestus_Spammer.txt";
    private static final String defaultMessage = "Join 0b0t.org - Worlds oldest Minecraft Server!";
    private static List<String> spamMessages = new ArrayList<String>();
    private static Random rnd = new Random();
    private static Timer timer;
    private static TimerTask task;
    private Setting<Boolean> random = this.register(Settings.b("Random", false));
    private Setting<Boolean> greentext = this.register(Settings.b("Greentext", false));
    private Setting<Boolean> randomsuffix = this.register(Settings.b("Anti Spam", true));
    private Setting<Integer> delay = this.register(Settings.integerBuilder("Send Delay").withRange(100, 60000).withValue(4000).build());
    private Setting<Boolean> readfile = this.register(Settings.b("Load File", false));

    @Override
    public void onEnable() {
        this.readSpamFile();
        timer = new Timer();
        if (Spammer.mc.field_71439_g == null) {
            this.disable();
            return;
        }
        task = new TimerTask(){

            @Override
            public void run() {
                Spammer.this.runCycle();
            }
        };
        timer.schedule(task, 0L, (long)this.delay.getValue().intValue());
    }

    @Override
    public void onDisable() {
        timer.cancel();
        timer.purge();
        spamMessages.clear();
    }

    private void runCycle() {
        if (Spammer.mc.field_71439_g == null) {
            return;
        }
        if (this.readfile.getValue().booleanValue()) {
            this.readSpamFile();
            this.readfile.setValue(false);
        }
        if (spamMessages.size() > 0) {
            String messageOut;
            if (this.random.getValue().booleanValue()) {
                int index = rnd.nextInt(spamMessages.size());
                messageOut = spamMessages.get(index);
                spamMessages.remove(index);
            } else {
                messageOut = spamMessages.get(0);
                spamMessages.remove(0);
            }
            spamMessages.add(messageOut);
            if (this.greentext.getValue().booleanValue()) {
                messageOut = "> " + messageOut;
            }
            int reserved = 0;
            ArrayList<String> messageAppendix = new ArrayList<String>();
            if (ModuleManager.isModuleEnabled("ChatSuffix")) {
                reserved += " \u23d0 \u722a\u4e47\u15ea\u3129\u4e59\u5342".length();
            }
            if (this.randomsuffix.getValue().booleanValue()) {
                messageAppendix.add(ChatTextUtils.generateRandomHexSuffix(2));
            }
            if (messageAppendix.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(" ");
                for (String msg : messageAppendix) {
                    sb.append(msg);
                }
                messageOut = ChatTextUtils.cropMaxLengthMessage(messageOut, sb.toString().length() + reserved);
                messageOut = messageOut + sb.toString();
            }
            Spammer.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage(messageOut.replaceAll("\u00a7", "")));
        }
    }

    private void readSpamFile() {
        List<String> fileInput = FileHelper.readTextFileAllLines("Meduza_Spammer.txt");
        Iterator<String> i = fileInput.iterator();
        spamMessages.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            spamMessages.add(s);
        }
        if (spamMessages.size() == 0) {
            spamMessages.add("Meduza on top! CrawlerExE rules me and all!");
        }
    }
}

