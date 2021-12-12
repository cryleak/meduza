/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(name="Speed", description="Modify player speed on ground.", category=Module.Category.MOVEMENT)
public class Speed
extends Module {
    private Setting<Float> speed = this.register(Settings.floatBuilder("Speed").withMinimum(Float.valueOf(0.0f)).withValue(Float.valueOf(1.4f)).withMaximum(Float.valueOf(10.0f)).build());

    @Override
    public void onUpdate() {
        if ((Speed.mc.field_71439_g.field_191988_bg != 0.0f || Speed.mc.field_71439_g.field_70702_br != 0.0f) && !Speed.mc.field_71439_g.func_70093_af() && Speed.mc.field_71439_g.field_70122_E) {
            Speed.mc.field_71439_g.func_70664_aZ();
            Speed.mc.field_71439_g.field_70159_w *= (double)this.speed.getValue().floatValue();
            Speed.mc.field_71439_g.field_70181_x *= 0.4;
            Speed.mc.field_71439_g.field_70179_y *= (double)this.speed.getValue().floatValue();
        }
    }
}

