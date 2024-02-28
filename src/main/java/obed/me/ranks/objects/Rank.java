package obed.me.ranks.objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class Rank implements Comparable<Rank>{
    private String name;
    private String prefix;
    private Map<String, Boolean> global_permissions;
    private Map<String, Boolean> server_permissions;
    private List<Rank> inherit;
    private boolean isdefault;
    private int priority;
    private FileConfiguration fileConfiguration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<Rank> getInherit() {
        return inherit;
    }

    public void setInherit(List<Rank> inherit) {
        this.inherit = inherit;
    }

    public boolean isIsdefault() {
        return isdefault;
    }

    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }

    public Map<String, Boolean> getServer_permissions() {
        return server_permissions;
    }

    public Map<String, Boolean> getGlobal_permissions() {
        return global_permissions;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void setFileConfiguration(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    @Override
    public int compareTo(@NotNull Rank o) {
        return this.priority - o.getPriority();
    }
}
