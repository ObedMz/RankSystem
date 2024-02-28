package obed.me.ranks.commands.rank;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class inherit extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return;
        }

        if(!plugin.getConfigManager().getConfig().getBoolean("backend")){
            sender.sendMessage(ChatColor.RED + "Esta consola no está habilitada para usar este comando.");
            return;
        }
        //rangos priority <rank> <inherit> <inherit> <inherit> <inherit>
        if(args.length <=1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        Rank rank = SystemManager.getRankByName(args[0]);
        if(rank == null){
            sender.sendMessage(ChatColor.RED + "Ese rango no existe.");
            return;
        }
        List<String> list = new ArrayList<>(Arrays.asList(args));
        List<Rank> list_rank = new ArrayList<>();
        list.remove(args[0]);
        for(String str : list){
            Rank rnk = SystemManager.getRankByName(str);
            if(rnk != null && rnk != rank)
                list_rank.add(rnk);

        }
        rank.setInherit(list_rank);
        plugin.getDatabase().RankUpdateInherit(rank.getName(), list_rank);
        // done
        sender.sendMessage(ChatColor.GREEN + "Herencia agregada correctamente.");
    }

    @Override
    public String name() {
        return "inherit";
    }

    @Override
    public String info() {
        return "/rangos inherit <rank> <inherit> <inherit> <inherit> <inherit>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
