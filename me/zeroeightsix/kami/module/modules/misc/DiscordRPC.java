/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.module.modules.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.zeroeightsix.kami.module.Module;

@Module.Info(name="DiscordRPC", category=Module.Category.MISC, description="Discord RPC")
public class DiscordRPC
extends Module {
    private static final String applicationId = "611180592192552970";
    private club.minnced.discord.rpc.DiscordRPC discordRPC;

    @Override
    public void onEnable() {
        this.init();
    }

    @Override
    public void onDisable() {
        this.discordRPC.Discord_Shutdown();
    }

    private void init() {
        this.discordRPC = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        this.discordRPC.Discord_Initialize("663453995120984073", handlers, true, "");
        DiscordRichPresence presence = new DiscordRichPresence();
        this.discordRPC.Discord_UpdatePresence(presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                this.discordRPC.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }, "RPC-Callback-Handler").start();
    }
}

