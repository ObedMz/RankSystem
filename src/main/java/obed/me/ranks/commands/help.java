package obed.me.ranks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class help extends SubCommand{
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            return;
        }
        //only console
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "-------------------------------"));
        for(SubCommand sb : plugin.getCommandManager().getCommands().values()){
            sender.sendMessage(ChatColor.GREEN + sb.name()  + "    " + ChatColor.AQUA + " > " + sb.info());
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "-------------------------------"));



    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String info() {
        return "show all commands";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
