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

@Module.Info(name="OffhandGap", category=Module.Category.DEV, description="Auto Offhand Gapple")
public class OffhandGap
extends Module {
    private int gapples;
    private boolean moving = false;
    private boolean returnI = false;
    private Setting<Boolean> soft = this.register(Settings.b("Soft", false));
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
        int i;
        int t;
        if (OffhandGap.mc.field_71462_r instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            t = -1;
            for (i = 0; i < 45; ++i) {
                if (!OffhandGap.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_190928_g) continue;
                t = i;
                break;
            }
            if (t == -1) {
                return;
            }
            OffhandGap.mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
            this.returnI = false;
        }
        this.gapples = OffhandGap.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
        if (OffhandGap.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao) {
            ++this.gapples;
        } else {
            if (this.soft.getValue().booleanValue() && !OffhandGap.mc.field_71439_g.func_184592_cb().field_190928_g) {
                return;
            }
            if (this.moving) {
                OffhandGap.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
                this.moving = false;
                if (!OffhandGap.mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                    this.returnI = true;
                }
                return;
            }
            if (OffhandGap.mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                if (this.gapples == 0) {
                    return;
                }
                t = -1;
                for (i = 0; i < 45; ++i) {
                    if (OffhandGap.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151153_ao) continue;
                    t = i;
                    break;
                }
                if (t == -1) {
                    return;
                }
                OffhandGap.mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
                this.moving = true;
            } else if (!this.soft.getValue().booleanValue()) {
                t = -1;
                for (i = 0; i < 45; ++i) {
                    if (!OffhandGap.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_190928_g) continue;
                    t = i;
                    break;
                }
                if (t == -1) {
                    return;
                }
                OffhandGap.mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
            }
        }
    }

    @Override
    public String getHudInfo() {
        return String.valueOf(this.gapples);
    }

    private static enum TotemMode {
        KAMI,
        ASIMOV;

    }
}

