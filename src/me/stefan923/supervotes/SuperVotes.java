package me.stefan923.supervotes;

import me.stefan923.supervotes.commands.CommandManager;
import me.stefan923.supervotes.database.Database;
import me.stefan923.supervotes.database.H2Database;
import me.stefan923.supervotes.database.MySQLDatabase;
import me.stefan923.supervotes.hooks.PlaceholderAPIHook;
import me.stefan923.supervotes.language.LanguageManager;
import me.stefan923.supervotes.listeners.PlayerJoinListener;
import me.stefan923.supervotes.listeners.PlayerQuitListener;
import me.stefan923.supervotes.listeners.PlayerVoteListener;
import me.stefan923.supervotes.settings.SettingsManager;
import me.stefan923.supervotes.utils.MessageUtils;
import me.stefan923.supervotes.utils.Metrics;
import me.stefan923.supervotes.utils.User;
import me.stefan923.supervotes.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;

public class SuperVotes extends JavaPlugin implements MessageUtils, VersionUtils {

    private static SuperVotes instance;

    private SettingsManager settingsManager;
    private HashMap<String, LanguageManager> languageManagers;
    private CommandManager commandManager;

    private HashMap<String, Database> databases;
    private HashMap<String, User> users;

    private Integer votes;

    @Override
    public void onEnable() {
        instance = this;

        votes = 0;

        settingsManager = SettingsManager.getInstance();
        settingsManager.setup(this);

        languageManagers = new HashMap<>();
        for (String fileName : settingsManager.getConfig().getStringList("Languages.Available Languages")) {
            LanguageManager languageManager = new LanguageManager();
            fileName = fileName.toLowerCase();
            languageManager.setup(this, fileName);
            languageManagers.put(fileName, languageManager);
        }

        databases = new HashMap<>();
        getDatabase("supervotes_stats");
        users = new HashMap<>();

        Metrics pluginMetrics = new Metrics(this, 7210);

        sendLogger("&8&l> &7&m------- &8&l( &3&lSuperVotes &b&lby Stefan923 &8&l) &7&m------- &8&l<");
        sendLogger("&b   Plugin has been initialized.");
        sendLogger("&b   Version: &3v" + getDescription().getVersion());
        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook(instance).register();
            sendLogger("&b   Placeholders: &aEnabled");
        } else {
            sendLogger("&b   Placeholders: &aDisabled");
        }
        sendLogger("&b   Enabled listeners: &3" + enableListeners());
        sendLogger("&b   Enabled commands: &3" + enableCommands());
        sendLogger("&8&l> &7&m------- &8&l( &3&lSuperVotes &b&lby Stefan923 &8&l) &7&m------- &8&l<");

        if (settingsManager.getConfig().getBoolean("Update Checker.Enable.On Plugin Enable"))
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                checkForUpdate(this, this);
            });
    }

    public static SuperVotes getInstance() {
        return instance;
    }

    private Integer enableListeners() {
        Integer i = 3;
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerVoteListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        return i;
    }

    private Integer enableCommands() {
        commandManager = new CommandManager(this);
        return commandManager.getCommands().size();
    }

    public User getUser(Player player) {
        String playerName = player.getName();
        if (!users.containsKey(playerName)) {
            addUser(player);
        }
        return users.get(playerName);
    }

    public void addUser(Player player) {
        users.put(player.getName(), new User(instance, player));
    }

    public void removeUser(Player player) {
        users.remove(player.getName());
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public void reloadSettingManager() {
        settingsManager.reload();
    }

    public LanguageManager getLanguageManager(String language) {
        return languageManagers.get(language);
    }

    public HashMap<String, LanguageManager> getLanguageManagers() {
        return languageManagers;
    }

    public void reloadLanguageManagers() {
        languageManagers.clear();
        for (String fileName : settingsManager.getConfig().getStringList("Languages.Available Languages")) {
            LanguageManager languageManager = new LanguageManager();
            fileName = fileName.toLowerCase();
            languageManager.setup(this, fileName);
            languageManagers.put(fileName, languageManager);
        }
    }

    public Database getDatabase(String table) {
        if (databases.containsKey(table))
            return databases.get(table);
        if (settingsManager.getConfig().getBoolean("Storage.MySQL.Enable"))
            return getMySQLDatabase(table);
        else
            return getFileDatabase(table);
    }

    protected Database getMySQLDatabase(String table) {
        if (databases.containsKey(table))
            return databases.get(table);
        ConfigurationSection section = settingsManager.getConfig().getConfigurationSection("Storage");
        String address = section.getString("MySQL.IP Adress", "localhost");
        Integer port = section.getInt("MySQL.Port");
        String password = section.getString("MySQL.Password");
        String name = section.getString("MySQL.Database Name");
        String user = section.getString("MySQL.User");
        Database database = null;
        try {
            database = new MySQLDatabase(address, port, name, table, user, password);
            sendLogger("&8(&3SuperVotes&8) &rMySQL connection " + address + " was a success!");
            databases.put(table, database);
            return database;
        } catch (SQLException exception) {
            sendLogger("&8(&3SuperVotes&8) &cMySQL connection failed!");
            sendLogger("&8(&3SuperVotes&8) &rAddress: " + address + " with user: " + user);
            sendLogger("&8(&3SuperVotes&8) &rReason: " + exception.getMessage());
        } finally {
            if (database == null) {
                sendLogger("&8(&3SuperVotes&8) &rAttempting to use H2 database instead...");
                database = getFileDatabase(table);
            }
        }
        return database;
    }

    private Database getFileDatabase(String table) {
        if (databases.containsKey(table))
            return databases.get(table);
        Database database = null;
        try {
            database = new H2Database(table);
            sendLogger("&8(&3SuperVotes&8) &rUsing H2 database for &b" + table + " &7data.");
            databases.put(table, database);
        } catch (ClassNotFoundException | SQLException e) {
            sendLogger("&8(&3SuperVotes&8) &cH2 failed...");
            e.printStackTrace();
        }
        return database;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
