/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.util.math.BlockPos
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class KamiTessellator
extends Tessellator {
    public static KamiTessellator INSTANCE = new KamiTessellator();

    public KamiTessellator() {
        super(0x200000);
    }

    public static void prepare(int mode) {
        KamiTessellator.prepareGL();
        KamiTessellator.begin(mode);
    }

    public static void prepareGL() {
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_187441_d((float)1.5f);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void begin(int mode) {
        INSTANCE.func_178180_c().func_181668_a(mode, DefaultVertexFormats.field_181706_f);
    }

    public static void release() {
        KamiTessellator.render();
        KamiTessellator.releaseGL();
    }

    public static void render() {
        INSTANCE.func_78381_a();
    }

    public static void releaseGL() {
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179126_j();
    }

    public static void drawBox(BlockPos blockPos, int argb, int sides) {
        int a = argb >>> 24 & 0xFF;
        int r = argb >>> 16 & 0xFF;
        int g = argb >>> 8 & 0xFF;
        int b = argb & 0xFF;
        KamiTessellator.drawBox(blockPos, r, g, b, a, sides);
    }

    public static void drawBox(float x, float y, float z, int argb, int sides) {
        int a = argb >>> 24 & 0xFF;
        int r = argb >>> 16 & 0xFF;
        int g = argb >>> 8 & 0xFF;
        int b = argb & 0xFF;
        KamiTessellator.drawBox(INSTANCE.func_178180_c(), x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }

    public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
        KamiTessellator.drawBox(INSTANCE.func_178180_c(), blockPos.field_177962_a, blockPos.field_177960_b, blockPos.field_177961_c, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }

    public static void drawLines(BlockPos blockPos, int argb, int sides) {
        int a = argb >>> 24 & 0xFF;
        int r = argb >>> 16 & 0xFF;
        int g = argb >>> 8 & 0xFF;
        int b = argb & 0xFF;
        KamiTessellator.drawLines(blockPos, r, g, b, a, sides);
    }

    public static void drawLines(float x, float y, float z, int argb, int sides) {
        int a = argb >>> 24 & 0xFF;
        int r = argb >>> 16 & 0xFF;
        int g = argb >>> 8 & 0xFF;
        int b = argb & 0xFF;
        KamiTessellator.drawLines(INSTANCE.func_178180_c(), x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }

    public static void drawLines(BlockPos blockPos, int r, int g, int b, int a, int sides) {
        KamiTessellator.drawLines(INSTANCE.func_178180_c(), blockPos.field_177962_a, blockPos.field_177960_b, blockPos.field_177961_c, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }

    public static BufferBuilder getBufferBuilder() {
        return INSTANCE.func_178180_c();
    }

    public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 1) != 0) {
            buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 2) != 0) {
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 4) != 0) {
            buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 8) != 0) {
            buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x10) != 0) {
            buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x20) != 0) {
            buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
    }

    public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 0x11) != 0) {
            buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x12) != 0) {
            buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x21) != 0) {
            buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x22) != 0) {
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 5) != 0) {
            buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 6) != 0) {
            buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 9) != 0) {
            buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0xA) != 0) {
            buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x14) != 0) {
            buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x24) != 0) {
            buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x18) != 0) {
            buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
        if ((sides & 0x28) != 0) {
            buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
        }
    }
}

