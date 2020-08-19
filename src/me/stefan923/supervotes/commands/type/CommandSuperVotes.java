package me.stefan923.supervotes.commands.type;

import me.stefan923.supervotes.commands.AbstractCommand;
import me.stefan923.supervotes.SuperVotes;
import me.stefan923.supervotes.utils.MessageUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CommandSuperVotes extends AbstractCommand implements MessageUtils {

    public CommandSuperVotes() {
        super(null, false, "supervotes");
    }

    @Override
    protected ReturnType runCommand(SuperVotes instance, CommandSender sender, String... args) {
        sender.sendMessage(formatAll(" "));
        sendCenteredMessage(sender, formatAll("&8&m--+----------------------------------------+--&r"));
        sendCenteredMessage(sender, formatAll("&3&lSuperVotes &f&lv" + instance.getDescription().getVersion()));
        sendCenteredMessage(sender, formatAll("&8&l» &fPlugin author: &bStefan923"));
        sendCenteredMessage(sender, formatAll(" "));
        sendCenteredMessage(sender, formatAll("&8&l» &fProvides an powerful voting system."));
        sendCenteredMessage(sender, formatAll("&8&m--+----------------------------------------+--&r"));
        sender.sendMessage(formatAll(" "));

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(SuperVotes instance, CommandSender sender, String... args) {
        if (sender.hasPermission("supervotes.admin"))
            return Collections.singletonList("reload");
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/supervotes";
    }

    @Override
    public String getDescription() {
        return "Displays plugin info";
    }

}
