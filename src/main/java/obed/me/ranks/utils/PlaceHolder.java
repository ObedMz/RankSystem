package obed.me.ranks.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import obed.me.ranks.Ranks;
import obed.me.ranks.managers.SystemManager;
import obed.me.ranks.objects.User;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Locale;

public class PlaceHolder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "system";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ObedMz";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        Ranks.getInstance().getDatabase().UserloadData(player.getName());
        User user = SystemManager.getUser(player.getName());

        switch (params.toLowerCase(Locale.ROOT)){
            case "rank.name":
                return user.getRank().getName();
            case "rank.prefix":
                return user.getRank().getPrefix();
            case "rank.priority":
                return Integer.toString(user.getRank().getPriority());
            case "rank.default":
                return Boolean.toString(user.getRank().isIsdefault());
            case "user.color":
                return user.getColor().toString();
            case "user.duration":
                return new Date(user.getTime() * 1000).toString();
            case "user.prefix":
                return user.getPrefix();
            case "user.suffix":
                return user.getSuffix();
        }
        return null;
    }
}
