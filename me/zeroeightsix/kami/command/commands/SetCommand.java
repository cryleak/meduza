/*
 * Decompiled with CFR 0.150.
 */
package me.zeroeightsix.kami.command.commands;

import java.util.Optional;
import java.util.stream.Collectors;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.ModuleParser;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.ISettingUnknown;
import me.zeroeightsix.kami.setting.Setting;

public class SetCommand
extends Command {
    public SetCommand() {
        super("set", new ChunkBuilder().append("module", true, new ModuleParser()).append("setting", true).append("value", true).build());
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module &a" + args[0] + "&r!");
            return;
        }
        if (args[1] == null) {
            String settings = String.join((CharSequence)", ", m.settingList.stream().map(setting -> setting.getName()).collect(Collectors.toList()));
            if (settings.isEmpty()) {
                Command.sendChatMessage("Module &a" + m.getName() + "&r has no settings.");
            } else {
                Command.sendStringChatMessage(new String[]{"Please specify a setting! Choose one of the following:", settings});
            }
            return;
        }
        Optional<Setting> optionalSetting = m.settingList.stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(args[1])).findFirst();
        if (!optionalSetting.isPresent()) {
            Command.sendChatMessage("Unknown setting &a" + args[1] + "&r in &a" + m.getName() + "&r!");
            return;
        }
        ISettingUnknown setting2 = optionalSetting.get();
        if (args[2] == null) {
            Command.sendChatMessage("&a" + setting2.getName() + "&r is a &2" + setting2.getValueClass().getSimpleName() + "&r. Its current value is &2" + setting2.getValueAsString());
            return;
        }
        try {
            setting2.setValueFromString(args[2]);
            Command.sendChatMessage("Set &a" + setting2.getName() + "&r to &2" + args[2] + "&r.");
        }
        catch (Exception e) {
            e.printStackTrace();
            Command.sendChatMessage("Unable to set value! &2" + e.getMessage());
        }
    }
}

