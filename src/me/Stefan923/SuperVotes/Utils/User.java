package me.Stefan923.SuperVotes.Utils;

import me.Stefan923.SuperVotes.Database.Database;
import me.Stefan923.SuperVotes.SuperVotes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private SuperVotes instance;

    private Player player;
    private Integer votes;

    public User(SuperVotes instance, Player player) {
        this.instance = instance;
        this.player = player;

        FileConfiguration settings = instance.getSettingsManager().getConfig();
        Database database = instance.getDatabase("supervotes_stats");

        this.votes = 0;

        if (database.has(player.getName())) {
            ResultSet resultSet = database.get(player.getName());
            try {
                this.votes = resultSet.getInt("votes");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            database.put(player.getName(), "votes", this.votes);
        }
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
        instance.getDatabase("supervotes_stats").put(player.getName(), "votes", this.votes);
    }

    public void addVotes() {
        this.votes++;
        instance.getDatabase("supervotes_stats").put(player.getName(), "votes", this.votes);
    }
}
