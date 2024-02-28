package obed.me.ranks.commands.user;

import net.md_5.bungee.api.chat.HoverEvent;
import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.User;
import obed.me.ranks.utils.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Date;

public class info extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("ranks.info")){
            plugin.getMessageComponent().sendMessageToSender(sender,
                    plugin.getMessageComponent().getMessage("message.no-permissions"),
                    null);
            return;
        }
        if(args.length <=0){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));

            return;
        }
        Player p = Bukkit.getPlayer(args[0]);
        Database.TaskAsync(()->{
            if(p == null){
                if(!plugin.getDatabase().UserIsCreated(args[0])){
                    sender.sendMessage(ChatColor.RED + "el usuario no existe.");
                    return;
                }
                plugin.getDatabase().UserloadData(args[0]);
            }
            User user = SystemManager.getUser(args[0]);
            /*
                Show the info of the player as a message.
             */
            if(p != null){
                //send with the user
                plugin.getMessageComponent().sendMessageFromArray(sender,
                        plugin.getMessageComponent().getMessageArray("message.rank-user.admin-msg.info"), p);
                return;
            }
            //send without user
            sender.sendMessage(ChatColor.GRAY + "       ");
            sender.sendMessage(ChatColor.GRAY + "-----------------");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Nombre: " + user.getName()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Rango: " + user.getRank().getName()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Prefijo: " + user.getPrefix()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Sufijo: " + user.getSuffix()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Duracion: " + new Date(user.getTime() * 1000).toString()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Color: " + user.getColor().toString()));

            sender.sendMessage(ChatColor.GRAY + "-----------------");
            sender.sendMessage(ChatColor.GRAY + "       ");


        });
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public String info() {
        return "/rangos info <user>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
