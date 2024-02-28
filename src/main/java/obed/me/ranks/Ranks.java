package obed.me.ranks;

import obed.me.ranks.listeners.BaseListener;
import obed.me.ranks.managers.CommandManager;
import obed.me.ranks.managers.ConfigManager;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import obed.me.ranks.utils.Database;
import obed.me.ranks.utils.MessageComponent;
import obed.me.ranks.utils.PlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public final class Ranks extends JavaPlugin {
    private static Ranks instance;
    private ConfigManager configManager;
    private Database database;
    private CommandManager commandManager;
    private MessageComponent messageComponent;

    @Override
    public void onEnable() {
        messageComponent = new MessageComponent();
        instance = this;
        configManager = new ConfigManager();
        configManager.registerConfig();
        configManager.registerMessage();
        loadDatabase();
        loadRankInformation();
        commandManager = new CommandManager();
        commandManager.setup();
        registerPlaceHolder();
        getServer().getPluginManager().registerEvents(new BaseListener(this), this);

    }
    public CommandManager getCommandManager(){
        return commandManager;
    }
    private void registerPlaceHolder() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("PlaceholderAPI found, creating expansion.");
            new PlaceHolder().register();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! variables will not work propertly.");
        }
    }

    private void loadDatabase() {
        database = new Database();
        database.LoadConfigDatabase();
        database.LoadDatabase();
        database.checkIfTableExist();
    }

    private void loadRankInformation() {
        database.loadDatabaseRanks();
        SystemManager.getRanks().values().forEach(rank -> {
            configManager.createYML(rank);
            rank.getFileConfiguration().getStringList("permissions").forEach(str -> {
                rank.getServer_permissions().put(str.replaceAll("\\*",""), !str.startsWith("*"));
            });

        });
        SystemManager.getRanks().values().forEach(rank -> {
            if(SystemManager.getDefaultRank() != null){
                rank.getGlobal_permissions().putAll(Objects.requireNonNull(SystemManager.getDefaultRank()).getGlobal_permissions());
                rank.getServer_permissions().putAll(Objects.requireNonNull(SystemManager.getDefaultRank()).getServer_permissions());
            }
            /* Loading inherit permissions */
            for(Rank inherit : rank.getInherit()){
                rank.getGlobal_permissions().putAll(inherit.getGlobal_permissions());
                rank.getServer_permissions().putAll(inherit.getServer_permissions());
            }
            
        });
    }

    public ConfigManager getConfigManager(){
        return this.configManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public Database getDatabase(){
        return database;
    }
    public static Ranks getInstance() {
        return instance;
    }

    public MessageComponent getMessageComponent() {
        return messageComponent;
    }
}
