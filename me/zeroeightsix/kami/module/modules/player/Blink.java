/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package me.zeroeightsix.kami.module.modules.player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Info(name="Blink", category=Module.Category.PLAYER)
public class Blink
extends Module {
    Queue<CPacketPlayer> packets = new LinkedList<CPacketPlayer>();
    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (this.isEnabled() && event.getPacket() instanceof CPacketPlayer) {
            event.cancel();
            this.packets.add((CPacketPlayer)event.getPacket());
        }
    }, new Predicate[0]);

    @Override
    protected void onDisable() {
        while (!this.packets.isEmpty()) {
            Blink.mc.field_71439_g.field_71174_a.func_147297_a((Packet)this.packets.poll());
        }
    }

    @Override
    public String getHudInfo() {
        return String.valueOf(this.packets.size());
    }
}

