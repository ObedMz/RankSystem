package obed.me.ranks.listeners;

import net.md_5.bungee.api.chat.HoverEvent;
import obed.me.ranks.Ranks;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.User;
import obed.me.ranks.utils.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener extends BaseListener {

    public JoinListener(Ranks ranks) {
        super(ranks);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerLogin(PlayerLoginEvent e){
        Database.TaskAsync(()->{
            plugin.getDatabase().UserCreateData(e.getPlayer().getName());
            plugin.getDatabase().UserloadData(e.getPlayer().getName());

            User user = SystemManager.getUser(e.getPlayer().getName());
            checkRankduration(user);
            user.setPlayer(e.getPlayer());
            user.loadAllPermissions();
        });
    }


    public void checkRankduration(User user) {
        if(user.getRank() == null)
            return;
        if(user.getTime() == -1)
            return;

        if(System.currentTimeMillis() >= user.getTime()) {
            user.setTime(0L);
            user.setRank(SystemManager.getDefaultRank());
            plugin.getDatabase().UsersaveData(user.getName());
        }
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent e){
        SystemManager.getUsers().remove(e.getPlayer().getName());
    }
}
