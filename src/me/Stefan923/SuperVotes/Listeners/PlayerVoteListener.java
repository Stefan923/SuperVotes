package me.Stefan923.SuperVotes.Listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.Stefan923.SuperVotes.SuperVotes;
import me.Stefan923.SuperVotes.Utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class PlayerVoteListener implements Listener, MessageUtils {

    private SuperVotes instance;

    private File voteFile;
    private FileConfiguration vote;

    public PlayerVoteListener(SuperVotes instance) {
        this.instance = instance;
        this.voteFile = new File(instance.getDataFolder(), "vote.yml");
        this.vote = YamlConfiguration.loadConfiguration(this.voteFile);

        if (!this.vote.contains("Votes")) {
            this.vote.set("Votes", 0);
            saveConfig(this.voteFile, this.vote);
        }
        int votes = this.vote.getInt("Votes");
        instance.setVotes(votes);
        if (votes >= instance.getSettingsManager().getConfig().getInt("Vote Party.Required Votes")) {
            this.vote.set("Votes", 0);
            saveConfig(this.voteFile, this.vote);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVotifierEvent(final VotifierEvent event) {
        Vote vote = event.getVote();
        FileConfiguration settings = instance.getSettingsManager().getConfig();
        if (vote.getUsername() == null || vote.getUsername().length() <= 0) {
            sendLogger("&fVot primit pe &e" + vote.getServiceName() + "&f fara a fi specificat un nume...");
        }
        sendLogger("&fVot primit pe &e" + vote.getServiceName() + "&f de la &a" + vote.getUsername() + "&f.");
        if (!vote.getUsername().trim().matches("[A-Za-z0-9-_]+") || vote.getUsername().length() > 16) {
            sendLogger("&cO persoana a votat si a introdus un nume invalid: &4" + vote.getUsername());
            return;
        }

        if (settings.getStringList("Vote.Reward Commands").isEmpty()) {
            return;
        }
        Player player = Bukkit.getPlayer(vote.getUsername());
        if (player != null) {
            instance.getUser(player).addVotes();
            processVote(vote);
        } else {
            if (settings.getBoolean("Vote.Offline Rewards")) {
                if (this.vote.isSet("Vote." + vote.getUsername())) {
                    this.vote.set("Vote." + vote.getUsername(), this.vote.getInt("Vote." + vote.getUsername()) + 1);
                    saveConfig(this.voteFile, this.vote);
                    return;
                }

                this.vote.set("Vote." + vote.getUsername(), 1);
                saveConfig(this.voteFile, this.vote);
            }
        }
    }

    public void processVote(Vote vote) {
        Player player = Bukkit.getPlayer(vote.getUsername());
        FileConfiguration settings = instance.getSettingsManager().getConfig();

        int votes = instance.getVotes() + 1;
        instance.setVotes(votes);
        this.vote.set("Votes", votes);
        saveConfig(this.voteFile, this.vote);

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(formatAll(prepareMessageByLang(onlinePlayer, "Vote Event.Player Voted")
                .replace("%playername%", player.getName())
                .replace("%votes%", String.valueOf(votes))
                .replace("%required_votes%", String.valueOf(settings.getInt("Vote Party.Required Votes"))))));
        giveReward(player);

        if (settings.getBoolean("Vote Party.Enabled")) {
            if (votes >= settings.getInt("Vote Party.Required Votes")) {
                new BukkitRunnable() {
                    int timer = 30;

                    @Override
                    public void run() {
                        if (timer == 30 || timer == 15 || timer == 5) {
                            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(formatAll(prepareMessageByLang(onlinePlayer, "Vote Party.Starting In").replace("%timer%", String.valueOf(timer)))));
                        } else if (timer <= 0) {
                            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(formatAll(prepareMessageByLang(onlinePlayer, "Vote Party.Started"))));
                            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                                for (String command : instance.getSettingsManager().getConfig().getStringList("Vote Party.Reward Commands")) {
                                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%playername%", onlinePlayer.getName()));
                                        }
                             });
                            cancel();
                            return;
                        }
                        timer--;
                    }
                }.runTaskTimerAsynchronously(instance, 0L, 20L);
                instance.setVotes(0);
                saveConfig(this.voteFile, this.vote);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.vote.contains("Vote." + player.getName())) {
            int vote = this.vote.getInt("Vote." + player.getName());
            for (int i = 1; i <= vote; ++i) {
                giveReward(player);
            }

            this.vote.set("Vote." + player.getName(), null);
            saveConfig(this.voteFile, this.vote);
        }
    }

    public void giveReward(final Player player) {
        FileConfiguration settings = instance.getSettingsManager().getConfig();
        for (String command : settings.getStringList("Vote.Reward Commands")) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%playername%", player.getName()));
        }
    }

    void saveConfig(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}