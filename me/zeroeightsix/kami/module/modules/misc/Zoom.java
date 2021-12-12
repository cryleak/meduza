/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(name="Zoom", description="An Optifine zoom alternative", category=Module.Category.RENDER)
public class Zoom
extends Module {
    private float maxzoom = 12.0f;
    private Setting<Float> zoom = this.register(Settings.floatBuilder("Range").withMinimum(Float.valueOf(1.0f)).withMaximum(Float.valueOf(this.maxzoom)).withValue(Float.valueOf(1.0f)).build());
    public float fov = -1.0f;

    @Override
    public void onUpdate() {
        float zoom = this.maxzoom - this.zoom.getValue().floatValue();
        if (Zoom.mc.field_71474_y.field_74334_X > zoom) {
            for (int i = 0; i < 100; ++i) {
                if (!(Zoom.mc.field_71474_y.field_74334_X > zoom)) continue;
                Zoom.mc.field_71474_y.field_74334_X -= 0.1f;
            }
        }
    }

    @Override
    public void onEnable() {
        if (this.fov == -1.0f || Zoom.mc.field_71474_y.field_74334_X == this.fov) {
            this.fov = Zoom.mc.field_71474_y.field_74334_X;
        }
    }

    @Override
    public void onDisable() {
        Zoom.mc.field_71474_y.field_74334_X = this.fov;
    }
}

