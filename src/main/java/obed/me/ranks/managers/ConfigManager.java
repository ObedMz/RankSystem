package obed.me.ranks.managers;

import obed.me.ranks.Ranks;
import obed.me.ranks.objects.Rank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Class ConfigManager used to manage all
 * the YML files of the Plugin.
 */
public class ConfigManager {
    private final Ranks plugin = Ranks.getPlugin(Ranks.class);
    public FileConfiguration config = null;
    public FileConfiguration message = null;
    private File configFile = null;
    private File configMessage = null;

    public void FilePermissions(){
        File file = new File(this.plugin.getDataFolder(), "ranks");
        if(!file.exists())
            Bukkit.getLogger().info((file.mkdirs() ? "Directorio creado correctamente." : "No se pudo crear el directorio."));
    }
    public void saveRankConfig(Rank rank){
        File file = new File(this.plugin.getDataFolder() + "ranks" , rank.getName() + ".yml");
        try {
            rank.getFileConfiguration().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createYML(Rank rank){
        File file = new File(this.plugin.getDataFolder() + "ranks" , rank.getName() + ".yml");
        FileConfiguration fileConfiguration;
        try {

            if(file.createNewFile()){
                fileConfiguration = YamlConfiguration.loadConfiguration(file);
                fileConfiguration.set("permissions", null);
            }
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
            rank.setFileConfiguration(fileConfiguration);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method that return the config object
     * @return FileConfiguration
     */
    public FileConfiguration getConfig() {
        if (this.config == null)
            reloadConfig();
        return this.config;
    }
    public FileConfiguration getMessage() {
        if (this.message == null)
            reloadMessage();
        return this.message;
    }
    /**
     * Public method to reload the config.yml file
     */
    public void reloadConfig() {
        if (this.config == null)
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(this.plugin.getResource("config.yml")), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        this.config.setDefaults(defConfig);
    }

    public void reloadMessage() {
        if (this.message == null)
            this.configMessage = new File(this.plugin.getDataFolder(), "message.yml");
        this.message = YamlConfiguration.loadConfiguration(this.configMessage);
        Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(this.plugin.getResource("message.yml")), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        this.message.setDefaults(defConfig);
    }

    /**
     * Used to save the Configuration of the File.
     */
    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMessage() {
        try {
            this.message.save(this.configMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register a new Config.yml File
     */
    public void registerConfig() {
        this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (!this.configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    public void registerMessage() {
        this.configMessage = new File(this.plugin.getDataFolder(), "message.yml");
        if (!this.configMessage.exists()) {
            getMessage().options().copyDefaults(true);
            saveMessage();
        }
    }
}
