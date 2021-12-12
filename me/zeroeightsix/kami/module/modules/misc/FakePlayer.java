/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package me.zeroeightsix.kami.module.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Module.Info(name="FakePlayer", category=Module.Category.MISC, description="Spawns a fake Player")
public class FakePlayer
extends Module {
    private Setting<SpawnMode> spawnMode = this.register(Settings.e("Spawn Mode", SpawnMode.SINGLE));
    private List<Integer> fakePlayerIdList = null;
    private static final String[][] fakePlayerInfo = new String[][]{{"66666666-6666-6666-6666-666666666600", "derp0", "-3", "0"}, {"66666666-6666-6666-6666-666666666601", "derp1", "0", "-3"}, {"66666666-6666-6666-6666-666666666602", "derp2", "3", "0"}, {"66666666-6666-6666-6666-666666666603", "derp3", "0", "3"}, {"66666666-6666-6666-6666-666666666604", "derp4", "-6", "0"}, {"66666666-6666-6666-6666-666666666605", "derp5", "0", "-6"}, {"66666666-6666-6666-6666-666666666606", "derp6", "6", "0"}, {"66666666-6666-6666-6666-666666666607", "derp7", "0", "6"}, {"66666666-6666-6666-6666-666666666608", "derp8", "-9", "0"}, {"66666666-6666-6666-6666-666666666609", "derp9", "0", "-9"}, {"66666666-6666-6666-6666-666666666610", "derp10", "9", "0"}, {"66666666-6666-6666-6666-666666666611", "derp11", "0", "9"}};

    @Override
    protected void onEnable() {
        if (FakePlayer.mc.field_71439_g == null || FakePlayer.mc.field_71441_e == null) {
            this.disable();
            return;
        }
        this.fakePlayerIdList = new ArrayList<Integer>();
        int entityId = -101;
        for (String[] data : fakePlayerInfo) {
            if (this.spawnMode.getValue().equals((Object)SpawnMode.SINGLE)) {
                this.addFakePlayer(data[0], data[1], entityId, 0, 0);
                break;
            }
            this.addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            --entityId;
        }
    }

    private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.field_71441_e, new GameProfile(UUID.fromString(uuid), name));
        fakePlayer.func_82149_j((Entity)FakePlayer.mc.field_71439_g);
        fakePlayer.field_70165_t += (double)offsetX;
        fakePlayer.field_70161_v += (double)offsetZ;
        FakePlayer.mc.field_71441_e.func_73027_a(entityId, (Entity)fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }

    @Override
    public void onUpdate() {
        if (this.fakePlayerIdList == null || this.fakePlayerIdList.isEmpty()) {
            this.disable();
        }
    }

    @Override
    protected void onDisable() {
        if (FakePlayer.mc.field_71439_g == null || FakePlayer.mc.field_71441_e == null) {
            return;
        }
        if (this.fakePlayerIdList != null) {
            for (int id : this.fakePlayerIdList) {
                FakePlayer.mc.field_71441_e.func_73028_b(id);
            }
        }
    }

    private static enum SpawnMode {
        SINGLE,
        MULTI;

    }
}

