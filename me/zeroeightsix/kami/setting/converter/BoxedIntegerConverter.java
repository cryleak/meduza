/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 */
package me.zeroeightsix.kami.setting.converter;

import com.google.gson.JsonElement;
import me.zeroeightsix.kami.setting.converter.AbstractBoxedNumberConverter;

public class BoxedIntegerConverter
extends AbstractBoxedNumberConverter<Integer> {
    protected Integer doBackward(JsonElement s) {
        return s.getAsInt();
    }
}

