package obed.me.ranks.commands.rank;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class info extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            return;
        }
        //rangos rankinfo <rank>
        if(args.length <= 0){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        Rank rank = SystemManager.getRankByName(args[0]);
        if(rank == null){
            sender.sendMessage(ChatColor.RED + "Ese rango no existe.");
            return;
        }
        //print rank information on txt.
        sender.sendMessage(ChatColor.GRAY + "       ");
        sender.sendMessage(ChatColor.GRAY + "-----------------");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&' , "&3Rango: &a" + rank.getName()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&' , "&3Prioridad:  &a" + rank.getPriority()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&' , "&3Prefix: " + rank.getPrefix()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&' , "&3Herencia:  &a" + rank.getInherit().toString()));
        sender.sendMessage(ChatColor.GRAY + "-----------------");
        sender.sendMessage(ChatColor.GRAY + "       ");
    }

    @Override
    public String name() {
        return "rankinfo";
    }

    @Override
    public String info() {
        return "/rangos rankinfo <rank>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
