/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Module.Info(name="ESP", category=Module.Category.RENDER)
public class ESP
extends Module {
    private Setting<ESPMode> mode = this.register(Settings.e("Mode", ESPMode.RECTANGLE));
    private Setting<Boolean> players = this.register(Settings.b("Players", true));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));

    @Override
    public void onWorldRender(RenderEvent event) {
        if (Wrapper.getMinecraft().func_175598_ae().field_78733_k == null) {
            return;
        }
        switch (this.mode.getValue()) {
            case RECTANGLE: {
                boolean isThirdPersonFrontal = Wrapper.getMinecraft().func_175598_ae().field_78733_k.field_74320_O == 2;
                float viewerYaw = Wrapper.getMinecraft().func_175598_ae().field_78735_i;
                ESP.mc.field_71441_e.field_72996_f.stream().filter(EntityUtil::isLiving).filter(entity -> ESP.mc.field_71439_g != entity).map(entity -> (EntityLivingBase)entity).filter(entityLivingBase -> !entityLivingBase.field_70128_L).filter(entity -> this.players.getValue() != false && entity instanceof EntityPlayer || (EntityUtil.isPassive((Entity)entity) ? this.animals.getValue() : this.mobs.getValue()) != false).forEach(e -> {
                    GlStateManager.func_179094_E();
                    Vec3d pos = EntityUtil.getInterpolatedPos((Entity)e, event.getPartialTicks());
                    GlStateManager.func_179137_b((double)(pos.field_72450_a - ESP.mc.func_175598_ae().field_78725_b), (double)(pos.field_72448_b - ESP.mc.func_175598_ae().field_78726_c), (double)(pos.field_72449_c - ESP.mc.func_175598_ae().field_78723_d));
                    GlStateManager.func_187432_a((float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(-viewerYaw), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.func_179114_b((float)(isThirdPersonFrontal ? -1 : 1), (float)1.0f, (float)0.0f, (float)0.0f);
                    GlStateManager.func_179140_f();
                    GlStateManager.func_179132_a((boolean)false);
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
                    if (e instanceof EntityPlayer) {
                        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
                    } else if (EntityUtil.isPassive((Entity)e)) {
                        GL11.glColor3f((float)0.11f, (float)0.9f, (float)0.11f);
                    } else {
                        GL11.glColor3f((float)0.9f, (float)0.1f, (float)0.1f);
                    }
                    GlStateManager.func_179090_x();
                    GL11.glLineWidth((float)2.0f);
                    GL11.glEnable((int)2848);
                    GL11.glBegin((int)2);
                    GL11.glVertex2d((double)(-e.field_70130_N / 2.0f), (double)0.0);
                    GL11.glVertex2d((double)(-e.field_70130_N / 2.0f), (double)e.field_70131_O);
                    GL11.glVertex2d((double)(e.field_70130_N / 2.0f), (double)e.field_70131_O);
                    GL11.glVertex2d((double)(e.field_70130_N / 2.0f), (double)0.0);
                    GL11.glEnd();
                    GlStateManager.func_179121_F();
                });
                GlStateManager.func_179126_j();
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179090_x();
                GlStateManager.func_179147_l();
                GlStateManager.func_179118_c();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                GlStateManager.func_179103_j((int)7425);
                GlStateManager.func_179097_i();
                GlStateManager.func_179089_o();
                GlStateManager.func_187441_d((float)1.0f);
                GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
                break;
            }
        }
    }

    public static enum ESPMode {
        RECTANGLE;

    }
}

