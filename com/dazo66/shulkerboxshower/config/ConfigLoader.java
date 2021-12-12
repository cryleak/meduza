/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 */
package com.dazo66.shulkerboxshower.config;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigLoader {
    public static Configuration config;

    public ConfigLoader(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        config = new Configuration(configFile);
        this.configLoader();
        this.initialize();
    }

    private void configLoader() {
        config.load();
    }

    private void initialize() {
        this.isOrganizing();
        this.configSave();
    }

    public void configSave() {
        config.save();
    }

    public boolean isOrganizing() {
        String comment = "Organizing the items in one stack or not";
        return config.getBoolean("Organizing The Items", "Common", false, comment);
    }
}

