package obed.me.ranks.commands.rank;

import me.clip.placeholderapi.libs.kyori.adventure.platform.facet.Facet;
import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class create extends SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        //rangos createrank <name> <prefix> <priority> <default>
        if(sender instanceof Player){
            return;
        }
        if(!plugin.getConfigManager().getConfig().getBoolean("backend")){
            sender.sendMessage(ChatColor.RED + "Esta consola no está habilitada para usar este comando.");
            return;
        }

        if(args.length <=3){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        String name = args[0];
        if(plugin.getDatabase().isRankCreated(name)){
            sender.sendMessage(ChatColor.RED + "El rango no existe.");

            return;
        }
        String prefix = args[1];
        String priority = args[2];
        boolean bol = Boolean.getBoolean(args[3]);
        if(!IsNumber(priority)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', info()));
            return;
        }
        Rank rank = new Rank();
        rank.setName(name);
        rank.setPrefix(prefix);
        rank.setPriority(Integer.parseInt(priority));
        rank.setIsdefault(bol);
        SystemManager.getRanks().put(rank.getName() , rank);
        //la inherencia y los permisos es con otro comando.
        plugin.getDatabase().createRank(name,prefix,Integer.parseInt(priority),bol);
        //rango creado.
        plugin.getConfigManager().createYML(rank);
        sender.sendMessage(ChatColor.GREEN + "Rango " + rank.getName() + " creado correctamente.");

    }

    private boolean IsNumber(String tr){
        try{
            int b = Integer.parseInt(tr);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public String name() {
        return "createrank";
    }

    @Override
    public String info() {
        return "/rangos createrank <name> <prefix> <priority> <default>";
    }

    @Override
    public String[] alias() {
        return new String[0];
    }
}
