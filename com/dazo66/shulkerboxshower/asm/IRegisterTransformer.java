/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.IClassTransformer
 */
package com.dazo66.shulkerboxshower.asm;

import java.util.List;
import net.minecraft.launchwrapper.IClassTransformer;

public interface IRegisterTransformer
extends IClassTransformer {
    public List<String> getMcVersion();

    public List<String> getClassName();
}

