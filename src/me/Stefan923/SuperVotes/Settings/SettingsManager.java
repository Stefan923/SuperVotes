package me.Stefan923.SuperVotes.Settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SettingsManager {

    private static SettingsManager instance = new SettingsManager();
    private FileConfiguration config;
    private File cfile;

    public static SettingsManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        cfile = new File(p.getDataFolder(), "settings.yml");
        config = YamlConfiguration.loadConfiguration(cfile);
        config.options().header("SuperVotes by Stefan923\n");
        config.addDefault("Languages.Default Language", "lang_en.yml");
        config.addDefault("Languages.Available Languages", Arrays.asList("lang_en.yml"));
        config.addDefault("Enabled Commands.Vote", true);
        config.addDefault("Storage.MySQL.Enable", false);
        config.addDefault("Storage.MySQL.IP Adress", "127.0.0.1");
        config.addDefault("Storage.MySQL.Port", 3306);
        config.addDefault("Storage.MySQL.Database Name", "yourDatabase");
        config.addDefault("Storage.MySQL.User", "yourUser");
        config.addDefault("Storage.MySQL.Password", "yourPassword");
        config.addDefault("Vote.Reward Commands", Arrays.asList("give %playername% diamond 8", "give %playername% grass 32"));
        config.addDefault("Vote.Count Offline Votes", true);
        config.addDefault("Vote.Offline Rewards", true);
        config.addDefault("Vote Party.Enabled", true);
        config.addDefault("Vote Party.Count Offline Votes", true);
        config.addDefault("Vote Party.Required Votes", 50);
        config.addDefault("Vote Party.Reward Commands", Arrays.asList("give %playername% diamond 16", "give %playername% grass 64"));
        config.addDefault("Update Checker.Enable.On Plugin Enable", true);
        config.addDefault("Update Checker.Enable.On Join", true);
        config.options().copyDefaults(true);
        save();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void resetConfig() {
        config.set("Languages.Default Language", "lang_en.yml");
        config.set("Languages.Available Languages", Arrays.asList("lang_en.yml"));
        config.set("Enabled Commands.Vote", true);
        config.set("Storage.MySQL.Enable", false);
        config.set("Storage.MySQL.IP Adress", "127.0.0.1");
        config.set("Storage.MySQL.Port", 3306);
        config.set("Storage.MySQL.Database Name", "yourDatabase");
        config.set("Storage.MySQL.User", "yourUser");
        config.set("Storage.MySQL.Password", "yourPassword");
        config.set("Vote.Reward Commands", Arrays.asList("give %playername% diamond 8", "give %playername% grass 32"));
        config.set("Vote.Offline Rewards", true);
        config.set("Vote Party.Enabled", true);
        config.set("Vote Party.Required Votes", 50);
        config.set("Vote Party.Reward Commands", Arrays.asList("give %playername% diamond 16", "give %playername% grass 64"));
        config.set("Update Checker.Enable.On Plugin Enable", true);
        config.set("Update Checker.Enable.On Join", true);
        save();
    }

    private void save() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'settings.yml' couldn't be saved!");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }

}
