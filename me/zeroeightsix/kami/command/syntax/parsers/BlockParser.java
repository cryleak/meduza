/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.ResourceLocation
 */
package me.zeroeightsix.kami.command.syntax.parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.command.syntax.parsers.AbstractParser;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class BlockParser
extends AbstractParser {
    private static HashMap<String, Block> blockNames = new HashMap();

    public BlockParser() {
        if (!blockNames.isEmpty()) {
            return;
        }
        for (ResourceLocation resourceLocation : Block.field_149771_c.func_148742_b()) {
            blockNames.put(resourceLocation.toString().replace("minecraft:", "").replace("_", ""), (Block)Block.field_149771_c.func_82594_a((Object)resourceLocation));
        }
    }

    @Override
    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        try {
            if (chunkValue == null) {
                return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + thisChunk.getType() + (thisChunk.isNecessary() ? ">" : "]");
            }
            HashMap<String, Block> possibilities = new HashMap<String, Block>();
            for (String s : blockNames.keySet()) {
                if (!s.toLowerCase().startsWith(chunkValue.toLowerCase().replace("minecraft:", "").replace("_", ""))) continue;
                possibilities.put(s, blockNames.get(s));
            }
            if (possibilities.isEmpty()) {
                return "";
            }
            TreeMap p = new TreeMap(possibilities);
            Map.Entry e = p.firstEntry();
            return ((String)e.getKey()).substring(chunkValue.length());
        }
        catch (Exception e) {
            return "";
        }
    }

    public static Block getBlockFromName(String name) {
        if (!blockNames.containsKey(name)) {
            return null;
        }
        return blockNames.get(name);
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (!hm.get(o).equals(value)) continue;
            return o;
        }
        return null;
    }

    public static String getNameFromBlock(Block b) {
        if (!blockNames.containsValue((Object)b)) {
            return null;
        }
        return (String)BlockParser.getKeyFromValue(blockNames, (Object)b);
    }
}

