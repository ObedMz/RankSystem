package obed.me.ranks.commands.user;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import obed.me.ranks.objects.User;
import obed.me.ranks.utils.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class removerank extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        //check if this server is available to execute this command
        if(sender instanceof Player){
            //No se puede ejecutar esto como usuario.
            return;
        }

        if(!plugin.getConfigManager().getConfig().getBoolean("backend")){
            sender.sendMessage(ChatColor.RED + "Esta consola no está habilitada para usar este comando.");
            return;
        }

        //rangos removerank <user>
        if(args.length <=0){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String name = args[0];
        Player p = Bukkit.getPlayer(name);
        Database.TaskAsync(()->{
            if(p == null){
                if(!plugin.getDatabase().UserIsCreated(name)){
                    sender.sendMessage(ChatColor.RED + "El usuario no existe.");
                    return;
                }
                plugin.getDatabase().UserloadData(name);
            }
            User user = SystemManager.getUser(name);
            Rank old_rank = user.getRank();
            Rank def = SystemManager.getDefaultRank();
            user.setRank(def);
            user.setTime(0L);
            plugin.getDatabase().UsersaveData(user.getName());
            if(p != null){
                old_rank.getGlobal_permissions().keySet().forEach(user::removePermission);
                old_rank.getServer_permissions().keySet().forEach(user::removePermission);
                user.loadAllPermissions();
            }

        /*
          message done with hover ptm
         */

        });

    }

    @Override
    public String name() {
        return "removerank";
    }

    @Override
    public String info() {
        return "/rangos removerank <user>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
