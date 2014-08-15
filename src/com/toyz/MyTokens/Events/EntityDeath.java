package com.toyz.MyTokens.Events;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Tools.Item;
import com.toyz.MyTokens.Utils.*;
import com.toyz.MyTokens.sql.SQLhandler;

public class EntityDeath implements Listener {
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Entity killedE = event.getEntity();
		if (killedE instanceof Player) {
			Player killed = (Player) killedE;
			//String dead = killed.getDisplayName();
			if(killed.getKiller() instanceof Player){
				Player _player = killed.getKiller();
				
				if(_player.getGameMode() == GameMode.CREATIVE){
					return;
				}
				
				if(!MyTokens._plugin.getConfig().getBoolean("modes.pvp")){
					return;
				}
				if(MathHelper.ShouldDropOnKill()){
					SQLhandler sql = MyTokens.sql;
					if(MyTokens._plugin.getConfig().getBoolean("settings.pvp.enablethrottle")){
						int allowed = MyTokens._plugin.getConfig().getInt("settings.pvp.killthrottle");
						int timeout = MyTokens._plugin.getConfig().getInt("settings.pvp.timeout");
						
						int totalTimeOut = (timeout * 1000) + MathHelper.currentTimeMillis();
						int kills = sql.GetKillCount(_player, killed);

						int tmp = sql.GetTimeOut(_player, killed);
						if(kills < allowed){
							if(MathHelper.currentTimeMillis() < tmp || tmp == 0){
								kills = kills + 1;
								sql.SetKillCount(_player, killed, kills, totalTimeOut);
							}else{
								sql.SetKillCount(_player, killed, 1, totalTimeOut);
							}
						}else{
							if(MathHelper.currentTimeMillis() < tmp){
								return;
							}else{
								sql.SetKillCount(_player, killed, 1, totalTimeOut);
							}
						}
					}
					int Drop = MathHelper.randInt(MyTokens.DropConfig.getConfig().getInt("Drop.kills.min"), MyTokens.DropConfig.getConfig().getInt("Drop.kills.max"));
					
					ConfigurationSection dropitem = MyTokens._plugin.getConfig().getConfigurationSection("dropitem");
					ConfigurationSection dropmsg = MyTokens._plugin.getConfig().getConfigurationSection("dropmsg");
					
					if(dropitem.getBoolean("drop")){
						List<String> msgs = new ArrayList<String>();
						
						for(String msg : dropitem.getStringList("item.lore")){
							String f = msg;
							f = MessageHelper.Format(_player, f, Drop + "");
							msgs.add(f);
						}
						
						ItemStack droppedItem = Item.CreateItem(dropitem.getString("item.id"), dropitem.getString("item.name") + "  [" + Drop + "]", msgs, 0, true);
						org.bukkit.entity.Item i = _player.getWorld().dropItem(_player.getLocation(), droppedItem);
						i.setPickupDelay(dropitem.getInt("item.delay"));
						_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens._plugin.getConfig().getString("prefix")) + " " + MessageHelper.Format(_player, dropitem.getString("alert"), Drop + ""));
					}else if(dropmsg.getBoolean("say")){
						int Current = sql.GetBalance(_player);
						Current = Current + Drop;
						sql.SetBalance(_player, Current);
						for(String msg : dropmsg.getStringList("messages")){
							String f = msg;
							f = MessageHelper.Format(_player, f, Drop + "");
							_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens._plugin.getConfig().getString("prefix")) + " " + f);
						}
					}
					//Current = GetAmount + Current;
					
					//killer.sendMessage();
				}
			}
		}
	}
}
