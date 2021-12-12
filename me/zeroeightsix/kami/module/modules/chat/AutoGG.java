/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 */
package me.zeroeightsix.kami.module.modules.chat;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@Module.Info(name="AutoGG", category=Module.Category.CHAT, description="Announce killed Players")
public class AutoGG
extends Module {
    private ConcurrentHashMap<String, Integer> targetedPlayers = null;
    private Setting<Boolean> toxicMode = this.register(Settings.b("Svenska", false));
    private Setting<Boolean> clientName = this.register(Settings.b("ClientName", true));
    private Setting<Integer> timeoutTicks = this.register(Settings.i("TimeoutTicks", 20));
    @EventHandler
    public Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (AutoGG.mc.field_71439_g == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
        }
        if (!(event.getPacket() instanceof CPacketUseEntity)) {
            return;
        }
        CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
        if (!cPacketUseEntity.func_149565_c().equals((Object)CPacketUseEntity.Action.ATTACK)) {
            return;
        }
        Entity targetEntity = cPacketUseEntity.func_149564_a((World)AutoGG.mc.field_71441_e);
        if (!EntityUtil.isPlayer(targetEntity)) {
            return;
        }
        this.addTargetedPlayer(targetEntity.func_70005_c_());
    }, new Predicate[0]);
    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener = new Listener<LivingDeathEvent>(event -> {
        EntityLivingBase entity;
        if (AutoGG.mc.field_71439_g == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
        }
        if ((entity = event.getEntityLiving()) == null) {
            return;
        }
        if (!EntityUtil.isPlayer((Entity)entity)) {
            return;
        }
        EntityPlayer player = (EntityPlayer)entity;
        if (player.func_110143_aJ() > 0.0f) {
            return;
        }
        String name = player.func_70005_c_();
        if (this.shouldAnnounce(name)) {
            this.doAnnounce(name);
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap();
    }

    @Override
    public void onDisable() {
        this.targetedPlayers = null;
    }

    @Override
    public void onUpdate() {
        if (this.isDisabled() || AutoGG.mc.field_71439_g == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
        }
        for (Entity entity : AutoGG.mc.field_71441_e.func_72910_y()) {
            String name2;
            EntityPlayer player;
            if (!EntityUtil.isPlayer(entity) || (player = (EntityPlayer)entity).func_110143_aJ() > 0.0f || !this.shouldAnnounce(name2 = player.func_70005_c_())) continue;
            this.doAnnounce(name2);
            break;
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            } else {
                this.targetedPlayers.put((String)name, timeout - 1);
            }
        });
    }

    private boolean shouldAnnounce(String name) {
        return this.targetedPlayers.containsKey(name);
    }

    private void doAnnounce(String name) {
        String messageSanitized;
        this.targetedPlayers.remove(name);
        StringBuilder message = new StringBuilder();
        if (this.toxicMode.getValue().booleanValue()) {
            message.append("L\u00e4tt! ");
        } else {
            message.append("Bra match ");
        }
        message.append(name);
        message.append("!");
        if (this.clientName.getValue().booleanValue()) {
            message.append(" ");
            message.append("Meduza");
            message.append(" rules me and all");
        }
        if ((messageSanitized = message.toString().replaceAll("\u00a7", "")).length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        AutoGG.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage(messageSanitized));
    }

    public void addTargetedPlayer(String name) {
        if (Objects.equals(name, AutoGG.mc.field_71439_g.func_70005_c_())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
        }
        this.targetedPlayers.put(name, this.timeoutTicks.getValue());
    }
}

