package obed.me.ranks.utils;

import obed.me.ranks.Ranks;
import obed.me.ranks.managers.ConfigManager;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.Rank;
import obed.me.ranks.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

/**
 * This class have all methods where the plugin can establish and storage
 * all the data into the MySQL server.
 */
public class Database {
    private String host;
    private String port;
    private String user;
    private String pass;
    private String database;
    private final String t_user = "t_user";
    private final String t_rank = "t_rank";
    /**
     * Use this methods to make a connection with the database when
     * the server start.
     */
    public void LoadDatabase(){
        try {
            InetAddress address = InetAddress.getByName(host);
            host = address.getHostAddress();
            DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=false", user, pass);
            System.out.print("Conectandose a: " + host + ":" + port);
        } catch (UnknownHostException | SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RankSystem] Error al cargar la base de datos.");
            Bukkit.shutdown();
        }
    }

    /**
     * Method to renew the MySQL connection.
     * @return Connection
     */
    public Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=false", user, pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Method to get the MySQL credentials from the config file.
     * @see obed.me.ranks.managers.ConfigManager
     */
    public void LoadConfigDatabase(){
        ConfigManager config = Ranks.getInstance().getConfigManager();

        host = config.getConfig().getString("database.host");
        port = config.getConfig().getString("database.port");
        user = config.getConfig().getString("database.user");
        pass = config.getConfig().getString("database.pass");
        database = config.getConfig().getString("database.db");
    }

    public void checkIfTableExist() {
        Connection con = getConnection();
        try {
            String user_db = "CREATE TABLE IF NOT EXISTS " + t_user + " (User VARCHAR(255),Rank VARCHAR(255), time BIGINT, Prefix VARCHAR(255), Suffix VARCHAR(255), Color VARCHAR(255));";
            String rank_db = "CREATE TABLE IF NOT EXISTS " + t_rank + " (Name VARCHAR(255),Prefix VARCHAR(255), GPerms VARCHAR(255), Inherit VARCHAR(255), Default BOOLEAN, Priority INTEGER);";

            Bukkit.getConsoleSender().sendMessage("[SystemRank] Creando tablas.");

            Statement stmt = con.createStatement();
            stmt.execute(user_db);
            stmt.execute(rank_db);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public boolean isRankCreated(String name){
        Connection con = getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM `" + t_rank + "` WHERE `Name` = ?;");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

    public void loadDatabaseRanks(){
        Connection con = getConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            String queryBuilder = "SELECT * " +
                    "FROM " +
                    "`" + t_rank + "`;";
            preparedStatement = con.prepareStatement(queryBuilder);
            resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                String name = resultSet.getString("Name");
                String prefix = resultSet.getString("Prefix");
                boolean def = resultSet.getBoolean("Default");
                List<Rank> inherit_ranks = new ArrayList<>();
                for(String str : resultSet.getString("Inherit").split(";"))
                    inherit_ranks.add(SystemManager.getRankByName(str));

                int priority = resultSet.getInt("Priority");
                List<String> raw_perms = new ArrayList<>(Arrays.asList(resultSet.getString("GPerms").split(";")));
                Map<String,Boolean> gperms = new HashMap<>();
                raw_perms.forEach(str -> gperms.put(str, !str.startsWith("*")));
                Rank rank = SystemManager.getRankByName(name);
                assert rank != null;
                rank.setPrefix(prefix);
                rank.setIsdefault(def);
                rank.setInherit(inherit_ranks);
                rank.setPriority(priority);
                rank.getGlobal_permissions().putAll(gperms);
            }
        } catch (Exception Exception) {
            Exception.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void savePerms(String rank){
        Rank rnk = SystemManager.getRankByName(rank);
        if(rnk == null)
            return;

        String concat = "";
        for(String str : rnk.getGlobal_permissions().keySet())
            concat = concat + (rnk.getGlobal_permissions().get(str) ? "" : "*") + str + ";";

        Connection con = getConnection();
        PreparedStatement preparedStatement;
        try {

            String Builder = "UPDATE `" + t_rank + "` SET " +
                    "GPerms = ? Where Name = ?;";
            preparedStatement = con.prepareStatement(Builder);
            preparedStatement.setString(1 ,concat);
            preparedStatement.setString(2, rank);
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
    public void createRank(String name, String prefix, int priority, boolean bol){
        if(isRankCreated(name)){
            Bukkit.getLogger().info("Este rango ya existe.");
            return;
        }
        if(name == null || prefix == null){
            Bukkit.getLogger().info("Faltan más valores.");
            return;
        }
        Connection con = getConnection();
        try {
            String queryBuilder = "INSERT INTO `" + t_rank + "` " +
                    "(`Name`, `Prefix`, `Priority`, `Default`) VALUES " +
                    "(?, ?, ?);";
            PreparedStatement preparedStatement = con.prepareStatement(queryBuilder);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, prefix);
            preparedStatement.setInt(3, priority);
            preparedStatement.setBoolean(4, bol);
            preparedStatement.executeUpdate();
            Bukkit.getLogger().info("Rango " + name +" creado correctamente.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public void DeleteRank(String name){
        Connection con = getConnection();
        String queryBuilder = "DELETE FROM `" +  t_rank + "` WHERE Name = ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(queryBuilder);
            preparedStatement.setString(1, name);
            Statement stmt = con.createStatement();
            stmt.execute(queryBuilder);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public void RankUpdatePrefix(String name, String p){
        Connection con = getConnection();
        PreparedStatement preparedStatement;
        try {

            String Builder = "UPDATE `" + t_rank + "` SET " +
                    "Prefix = ? Where Name = ?;";
            preparedStatement = con.prepareStatement(Builder);
            preparedStatement.setString(1 ,p);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    //Inherit
    public void RankUpdatePriority(String name, int p){
        Connection con = getConnection();
        PreparedStatement preparedStatement;
        try {

            String Builder = "UPDATE `" + t_rank + "` SET " +
                    "Priority = ? Where Name = ?;";
            preparedStatement = con.prepareStatement(Builder);
            preparedStatement.setInt(1 ,p);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public void RankUpdateInherit(String rank, List<Rank> list){
        Connection con = getConnection();
        PreparedStatement preparedStatement;
        String path = "";
        for(Rank str : list)
            path = path + str.getName() + ";";

        try {

            String Builder = "UPDATE `" + t_rank + "` SET " +
                    "Inherit = ? Where Name = ?;";
            preparedStatement = con.prepareStatement(Builder);
            preparedStatement.setString(2, path);
            preparedStatement.setString(2, rank);
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }


    /*************************************/
    // Table for users.
    /*************************************/

    public static void TaskAsync(Runnable runnable){
        Ranks.getInstance().getServer().getScheduler().runTaskAsynchronously(Ranks.getInstance(), runnable);
    }

    public void UserloadData(String name){
        Connection con = getConnection();
        PreparedStatement preparedStatement;
        try{
            String Builder = "SELECT * FROM `" + t_user + "` Where User = ?;";
            preparedStatement = con.prepareStatement(Builder);
            preparedStatement.setString(0, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                String prefix = resultSet.getString("Prefix");
                String suffix = resultSet.getString("Suffix");
                Rank rank = SystemManager.getRankByName(resultSet.getString("Rank"));
                String color = resultSet.getString("Color");
                long time = Long.getLong(resultSet.getString("time"));
                User user = SystemManager.getUser(name);
                user.setRank(rank);
                user.setSuffix(suffix);
                user.setPrefix(prefix);
                user.setColor(ChatColor.valueOf(color));
                user.setTime(time);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean UserIsCreated(String user){
        Connection con = getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM `" + t_user + "` WHERE `User` = ?;");
            statement.setString(1, user);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return false;

    }
    public void UserCreateData(String name){
        if((UserIsCreated(name))){
            Bukkit.getLogger().info("Este usuario ya existe.");
            return;
        }
        Connection con = getConnection();
        try {
            String queryBuilder = "INSERT INTO `" + t_user + "` " +
                    "(`User`, `Rank`, `time`, `Prefix`,`Suffix`,  `Color`) VALUES " +
                    "(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = con.prepareStatement(queryBuilder);
            preparedStatement.setString(1, name);
            if(SystemManager.getDefaultRank() == null)
                preparedStatement.setString(2, null);
            preparedStatement.setString(2, SystemManager.getDefaultRank().getName());
            preparedStatement.setLong(3, 0);
            preparedStatement.setString(4, null);
            preparedStatement.setString(5, null);
            preparedStatement.setString(6, null);
            preparedStatement.executeUpdate();
            Bukkit.getLogger().info("Rango " + name +" creado correctamente.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void UsersaveData(String p){
        Connection con = getConnection();
        User user = SystemManager.getUser(p);
        try {
            String queryBuilder = "UPDATE `" + t_user + "` " +
                    "SET Rank = ?, time = ?, Prefix = ? ,Suffix = ? ,  Color = ? WHERE User = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(queryBuilder);
            preparedStatement.setString(1, user.getRank().getName());
            preparedStatement.setLong(2, user.getTime());
            preparedStatement.setString(3, user.getPrefix());
            preparedStatement.setString(4, user.getSuffix());
            preparedStatement.setString(5, user.getColor().toString());
            preparedStatement.setString(6, user.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
