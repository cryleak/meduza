/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraftforge.common.config.ConfigElement
 *  net.minecraftforge.fml.client.IModGuiFactory
 *  net.minecraftforge.fml.client.IModGuiFactory$RuntimeOptionCategoryElement
 *  net.minecraftforge.fml.client.config.GuiConfig
 *  net.minecraftforge.fml.client.config.GuiConfigEntries$IConfigEntry
 *  net.minecraftforge.fml.client.config.IConfigElement
 */
package com.dazo66.shulkerboxshower.config;

import com.dazo66.shulkerboxshower.ShulkerBoxViewer;
import com.dazo66.shulkerboxshower.config.ConfigLoader;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigFactory
implements IModGuiFactory {
    public static final String CLASS_NAME = "com.dazo66.shulkerboxshower.config.ConfigFactory";

    public GuiScreen createConfigGui(GuiScreen parent) {
        return new ShulkerBoxViewerConfigGui(parent, new ConfigElement(ConfigLoader.config.getCategory("common")).getChildElements(), "shulkerboxviewer", "ShulkerBoxViewerConfigGui.Common");
    }

    public boolean hasConfigGui() {
        return true;
    }

    public void initialize(Minecraft minecraftInstance) {
    }

    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ShulkerBoxViewerConfigGui.class;
    }

    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public static class ShulkerBoxViewerConfigGui
    extends GuiConfig {
        public ShulkerBoxViewerConfigGui(GuiScreen parent) {
            this(parent, new ConfigElement(ConfigLoader.config.getCategory("common")).getChildElements(), "shulkerboxviewer", "ShulkerBoxViewerConfigGui.Common");
        }

        public ShulkerBoxViewerConfigGui(GuiScreen parentScreen, List<IConfigElement> list, String modid, String title) {
            super(parentScreen, list, modid, false, false, title);
        }

        public void func_146281_b() {
            ShulkerBoxViewer.config.configSave();
        }

        public GuiConfigEntries.IConfigEntry getCategoryEntry() {
            for (GuiConfigEntries.IConfigEntry entry : this.entryList.listEntries) {
                if (!"common".equals(entry.getName())) continue;
                return entry;
            }
            return null;
        }
    }
}

