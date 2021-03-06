package me.stefan923.supervotes.utils;

import me.stefan923.supervotes.language.LanguageManager;
import me.stefan923.supervotes.SuperVotes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public interface VersionUtils extends MessageUtils {

    default void checkForUpdate(Plugin plugin, SuperVotes instance) {
        String version = getLatestPluginVersion(plugin);

        LanguageManager languageManager = instance.getLanguageManager(instance.getSettingsManager().getConfig().getString("Languages.Default Language"));
        if (!version.equalsIgnoreCase(getCurrentPluginVersion()))
            sendLogger(formatAll(languageManager.getConfig().getString("Update Checker.Available").replace("%link%", "https://www.spigotmc.org/resources/72224")));
        else
            sendLogger(formatAll(languageManager.getConfig().getString("Update Checker.Not Available")));
    }

    default void checkForUpdate(Plugin plugin, Player player) {
        String version = getLatestPluginVersion(plugin);

        if (!version.equalsIgnoreCase(getCurrentPluginVersion()))
            player.sendMessage(formatAll(prepareMessageByLang(player, "Update Checker.Available").replace("%link%", "https://www.spigotmc.org/resources/72224")));
        else
            player.sendMessage(formatAll(prepareMessageByLang(player, "Update Checker.Not Available")));
    }

    default String getLatestPluginVersion(Plugin plugin) {
        String version = "";
        try {
            InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=77907").openStream();
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext())
                version = scanner.next();
        } catch (IOException exception) {
            plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
        }
        return version;
    }

    default String getCurrentPluginVersion() {
        return Bukkit.getPluginManager().getPlugin("SuperVotes").getDescription().getVersion();
    }

}
