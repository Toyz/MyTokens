package com.toyz.MyTokens.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.toyz.MyTokens.MyTokens;

public class PlayerJoin  implements Listener {
	 @EventHandler
	 public void onPlayerJoin(PlayerJoinEvent e){
		 Player player = e.getPlayer();
		 
		 if(MyTokens.UserTokens.getConfig().getString(player.getUniqueId().toString()) == null){
			 MyTokens.UserTokens.getConfig().set(player.getUniqueId().toString(), "0");
		 }
	 }
}
