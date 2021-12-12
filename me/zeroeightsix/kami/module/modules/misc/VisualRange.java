/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.zeroeightsix.kami.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.Friends;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Info(name="VisualRange", description="Reports Players in VisualRange", category=Module.Category.MISC)
public class VisualRange
extends Module {
    private Setting<Boolean> publicChat = this.register(Settings.b("PublicChat", false));
    private Setting<Boolean> leaving = this.register(Settings.b("Leaving", false));
    private List<String> knownPlayers;

    @Override
    public void onUpdate() {
        if (VisualRange.mc.field_71439_g == null) {
            return;
        }
        ArrayList<String> tickPlayerList = new ArrayList<String>();
        for (Entity entity : VisualRange.mc.field_71441_e.func_72910_y()) {
            if (!(entity instanceof EntityPlayer)) continue;
            tickPlayerList.add(entity.func_70005_c_());
        }
        if (tickPlayerList.size() > 0) {
            for (String playerName : tickPlayerList) {
                if (playerName.equals(VisualRange.mc.field_71439_g.func_70005_c_()) || this.knownPlayers.contains(playerName)) continue;
                this.knownPlayers.add(playerName);
                if (this.publicChat.getValue().booleanValue()) {
                    VisualRange.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("Oh hey, there is " + playerName + " in my range! This announcement was presented by: " + "\u722a\u4e47\u15ea\u3129\u4e59\u5342"));
                } else if (Friends.isFriend(playerName)) {
                    this.sendNotification("[VisualRange] " + ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                } else {
                    this.sendNotification("[VisualRange] " + ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                }
                return;
            }
        }
        if (this.knownPlayers.size() > 0) {
            for (String playerName : this.knownPlayers) {
                if (tickPlayerList.contains(playerName)) continue;
                this.knownPlayers.remove(playerName);
                if (this.leaving.getValue().booleanValue()) {
                    if (this.publicChat.getValue().booleanValue()) {
                        VisualRange.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("I cant see " + playerName + " anymore! This announcement was presented by: " + "\u722a\u4e47\u15ea\u3129\u4e59\u5342"));
                    } else if (Friends.isFriend(playerName)) {
                        this.sendNotification("[VisualRange] " + ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                    } else {
                        this.sendNotification("[VisualRange] " + ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                    }
                }
                return;
            }
        }
    }

    private void sendNotification(String s) {
        Command.sendChatMessage(s);
    }

    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<String>();
    }
}

