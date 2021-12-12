/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;

@Module.Info(name="Strength-Detect", category=Module.Category.COMBAT, description="Detects when players have Strength 2")
public class StrengthDetect
extends Module {
    private Setting<Boolean> watermark = this.register(Settings.b("Watermark", true));
    private Setting<Boolean> color = this.register(Settings.b("Color", false));
    private Set<EntityPlayer> str = Collections.newSetFromMap(new WeakHashMap());
    public static final Minecraft mc = Minecraft.func_71410_x();

    @Override
    public void onUpdate() {
        for (EntityPlayer player : StrengthDetect.mc.field_71441_e.field_73010_i) {
            if (player.equals((Object)StrengthDetect.mc.field_71439_g)) continue;
            if (player.func_70644_a(MobEffects.field_76420_g) && !this.str.contains((Object)player)) {
                if (this.watermark.getValue().booleanValue()) {
                    if (this.color.getValue().booleanValue()) {
                        Command.sendChatMessage("&a" + player.getDisplayNameString() + " Has Gained Strength 2 Potion Buff,");
                    } else {
                        Command.sendChatMessage(player.getDisplayNameString() + " Has Gained Strength 2 Potion Buff,");
                    }
                } else if (this.color.getValue().booleanValue()) {
                    Command.sendRawChatMessage("&a" + player.getDisplayNameString() + " Has Gained Strength 2 Potion Buff,");
                } else {
                    Command.sendRawChatMessage(player.getDisplayNameString() + " Has Gained Strength 2 Potion Buff,");
                }
                this.str.add(player);
            }
            if (!this.str.contains((Object)player) || player.func_70644_a(MobEffects.field_76420_g)) continue;
            if (this.watermark.getValue().booleanValue()) {
                if (this.color.getValue().booleanValue()) {
                    Command.sendChatMessage("&a" + player.getDisplayNameString() + " Has Lost Strength 2 Potion Buff,");
                } else {
                    Command.sendChatMessage(player.getDisplayNameString() + " Has Lost Strength 2 Potion Buff,");
                }
            } else if (this.color.getValue().booleanValue()) {
                Command.sendRawChatMessage("&a" + player.getDisplayNameString() + " Has Lost Strength 2 Potion Buff,");
            } else {
                Command.sendRawChatMessage(player.getDisplayNameString() + " Has Lost Strength 2 Potion Buff,");
            }
            this.str.remove((Object)player);
        }
    }
}

