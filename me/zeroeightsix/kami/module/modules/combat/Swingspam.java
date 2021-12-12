/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.zeroeightsix.kami.module.modules.combat.AutoSword
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.Vec3d
 */
package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.module.modules.combat.AutoSword;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.LagCompensator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

@Module.Info(name="Swingspam", category=Module.Category.COMBAT, description="Hits entities around you")
public class Swingspam
extends Module {
    private Setting<Boolean> players = this.register(Settings.b("Players", true));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));
    private Setting<Double> range = this.register(Settings.d("Range", 5.5));
    private Setting<Boolean> wait = this.register(Settings.b("Wait", true));
    private Setting<Boolean> walls = this.register(Settings.b("Walls", false));

    @Override
    public void onUpdate() {
        boolean golden_apple;
        if (Swingspam.mc.field_71439_g.field_70128_L) {
            return;
        }
        boolean bl = golden_apple = Swingspam.mc.field_71439_g.func_184592_cb().func_77973_b().equals((Object)Items.field_151153_ao) && Swingspam.mc.field_71439_g.func_184600_cs() == EnumHand.OFF_HAND;
        if (Swingspam.mc.field_71439_g.func_184587_cr() && !golden_apple) {
            return;
        }
        if (this.wait.getValue().booleanValue()) {
            if (Swingspam.mc.field_71439_g.func_184825_o(this.getLagComp()) < 1.0f) {
                return;
            }
            if (Swingspam.mc.field_71439_g.field_70173_aa % 2 != 0) {
                return;
            }
        }
        for (Entity target : Minecraft.func_71410_x().field_71441_e.field_72996_f) {
            if (!EntityUtil.isLiving(target) || target == Swingspam.mc.field_71439_g) continue;
            if ((double)Swingspam.mc.field_71439_g.func_70032_d(target) > this.range.getValue()) {
                this.attack(target);
            }
            if (((EntityLivingBase)target).func_110143_aJ() <= 0.0f || ((EntityLivingBase)target).field_70737_aN != 0 && this.wait.getValue().booleanValue() || !this.walls.getValue().booleanValue() && !Swingspam.mc.field_71439_g.func_70685_l(target) && !this.canEntityFeetBeSeen(target)) continue;
            if (this.players.getValue().booleanValue() && target instanceof EntityPlayer && !Friends.isFriend(target.func_70005_c_())) {
                this.attack(target);
                return;
            }
            if (!(EntityUtil.isPassive(target) ? this.animals.getValue() != false : EntityUtil.isMobAggressive(target) && this.mobs.getValue() != false)) continue;
            if (ModuleManager.isModuleEnabled("AutoSword")) {
                AutoSword.equipBestWeapon();
            }
            this.attack(target);
            return;
        }
    }

    private void attack(Entity e) {
        Swingspam.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
    }

    private float getLagComp() {
        if (this.wait.getValue().booleanValue()) {
            return -(20.0f - LagCompensator.INSTANCE.getTickRate());
        }
        return 0.0f;
    }

    private boolean canEntityFeetBeSeen(Entity entityIn) {
        return Swingspam.mc.field_71441_e.func_147447_a(new Vec3d(Swingspam.mc.field_71439_g.field_70165_t, Swingspam.mc.field_71439_g.field_70165_t + (double)Swingspam.mc.field_71439_g.func_70047_e(), Swingspam.mc.field_71439_g.field_70161_v), new Vec3d(entityIn.field_70165_t, entityIn.field_70163_u, entityIn.field_70161_v), false, true, false) == null;
    }
}

