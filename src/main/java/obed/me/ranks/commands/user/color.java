package obed.me.ranks.commands.user;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.User;
import obed.me.ranks.utils.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class color extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("ranks.color")){
            plugin.getMessageComponent().sendMessageToSender(sender,
                    plugin.getMessageComponent().getMessage("message.no-permissions"),
                    null);
            return;
        }
        //
        String name = args[0];
        String color = args[1];
        if(name == null || color == null){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));

            return;
        }
        Player p = Bukkit.getPlayer(name);
        ChatColor chatcolor = ChatColor.valueOf(color);
        if(!chatcolor.isColor()){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));

            return;
        }
        Database.TaskAsync(()->{
            if(p == null){
                if(!plugin.getDatabase().UserIsCreated(args[0])){
                    sender.sendMessage(ChatColor.RED + "el usuario no existe.");
                    return;
                }
                plugin.getDatabase().UserloadData(args[0]);
            }
            User user = SystemManager.getUser(args[0]);
            user.setColor(chatcolor);
            plugin.getDatabase().UsersaveData(user.getName());
            if(p != null){
                plugin.getMessageComponent().sendMessageFromArray(sender,
                        plugin.getMessageComponent().getMessageArray("message.rank-user.admin-msg.color"),
                        p);
            } else{
                sender.sendMessage(ChatColor.GREEN + "Se actualizaron los datos del jugador.");
            }
        });

    }

    @Override
    public String name() {
        return "color";
    }

    @Override
    public String info() {
        return "/rangos color <user> <color>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
