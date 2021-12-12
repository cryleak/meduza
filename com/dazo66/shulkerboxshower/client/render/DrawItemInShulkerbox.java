/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 */
package com.dazo66.shulkerboxshower.client.render;

import com.dazo66.shulkerboxshower.ShulkerBoxViewer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class DrawItemInShulkerbox {
    public int x = 0;
    public int y = 0;
    private Minecraft mc = Minecraft.func_71410_x();
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");

    public void draw(GuiScreen gui, ItemStack itemStack) {
        List<ItemStack> list = this.arrangementItem(itemStack);
        if (!list.isEmpty()) {
            this.drawItemStack(gui, list, this.x + 4, this.y - 100);
        }
    }

    public void draw(GuiScreen gui, ItemStack itemStack, ItemStack itemStack1, int x, int y) {
        List<ItemStack> list1;
        List<ItemStack> list = this.arrangementItem(itemStack);
        if (!list.isEmpty()) {
            int size = list.size();
            int i = size / 9 + (size % 9 == 0 ? 0 : 1);
            if (!ShulkerBoxViewer.config.isOrganizing()) {
                i = 3;
            }
            this.drawItemStack(gui, list, x + 7, y - 110 - 18 + 42 + i * 18);
        }
        if (!(list1 = this.arrangementItem(itemStack1)).isEmpty()) {
            this.drawItemStack(gui, list1, x + 7, y - 100);
        }
    }

    private List<ItemStack> arrangementItem(ItemStack itemStack) {
        NBTTagCompound blockEntityTag;
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        NBTTagCompound nbttagcompound = itemStack.func_77978_p();
        if (nbttagcompound != null && nbttagcompound.func_150297_b("BlockEntityTag", 10) && (blockEntityTag = nbttagcompound.func_74775_l("BlockEntityTag")).func_150297_b("Items", 9)) {
            NonNullList nonNullList = NonNullList.func_191197_a((int)27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b((NBTTagCompound)blockEntityTag, (NonNullList)nonNullList);
            if (!ShulkerBoxViewer.config.isOrganizing()) {
                return nonNullList;
            }
            for (ItemStack itemStack2 : nonNullList) {
                if (itemStack2.func_190926_b()) continue;
                boolean flag = true;
                for (ItemStack itemStack1 : list) {
                    if (!itemStack2.func_77969_a(itemStack1) || !ItemStack.func_77970_a((ItemStack)itemStack1, (ItemStack)itemStack2)) continue;
                    itemStack1.func_190920_e(itemStack2.func_190916_E() + itemStack1.func_190916_E());
                    flag = false;
                }
                if (!flag) continue;
                list.add(itemStack2);
            }
        }
        return list;
    }

    private void drawItemStack(GuiScreen gui, List<ItemStack> list, int x, int y) {
        int i;
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179084_k();
        int i1 = i = list.size() / 9 + (list.size() % 9 == 0 ? 0 : 1);
        if (i1 == 3) {
            i = 1;
        } else if (i1 == 1) {
            i = 3;
        }
        this.mc.func_110434_K().func_110577_a(GUI_TEXTURE);
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        gui.func_73729_b(x - 8, y + 12 + i * 18, 0, 0, 176, 5);
        gui.func_73729_b(x - 8, y + 12 + i * 18 + 5, 0, 16, 176, i1 * 18);
        gui.func_73729_b(x - 8, y + 17 + i * 18 + i1 * 18, 0, 160, 176, 6);
        GlStateManager.func_179126_j();
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)32.0f);
        int size = list.size();
        for (int l = 0; l < size; ++l) {
            this.drawItemStack(this.mc.func_175599_af(), list.get(l), l % 9 * 18 + x, i * 18 + (l / 9 + 1) * 18 + y + 1);
        }
        GlStateManager.func_179140_f();
        this.mc.func_175599_af().field_77023_b = 0.0f;
    }

    private void drawItemStack1(List<ItemStack> nonNullList, int x, int y) {
        RenderItem itemRender = this.mc.func_175599_af();
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)32.0f);
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.drawItemStack(itemRender, nonNullList.get(l + k * 9), 8 + l * 18 + x - 15, 18 + k * 18 + y - 35);
            }
        }
        GlStateManager.func_179140_f();
    }

    private void drawItemStack(RenderItem itemRender, ItemStack stack, int x, int y) {
        FontRenderer font = stack.func_77973_b().getFontRenderer(stack);
        if (font == null) {
            font = this.mc.field_71466_p;
        }
        GlStateManager.func_179126_j();
        itemRender.field_77023_b = 120.0f;
        RenderHelper.func_74520_c();
        itemRender.func_180450_b(stack, x, y);
        String count = stack.func_190916_E() == 1 ? "" : String.valueOf(stack.func_190916_E());
        String more = stack.func_190916_E() % stack.func_77976_d() == 0 ? "" : "+" + stack.func_190916_E() % stack.func_77976_d();
        String count1 = stack.func_190916_E() == 1 ? "" : stack.func_190916_E() / stack.func_77976_d() + "S" + more;
        itemRender.func_180453_a(font, stack, x, y, count);
        itemRender.field_77023_b = 0.0f;
    }
}

