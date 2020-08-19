package me.stefan923.supervotes.hooks;

import me.stefan923.supervotes.SuperVotes;
import me.stefan923.supervotes.utils.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private SuperVotes instance;

    public PlaceholderAPIHook(SuperVotes instance) {
        this.instance = instance;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "supervotes";
    }

    @Override
    public String getAuthor() {
        return "Stefan923";
    }

    @Override
    public String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(final Player player, final String identifier) {
        User user = instance.getUser(player);
        if (identifier.equalsIgnoreCase("votes")) {
            return String.valueOf(user.getVotes());
        }
        if (identifier.equalsIgnoreCase("voteparty_votes")) {
            return String.valueOf(instance.getVotes());
        }
        if (identifier.equalsIgnoreCase("voteparty_required_votes")) {
            return String.valueOf(instance.getSettingsManager().getConfig().getInt("Vote Party.Required Votes"));
        }
        return null;
    }
}
