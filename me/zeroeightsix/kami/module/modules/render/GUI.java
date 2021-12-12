/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.zeroeightsix.kami.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.GuiManager;

@Module.Info(name="GUI", category=Module.Category.RENDER, description="Change the GUI")
public class GUI
extends Module {
    private Setting<Integer> guiRed = this.register(Settings.integerBuilder("GuiRed").withMinimum(1).withValue(143).withMaximum(255).build());
    private Setting<Integer> guiGreen = this.register(Settings.integerBuilder("GuiGreen").withMinimum(1).withValue(182).withMaximum(255).build());
    private Setting<Integer> guiBlue = this.register(Settings.integerBuilder("GuiBlue").withMinimum(1).withValue(65).withMaximum(255).build());
    private Setting<TextColor> textColor = this.register(Settings.e("TextColor", TextColor.GRAY));
    private Setting<GuiManager.ModuleListMode> moduleListMode = this.register(Settings.e("ModuleListMode", GuiManager.ModuleListMode.STATIC));
    private Setting<Integer> moduleListRed = this.register(Settings.integerBuilder("ModuleListRed").withMinimum(1).withValue(255).withMaximum(255).withVisibility(v -> this.moduleListMode.getValue().equals((Object)GuiManager.ModuleListMode.STATIC)).build());
    private Setting<Integer> moduleListGreen = this.register(Settings.integerBuilder("ModuleListGreen").withMinimum(1).withValue(255).withMaximum(255).withVisibility(v -> this.moduleListMode.getValue().equals((Object)GuiManager.ModuleListMode.STATIC)).build());
    private Setting<Integer> moduleListBlue = this.register(Settings.integerBuilder("ModuleListBlue").withMinimum(1).withValue(255).withMaximum(255).withVisibility(v -> this.moduleListMode.getValue().equals((Object)GuiManager.ModuleListMode.STATIC)).build());
    private Setting<Integer> textRadarPlayers = this.register(Settings.integerBuilder("TextRadarPlayers").withMinimum(1).withValue(8).withMaximum(32).build());
    private Setting<Boolean> textRadarPots = this.register(Settings.b("TextRadarPots", true));
    private GuiManager guiManager;

    @Override
    public void onDisable() {
        this.enable();
    }

    @Override
    public void onUpdate() {
        if (this.guiManager == null) {
            this.guiManager = KamiMod.getInstance().guiManager;
        }
        this.guiManager.setGuiColors((float)this.guiRed.getValue().intValue() / 255.0f, (float)this.guiGreen.getValue().intValue() / 255.0f, (float)this.guiBlue.getValue().intValue() / 255.0f);
        if (this.textColor.getValue().equals((Object)TextColor.BLACK)) {
            this.guiManager.setTextColor(ChatFormatting.BLACK.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.DARK_BLUE)) {
            this.guiManager.setTextColor(ChatFormatting.DARK_BLUE.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.DARK_GREEN)) {
            this.guiManager.setTextColor(ChatFormatting.DARK_GREEN.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.DARK_AQUA)) {
            this.guiManager.setTextColor(ChatFormatting.DARK_AQUA.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.DARK_RED)) {
            this.guiManager.setTextColor(ChatFormatting.DARK_RED.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.DARK_PURPLE)) {
            this.guiManager.setTextColor(ChatFormatting.DARK_PURPLE.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.GOLD)) {
            this.guiManager.setTextColor(ChatFormatting.GOLD.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.GRAY)) {
            this.guiManager.setTextColor(ChatFormatting.GRAY.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.DARK_GRAY)) {
            this.guiManager.setTextColor(ChatFormatting.DARK_GRAY.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.BLUE)) {
            this.guiManager.setTextColor(ChatFormatting.BLUE.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.GREEN)) {
            this.guiManager.setTextColor(ChatFormatting.GREEN.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.AQUA)) {
            this.guiManager.setTextColor(ChatFormatting.AQUA.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.RED)) {
            this.guiManager.setTextColor(ChatFormatting.RED.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.LIGHT_PURPLE)) {
            this.guiManager.setTextColor(ChatFormatting.LIGHT_PURPLE.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.YELLOW)) {
            this.guiManager.setTextColor(ChatFormatting.YELLOW.toString());
        } else if (this.textColor.getValue().equals((Object)TextColor.WHITE)) {
            this.guiManager.setTextColor(ChatFormatting.WHITE.toString());
        } else {
            this.guiManager.setTextColor(ChatFormatting.WHITE.toString());
        }
        this.guiManager.setModuleListColors(this.moduleListRed.getValue(), this.moduleListGreen.getValue(), this.moduleListBlue.getValue());
        this.guiManager.setModuleListMode(this.moduleListMode.getValue());
        this.guiManager.setTextRadarPlayers(this.textRadarPlayers.getValue());
        this.guiManager.setTextRadarPots(this.textRadarPots.getValue());
    }

    private static enum TextColor {
        BLACK,
        DARK_BLUE,
        DARK_GREEN,
        DARK_AQUA,
        DARK_RED,
        DARK_PURPLE,
        GOLD,
        GRAY,
        DARK_GRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHT_PURPLE,
        YELLOW,
        WHITE;

    }
}

