package me.stefan923.supervotes.listeners;

import me.stefan923.supervotes.SuperVotes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private SuperVotes instance;

    public PlayerJoinListener(SuperVotes instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player != null)
            instance.getUser(player);
    }

}
