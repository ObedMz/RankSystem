package obed.me.ranks.commands;

import obed.me.ranks.Ranks;
import obed.me.ranks.managers.ConfigManager;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    protected ConfigManager config = new ConfigManager();
    protected Ranks plugin = Ranks.getInstance();

    public SubCommand(){

    }

    public abstract void execute(CommandSender sender, String[] args);
    public abstract String name();
    public abstract String info();
    public abstract String[] alias();
}
