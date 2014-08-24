package com.toyz.MyTokens.Events;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Utils.ConfigAccessor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        File tokensFile = new File(MyTokens.getAPI().getMyTokens().getDataFolder(), "Tokens.yml");
        if (tokensFile.exists()) {
           ConfigAccessor file = new ConfigAccessor(MyTokens.getAPI().getMyTokens(), tokensFile.getAbsolutePath());
           int balance = file.getConfig().getInt(e.getPlayer().getUniqueId().toString());
            if(balance > 0){
                MyTokens.getAPI().setBalance(e.getPlayer(), balance);
                file.getConfig().set(e.getPlayer().getUniqueId().toString(), null);
                MyTokens.getAPI().getLogger().info("Imported: " + e.getPlayer().getName());
                file.saveConfig();
            }
        }
    }
}
