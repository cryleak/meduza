/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.FoodStats
 */
package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;

@Module.Info(name="AutoEat", description="Automatically eat when hungry", category=Module.Category.PLAYER)
public class AutoEat
extends Module {
    private int lastSlot = -1;
    private boolean eating = false;

    private boolean isValid(ItemStack stack, int food) {
        return stack.func_77973_b() instanceof ItemFood && 20 - food >= ((ItemFood)stack.func_77973_b()).func_150905_g(stack);
    }

    @Override
    public void onUpdate() {
        if (this.eating && !AutoEat.mc.field_71439_g.func_184587_cr()) {
            if (this.lastSlot != -1) {
                AutoEat.mc.field_71439_g.field_71071_by.field_70461_c = this.lastSlot;
                this.lastSlot = -1;
            }
            this.eating = false;
            KeyBinding.func_74510_a((int)AutoEat.mc.field_71474_y.field_74313_G.func_151463_i(), (boolean)false);
            return;
        }
        if (this.eating) {
            return;
        }
        FoodStats stats = AutoEat.mc.field_71439_g.func_71024_bL();
        if (this.isValid(AutoEat.mc.field_71439_g.func_184592_cb(), stats.func_75116_a())) {
            AutoEat.mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
            this.eating = true;
            KeyBinding.func_74510_a((int)AutoEat.mc.field_71474_y.field_74313_G.func_151463_i(), (boolean)true);
            mc.func_147121_ag();
        } else {
            for (int i = 0; i < 9; ++i) {
                if (!this.isValid(AutoEat.mc.field_71439_g.field_71071_by.func_70301_a(i), stats.func_75116_a())) continue;
                this.lastSlot = AutoEat.mc.field_71439_g.field_71071_by.field_70461_c;
                AutoEat.mc.field_71439_g.field_71071_by.field_70461_c = i;
                this.eating = true;
                KeyBinding.func_74510_a((int)AutoEat.mc.field_71474_y.field_74313_G.func_151463_i(), (boolean)true);
                mc.func_147121_ag();
                return;
            }
        }
    }
}

