package me.Stefan923.SuperVotes.Utils;

import me.Stefan923.SuperCore.API.SuperCoreAPI;
import me.Stefan923.SuperCore.API.UserAPI;
import me.Stefan923.SuperVotes.SuperVotes;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public interface MessageUtils {

    default String formatAll(String string) {
        return string.replace("&a", "§a")
                .replace("&b", "§b")
                .replace("&c", "§c")
                .replace("&d", "§d")
                .replace("&e", "§e")
                .replace("&f", "§f")
                .replace("&0", "§0")
                .replace("&1", "§1")
                .replace("&2", "§2")
                .replace("&3", "§3")
                .replace("&4", "§4")
                .replace("&5", "§5")
                .replace("&6", "§6")
                .replace("&7", "§7")
                .replace("&8", "§8")
                .replace("&9", "§9")
                .replace("&o", "§o")
                .replace("&l", "§l")
                .replace("&m", "§m")
                .replace("&n", "§n")
                .replace("&k", "§k")
                .replace("&r", "§r");
    }

    default String removeFormat(String string) {
        return string.replace("&a", "")
                .replace("&b", "")
                .replace("&c", "")
                .replace("&d", "")
                .replace("&e", "")
                .replace("&f", "")
                .replace("&0", "")
                .replace("&1", "")
                .replace("&2", "")
                .replace("&3", "")
                .replace("&4", "")
                .replace("&5", "")
                .replace("&6", "")
                .replace("&7", "")
                .replace("&8", "")
                .replace("&9", "")
                .replace("&o", "")
                .replace("&l", "")
                .replace("&m", "")
                .replace("&n", "")
                .replace("&k", "")
                .replace("&r", "");
    }

    default String formatEmojis(FileConfiguration settings, Player player, String string) {
        for (String option : settings.getConfigurationSection("Chat.Emotes").getKeys(false)) {
            if (player.hasPermission("SuperVotes.use.emotes." + option))
                string = string.replace(option, settings.getString("Chat.Emotes." + option));
        }
        return string;
    }

    default void sendLogger(final String string) {
        Bukkit.getConsoleSender().sendMessage(formatAll(string));
    }

    default void sendCenteredMessage(Player player, String message) {
        if (message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontSize dFI = FontSize.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontSize.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    default void sendCenteredMessage(CommandSender player, String message) {
        if (message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontSize dFI = FontSize.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontSize.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    default String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    default String replacePlaceholders(Player player, String string) {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, string) : string;
    }

    default String convertTime(final long time, final FileConfiguration languageManager) {
        StringBuilder stringBuilder = new StringBuilder();

        int years = (int) (time / 1000 / 60 / 60 / 24 / 30 / 12);
        if (years != 0)
            stringBuilder.append(years).append(" ").append(years == 1 ? languageManager.getString("General.Word Year") : languageManager.getString("General.Word Years"));
        int months = (int) ((time / 1000 / 60 / 60 / 24 / 30) % 12);
        if (months != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(months).append(" ").append(months == 1 ? languageManager.getString("General.Word Month") : languageManager.getString("General.Word Months"));
        int days = (int) ((time / 1000 / 60 / 60 / 24) % 30);
        if (days != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(days).append(" ").append(days == 1 ? languageManager.getString("General.Word Day") : languageManager.getString("General.Word Days"));
        int hours = (int) ((time / 1000 / 60 / 60) % 24);
        if (hours != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(hours).append(" ").append(hours == 1 ? languageManager.getString("General.Word Hour") : languageManager.getString("General.Word Hours"));
        int minutes = (int) ((time / 1000 / 60) % 60);
        if (minutes != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(minutes).append(" ").append(minutes == 1 ? languageManager.getString("General.Word Minute") : languageManager.getString("General.Word Minutes"));
        int seconds = (int) ((time / 1000) % 60);
        if (seconds != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(seconds).append(" ").append(seconds == 1 ? languageManager.getString("General.Word Second") : languageManager.getString("General.Word Seconds"));

        return stringBuilder.toString();
    }

    default String prepareMessageByLang(CommandSender sender, String option) {
        SuperVotes instance = SuperVotes.getInstance();
        if (sender instanceof Player)
            return prepareMessageByLang((Player) sender, option);
        return formatAll(instance.getLanguageManager(instance.getSettingsManager().getConfig().getString("Languages.Default Language")).getConfig().getString(option));
    }

    default String prepareMessageByLang(Player player, String option) {
        SuperVotes instance = SuperVotes.getInstance();
        if (Bukkit.getPluginManager().getPlugin("SuperCore") != null) {
            UserAPI userAPI = SuperCoreAPI.getInstance().getUser(player.getName());
            String language = userAPI.getLanguage();
            if (language == null)
                return "";
            if (instance.getLanguageManagers().containsKey(language))
                return prepareMessage(player, instance.getLanguageManager(language).getConfig().getString(option));
        }
        return prepareMessage(player, instance.getLanguageManager(instance.getSettingsManager().getConfig().getString("Languages.Default Language")).getConfig().getString(option));
    }

    default String prepareMessageListByLang(Player player, String option) {
        SuperVotes instance = SuperVotes.getInstance();
        if (Bukkit.getPluginManager().getPlugin("SuperCore") != null) {
            String language = SuperCoreAPI.getInstance().getUser(player.getName()).getLanguage();
            if (language == null)
                return "";
            if (instance.getLanguageManagers().containsKey(language))
                return prepareMessage(player, instance.getLanguageManager(language).getConfig().getStringList(option));
        }
        return prepareMessage(player, instance.getLanguageManager(instance.getSettingsManager().getConfig().getString("Languages.Default Language")).getConfig().getStringList(option));
    }

    default String prepareMessage(Player player, String message) {
        return formatAll(replacePlaceholders(player, message));
    }

    default String prepareMessage(Player player, List<String> messages) {
        return formatAll(replacePlaceholders(player, String.join("\n", messages)));
    }

}