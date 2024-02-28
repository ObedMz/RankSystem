package obed.me.ranks.commands.user;

import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class setduration extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            //Not valid
            return;
        }

        if(!plugin.getConfigManager().getConfig().getBoolean("backend")){
            sender.sendMessage(ChatColor.RED + "Esta consola no está habilitada para usar este comando.");
            return;
        }


        if(args.length <=1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));

            return;
        }

        String name = args[0];
        String value = args[1];

        String indicator = value.substring(value.length() - 1);
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
        Player p = Bukkit.getPlayer(name);
        if(p == null){
            if(!plugin.getDatabase().UserIsCreated(name)){
                sender.sendMessage(ChatColor.RED + "el usuario no existe.");
                return;
            }
            plugin.getDatabase().UserloadData(name);
        }
        User user = SystemManager.getUser(name);
        if(permanent)
            user.setTime(-1);
        else
            user.setTime(time + System.currentTimeMillis());

        plugin.getDatabase().UsersaveData(user.getName());
        /*
          message done.
         */
        sender.sendMessage(ChatColor.GREEN + "Se actualizaron los datos del usuario " + user.getName());
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
        return "setduration";
    }

    @Override
    public String info() {
        return "/rangos setduration <player> <time>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
