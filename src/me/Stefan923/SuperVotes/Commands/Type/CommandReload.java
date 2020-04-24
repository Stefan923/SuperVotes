package me.Stefan923.SuperVotes.Commands.Type;

import me.Stefan923.SuperVotes.Commands.AbstractCommand;
import me.Stefan923.SuperVotes.SuperVotes;
import me.Stefan923.SuperVotes.Utils.MessageUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandReload extends AbstractCommand implements MessageUtils {

    public CommandReload(AbstractCommand abstractCommand) {
        super(abstractCommand, false, "reload");
    }

    @Override
    protected ReturnType runCommand(SuperVotes instance, CommandSender sender, String... args) {
        if (args.length != 2)
            return ReturnType.SYNTAX_ERROR;

        if (args[1].equalsIgnoreCase("all")) {
            instance.reloadSettingManager();
            instance.reloadLanguageManagers();
            sender.sendMessage(formatAll("&8[&3SuperVotes&8] &fYou have successfully reloaded &ball &fmodules!"));
            return ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("settings")) {
            instance.reloadSettingManager();
            sender.sendMessage(formatAll("&8[&3SuperVotes&8] &fYou have successfully reloaded &bsettings &fmodule!"));
            return ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("languages")) {
            instance.reloadLanguageManagers();
            sender.sendMessage(formatAll("&8[&3SuperVotes&8] &fYou have successfully reloaded &blanguages &fmodule!"));
            return ReturnType.SUCCESS;
        }

        return ReturnType.SYNTAX_ERROR;
    }

    @Override
    protected List<String> onTab(SuperVotes instance, CommandSender sender, String... args) {
        if (sender.hasPermission("SuperVotes.admin"))
            return Arrays.asList("settings", "languages", "all");
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "supervotes.admin";
    }

    @Override
    public String getSyntax() {
        return "/supervotes reload <all|settings|languages>";
    }

    @Override
    public String getDescription() {
        return "Reloads plugin settings.";
    }

}
