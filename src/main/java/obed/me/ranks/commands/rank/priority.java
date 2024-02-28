package obed.me.ranks.commands.rank;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class priority extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return;
        }

        if(!plugin.getConfigManager().getConfig().getBoolean("backend")){
            sender.sendMessage(ChatColor.RED + "Esta consola no está habilitada para usar este comando.");
            return;
        }
        //rangos priority <rank> <number>
        if(args.length <=1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String name = args[0];
        String number = args[1];
        if(!IsNumber(number)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        Rank rank = SystemManager.getRankByName(name);
        if(rank == null){
            sender.sendMessage(ChatColor.RED + "Ese rango no existe.");
            return;
        }
        rank.setPriority(Integer.parseInt(number));
        plugin.getDatabase().RankUpdatePriority(name, Integer.parseInt(number));

        plugin.getMessageComponent().sendMessageFromArray(sender,
                plugin.getMessageComponent().getMessageArray("message.rank-user.admin-msg.priority"),
                null);
    }

    private boolean IsNumber(String a){
        try {
            int b = Integer.parseInt(a);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    @Override
    public String name() {
        return "priority";
    }

    @Override
    public String info() {
        return "/rangos priority <rank> <number>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
