/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.lang.invoke.LambdaMetafactory;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.BlockInteractionHelper;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Module.Info(name="HoleFill", category=Module.Category.COMBAT)
public class HoleFill
extends Module {
    private /* synthetic */ ArrayList<BlockPos> holes;
    private /* synthetic */ Setting<Boolean> chat;
    private /* synthetic */ int waitCounter;
    /* synthetic */ BlockPos pos;
    private static final /* synthetic */ int[] lllIIlIlI;
    private /* synthetic */ Setting<Double> yRange;
    private /* synthetic */ List<Block> whiteList;
    private /* synthetic */ Setting<Integer> waitTick;
    private /* synthetic */ Setting<Double> range;
    private static final /* synthetic */ String[] lllIIIIlI;

    @Override
    public void onDisable() {
        HoleFill lIlIIlIlIlllIll;
        if (HoleFill.lllllIIIII((Object)HoleFill.mc.field_71439_g) && HoleFill.llllIlIlIl((int)lIlIIlIlIlllIll.chat.getValue().booleanValue())) {
            Command.sendChatMessage(lllIIIIlI[lllIIlIlI[7]]);
        }
    }

    private static boolean llllIllllI(int n, int n2) {
        return n == n2;
    }

    private static void llllIlIlII() {
        lllIIlIlI = new int[10];
        HoleFill.lllIIlIlI[0] = " ".length();
        HoleFill.lllIIlIlI[1] = (57 + 112 - 143 + 197 ^ 104 + 184 - 184 + 86) & (0x3D ^ 0x61 ^ (0x85 ^ 0xB8) ^ -" ".length());
        HoleFill.lllIIlIlI[2] = "  ".length();
        HoleFill.lllIIlIlI[3] = "   ".length();
        HoleFill.lllIIlIlI[4] = -" ".length();
        HoleFill.lllIIlIlI[5] = 0xF4 ^ 0xAA ^ (0x5F ^ 8);
        HoleFill.lllIIlIlI[6] = 27 + 164 - 79 + 66 ^ 126 + 24 - 55 + 87;
        HoleFill.lllIIlIlI[7] = 0xB7 ^ 0xB2;
        HoleFill.lllIIlIlI[8] = 0xBF ^ 0xAB ^ (0x1B ^ 9);
        HoleFill.lllIIlIlI[9] = 91 + 148 - 183 + 133 ^ 6 + 97 - 58 + 136;
    }

    private static String lllIlIlllI(String lIlIIlIlIIlIIll, String lIlIIlIlIIlIlII) {
        try {
            SecretKeySpec lIlIIlIlIIllIII = new SecretKeySpec(Arrays.copyOf(MessageDigest.getInstance("MD5").digest(lIlIIlIlIIlIlII.getBytes(StandardCharsets.UTF_8)), lllIIlIlI[9]), "DES");
            Cipher lIlIIlIlIIlIlll = Cipher.getInstance("DES");
            lIlIIlIlIIlIlll.init(lllIIlIlI[2], lIlIIlIlIIllIII);
            return new String(lIlIIlIlIIlIlll.doFinal(Base64.getDecoder().decode(lIlIIlIlIIlIIll.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        }
        catch (Exception lIlIIlIlIIlIllI) {
            lIlIIlIlIIlIllI.printStackTrace();
            return null;
        }
    }

    private static void lllIllIIII() {
        lllIIIIlI = new String[lllIIlIlI[8]];
        HoleFill.lllIIIIlI[HoleFill.lllIIlIlI[1]] = HoleFill.lllIlIlllI("TPGcWRd/anc=", "ZRDCW");
        HoleFill.lllIIIIlI[HoleFill.lllIIlIlI[0]] = HoleFill.lllIlIllll("klfzmjf7IiM=", "PJBbO");
        HoleFill.lllIIIIlI[HoleFill.lllIIlIlI[2]] = HoleFill.lllIlIlllI("AmF7bL0NFuMWIt8+72/DMg==", "ApTWv");
        HoleFill.lllIIIIlI[HoleFill.lllIIlIlI[3]] = HoleFill.lllIlIllll("9ck8IM+7Y0f9wvS2ap8ICw==", "DoRVm");
        HoleFill.lllIIIIlI[HoleFill.lllIIlIlI[6]] = HoleFill.lllIlIlllI("1p7zTxy3ZMgPzRY/u9R8iRq/Eiin1U6Y", "ECDXe");
        HoleFill.lllIIIIlI[HoleFill.lllIIlIlI[7]] = HoleFill.lllIlIlllI("raC5R+r9Mcp7eqk4jIuCq7rOdHLk3wBg", "TZleV");
    }

    private static boolean llllIlIlll(Object object, Object object2) {
        return object == object2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void onUpdate() {
        lIlIIlIllIIIllI.holes = new ArrayList<E>();
        lIlIIlIllIIlIIl = BlockPos.func_177980_a((BlockPos)HoleFill.mc.field_71439_g.func_180425_c().func_177963_a(-lIlIIlIllIIIllI.range.getValue().doubleValue(), -lIlIIlIllIIIllI.yRange.getValue().doubleValue(), -lIlIIlIllIIIllI.range.getValue().doubleValue()), (BlockPos)HoleFill.mc.field_71439_g.func_180425_c().func_177963_a(lIlIIlIllIIIllI.range.getValue().doubleValue(), lIlIIlIllIIIllI.yRange.getValue().doubleValue(), lIlIIlIllIIIllI.range.getValue().doubleValue()));
        lIlIIlIllIIIlII = lIlIIlIllIIlIIl.iterator();
        while (HoleFill.llllIlIlIl((int)lIlIIlIllIIIlII.hasNext())) {
            block45: {
                lIlIIlIllIIlllI = (BlockPos)lIlIIlIllIIIlII.next();
                if (!HoleFill.llllIlIllI((int)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI).func_185904_a().func_76230_c()) || !HoleFill.llllIlIllI((int)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[0], HoleFill.lllIIlIlI[1])).func_185904_a().func_76230_c())) break block45;
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[4], HoleFill.lllIIlIlI[1])).func_177230_c(), (Object)Blocks.field_150357_h)) {
                    v0 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if (((137 ^ 158) & ~(40 ^ 63)) >= (38 ^ 34)) {
                        return;
                    }
                } else {
                    v0 = HoleFill.lllIIlIlI[1];
                }
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[4], HoleFill.lllIIlIlI[1])).func_177230_c(), (Object)Blocks.field_150343_Z)) {
                    v1 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if ("   ".length() == 0) {
                        return;
                    }
                } else {
                    v1 = HoleFill.lllIIlIlI[1];
                }
                if (!HoleFill.llllIlIlIl(v0 | v1)) ** GOTO lbl-1000
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[0], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1])).func_177230_c(), (Object)Blocks.field_150357_h)) {
                    v2 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if (-" ".length() >= 0) {
                        return;
                    }
                } else {
                    v2 = HoleFill.lllIIlIlI[1];
                }
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[0], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1])).func_177230_c(), (Object)Blocks.field_150343_Z)) {
                    v3 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if ("  ".length() > "  ".length()) {
                        return;
                    }
                } else {
                    v3 = HoleFill.lllIIlIlI[1];
                }
                if (!HoleFill.llllIlIlIl(v2 | v3)) ** GOTO lbl-1000
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[0])).func_177230_c(), (Object)Blocks.field_150357_h)) {
                    v4 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if (" ".length() < 0) {
                        return;
                    }
                } else {
                    v4 = HoleFill.lllIIlIlI[1];
                }
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[0])).func_177230_c(), (Object)Blocks.field_150343_Z)) {
                    v5 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if ("  ".length() > "  ".length()) {
                        return;
                    }
                } else {
                    v5 = HoleFill.lllIIlIlI[1];
                }
                if (!HoleFill.llllIlIlIl(v4 | v5)) ** GOTO lbl-1000
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[4], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1])).func_177230_c(), (Object)Blocks.field_150357_h)) {
                    v6 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if ((69 ^ 124 ^ (105 ^ 84)) <= 0) {
                        return;
                    }
                } else {
                    v6 = HoleFill.lllIIlIlI[1];
                }
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[4], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1])).func_177230_c(), (Object)Blocks.field_150343_Z)) {
                    v7 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if (-" ".length() < -" ".length()) {
                        return;
                    }
                } else {
                    v7 = HoleFill.lllIIlIlI[1];
                }
                if (!HoleFill.llllIlIlIl(v6 | v7)) ** GOTO lbl-1000
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[4])).func_177230_c(), (Object)Blocks.field_150357_h)) {
                    v8 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if ((65 ^ 114 ^ (141 ^ 186)) != (76 ^ 40 ^ (202 ^ 170))) {
                        return;
                    }
                } else {
                    v8 = HoleFill.lllIIlIlI[1];
                }
                if (HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[4])).func_177230_c(), (Object)Blocks.field_150343_Z)) {
                    v9 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if (-(31 + 176 - 135 + 111 ^ 86 + 164 - 242 + 171) > 0) {
                        return;
                    }
                } else {
                    v9 = HoleFill.lllIIlIlI[1];
                }
                if (HoleFill.llllIlIlIl(v8 | v9) && HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[1])).func_185904_a(), (Object)Material.field_151579_a) && HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[0], HoleFill.lllIIlIlI[1])).func_185904_a(), (Object)Material.field_151579_a) && HoleFill.llllIlIlll((Object)HoleFill.mc.field_71441_e.func_180495_p(lIlIIlIllIIlllI.func_177982_a(HoleFill.lllIIlIlI[1], HoleFill.lllIIlIlI[2], HoleFill.lllIIlIlI[1])).func_185904_a(), (Object)Material.field_151579_a)) {
                    v10 = HoleFill.lllIIlIlI[0];
                    "".length();
                    if (-"   ".length() > 0) {
                        return;
                    }
                } else lbl-1000:
                // 5 sources

                {
                    v10 = HoleFill.lllIIlIlI[1];
                }
                if (HoleFill.llllIlIlIl(lIlIIlIllIIllll = v10)) {
                    lIlIIlIllIIIllI.holes.add(lIlIIlIllIIlllI);
                    "".length();
                }
            }
            "".length();
            if (-" ".length() <= " ".length()) continue;
            return;
        }
        lIlIIlIllIIlIII = HoleFill.lllIIlIlI[4];
        lIlIIlIllIIlIll = HoleFill.lllIIlIlI[1];
        while (HoleFill.llllIllIlI(lIlIIlIllIIlIll, HoleFill.lllIIlIlI[5])) {
            lIlIIlIllIIllIl = Wrapper.getPlayer().field_71071_by.func_70301_a(lIlIIlIllIIlIll);
            if (HoleFill.llllIllIll((Object)lIlIIlIllIIllIl, (Object)ItemStack.field_190927_a)) {
                if (HoleFill.llllIlIllI(lIlIIlIllIIllIl.func_77973_b() instanceof ItemBlock)) {
                    "".length();
                    if (-"   ".length() > 0) {
                        return;
                    }
                } else {
                    lIlIIlIllIIllII = ((ItemBlock)lIlIIlIllIIllIl.func_77973_b()).func_179223_d();
                    if (HoleFill.llllIlIllI((int)lIlIIlIllIIIllI.whiteList.contains((Object)lIlIIlIllIIllII))) {
                        "".length();
                        if ((" ".length() & ~" ".length()) != 0) {
                            return;
                        }
                    } else {
                        lIlIIlIllIIlIII = lIlIIlIllIIlIll;
                        "".length();
                        if (((150 + 49 - 183 + 141 ^ 65 + 146 - 130 + 74) & (153 + 33 - 43 + 11 ^ 33 + 104 - 50 + 69 ^ -" ".length())) >= 0) break;
                        return;
                    }
                }
            }
            ++lIlIIlIllIIlIll;
            "".length();
            if ("   ".length() > 0) continue;
            return;
        }
        if (HoleFill.llllIllllI(lIlIIlIllIIlIII, HoleFill.lllIIlIlI[4])) {
            return;
        }
        lIlIIlIllIIIlll = Wrapper.getPlayer().field_71071_by.field_70461_c;
        if (HoleFill.llllIlllll(lIlIIlIllIIIllI.waitTick.getValue()) == false) return;
        if (HoleFill.llllIllIlI(lIlIIlIllIIIllI.waitCounter, lIlIIlIllIIIllI.waitTick.getValue())) {
            Wrapper.getPlayer().field_71071_by.field_70461_c = lIlIIlIllIIlIII;
            lIlIIlIllIIIllI.holes.forEach((Consumer<BlockPos>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$onUpdate$0(net.minecraft.util.math.BlockPos ), (Lnet/minecraft/util/math/BlockPos;)V)((HoleFill)lIlIIlIllIIIllI));
            Wrapper.getPlayer().field_71071_by.field_70461_c = lIlIIlIllIIIlll;
            return;
        }
        lIlIIlIllIIIllI.waitCounter = HoleFill.lllIIlIlI[1];
    }

    private void place(BlockPos lIlIIlIlIllIlII) {
        Iterator lIlIIlIlIllIIIl = HoleFill.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(lIlIIlIlIllIlII)).iterator();
        while (HoleFill.llllIlIlIl((int)lIlIIlIlIllIIIl.hasNext())) {
            Entity lIlIIlIlIllIllI = (Entity)lIlIIlIlIllIIIl.next();
            if (HoleFill.llllIlIlIl(lIlIIlIlIllIllI instanceof EntityLivingBase)) {
                return;
            }
            "".length();
            if (null == null) continue;
            return;
        }
        if (HoleFill.llllIlIllI((int)HoleFill.mc.field_71439_g.func_70093_af())) {
            HoleFill.mc.field_71439_g.func_70095_a(lllIIlIlI[0]);
        }
        BlockInteractionHelper.placeBlockScaffold(lIlIIlIlIllIlII);
        if (HoleFill.llllIlIlIl((int)HoleFill.mc.field_71439_g.func_70093_af())) {
            HoleFill.mc.field_71439_g.func_70095_a(lllIIlIlI[1]);
        }
        lIlIIlIlIllIlIl.waitCounter += lllIIlIlI[0];
    }

    private static String lllIlIllll(String lIlIIlIlIlIIIlI, String lIlIIlIlIlIIIIl) {
        try {
            SecretKeySpec lIlIIlIlIlIIlIl = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(lIlIIlIlIlIIIIl.getBytes(StandardCharsets.UTF_8)), "Blowfish");
            Cipher lIlIIlIlIlIIlII = Cipher.getInstance("Blowfish");
            lIlIIlIlIlIIlII.init(lllIIlIlI[2], lIlIIlIlIlIIlIl);
            return new String(lIlIIlIlIlIIlII.doFinal(Base64.getDecoder().decode(lIlIIlIlIlIIIlI.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        }
        catch (Exception lIlIIlIlIlIIIll) {
            lIlIIlIlIlIIIll.printStackTrace();
            return null;
        }
    }

    public HoleFill() {
        HoleFill lIlIIlIllIlIlll;
        lIlIIlIllIlIlll.holes = new ArrayList();
        Block[] arrblock = new Block[lllIIlIlI[0]];
        arrblock[HoleFill.lllIIlIlI[1]] = Blocks.field_150343_Z;
        lIlIIlIllIlIlll.whiteList = Arrays.asList(arrblock);
        lIlIIlIllIlIlll.range = lIlIIlIllIlIlll.register(Settings.doubleBuilder(lllIIIIlI[lllIIlIlI[1]]).withRange(0.0, 10.0).withValue(4.0).build());
        lIlIIlIllIlIlll.yRange = lIlIIlIllIlIlll.register(Settings.doubleBuilder(lllIIIIlI[lllIIlIlI[0]]).withRange(0.0, 10.0).withValue(1.0).build());
        lIlIIlIllIlIlll.waitTick = lIlIIlIllIlIlll.register(Settings.integerBuilder(lllIIIIlI[lllIIlIlI[2]]).withMinimum(lllIIlIlI[1]).withValue(lllIIlIlI[3]).build());
        lIlIIlIllIlIlll.chat = lIlIIlIllIlIlll.register(Settings.b(lllIIIIlI[lllIIlIlI[3]], lllIIlIlI[0]));
    }

    static {
        HoleFill.llllIlIlII();
        HoleFill.lllIllIIII();
    }

    private static boolean llllIlIllI(int n) {
        return n == 0;
    }

    private static boolean lllllIIIII(Object object) {
        return object != null;
    }

    private static boolean llllIllIll(Object object, Object object2) {
        return object != object2;
    }

    private static boolean llllIlllll(int n) {
        return n > 0;
    }

    private static boolean llllIllIlI(int n, int n2) {
        return n < n2;
    }

    private static boolean llllIlIlIl(int n) {
        return n != 0;
    }

    @Override
    public void onEnable() {
        HoleFill lIlIIlIlIlllllI;
        if (HoleFill.lllllIIIII((Object)HoleFill.mc.field_71439_g) && HoleFill.llllIlIlIl((int)lIlIIlIlIlllllI.chat.getValue().booleanValue())) {
            Command.sendChatMessage(lllIIIIlI[lllIIlIlI[6]]);
        }
    }

    private /* synthetic */ void lambda$onUpdate$0(BlockPos lIlIIlIlIlIlIlI) {
        HoleFill lIlIIlIlIlIllIl;
        lIlIIlIlIlIllIl.place(lIlIIlIlIlIlIlI);
    }
}

