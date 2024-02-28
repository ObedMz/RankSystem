package obed.me.ranks.commands.user;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import obed.me.ranks.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class addrank extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            return;
        }
        //check if this server is available to execute this command
        if(!plugin.getConfigManager().getConfig().getBoolean("backend")){
            sender.sendMessage(ChatColor.RED + "Esta consola no está habilitada para usar este comando.");
            return;
        }
        //rangos addrank <user> <rank> <time>
        if (args.length <= 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String name = args[0];
        Rank rank = SystemManager.getRankByName(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "Ese rango no existe.");
            return;
        }
        try {
            Player p = Bukkit.getPlayer(name);

            //rankgos addrank <user> <rank> <time/value>
            String value = args[2];
            String indicator = value.substring(value.length() - 1);
            //1d, 1y, 2s
            boolean permanent;
            long time = 0L;
            int number;
            permanent = value.equalsIgnoreCase("permanent");

            if(IsaNumber(value.replaceAll(indicator, ""))){
                number = Integer.getInteger(value.replaceAll(indicator, ""));
                switch (indicator.toLowerCase(Locale.ROOT)){
                    case "h":
                        time = number * 3600000L;
                        break;
                    case "d":
                        time = number * 86400000L;
                        break;
                    case "m":
                        time = number * 2592000000L;
                        break;
                    case "y":
                        time = number * 31104000000L;
                        break;
                    default:
                        break;
                }
            }

            if(time == 0L){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));

                return;
            }

            if(p == null){
                if(!plugin.getDatabase().UserIsCreated(args[0])){
                    sender.sendMessage(ChatColor.RED + "El usuario no existe.");
                    return;
                }
                plugin.getDatabase().UserloadData(args[0]);
            }
            User user = SystemManager.getUser(args[0]);
            Rank old_rank = user.getRank();
            if(old_rank.getPriority() > rank.getPriority()){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cel rango antiguo tiene mayor rango al nuevo."));
                return;
            }
            user.setRank(rank);

            if(permanent)
                user.setTime(-1);
            else
                user.setTime(time + System.currentTimeMillis());

            plugin.getDatabase().UsersaveData(user.getName());
            if(p != null){
                old_rank.getGlobal_permissions().keySet().forEach(user::removePermission);
                old_rank.getServer_permissions().keySet().forEach(user::removePermission);
                user.loadAllPermissions();
                //enviar mensaje al jugador.
                plugin.getMessageComponent().sendMessageFromArray(p,
                        plugin.getMessageComponent().getMessageArray("message.rank-user.user-msg.add"));
                plugin.getMessageComponent()
                        .sendMessageFromArray(sender,
                                plugin.getMessageComponent().getMessageArray("message.rank-user.admin-msg.add"), p);
            }


        }
        catch (Exception E){
            E.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Error al intentar aplicar el rango al jugador.");
        }

        /*
          message done.
         */

    }

    private boolean IsaNumber(String a){
        try {
            int b = Integer.parseInt(a);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    @Override
    public String name() {
        return "addrank";
    }

    @Override
    public String info() {
        return "/rangos addrank <user> <rank> <time> (1h,1d,1m,1y,permanent)";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
