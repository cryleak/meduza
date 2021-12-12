/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.text.translation.I18n
 *  net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post
 *  net.minecraftforge.client.event.RenderTooltipEvent$PostBackground
 *  net.minecraftforge.event.entity.player.ItemTooltipEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.dazo66.shulkerboxshower.eventhandler;

import com.dazo66.shulkerboxshower.client.render.DrawItemInShulkerbox;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShulkerBoxViewerEventHandler {
    public static ShulkerBoxViewerEventHandler instance = new ShulkerBoxViewerEventHandler();
    private DrawItemInShulkerbox drawer = new DrawItemInShulkerbox();
    private Minecraft mc = Minecraft.func_71410_x();

    @SubscribeEvent
    public void onTooltipGen(ItemTooltipEvent event) {
        if (event.getItemStack().func_77973_b() instanceof ItemShulkerBox) {
            List list = event.getToolTip();
            ArrayList<String> temp = new ArrayList<String>();
            for (String s : list) {
                if (!s.matches("^.*\\sx\\d+$")) continue;
                temp.add(s);
            }
            for (String s : temp) {
                list.remove(s);
            }
            if (list.size() < 2) {
                return;
            }
            String[] strings = I18n.func_74838_a((String)"container.shulkerBox.more").split("%s");
            if (((String)list.get(1)).contains(strings[0]) && ((String)list.get(1)).contains(strings[1])) {
                list.remove(1);
            }
        }
    }

    @SubscribeEvent
    public void afterDrawGui(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiContainer) {
            GuiContainer gui = (GuiContainer)event.getGui();
            Slot slotUnderMouse = gui.getSlotUnderMouse();
            ItemStack itemInHand = this.mc.field_71439_g.field_71071_by.func_70445_o();
            if (null == slotUnderMouse) {
                if (!itemInHand.func_190926_b() && itemInHand.func_77973_b() instanceof ItemShulkerBox) {
                    this.drawer.draw((GuiScreen)gui, itemInHand, ItemStack.field_190927_a, event.getMouseX() + 10, event.getMouseY());
                }
            } else if (slotUnderMouse.func_75216_d()) {
                ItemStack itemUnderMouse = slotUnderMouse.func_75211_c();
                if (itemUnderMouse.func_77973_b() instanceof ItemShulkerBox) {
                    boolean flag;
                    boolean bl = flag = !itemInHand.func_190926_b() && itemInHand.func_77973_b() instanceof ItemShulkerBox;
                    if (flag) {
                        this.drawer.draw((GuiScreen)gui, itemInHand, itemUnderMouse, event.getMouseX() + 10, event.getMouseY());
                    } else {
                        this.drawer.draw((GuiScreen)gui, itemUnderMouse);
                    }
                } else if (itemInHand.func_77973_b() instanceof ItemShulkerBox) {
                    this.drawer.draw((GuiScreen)gui, itemInHand, ItemStack.field_190927_a, event.getMouseX() + 10, event.getMouseY());
                }
            } else if (itemInHand.func_77973_b() instanceof ItemShulkerBox) {
                this.drawer.draw((GuiScreen)gui, itemInHand, ItemStack.field_190927_a, event.getMouseX() + 10, event.getMouseY());
            }
        }
    }

    @SubscribeEvent
    public void onTooltipRender(RenderTooltipEvent.PostBackground event) {
        if (null == event.getStack()) {
            return;
        }
        if (event.getStack().func_77973_b() instanceof ItemShulkerBox) {
            this.drawer.x = event.getX();
            this.drawer.y = event.getY();
        }
    }
}

