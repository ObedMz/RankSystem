package obed.me.ranks.commands.user;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.User;
import obed.me.ranks.utils.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class suffix extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("ranks.suffix")){
            plugin.getMessageComponent().sendMessageToSender(sender,
                    plugin.getMessageComponent().getMessage("message.no-permissions"),
                    null);
            return;
        }
        //rango suffix <user> <suffix>
        if(args.length <2){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String name = args[0];
        String suffix = args[1];

        Player p = Bukkit.getPlayer(name);
        Database.TaskAsync(()->{
            if(p == null){
                if(!plugin.getDatabase().UserIsCreated(name)){
                    sender.sendMessage(ChatColor.RED + "el usuario no existe.");
                    return;
                }
                plugin.getDatabase().UserloadData(name);
            }
            User user = SystemManager.getUser(name);
            user.setSuffix(suffix);
            plugin.getDatabase().UsersaveData(user.getName());
            if(p != null){
                plugin.getMessageComponent().sendMessageFromArray(sender,
                        plugin.getMessageComponent().getMessageArray("message.rank-user.admin-msg.suffix"),
                        p);
            } else{
                sender.sendMessage(ChatColor.GREEN + "Se actualizaron los datos del jugador.");
            }
        });

    }

    @Override
    public String name() {
        return "suffix";
    }

    @Override
    public String info() {
        return "/rango suffix <user> <suffix>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
