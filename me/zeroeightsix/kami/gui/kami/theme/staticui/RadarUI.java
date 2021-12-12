/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.gui.kami.theme.staticui;

import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.kami.component.Radar;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.GuiManager;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RadarUI
extends AbstractComponentUI<Radar> {
    float scale = 2.0f;
    public static final int radius = 45;
    private GuiManager guiManager;

    public RadarUI() {
        this.guiManager = KamiMod.getInstance().guiManager;
    }

    @Override
    public void handleSizeComponent(Radar component) {
        component.setWidth(90);
        component.setHeight(90);
    }

    @Override
    public void renderComponent(Radar component, FontRenderer fontRenderer) {
        this.scale = 2.0f;
        GL11.glTranslated((double)(component.getWidth() / 2), (double)(component.getHeight() / 2), (double)0.0);
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        GlStateManager.func_179147_l();
        GlStateManager.func_179129_p();
        GlStateManager.func_179094_E();
        GL11.glColor4f((float)0.11f, (float)0.11f, (float)0.11f, (float)0.6f);
        RenderHelper.drawCircle(0.0f, 0.0f, 45.0f);
        GL11.glRotatef((float)(Wrapper.getPlayer().field_70177_z + 180.0f), (float)0.0f, (float)0.0f, (float)-1.0f);
        for (Entity e : Wrapper.getWorld().field_72996_f) {
            if (!(e instanceof EntityLiving)) continue;
            float red = 1.0f;
            float green = 1.0f;
            if (EntityUtil.isPassive(e)) {
                red = 0.0f;
            } else {
                green = 0.0f;
            }
            double dX = e.field_70165_t - Wrapper.getPlayer().field_70165_t;
            double dZ = e.field_70161_v - Wrapper.getPlayer().field_70161_v;
            double distance = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
            if (distance > (double)(45.0f * this.scale) || Math.abs(Wrapper.getPlayer().field_70163_u - e.field_70163_u) > 30.0) continue;
            GL11.glColor4f((float)red, (float)green, (float)0.0f, (float)0.5f);
            RenderHelper.drawCircle((float)((int)dX) / this.scale, (float)((int)dZ) / this.scale, 2.5f / this.scale);
        }
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.drawCircle(0.0f, 0.0f, 3.0f / this.scale);
        GL11.glLineWidth((float)1.8f);
        GL11.glColor3f((float)this.guiManager.getGuiRed(), (float)this.guiManager.getGuiGreen(), (float)this.guiManager.getGuiBlue());
        GL11.glEnable((int)2848);
        RenderHelper.drawCircleOutline(0.0f, 0.0f, 45.0f);
        GL11.glDisable((int)2848);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("+z") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "\u00a77z+");
        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("+x") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "\u00a77x-");
        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("-z") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "\u00a77z-");
        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("+x") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "\u00a77x+");
        GlStateManager.func_179121_F();
        GlStateManager.func_179098_w();
        GL11.glTranslated((double)(-component.getWidth() / 2), (double)(-component.getHeight() / 2), (double)0.0);
    }
}

