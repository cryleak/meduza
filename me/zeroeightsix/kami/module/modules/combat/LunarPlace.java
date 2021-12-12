/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

@Module.Info(name="LunarPlace", category=Module.Category.COMBAT)
public class LunarPlace
extends Module {
    private Setting<Boolean> autoSwitch = this.register(Settings.b("Auto Switch"));
    private Setting<Boolean> players = this.register(Settings.b("Players"));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));
    private Setting<Boolean> place = this.register(Settings.b("Place", false));
    private Setting<Boolean> slow = this.register(Settings.b("Slow", false));
    private Setting<Double> range = this.register(Settings.d("Range", 6.0));
    private Setting<Double> reset = this.register(Settings.d("Placements before reset", 2.0));
    private Setting<Boolean> antiWeakness = this.register(Settings.b("Anti Weakness", false));
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime = -1L;
    private static boolean togglePitch = false;
    private boolean switchCooldown = false;
    private boolean isAttacking = false;
    private int oldSlot = -1;
    private int newSlot;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener = new Listener<PacketEvent.Send>(event -> {
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).field_149476_e = (float)yaw;
            ((CPacketPlayer)packet).field_149473_f = (float)pitch;
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        int crystalSlot;
        double Placements = 0.0;
        EntityEnderCrystal crystal = LunarPlace.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> (EntityEnderCrystal)entity).min(Comparator.comparing(c -> Float.valueOf(LunarPlace.mc.field_71439_g.func_70032_d((Entity)c)))).orElse(null);
        if (crystal != null && (double)LunarPlace.mc.field_71439_g.func_70032_d((Entity)crystal) <= this.range.getValue()) {
            this.lookAtPacket(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, (EntityPlayer)LunarPlace.mc.field_71439_g);
            LunarPlace.mc.field_71442_b.func_78764_a((EntityPlayer)LunarPlace.mc.field_71439_g, (Entity)crystal);
            LunarPlace.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        } else {
            LunarPlace.resetRotation();
            if (this.oldSlot != -1) {
                Wrapper.getPlayer().field_71071_by.field_70461_c = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
        }
        int n = crystalSlot = LunarPlace.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP ? LunarPlace.mc.field_71439_g.field_71071_by.field_70461_c : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (LunarPlace.mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() != Items.field_185158_cP) continue;
                crystalSlot = l;
                break;
            }
        }
        boolean offhand = false;
        if (LunarPlace.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        List<BlockPos> blocks = this.findCrystalBlocks();
        ArrayList entities = new ArrayList();
        if (this.players.getValue().booleanValue()) {
            entities.addAll(LunarPlace.mc.field_71441_e.field_73010_i.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.func_70005_c_())).collect(Collectors.toList()));
        }
        entities.addAll(LunarPlace.mc.field_71441_e.field_72996_f.stream().filter(entity -> EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue()) != false).collect(Collectors.toList()));
        BlockPos q = null;
        double damage = 0.5;
        for (Entity entity2 : entities) {
            if (entity2 == LunarPlace.mc.field_71439_g || ((EntityLivingBase)entity2).func_110143_aJ() <= 0.0f) continue;
            for (BlockPos blockPos : blocks) {
                double self;
                double d;
                double b = entity2.func_174818_b(blockPos);
                if (b >= 169.0 || !((d = (double)LunarPlace.calculateDamage((double)blockPos.field_177962_a + 0.5, blockPos.field_177960_b + 1, (double)blockPos.field_177961_c + 0.5, entity2)) > damage) || (self = (double)LunarPlace.calculateDamage((double)blockPos.field_177962_a + 0.5, blockPos.field_177960_b + 1, (double)blockPos.field_177961_c + 0.5, (Entity)LunarPlace.mc.field_71439_g)) > d && !(d < (double)((EntityLivingBase)entity2).func_110143_aJ()) || self - 0.5 > (double)LunarPlace.mc.field_71439_g.func_110143_aJ()) continue;
                damage = d;
                q = blockPos;
                this.renderEnt = entity2;
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            LunarPlace.resetRotation();
            return;
        }
        this.render = q;
        if (this.place.getValue().booleanValue()) {
            EnumFacing f;
            if (!offhand && LunarPlace.mc.field_71439_g.field_71071_by.field_70461_c != crystalSlot) {
                if (this.autoSwitch.getValue().booleanValue()) {
                    LunarPlace.mc.field_71439_g.field_71071_by.field_70461_c = crystalSlot;
                    LunarPlace.resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket((double)q.field_177962_a + 0.5, (double)q.field_177960_b - 0.5, (double)q.field_177961_c + 0.5, (EntityPlayer)LunarPlace.mc.field_71439_g);
            RayTraceResult result = LunarPlace.mc.field_71441_e.func_72933_a(new Vec3d(LunarPlace.mc.field_71439_g.field_70165_t, LunarPlace.mc.field_71439_g.field_70163_u + (double)LunarPlace.mc.field_71439_g.func_70047_e(), LunarPlace.mc.field_71439_g.field_70161_v), new Vec3d((double)q.field_177962_a + 0.5, (double)q.field_177960_b - 0.5, (double)q.field_177961_c + 0.5));
            if (result == null || result.field_178784_b == null) {
                f = EnumFacing.UP;
            } else {
                f = result.field_178784_b;
                if (System.nanoTime() / 1000000L - this.systemTime >= 10L) {
                    LunarPlace.mc.field_71442_b.func_187099_a(LunarPlace.mc.field_71439_g, LunarPlace.mc.field_71441_e, q, f, new Vec3d(0.0, 0.0, 0.0), EnumHand.MAIN_HAND);
                }
            }
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            while (Placements < this.reset.getValue() + 1.0) {
                LunarPlace.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                Placements += 1.0;
            }
            if (Placements == this.reset.getValue() + 1.0) {
                LunarPlace.resetRotation();
                this.systemTime = System.nanoTime() / 500L;
                Placements = 0.0;
            }
        }
        if (isSpoofingAngles) {
            if (togglePitch) {
                LunarPlace.mc.field_71439_g.field_70125_A = (float)((double)LunarPlace.mc.field_71439_g.field_70125_A + 4.0E-4);
                togglePitch = false;
            } else {
                LunarPlace.mc.field_71439_g.field_70125_A = (float)((double)LunarPlace.mc.field_71439_g.field_70125_A - 4.0E-4);
                togglePitch = true;
            }
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, -1711341313, 63);
            KamiTessellator.release();
            if (this.renderEnt != null) {
                Vec3d vec3d = EntityUtil.getInterpolatedRenderPos(this.renderEnt, mc.func_184121_ak());
            }
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        LunarPlace.setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        return (LunarPlace.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || LunarPlace.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && LunarPlace.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && LunarPlace.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && LunarPlace.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty();
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(LunarPlace.mc.field_71439_g.field_70165_t), Math.floor(LunarPlace.mc.field_71439_g.field_70163_u), Math.floor(LunarPlace.mc.field_71439_g.field_70161_v));
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)this.getSphere(LunarPlace.getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.func_177958_n();
        int cy = loc.func_177956_o();
        int cz = loc.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f = y;
                    float f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.func_70011_f(posX, posY, posZ) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = LunarPlace.getBlastReduction((EntityLivingBase)entity, LunarPlace.getDamageMultiplied(damage), new Explosion((World)LunarPlace.mc.field_71441_e, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.func_94539_a((Explosion)explosion);
            damage = CombatRules.func_189427_a((float)damage, (float)ep.func_70658_aO(), (float)((float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
            int k = EnchantmentHelper.func_77508_a((Iterable)ep.func_184193_aE(), (DamageSource)ds);
            float f = MathHelper.func_76131_a((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.func_70644_a(Potion.func_188412_a((int)11))) {
                damage -= damage / 5.0f;
            }
            damage = Math.max(damage - ep.func_110139_bj(), 0.0f);
            return damage;
        }
        damage = CombatRules.func_189427_a((float)damage, (float)entity.func_70658_aO(), (float)((float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = LunarPlace.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
        return LunarPlace.calculateDamage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = LunarPlace.mc.field_71439_g.field_70177_z;
            pitch = LunarPlace.mc.field_71439_g.field_70125_A;
            isSpoofingAngles = false;
        }
    }

    @Override
    public void onDisable() {
        this.render = null;
        this.renderEnt = null;
        LunarPlace.resetRotation();
    }
}

