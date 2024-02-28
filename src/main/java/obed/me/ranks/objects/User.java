package obed.me.ranks.objects;

import obed.me.ranks.Ranks;
import obed.me.ranks.managers.SystemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class User {
    private String name;
    private Player player;
    private Rank rank;
    private long time;
    private String prefix;
    private ChatColor color;
    private String suffix;

    public User(String name) {
        this.name = name;
        SystemManager.getUsers().put(name, this);
    }
    public User(Player name) {
        this.name = name.getName();
        this.player = name;
        SystemManager.getUsers().put(name.getName(), this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPrefix() {
        if(prefix == null)
            return ChatColor.translateAlternateColorCodes('&', rank.getPrefix());

        if(prefix.isEmpty())
            return ChatColor.translateAlternateColorCodes('&', rank.getPrefix());

        return ChatColor.translateAlternateColorCodes('&', prefix);

    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean removePermission(String permission) {
        for (PermissionAttachmentInfo paInfo : player.getEffectivePermissions()) {
            if (paInfo.getAttachment() != null && paInfo.getAttachment().getPlugin().equals(Ranks.getInstance())) {
                paInfo.getAttachment().unsetPermission(permission);
                return true;
            }
        }
        return false;
    }

    public void loadAllPermissions(){
        loadGlobalPerms();
        loadServerPerms();
    }
    public void loadGlobalPerms(){
        if(rank == null)
            return;
        getRank().getGlobal_permissions().keySet().forEach(str ->{
            player.addAttachment(Ranks.getInstance(), str, getRank().getGlobal_permissions().get(str));
        });
    }
    public void loadServerPerms(){
        if(rank == null)
            return;
        getRank().getServer_permissions().keySet().forEach(str ->{
            player.addAttachment(Ranks.getInstance(), str, getRank().getServer_permissions().get(str));
        });
    }

    public String getSuffix() {
        return ChatColor.translateAlternateColorCodes('&', suffix);
    }

    public void setSuffix(String suffix){
        this.suffix = suffix;
    }
}
