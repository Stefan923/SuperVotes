package me.stefan923.supervotes.listeners;

import me.stefan923.supervotes.SuperVotes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private SuperVotes instance;

    public PlayerQuitListener(SuperVotes instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        instance.removeUser(player);
    }

}
