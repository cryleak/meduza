/*
 * Decompiled with CFR 0.150.
 */
package org.reflections.scanners;

import org.reflections.scanners.AbstractScanner;
import org.reflections.vfs.Vfs;

public class ResourcesScanner
extends AbstractScanner {
    @Override
    public boolean acceptsInput(String file) {
        return !file.endsWith(".class");
    }

    @Override
    public Object scan(Vfs.File file, Object classObject) {
        this.getStore().put((Object)file.getName(), (Object)file.getRelativePath());
        return classObject;
    }

    @Override
    public void scan(Object cls) {
        throw new UnsupportedOperationException();
    }
}

