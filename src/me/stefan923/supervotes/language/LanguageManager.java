package me.stefan923.supervotes.language;

import me.stefan923.supervotes.SuperVotes;
import me.stefan923.supervotes.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class LanguageManager implements MessageUtils {

    private static LanguageManager instance = new LanguageManager();
    private FileConfiguration config;
    private File cfile;
    private String languageFile;

    public static LanguageManager getInstance() {
        return instance;
    }

    public void setup(SuperVotes p, String languageFile) {
        this.languageFile = languageFile;

        cfile = new File(p.getDataFolder(), "languages/" + languageFile);
        if (!cfile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                File dir = new File(p.getDataFolder() + "/languages");

                if (!dir.exists())
                    dir.mkdir();

                cfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration settings = p.getSettingsManager().getConfig();

        config = YamlConfiguration.loadConfiguration(cfile);
        config.options().header("UltimateDonors by Stefan923.\n");
        config.addDefault("Command.Vote", Arrays.asList("&f&m---------&r&b[ &3&m-------------------- &r&b]&f&m---------&r",
                        "",
                        "&3- &fYou have voted us &b%votes% &ftimes.",
                        "&3- &fYou can vote by clicking on the following links:",
                        "&3- &b&nhttps://www.link-1.com",
                        "&3- &b&nhttps://www.link-2.com",
                        "",
                        "&f&m---------&r&b[ &3&m-------------------- &r&b]&f&m---------&r"));
        config.addDefault("General.Invalid Command Syntax", "&8(&3!&8) &cInvalid Syntax or you have no permission!\n&8(&3!&8) &fThe valid syntax is: &b%syntax%");
        config.addDefault("General.Must Be Player", "&8(&3!&8) &cYou must be a player to do this!");
        config.addDefault("General.No Permission", "&8(&3!&8) &cYou need the &4%permission% &cpermission to do that!");
        config.addDefault("Vote Event.Player Voted", "&8(&3!&8) &b%playername% &fvoted the server on &b%website%&f! &8(&b%votes%&8/&3%required_votes%&8)");
        config.addDefault("Vote Party.Started", "&8(&3!&8) &bThe vote party has been started!");
        config.addDefault("Vote Party.Starting In", "&8(&3!&8) &fThe vote party will start in &b%timer% seconds&f.");
        config.addDefault("Update Checker.Available", "&8(&3!&8) &fThere is a new version of &bSuperVotes &favailable!\n&8(&3!&8) &fDownload link: &b%link%");
        config.addDefault("Update Checker.Not Available", "&8(&3!&8) &fThere's no update available for &bSuperVotes&f.");
        config.options().copyDefaults(true);

        save();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reset(SuperVotes p) {
        config.set("Vote Event.Player Voted", "&8(&3!&8) &b%playername% &fvoted the server! &8(&b%votes%&8/&3%required_votes%&8)");
        config.set("Vote Party.Started", "&8(&3!&8) &bThe vote party has been started!");
        config.set("Vote Party.Starting In", "&8(&3!&8) &fThe vote party will be started in &b%timer%&f.");
        config.set("Update Checker.Available", "&8(&3!&8) &fThere is a new version of &bSuperVotes &favailable!\n&8(&3!&8) &fDownload link: &b%link%");
        config.set("Update Checker.Not Available", "&8(&3!&8) &fThere's no update available for &bSuperVotes&f.");

        save();
    }

    public void save() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            sendLogger(ChatColor.RED + "File '" + languageFile + "' couldn't be saved!");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public String getLanguageFileName() {
        return languageFile;
    }

}
