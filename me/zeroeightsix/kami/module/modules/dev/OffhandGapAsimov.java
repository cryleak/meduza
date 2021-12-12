/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 */
package me.zeroeightsix.kami.module.modules.dev;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.module.modules.combat.AutoTotem;
import me.zeroeightsix.kami.module.modules.dev.AutoTotemDev;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Info(name="OffhandGapAsimov", category=Module.Category.DEV, description="Auto Offhand Gapple")
public class OffhandGapAsimov
extends Module {
    private int numOfGaps;
    private int preferredGapSlot;
    private Setting<Boolean> totemOnDisable = this.register(Settings.b("TotemOnDisable", true));
    private Setting<TotemMode> totemMode = this.register(Settings.enumBuilder(TotemMode.class).withName("TotemMode").withValue(TotemMode.KAMI).withVisibility(v -> this.totemOnDisable.getValue()).build());

    @Override
    public void onEnable() {
        if (ModuleManager.getModuleByName("AutoTotem").isEnabled()) {
            ModuleManager.getModuleByName("AutoTotem").disable();
        }
        if (ModuleManager.getModuleByName("AutoTotemDev").isEnabled()) {
            ModuleManager.getModuleByName("AutoTotemDev").disable();
        }
    }

    @Override
    public void onDisable() {
        if (!this.totemOnDisable.getValue().booleanValue()) {
            return;
        }
        if (this.totemMode.getValue().equals((Object)TotemMode.KAMI)) {
            AutoTotem autoTotem = (AutoTotem)ModuleManager.getModuleByName("AutoTotem");
            autoTotem.disableSoft();
            if (autoTotem.isDisabled()) {
                autoTotem.enable();
            }
        }
        if (this.totemMode.getValue().equals((Object)TotemMode.ASIMOV)) {
            AutoTotemDev autoTotemDev = (AutoTotemDev)ModuleManager.getModuleByName("AutoTotemDev");
            autoTotemDev.disableSoft();
            if (autoTotemDev.isDisabled()) {
                autoTotemDev.enable();
            }
        }
    }

    @Override
    public void onUpdate() {
        if (OffhandGapAsimov.mc.field_71439_g == null) {
            return;
        }
        if (OffhandGapAsimov.mc.field_71462_r instanceof GuiContainer) {
            return;
        }
        if (!this.findGaps()) {
            this.disable();
            return;
        }
        if (!OffhandGapAsimov.mc.field_71439_g.func_184592_cb().func_77973_b().equals((Object)Items.field_151153_ao)) {
            boolean offhandEmptyPreSwitch = false;
            if (OffhandGapAsimov.mc.field_71439_g.func_184592_cb().func_77973_b().equals((Object)Items.field_190931_a)) {
                offhandEmptyPreSwitch = true;
            }
            OffhandGapAsimov.mc.field_71442_b.func_187098_a(0, this.preferredGapSlot, 0, ClickType.PICKUP, (EntityPlayer)OffhandGapAsimov.mc.field_71439_g);
            OffhandGapAsimov.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGapAsimov.mc.field_71439_g);
            if (!offhandEmptyPreSwitch) {
                OffhandGapAsimov.mc.field_71442_b.func_187098_a(0, this.preferredGapSlot, 0, ClickType.PICKUP, (EntityPlayer)OffhandGapAsimov.mc.field_71439_g);
            }
            OffhandGapAsimov.mc.field_71442_b.func_78765_e();
        }
    }

    private boolean findGaps() {
        this.numOfGaps = 0;
        AtomicInteger preferredGapSlotStackSize = new AtomicInteger();
        preferredGapSlotStackSize.set(Integer.MIN_VALUE);
        OffhandGapAsimov.getInventoryAndHotbarSlots().forEach((slotKey, slotValue) -> {
            int numOfGapsInStack = 0;
            if (slotValue.func_77973_b().equals((Object)Items.field_151153_ao)) {
                numOfGapsInStack = slotValue.func_190916_E();
                if (preferredGapSlotStackSize.get() < numOfGapsInStack) {
                    preferredGapSlotStackSize.set(numOfGapsInStack);
                    this.preferredGapSlot = slotKey;
                }
            }
            this.numOfGaps += numOfGapsInStack;
        });
        if (OffhandGapAsimov.mc.field_71439_g.func_184592_cb().func_77973_b().equals((Object)Items.field_151153_ao)) {
            this.numOfGaps += OffhandGapAsimov.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        return this.numOfGaps != 0;
    }

    private static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return OffhandGapAsimov.getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)OffhandGapAsimov.mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
            ++current;
        }
        return fullInventorySlots;
    }

    private static enum TotemMode {
        KAMI,
        ASIMOV;

    }
}

