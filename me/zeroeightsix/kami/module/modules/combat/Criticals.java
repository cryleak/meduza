/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

@Module.Info(name="Criticals", category=Module.Category.COMBAT)
public class Criticals
extends Module {
    @EventHandler
    private /* synthetic */ Listener<AttackEntityEvent> attackEntityEventListener;
    private static final /* synthetic */ int[] lIIIIllI;

    public Criticals() {
        Criticals lllIlIlIlIlIIll;
        lllIlIlIlIlIIll.attackEntityEventListener = new Listener<AttackEntityEvent>(lllIlIlIlIlIIIl -> {
            if (Criticals.llllllIl((int)Criticals.mc.field_71439_g.func_70090_H()) && Criticals.llllllIl((int)Criticals.mc.field_71439_g.func_180799_ab()) && Criticals.lllllllI((int)Criticals.mc.field_71439_g.field_70122_E)) {
                Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.1625, Criticals.mc.field_71439_g.field_70161_v, lIIIIllI[0]));
                Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, lIIIIllI[0]));
                Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 4.0E-6, Criticals.mc.field_71439_g.field_70161_v, lIIIIllI[0]));
                Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, lIIIIllI[0]));
                Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 1.0E-6, Criticals.mc.field_71439_g.field_70161_v, lIIIIllI[0]));
                Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, lIIIIllI[0]));
                Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer());
                Criticals.mc.field_71439_g.func_71009_b(lllIlIlIlIlIIIl.getTarget());
            }
        }, new Predicate[lIIIIllI[0]]);
    }

    private static void llllllII() {
        lIIIIllI = new int[1];
        Criticals.lIIIIllI[0] = (0xFA ^ 0x93 ^ (0x7A ^ 0x70)) & (112 + 202 - 239 + 167 ^ 65 + 35 - -44 + 1 ^ -" ".length());
    }

    private static boolean llllllIl(int n) {
        return n == 0;
    }

    static {
        Criticals.llllllII();
    }

    private static boolean lllllllI(int n) {
        return n != 0;
    }
}

