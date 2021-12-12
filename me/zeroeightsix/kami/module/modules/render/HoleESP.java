/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.zeroeightsix.kami.module.modules.render;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.modules.combat.CrystalAura;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.BlockInteractionHelper;
import me.zeroeightsix.kami.util.KamiTessellator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Info(name="HoleESP", category=Module.Category.RENDER, description="Show safe holes")
public class HoleESP
extends Module {
    private final BlockPos[] surroundOffset = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};
    private Setting<HoleType> holeType = this.register(Settings.e("HoleType", HoleType.BOTH));
    private Setting<Boolean> hideOwn = this.register(Settings.b("HideOwn", false));
    private Setting<Double> renderDistance = this.register(Settings.doubleBuilder("RenderDistance").withMinimum(1.0).withValue(8.0).withMaximum(32.0).build());
    private Setting<RenderMode> renderMode = this.register(Settings.e("RenderMode", RenderMode.DOWN));
    private Setting<Integer> obiRed = this.register(Settings.integerBuilder("ObiRed").withMinimum(0).withValue(104).withMaximum(255).build());
    private Setting<Integer> obiGreen = this.register(Settings.integerBuilder("ObiGreen").withMinimum(0).withValue(12).withMaximum(255).build());
    private Setting<Integer> obiBlue = this.register(Settings.integerBuilder("ObiBlue").withMinimum(0).withValue(35).withMaximum(255).build());
    private Setting<Integer> brockRed = this.register(Settings.integerBuilder("BrockRed").withMinimum(0).withValue(81).withMaximum(255).build());
    private Setting<Integer> brockGreen = this.register(Settings.integerBuilder("BrockGreen").withMinimum(0).withValue(12).withMaximum(255).build());
    private Setting<Integer> brockBlue = this.register(Settings.integerBuilder("BrockBlue").withMinimum(0).withValue(104).withMaximum(255).build());
    private Setting<Integer> alpha = this.register(Settings.integerBuilder("Alpha").withMinimum(0).withValue(169).withMaximum(255).build());
    private ConcurrentHashMap<BlockPos, Boolean> safeHoles;

    @Override
    public void onUpdate() {
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap();
        } else {
            this.safeHoles.clear();
        }
        int range = (int)Math.ceil(this.renderDistance.getValue());
        List<BlockPos> blockPosList = BlockInteractionHelper.getSphere(CrystalAura.getPlayerPos(), range, range, false, true, 0);
        for (BlockPos pos : blockPosList) {
            if (!HoleESP.mc.field_71441_e.func_180495_p(pos).func_177230_c().equals((Object)Blocks.field_150350_a) || !HoleESP.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals((Object)Blocks.field_150350_a) || !HoleESP.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals((Object)Blocks.field_150350_a) || this.hideOwn.getValue().booleanValue() && pos.equals((Object)new BlockPos(HoleESP.mc.field_71439_g.field_70165_t, HoleESP.mc.field_71439_g.field_70163_u, HoleESP.mc.field_71439_g.field_70161_v))) continue;
            boolean isSafe = true;
            boolean isBedrock = true;
            for (BlockPos offset : this.surroundOffset) {
                Block block = HoleESP.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
                if (block != Blocks.field_150357_h) {
                    isBedrock = false;
                }
                if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ) continue;
                isSafe = false;
                break;
            }
            if (!isSafe) continue;
            this.safeHoles.put(pos, isBedrock);
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (HoleESP.mc.field_71439_g == null || this.safeHoles == null) {
            return;
        }
        if (this.safeHoles.isEmpty()) {
            return;
        }
        KamiTessellator.prepare(7);
        this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
                if (this.holeType.getValue().equals((Object)HoleType.BOTH) || this.holeType.getValue().equals((Object)HoleType.BROCK)) {
                    this.drawBlock((BlockPos)blockPos, this.brockRed.getValue(), this.brockGreen.getValue(), this.brockBlue.getValue());
                }
            } else if (this.holeType.getValue().equals((Object)HoleType.BOTH) || this.holeType.getValue().equals((Object)HoleType.OBI)) {
                this.drawBlock((BlockPos)blockPos, this.obiRed.getValue(), this.obiGreen.getValue(), this.obiBlue.getValue());
            }
        });
        KamiTessellator.release();
    }

    private void drawBlock(BlockPos blockPos, int r, int g, int b) {
        Color color = new Color(r, g, b, this.alpha.getValue());
        int mask = 1;
        if (this.renderMode.getValue().equals((Object)RenderMode.BLOCK)) {
            mask = 63;
        }
        KamiTessellator.drawBox(blockPos, color.getRGB(), mask);
    }

    @Override
    public String getHudInfo() {
        return this.holeType.getValue().toString();
    }

    private static enum HoleType {
        BROCK,
        OBI,
        BOTH;

    }

    private static enum RenderMode {
        DOWN,
        BLOCK;

    }
}

