/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 */
package com.dazo66.shulkerboxshower;

import com.dazo66.shulkerboxshower.config.ConfigLoader;
import com.dazo66.shulkerboxshower.eventhandler.ShulkerBoxViewerEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="shulkerboxviewer", version="1.5", acceptedMinecraftVersions="[1.11, 1.12.2]", clientSideOnly=true, guiFactory="com.dazo66.shulkerboxshower.config.ConfigFactory")
public class ShulkerBoxViewer {
    public static final String MODID = "shulkerboxviewer";
    public static final String NAME = "ShulkerBoxViewer";
    public static final String VERSION = "1.5";
    public static final String MCVersion = "[1.11, 1.12.2]";
    public static ConfigLoader config;

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)ShulkerBoxViewerEventHandler.instance);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new ConfigLoader(event);
    }
}

