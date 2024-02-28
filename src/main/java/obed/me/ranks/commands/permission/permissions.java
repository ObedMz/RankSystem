package obed.me.ranks.commands.permission;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import obed.me.ranks.utils.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class permissions extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            return;
        }
        //rangos permission <add> <rango> <global/server> <node>
        //rangos permission list <rango>
        //rangos permission remove <---->
        if(args.length <=1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        if(args[0].equalsIgnoreCase("list")){
            Rank rn = SystemManager.getRankByName(args[1]);
            if(rn == null){
                sender.sendMessage(ChatColor.RED + "El rango no existe.");
                return;
            }
            sender.sendMessage(ChatColor.GRAY + "       ");
            sender.sendMessage(ChatColor.GRAY + "-----------------");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPermisos globales: "));
            for(String str : rn.getGlobal_permissions().keySet()){
                sender.sendMessage(str);
            }
            sender.sendMessage(ChatColor.GRAY + "       ");

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPermisos del servidor: "));
            for(String str : rn.getServer_permissions().keySet()){
                sender.sendMessage(str);
            }

            sender.sendMessage(ChatColor.GRAY + "-----------------");
            sender.sendMessage(ChatColor.GRAY + "       ");

        }
        if(args.length <= 3){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String path = args[0].toLowerCase(Locale.ROOT);
        if(!(path.equalsIgnoreCase("add") || path.equalsIgnoreCase("remove"))){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String type = args[2];
        if(!(type.equalsIgnoreCase("global") || type.equalsIgnoreCase("server"))){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        Rank rank = SystemManager.getRankByName(args[1]);
        if(rank == null){
            sender.sendMessage(ChatColor.RED + "Ese rango no existe.");
            return;
        }
        try{
            Database.TaskAsync(()->{
                SystemManager.getUsers().values().forEach(user ->{
                    if(user.getRank() == rank){
                        rank.getGlobal_permissions().keySet().forEach(user::removePermission);
                        rank.getServer_permissions().keySet().forEach(user::removePermission);
                    }
                });
                String node = args[3];
                switch (path){
                    case "add":
                        AddPermission(rank,type,node);
                        break;
                    case "remove":

                        RemovePermission(rank,type,node);
                        break;
                }

                SystemManager.getUsers().values().forEach(user ->{
                    if(user.getRank() == rank)
                        user.loadAllPermissions();
                });
                plugin.getDatabase().savePerms(rank.getName());
                //save into the config.yml file
                rank.getFileConfiguration().set("permissions", rank.getServer_permissions().keySet());
                plugin.getConfigManager().saveRankConfig(rank);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCambios aplicados correctamente."));
            });

        }catch (Exception E){
            E.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Error al tratar de actualizar permisos, revisar la consola.");
        }
    }

    private void AddPermission(Rank rank, String type, String node){
        switch (type.toLowerCase(Locale.ROOT)){
            case "global":
                //for global
                rank.getGlobal_permissions().put(node.replaceAll("\\*",""), !node.startsWith("*"));
                break;
            case "server":
                //for server
                rank.getServer_permissions().put(node.replaceAll("\\*",""), !node.startsWith("*"));
                break;
        }
    }
    private void RemovePermission(Rank rank, String type, String node){
        switch (type.toLowerCase(Locale.ROOT)){
            case "global":
                //for global
                rank.getGlobal_permissions().remove(node);
                break;
            case "server":
                //for server
                rank.getServer_permissions().remove(node);
                break;
        }
    }



    @Override
    public String name() {
        return "permission";
    }

    @Override
    public String info() {
        return "permission <add/remove> <rank> <global/server> <permission>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
