/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.util.ResourceLocation
 */
package me.zeroeightsix.kami.mixin.client;

import java.util.UUID;
import javax.annotation.Nullable;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.CapeManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo func_175155_b();

    @Inject(method={"getLocationCape"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (ModuleManager.isModuleEnabled("Capes")) {
            NetworkPlayerInfo info = this.func_175155_b();
            UUID uuid = null;
            if (info != null) {
                uuid = this.func_175155_b().func_178845_a().getId();
            }
            if (uuid != null && CapeManager.hasCape(uuid)) {
                if (CapeManager.isOg(uuid)) {
                    callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/cape_og.png"));
                } else {
                    callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/cape.png"));
                }
            }
        }
    }
}

