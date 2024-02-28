package obed.me.ranks.commands;

import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import obed.me.ranks.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class reload extends SubCommand{

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            //message of not allowed
            return;
        }

        SystemManager.getRanks().clear();

        plugin.getDatabase().loadDatabaseRanks();
        SystemManager.getRanks().values().forEach(rank -> {
            plugin.getConfigManager().createYML(rank);
            rank.getFileConfiguration().getStringList("permissions").forEach(str -> {
                rank.getServer_permissions().put(str.replaceAll("\\*",""), !str.startsWith("*"));
            });

        });
        SystemManager.getRanks().values().forEach(rank -> {
            /* Loading inherit permissions */
            for(Rank inherit : rank.getInherit()){
                rank.getGlobal_permissions().putAll(inherit.getGlobal_permissions());
                rank.getServer_permissions().putAll(inherit.getServer_permissions());
            }
        });

        Bukkit.getOnlinePlayers().forEach(p->{
            User user = SystemManager.getUser(p.getName());
            user.getRank().getGlobal_permissions().keySet().forEach(user::removePermission);
            user.getRank().getServer_permissions().keySet().forEach(user::removePermission);
        });
        SystemManager.getUsers().clear();
        Bukkit.getOnlinePlayers().forEach(p->{
            plugin.getDatabase().UserCreateData(p.getName());
            plugin.getDatabase().UserloadData(p.getName());
            User user = SystemManager.getUser(p.getName());
            user.loadAllPermissions();
        });

        plugin.getConfigManager().reloadConfig();
        plugin.getConfigManager().reloadMessage();
        sender.sendMessage(ChatColor.GREEN + "Plugin recargado correctamente.");
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String info() {
        return "/rangos reload";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }

}
