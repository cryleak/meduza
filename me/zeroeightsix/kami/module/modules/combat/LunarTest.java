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
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.KamiTessellator;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

@Module.Info(name="SolarPlace", category=Module.Category.COMBAT)
public class LunarTest
extends Module {
    private Setting<Boolean> autoSwitch = this.register(Settings.b("Auto Switch"));
    private Setting<Boolean> players = this.register(Settings.b("Players"));
    private Setting<Boolean> place = this.register(Settings.b("Place", true));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));
    private Setting<Double> placerange = this.register(Settings.d("PlaceRange", 6.0));
    private Setting<Double> hitrange = this.register(Settings.d("HitRange", 3.5));
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime = -1L;
    private static boolean togglePitch = false;
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

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void onUpdate() {
        crystal = LunarTest.mc.field_71441_e.field_72996_f.stream().filter((Predicate<Entity>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, lambda$onUpdate$1(net.minecraft.entity.Entity ), (Lnet/minecraft/entity/Entity;)Z)()).map((Function<Entity, EntityEnderCrystal>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$onUpdate$2(net.minecraft.entity.Entity ), (Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/item/EntityEnderCrystal;)()).min(Comparator.comparing((Function<EntityEnderCrystal, Float>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$onUpdate$3(net.minecraft.entity.item.EntityEnderCrystal ), (Lnet/minecraft/entity/item/EntityEnderCrystal;)Ljava/lang/Float;)())).orElse(null);
        if (crystal != null && (double)LunarTest.mc.field_71439_g.func_70032_d((Entity)crystal) <= this.hitrange.getValue()) {
            if (System.nanoTime() / 1000000L - this.systemTime < 250L) return;
            this.lookAtPacket(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, (EntityPlayer)LunarTest.mc.field_71439_g);
            LunarTest.mc.field_71442_b.func_78764_a((EntityPlayer)LunarTest.mc.field_71439_g, (Entity)crystal);
            LunarTest.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            this.systemTime = System.nanoTime() / 1000000L;
            return;
        }
        LunarTest.resetRotation();
        v0 = crystalSlot = LunarTest.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP ? LunarTest.mc.field_71439_g.field_71071_by.field_70461_c : -1;
        if (crystalSlot == -1) {
            for (l = 0; l < 9; ++l) {
                if (LunarTest.mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() != Items.field_185158_cP) continue;
                crystalSlot = l;
                break;
            }
        }
        offhand = false;
        if (LunarTest.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        blocks = this.findCrystalBlocks();
        entities = new ArrayList<E>();
        if (this.players.getValue().booleanValue()) {
            entities.addAll(LunarTest.mc.field_71441_e.field_73010_i.stream().filter((Predicate<EntityPlayer>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, lambda$onUpdate$4(net.minecraft.entity.player.EntityPlayer ), (Lnet/minecraft/entity/player/EntityPlayer;)Z)()).collect(Collectors.toList()));
        }
        entities.addAll(LunarTest.mc.field_71441_e.field_72996_f.stream().filter((Predicate<Entity>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, lambda$onUpdate$5(net.minecraft.entity.Entity ), (Lnet/minecraft/entity/Entity;)Z)((LunarTest)this)).collect(Collectors.toList()));
        q = null;
        damage = 0.5;
        var9 = entities.iterator();
        block1: while (true) lbl-1000:
        // 3 sources

        {
            if (!var9.hasNext()) {
                if (damage == 0.5) {
                    this.render = null;
                    this.renderEnt = null;
                    LunarTest.resetRotation();
                    return;
                }
                this.render = q;
                if (this.place.getValue().booleanValue()) {
                    if (!offhand && LunarTest.mc.field_71439_g.field_71071_by.field_70461_c != crystalSlot) {
                        if (this.autoSwitch.getValue() == false) return;
                        LunarTest.mc.field_71439_g.field_71071_by.field_70461_c = crystalSlot;
                        LunarTest.resetRotation();
                        return;
                    }
                    this.lookAtPacket((double)q.field_177962_a + 0.5, (double)q.field_177960_b - 0.5, (double)q.field_177961_c + 0.5, (EntityPlayer)LunarTest.mc.field_71439_g);
                    result = LunarTest.mc.field_71441_e.func_72933_a(new Vec3d(LunarTest.mc.field_71439_g.field_70165_t, LunarTest.mc.field_71439_g.field_70163_u + (double)LunarTest.mc.field_71439_g.func_70047_e(), LunarTest.mc.field_71439_g.field_70161_v), new Vec3d((double)q.field_177962_a + 0.5, (double)q.field_177960_b - 0.5, (double)q.field_177961_c + 0.5));
                    f = result != null && result.field_178784_b != null ? result.field_178784_b : EnumFacing.UP;
                    LunarTest.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand != false ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
                if (LunarTest.isSpoofingAngles == false) return;
                if (LunarTest.togglePitch) {
                    LunarTest.mc.field_71439_g.field_70125_A = (float)((double)LunarTest.mc.field_71439_g.field_70125_A + 4.0E-4);
                    LunarTest.togglePitch = false;
                    return;
                }
                LunarTest.mc.field_71439_g.field_70125_A = (float)((double)LunarTest.mc.field_71439_g.field_70125_A - 4.0E-4);
                LunarTest.togglePitch = true;
                return;
            }
            entity = (Entity)var9.next();
            if (entity == LunarTest.mc.field_71439_g || ((EntityLivingBase)entity).func_110143_aJ() <= 0.0f) ** GOTO lbl-1000
            var11 = blocks.iterator();
            while (true) {
                if (!var11.hasNext()) continue block1;
                blockPos = var11.next();
                b = entity.func_174818_b(blockPos);
                if (b >= 169.0 || (d = (double)LunarTest.calculateDamage((double)blockPos.field_177962_a + 0.5, blockPos.field_177960_b + 1, (double)blockPos.field_177961_c + 0.5, entity)) <= damage || (self = (double)LunarTest.calculateDamage((double)blockPos.field_177962_a + 0.5, blockPos.field_177960_b + 1, (double)blockPos.field_177961_c + 0.5, (Entity)LunarTest.mc.field_71439_g)) > d && d >= (double)((EntityLivingBase)entity).func_110143_aJ() || !(self - 0.5 <= (double)LunarTest.mc.field_71439_g.func_110143_aJ())) continue;
                damage = d;
                q = blockPos;
                this.renderEnt = entity;
            }
            break;
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, 0x44FFFFFF, 63);
            KamiTessellator.release();
            if (this.renderEnt != null) {
                Vec3d vec3d = EntityUtil.getInterpolatedRenderPos(this.renderEnt, mc.func_184121_ak());
            }
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        LunarTest.setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        return (LunarTest.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || LunarTest.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && LunarTest.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && LunarTest.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && LunarTest.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty();
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(LunarTest.mc.field_71439_g.field_70165_t), Math.floor(LunarTest.mc.field_71439_g.field_70163_u), Math.floor(LunarTest.mc.field_71439_g.field_70161_v));
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)this.getSphere(LunarTest.getPlayerPos(), this.placerange.getValue().floatValue(), this.placerange.getValue().intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
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
            finald = LunarTest.getBlastReduction((EntityLivingBase)entity, LunarTest.getDamageMultiplied(damage), new Explosion((World)LunarTest.mc.field_71441_e, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage - ep.func_110139_bj(), 0.0f);
            return damage;
        }
        damage = CombatRules.func_189427_a((float)damage, (float)entity.func_70658_aO(), (float)((float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = LunarTest.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
        return LunarTest.calculateDamage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = LunarTest.mc.field_71439_g.field_70177_z;
            pitch = LunarTest.mc.field_71439_g.field_70125_A;
            isSpoofingAngles = false;
        }
    }

    @Override
    public void onEnable() {
        if (LunarTest.mc.field_71441_e != null) {
            Command.sendChatMessage("&7 CrystalAura ON");
        }
    }

    @Override
    public void onDisable() {
        this.render = null;
        this.renderEnt = null;
        LunarTest.resetRotation();
        if (LunarTest.mc.field_71441_e != null) {
            Command.sendChatMessage("&7 CrystalAura OFF");
        }
    }

    private /* synthetic */ boolean lambda$onUpdate$5(Entity entityx) {
        return EntityUtil.isLiving(entityx) && EntityUtil.isPassive(entityx) ? this.animals.getValue() : this.mobs.getValue();
    }

    private static /* synthetic */ boolean lambda$onUpdate$4(EntityPlayer entityPlayer) {
        return !Friends.isFriend(entityPlayer.func_70005_c_());
    }

    private static /* synthetic */ Float lambda$onUpdate$3(EntityEnderCrystal c) {
        return Float.valueOf(LunarTest.mc.field_71439_g.func_70032_d((Entity)c));
    }

    private static /* synthetic */ EntityEnderCrystal lambda$onUpdate$2(Entity entityx) {
        return (EntityEnderCrystal)entityx;
    }

    private static /* synthetic */ boolean lambda$onUpdate$1(Entity entityx) {
        return entityx instanceof EntityEnderCrystal;
    }
}

