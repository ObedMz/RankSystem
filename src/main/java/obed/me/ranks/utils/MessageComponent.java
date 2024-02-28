package obed.me.ranks.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import obed.me.ranks.Ranks;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageComponent {

    public String getMessage(String path){
        return Ranks.getInstance().getConfigManager().getMessage().getString("message.prefix") + Ranks.getInstance().getConfigManager().getMessage().getString(path);
    }
    public List<String> getMessageArray(String path){
        List<String> list = new ArrayList<>();
        for(String str :Ranks.getInstance().getConfigManager().getMessage().getStringList(path)){
            list.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        return list;
    }

    public void sendHoverMessageURL(Player p, String msg, String hover, HoverEvent.Action action, String url){
        TextComponent hmessage = new TextComponent(PlaceholderAPI.setPlaceholders(p, msg));
        hmessage.setHoverEvent( new HoverEvent( action, new ComponentBuilder(PlaceholderAPI.setPlaceholders(p, hover)).create() ) );
        p.spigot().sendMessage(hmessage);
    }

    public void sendHoverMessage(Player p, String msg, String hover){
        TextComponent hmessage = new TextComponent(PlaceholderAPI.setPlaceholders(p, msg));
        hmessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(PlaceholderAPI.setPlaceholders(p, hover)).create() ) );
        p.spigot().sendMessage(hmessage);
    }
    public void sendMessage(Player p, String msg){
        String placeholder = PlaceholderAPI.setPlaceholders(p, msg);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', placeholder));
    }
    public void sendMessageToSender(CommandSender sender, String msg, Player p){
        String placeholder = msg;
        if(p != null){
             placeholder = PlaceholderAPI.setPlaceholders(p, msg);
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', placeholder));
    }


    public void sendMessageFromArray(Player p, List<String> array){
        array.forEach(str -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, str))));
    }

    public void sendMessageFromArray(CommandSender p, List<String> array, Player player){
        if(player != null){
            array.forEach(str ->
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, str)
                    )));
            return;
        }
        array.forEach(str -> p.sendMessage(ChatColor.translateAlternateColorCodes('&',  str)));
    }
}
