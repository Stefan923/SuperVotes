package me.Stefan923.SuperVotes.Commands.Type;

import me.Stefan923.SuperVotes.Commands.AbstractCommand;
import me.Stefan923.SuperVotes.SuperVotes;
import me.Stefan923.SuperVotes.Utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandVote extends AbstractCommand implements MessageUtils {

    public CommandVote() {
        super(true, false, "vote");
    }

    @Override
    protected ReturnType runCommand(SuperVotes instance, CommandSender sender, String... args) {
        if (!(sender instanceof Player))
            return ReturnType.FAILURE;

        Player senderPlayer = (Player) sender;
        senderPlayer.sendMessage(prepareMessageListByLang(senderPlayer, "Command.Vote").replace("%votes%", String.valueOf(instance.getUser(senderPlayer).getVotes())));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(SuperVotes instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "supervotes.vote";
    }

    @Override
    public String getSyntax() {
        return "/vote";
    }

    @Override
    public String getDescription() {
        return "Shows a list of available vote links.";
    }

}
