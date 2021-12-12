/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiPlayerTabOverlay
 *  net.minecraft.client.network.NetworkPlayerInfo
 */
package me.zeroeightsix.kami.mixin.client;

import me.zeroeightsix.kami.module.modules.render.TabFriends;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiPlayerTabOverlay.class})
public class MixinGuiPlayerTabOverlay {
    @Inject(method={"getPlayerName"}, at={@At(value="HEAD")}, cancellable=true)
    public void getPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable returnable) {
        if (TabFriends.INSTANCE.isEnabled()) {
            returnable.cancel();
            returnable.setReturnValue(TabFriends.getPlayerName(networkPlayerInfoIn));
        }
    }
}

