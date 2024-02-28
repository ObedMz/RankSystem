package obed.me.ranks.commands.rank;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class prefix extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return;
        }
        //rangos priority <rank> <prefix>
        if(args.length <=1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String name = args[0];
        String prefix = args[1];
        Rank rank = SystemManager.getRankByName(name);
        if(rank == null){
            sender.sendMessage(ChatColor.RED + "Ese rango no existe.");
            return;
        }
        rank.setPrefix(prefix);
        plugin.getDatabase().RankUpdatePrefix(name, prefix);
        sender.sendMessage(ChatColor.GREEN + "Prefijo actualizado correctamente.");
    }

    @Override
    public String name() {
        return "rankprefix";
    }

    @Override
    public String info() {
        return "/rangos rankprefix <rank> <prefix>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
