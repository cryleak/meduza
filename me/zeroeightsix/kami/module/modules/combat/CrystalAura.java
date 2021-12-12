/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
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

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.module.modules.chat.AutoGG;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.BlockInteractionHelper;
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
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
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

@Module.Info(name="CrystalAura", category=Module.Category.COMBAT)
public class CrystalAura
extends Module {
    private static boolean togglePitch = false;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    private Setting<Boolean> place = this.register(Settings.b("Place", true));
    private Setting<Boolean> explode = this.register(Settings.b("Explode", true));
    private Setting<Boolean> autoSwitch = this.register(Settings.b("Auto Switch", true));
    private Setting<Boolean> antiWeakness = this.register(Settings.b("Anti Weakness", true));
    private Setting<Integer> hitTickDelay = this.register(Settings.integerBuilder("Hit Delay").withMinimum(0).withValue(4).withMaximum(20).build());
    private Setting<Double> hitRange = this.register(Settings.doubleBuilder("Hit Range").withMinimum(0.0).withValue(5.5).build());
    private Setting<Double> placeRange = this.register(Settings.doubleBuilder("Place Range").withMinimum(0.0).withValue(3.5).build());
    private Setting<Double> minDamage = this.register(Settings.doubleBuilder("Min Damage").withMinimum(0.0).withValue(2.0).withMaximum(20.0).build());
    private Setting<Boolean> spoofRotations = this.register(Settings.b("Spoof Rotations", false));
    private Setting<Boolean> rayTraceHit = this.register(Settings.b("RayTraceHit", false));
    private Setting<RenderMode> renderMode = this.register(Settings.e("Render Mode", RenderMode.UP));
    private Setting<Integer> red = this.register(Settings.integerBuilder("Red").withMinimum(0).withValue(104).withMaximum(255).build());
    private Setting<Integer> green = this.register(Settings.integerBuilder("Green").withMinimum(0).withValue(12).withMaximum(255).build());
    private Setting<Integer> blue = this.register(Settings.integerBuilder("Blue").withMinimum(0).withValue(35).withMaximum(255).build());
    private Setting<Integer> alpha = this.register(Settings.integerBuilder("Alpha").withMinimum(0).withValue(169).withMaximum(255).build());
    private Setting<Boolean> announceUsage = this.register(Settings.b("Announce Usage", true));
    private BlockPos renderBlock;
    private EntityPlayer target;
    private boolean switchCooldown = false;
    private boolean isAttacking = false;
    private int oldSlot = -1;
    private int newSlot;
    private int hitDelayCounter;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener = new Listener<PacketEvent.Send>(event -> {
        if (!this.spoofRotations.getValue().booleanValue()) {
            return;
        }
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).field_149476_e = (float)yaw;
            ((CPacketPlayer)packet).field_149473_f = (float)pitch;
        }
    }, new Predicate[0]);

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAura.mc.field_71439_g.field_70165_t), Math.floor(CrystalAura.mc.field_71439_g.field_70163_u), Math.floor(CrystalAura.mc.field_71439_g.field_70161_v));
    }

    static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.func_70011_f(posX, posY, posZ) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = CrystalAura.getBlastReduction((EntityLivingBase)entity, CrystalAura.getDamageMultiplied(damage), new Explosion((World)CrystalAura.mc.field_71441_e, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    private static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.func_94539_a((Explosion)explosion);
            damage = CombatRules.func_189427_a((float)damage, (float)ep.func_70658_aO(), (float)((float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
            int k = EnchantmentHelper.func_77508_a((Iterable)ep.func_184193_aE(), (DamageSource)ds);
            float f = MathHelper.func_76131_a((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.func_70644_a(MobEffects.field_76429_m)) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.func_189427_a((float)damage, (float)entity.func_70658_aO(), (float)((float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = CrystalAura.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = CrystalAura.mc.field_71439_g.field_70177_z;
            pitch = CrystalAura.mc.field_71439_g.field_70125_A;
            isSpoofingAngles = false;
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (this.renderBlock != null && !this.renderMode.getValue().equals((Object)RenderMode.NONE)) {
            this.drawBlock(this.renderBlock, this.red.getValue(), this.green.getValue(), this.blue.getValue());
        }
    }

    private void drawBlock(BlockPos blockPos, int r, int g, int b) {
        Color color = new Color(r, g, b, this.alpha.getValue());
        KamiTessellator.prepare(7);
        if (this.renderMode.getValue().equals((Object)RenderMode.UP)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 2);
        } else if (this.renderMode.getValue().equals((Object)RenderMode.BLOCK)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
        KamiTessellator.release();
    }

    @Override
    public void onUpdate() {
        int crystalSlot;
        if (CrystalAura.mc.field_71439_g == null) {
            return;
        }
        EntityEnderCrystal crystal = CrystalAura.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> (EntityEnderCrystal)entity).min(Comparator.comparing(c -> Float.valueOf(CrystalAura.mc.field_71439_g.func_70032_d((Entity)c)))).orElse(null);
        if (this.explode.getValue().booleanValue() && crystal != null && (double)CrystalAura.mc.field_71439_g.func_70032_d((Entity)crystal) <= this.hitRange.getValue() && this.rayTraceHitCheck(crystal)) {
            if (this.hitDelayCounter < this.hitTickDelay.getValue()) {
                ++this.hitDelayCounter;
                return;
            }
            this.hitDelayCounter = 0;
            if (this.antiWeakness.getValue().booleanValue() && CrystalAura.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
                if (!this.isAttacking) {
                    this.oldSlot = CrystalAura.mc.field_71439_g.field_71071_by.field_70461_c;
                    this.isAttacking = true;
                }
                this.newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = CrystalAura.mc.field_71439_g.field_71071_by.func_70301_a(i);
                    if (stack == ItemStack.field_190927_a) continue;
                    if (stack.func_77973_b() instanceof ItemSword) {
                        this.newSlot = i;
                        break;
                    }
                    if (!(stack.func_77973_b() instanceof ItemTool)) continue;
                    this.newSlot = i;
                    break;
                }
                if (this.newSlot != -1) {
                    CrystalAura.mc.field_71439_g.field_71071_by.field_70461_c = this.newSlot;
                    this.switchCooldown = true;
                }
            }
            this.lookAtPacket(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, (EntityPlayer)CrystalAura.mc.field_71439_g);
            CrystalAura.mc.field_71442_b.func_78764_a((EntityPlayer)CrystalAura.mc.field_71439_g, (Entity)crystal);
            CrystalAura.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            return;
        }
        CrystalAura.resetRotation();
        if (this.oldSlot != -1) {
            CrystalAura.mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
            this.oldSlot = -1;
        }
        this.isAttacking = false;
        int n = crystalSlot = CrystalAura.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP ? CrystalAura.mc.field_71439_g.field_71071_by.field_70461_c : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (CrystalAura.mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() != Items.field_185158_cP) continue;
                crystalSlot = l;
                break;
            }
        }
        boolean offhand = false;
        if (CrystalAura.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        List entities = CrystalAura.mc.field_71441_e.field_73010_i.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.func_70005_c_())).sorted((entity1, entity2) -> Float.compare(CrystalAura.mc.field_71439_g.func_70032_d(entity1), CrystalAura.mc.field_71439_g.func_70032_d(entity2))).collect(Collectors.toList());
        List<BlockPos> blocks = this.findCrystalBlocks();
        BlockPos targetBlock = null;
        double targetBlockDamage = 0.0;
        this.target = null;
        for (Entity entity3 : entities) {
            if (entity3 == CrystalAura.mc.field_71439_g || !(entity3 instanceof EntityPlayer)) continue;
            EntityPlayer testTarget = (EntityPlayer)entity3;
            if (testTarget.field_70128_L || testTarget.func_110143_aJ() <= 0.0f) continue;
            for (BlockPos blockPos : blocks) {
                if (testTarget.func_174818_b(blockPos) >= 169.0) continue;
                double targetDamage = CrystalAura.calculateDamage((double)blockPos.field_177962_a + 0.5, blockPos.field_177960_b + 1, (double)blockPos.field_177961_c + 0.5, (Entity)testTarget);
                double selfDamage = CrystalAura.calculateDamage((double)blockPos.field_177962_a + 0.5, blockPos.field_177960_b + 1, (double)blockPos.field_177961_c + 0.5, (Entity)CrystalAura.mc.field_71439_g);
                float healthTarget = testTarget.func_110143_aJ() + testTarget.func_110139_bj();
                float healthSelf = CrystalAura.mc.field_71439_g.func_110143_aJ() + CrystalAura.mc.field_71439_g.func_110139_bj();
                if (targetDamage < this.minDamage.getValue() || selfDamage >= (double)healthSelf - 0.5 || selfDamage > targetDamage && targetDamage < (double)healthTarget || !(targetDamage > targetBlockDamage)) continue;
                targetBlock = blockPos;
                targetBlockDamage = targetDamage;
                this.target = testTarget;
            }
            if (this.target == null) continue;
            break;
        }
        if (this.target == null) {
            this.renderBlock = null;
            CrystalAura.resetRotation();
            return;
        }
        this.renderBlock = targetBlock;
        if (ModuleManager.getModuleByName("AutoGG").isEnabled()) {
            AutoGG autoGG = (AutoGG)ModuleManager.getModuleByName("AutoGG");
            autoGG.addTargetedPlayer(this.target.func_70005_c_());
        }
        if (this.place.getValue().booleanValue()) {
            if (!offhand && CrystalAura.mc.field_71439_g.field_71071_by.field_70461_c != crystalSlot) {
                if (this.autoSwitch.getValue().booleanValue()) {
                    CrystalAura.mc.field_71439_g.field_71071_by.field_70461_c = crystalSlot;
                    CrystalAura.resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket((double)targetBlock.field_177962_a + 0.5, (double)targetBlock.field_177960_b - 0.5, (double)targetBlock.field_177961_c + 0.5, (EntityPlayer)CrystalAura.mc.field_71439_g);
            RayTraceResult result = CrystalAura.mc.field_71441_e.func_72933_a(new Vec3d(CrystalAura.mc.field_71439_g.field_70165_t, CrystalAura.mc.field_71439_g.field_70163_u + (double)CrystalAura.mc.field_71439_g.func_70047_e(), CrystalAura.mc.field_71439_g.field_70161_v), new Vec3d((double)targetBlock.field_177962_a + 0.5, (double)targetBlock.field_177960_b - 0.5, (double)targetBlock.field_177961_c + 0.5));
            EnumFacing f = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            CrystalAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(targetBlock, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        if (this.spoofRotations.getValue().booleanValue() && isSpoofingAngles) {
            if (togglePitch) {
                CrystalAura.mc.field_71439_g.field_70125_A = (float)((double)CrystalAura.mc.field_71439_g.field_70125_A + 4.0E-4);
                togglePitch = false;
            } else {
                CrystalAura.mc.field_71439_g.field_70125_A = (float)((double)CrystalAura.mc.field_71439_g.field_70125_A - 4.0E-4);
                togglePitch = true;
            }
        }
    }

    private boolean rayTraceHitCheck(EntityEnderCrystal crystal) {
        if (!this.rayTraceHit.getValue().booleanValue()) {
            return true;
        }
        return CrystalAura.mc.field_71439_g.func_70685_l((Entity)crystal);
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        CrystalAura.setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        return (CrystalAura.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || CrystalAura.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && CrystalAura.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && CrystalAura.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && CrystalAura.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalAura.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockInteractionHelper.getSphere(CrystalAura.getPlayerPos(), this.placeRange.getValue().floatValue(), this.placeRange.getValue().intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    @Override
    public void onEnable() {
        if (this.announceUsage.getValue().booleanValue()) {
            Command.sendChatMessage("[CrystalAura] " + ChatFormatting.GREEN.toString() + "Enabled!");
        }
        this.hitDelayCounter = 0;
    }

    @Override
    public void onDisable() {
        this.renderBlock = null;
        this.target = null;
        CrystalAura.resetRotation();
        if (this.announceUsage.getValue().booleanValue()) {
            Command.sendChatMessage("[CrystalAura] " + ChatFormatting.RED.toString() + "Disabled!");
        }
    }

    @Override
    public String getHudInfo() {
        if (this.target == null) {
            return "";
        }
        return this.target.func_70005_c_().toUpperCase();
    }

    private static enum RenderMode {
        UP,
        BLOCK,
        NONE;

    }
}

