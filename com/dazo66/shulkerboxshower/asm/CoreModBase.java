/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin$MCVersion
 */
package com.dazo66.shulkerboxshower.asm;

import com.dazo66.shulkerboxshower.asm.AccessTransformer;
import com.dazo66.shulkerboxshower.asm.MainTransformer;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(value="")
public class CoreModBase
implements IFMLLoadingPlugin {
    private static boolean isDeo;

    public String[] getASMTransformerClass() {
        return new String[]{MainTransformer.class.getName()};
    }

    public String getModContainerClass() {
        return null;
    }

    @Nullable
    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        isDeo = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return AccessTransformer.class.getName();
    }

    public static boolean isDeo() {
        return isDeo;
    }
}

