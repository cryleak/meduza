/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.util.Wrapper;

@Module.Info(name="EZ", category=Module.Category.MISC, description="Sends a message when enabled")
public class EZ
extends Module {
    @Override
    protected void onEnable() {
        if (EZ.mc.field_71439_g != null) {
            Wrapper.getPlayer().func_71165_d("Ez Meduza on top.");
            this.disable();
        }
    }

    @Override
    protected void onDisable() {
    }
}

