/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;

@Module.Info(name="Sprint", description="Automatically makes the player sprint", category=Module.Category.MOVEMENT)
public class Sprint
extends Module {
    @Override
    public void onUpdate() {
        try {
            if (!Sprint.mc.field_71439_g.field_70123_F && Sprint.mc.field_71439_g.field_191988_bg > 0.0f) {
                Sprint.mc.field_71439_g.func_70031_b(true);
            } else {
                Sprint.mc.field_71439_g.func_70031_b(false);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

