package obed.me.ranks.listeners;

import obed.me.ranks.Ranks;
import org.bukkit.event.Listener;

public class BaseListener implements Listener {
    protected final Ranks plugin;

    public BaseListener(Ranks ranks) {
        this.plugin = ranks;
    }
}
