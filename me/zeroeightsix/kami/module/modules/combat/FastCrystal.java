/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.init.Items;

@Module.Info(name="FastCrystal", category=Module.Category.COMBAT, description="Place crystals faster")
public class FastCrystal
extends Module {
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (FastCrystal.mc.field_71439_g != null && (FastCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP || FastCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP)) {
            FastCrystal.mc.field_71467_ac = 0;
        }
    }, new Predicate[0]);
}

