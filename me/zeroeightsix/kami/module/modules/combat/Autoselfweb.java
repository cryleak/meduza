/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Info(name="Autoselfweb", category=Module.Category.COMBAT)
public class Autoselfweb
extends Module {
    BlockPos head;
    BlockPos feet;
    int delay;
    public static List<EntityPlayer> targets;
    public static float yaw;
    public static float pitch;

    public boolean isInBlockRange(Entity target) {
        return true;
    }

    public static boolean canBeClicked(BlockPos pos) {
        return Autoselfweb.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_176209_a(Autoselfweb.mc.field_71441_e.func_180495_p(pos), false);
    }

    private static void faceVectorPacket(Vec3d vec) {
        double diffX = vec.field_72450_a - Autoselfweb.mc.field_71439_g.field_70165_t;
        double diffY = vec.field_72448_b - Autoselfweb.mc.field_71439_g.field_70163_u + (double)Autoselfweb.mc.field_71439_g.func_70047_e();
        double diffZ = vec.field_72449_c - Autoselfweb.mc.field_71439_g.field_70161_v;
        double dist = MathHelper.func_76133_a((double)(diffX * diffX + diffZ * diffZ));
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        mc.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Rotation(Autoselfweb.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - Autoselfweb.mc.field_71439_g.field_70177_z)), Autoselfweb.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - Autoselfweb.mc.field_71439_g.field_70125_A)), Autoselfweb.mc.field_71439_g.field_70122_E));
    }

    public boolean isValid() {
        return true;
    }

    private boolean isStackObby(ItemStack stack) {
        return stack != null && stack.func_77973_b() == Item.func_150899_d((int)30);
    }

    private boolean doesHotbarHaveObby() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = Autoselfweb.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null || !this.isStackObby(stack)) continue;
            return true;
        }
        return false;
    }

    public static Block getBlock(BlockPos pos) {
        return Autoselfweb.getState(pos).func_177230_c();
    }

    public static IBlockState getState(BlockPos pos) {
        return Autoselfweb.mc.field_71441_e.func_180495_p(pos);
    }

    public static boolean placeBlockLegit(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(Autoselfweb.mc.field_71439_g.field_70165_t, Autoselfweb.mc.field_71439_g.field_70163_u + (double)Autoselfweb.mc.field_71439_g.func_70047_e(), Autoselfweb.mc.field_71439_g.field_70161_v);
        Vec3d posVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3d hitVec;
            BlockPos neighbor = pos.func_177972_a(side);
            if (!Autoselfweb.canBeClicked(neighbor) || !(eyesPos.func_72436_e(hitVec = posVec.func_178787_e(new Vec3d(side.func_176730_m()).func_186678_a(0.5))) <= 36.0)) continue;
            Autoselfweb.mc.field_71442_b.func_187099_a(Autoselfweb.mc.field_71439_g, Autoselfweb.mc.field_71441_e, neighbor, side.func_176734_d(), hitVec, EnumHand.MAIN_HAND);
            Autoselfweb.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            try {
                TimeUnit.MILLISECONDS.sleep(10L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return true;
    }

    @Override
    public void onEnable() {
        this.delay = 0;
    }

    private void trap(EntityPlayer player) {
        if (!this.doesHotbarHaveObby()) {
            this.delay = 0;
        }
        if (this.doesHotbarHaveObby()) {
            this.head = new BlockPos(Autoselfweb.mc.field_71439_g.field_70165_t, Autoselfweb.mc.field_71439_g.field_70163_u + 1.0, Autoselfweb.mc.field_71439_g.field_70161_v);
            this.feet = new BlockPos(Autoselfweb.mc.field_71439_g.field_70165_t, Autoselfweb.mc.field_71439_g.field_70163_u, Autoselfweb.mc.field_71439_g.field_70161_v);
            for (int i = 36; i < 45; ++i) {
                ItemStack stack = Autoselfweb.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (stack != null && this.isStackObby(stack)) {
                    int oldSlot = Autoselfweb.mc.field_71439_g.field_71071_by.field_70461_c;
                    if (Autoselfweb.mc.field_71441_e.func_180495_p(this.head).func_185904_a().func_76222_j() || Autoselfweb.mc.field_71441_e.func_180495_p(this.feet).func_185904_a().func_76222_j()) {
                        Autoselfweb.mc.field_71439_g.field_71071_by.field_70461_c = i - 36;
                        if (Autoselfweb.mc.field_71441_e.func_180495_p(this.head).func_185904_a().func_76222_j()) {
                            Autoselfweb.placeBlockLegit(this.head);
                        }
                        if (Autoselfweb.mc.field_71441_e.func_180495_p(this.feet).func_185904_a().func_76222_j()) {
                            Autoselfweb.placeBlockLegit(this.feet);
                        }
                        Autoselfweb.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
                        this.delay = 0;
                        break;
                    }
                    this.delay = 0;
                }
                this.delay = 0;
            }
        }
    }

    @Override
    public void onDisable() {
        this.delay = 0;
        yaw = Autoselfweb.mc.field_71439_g.field_70177_z;
        pitch = Autoselfweb.mc.field_71439_g.field_70125_A;
    }

    public EnumFacing getEnumFacing(float posX, float posY, float posZ) {
        return EnumFacing.func_176737_a((float)posX, (float)posY, (float)posZ);
    }

    public BlockPos getBlockPos(double x, double y, double z) {
        return new BlockPos(x, y, z);
    }
}

