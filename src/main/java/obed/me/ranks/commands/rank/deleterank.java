package obed.me.ranks.commands.rank;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import obed.me.ranks.objects.User;
import obed.me.ranks.utils.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class deleterank extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            return;
        }

        if(!plugin.getConfigManager().getConfig().getBoolean("backend")){
            sender.sendMessage(ChatColor.RED + "Esta consola no está habilitada para usar este comando.");
            return;
        }
        //rangos removerank <rango>
        if(args.length <=0){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String name = args[0];
        Rank rank = SystemManager.getRankByName(name);
        if(rank == null){
            sender.sendMessage(ChatColor.RED + "Ese rango no existe.");
            return;
        }
        try {
            Database.TaskAsync(()->{
                for(User user : SystemManager.getUsers().values()){
                    if(user.getRank() == rank){
                        rank.getGlobal_permissions().keySet().forEach(user::removePermission);
                        rank.getServer_permissions().keySet().forEach(user::removePermission);
                        user.setRank(SystemManager.getDefaultRank());
                        user.loadAllPermissions();
                        plugin.getDatabase().UsersaveData(user.getName());
                    }
                }
                plugin.getDatabase().DeleteRank(name);
                SystemManager.getRanks().remove(name);
            });
            sender.sendMessage(ChatColor.GREEN + "Rango eliminado correctamente.");
        }catch (Exception e){
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Error al eliminar el rango, revisar consola.");
        }

    }

    @Override
    public String name() {
        return "deleterank";
    }

    @Override
    public String info() {
        return "/rangos deleterank <rango>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
