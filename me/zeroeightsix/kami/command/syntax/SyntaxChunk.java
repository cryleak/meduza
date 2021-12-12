/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.command.syntax;

import me.zeroeightsix.kami.command.syntax.SyntaxParser;

public class SyntaxChunk {
    boolean headless = false;
    String head;
    String type;
    private boolean necessary;
    private SyntaxParser parser;
    public static final SyntaxChunk[] EMPTY = new SyntaxChunk[0];

    public SyntaxChunk(String head, String type, boolean necessary) {
        this.head = head;
        this.type = type;
        this.necessary = necessary;
        this.parser = (chunks, thisChunk, values, chunkValue) -> {
            if (chunkValue != null) {
                return null;
            }
            return head + (this.isNecessary() ? "<" : "[") + type + (this.isNecessary() ? ">" : "]");
        };
    }

    public SyntaxChunk(String type, boolean necessary) {
        this("", type, necessary);
        this.headless = true;
    }

    public String getHead() {
        return this.head;
    }

    public boolean isHeadless() {
        return this.headless;
    }

    public boolean isNecessary() {
        return this.necessary;
    }

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] args, String chunkValue) {
        String s = this.parser.getChunk(chunks, thisChunk, args, chunkValue);
        if (s == null) {
            return "";
        }
        return s;
    }

    public String getType() {
        return this.type;
    }

    public void setParser(SyntaxParser parser) {
        this.parser = parser;
    }
}

