package obed.me.ranks.managers;

import obed.me.ranks.objects.Rank;
import obed.me.ranks.objects.User;

import java.util.HashMap;

public class SystemManager {
    public static HashMap<String, Rank> ranks = new HashMap<>();
    public static HashMap<String, User> users = new HashMap<>();
    public static HashMap<String,Rank> getRanks(){
        return ranks;
    }
    public static HashMap<String, User> getUsers(){ return users; }

    public static User getUser(String name){
        if(users.containsKey(name))
            return users.get(name);
        return new User(name);
    }

    public static Rank getRankByName(String name){
        if(!ranks.containsKey(name))
            return null;
        return ranks.get(name);
    }

    public static Rank getDefaultRank() {
        for(Rank rank : getRanks().values()){
            if(rank.isIsdefault()){
                return rank;
            }
        }
        return null;
    }
}
