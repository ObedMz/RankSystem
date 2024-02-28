package obed.me.ranks.managers;

import net.md_5.bungee.api.ChatColor;
import obed.me.ranks.Ranks;
import obed.me.ranks.commands.SubCommand;
import obed.me.ranks.commands.help;
import obed.me.ranks.commands.rank.*;
import obed.me.ranks.commands.reload;
import obed.me.ranks.commands.user.*;
import obed.me.ranks.commands.user.info;
import obed.me.ranks.commands.user.prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CommandManager implements CommandExecutor {
    private static HashMap<String, SubCommand> commands = new HashMap<>();
    private Ranks plugin = Ranks.getInstance();

    public void setup(){
        Objects.requireNonNull(plugin.getCommand("rangos")).setExecutor(this);
        this.getCommands().put("reload", new reload());
        this.getCommands().put("help", new help());
        this.getCommands().put("suffix", new suffix());
        this.getCommands().put("setduration", new setduration());
        this.getCommands().put("removerank", new removerank());
        this.getCommands().put("prefix", new prefix());
        this.getCommands().put("info", new info());
        this.getCommands().put("color", new color());
        this.getCommands().put("addrank", new addrank());

        //register commands for rank
        this.getCommands().put("createrank", new create());
        this.getCommands().put("rankinfo", new obed.me.ranks.commands.rank.info());
        this.getCommands().put("inherit", new inherit());
        this.getCommands().put("list", new list());
        this.getCommands().put("rankprefix", new obed.me.ranks.commands.rank.prefix());
        this.getCommands().put("priority", new priority());
        this.getCommands().put("deleterank", new deleterank());

    }
    public HashMap<String,SubCommand> getCommands(){
        return commands;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0){
            sender.sendMessage(ChatColor.RED + "Not enough arguments, type /rangos help");
            return false;
        }
        SubCommand sbcomand = this.getCommands().get(args[0].toLowerCase());
        if(sbcomand == null){
            sender.sendMessage(ChatColor.RED + "This command doesnt exist.");

        }

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(args));
        arrayList.remove(0);
        String[] str = new String[arrayList.size()];
        for(int x=0; x<arrayList.size();x++){
            str[x] = arrayList.get(x);
        }
        try{
            assert sbcomand != null;
            sbcomand.execute(sender, str);
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }

}
