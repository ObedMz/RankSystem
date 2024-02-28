package obed.me.ranks.commands.rank;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class list extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            return;
        }
        //just print the SystemManager.getRanks() order by priority;

        List<Rank> ranks = new ArrayList<>(SystemManager.getRanks().values());

        ranks.sort(Comparator.comparing(Rank::getPriority));
        Collections.reverse(ranks);

        ranks.forEach(rank -> {
            sender.sendMessage(ChatColor.GREEN + ""+rank.getPriority() + ": " +ChatColor.DARK_AQUA+ rank.getName());
        });



    }

    @Override
    public String name() {
        return "ranklist";
    }

    @Override
    public String info() {
        return "/rangos ranklist";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
