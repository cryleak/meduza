/*
 * Decompiled with CFR 0.150.
 */
package javassist;

public class NotFoundException
extends Exception {
    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Exception e) {
        super(msg + " because of " + e.toString());
    }
}

