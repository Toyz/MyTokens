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
import com.toyz.MyTokens.sql.SQLhandler;

public class EntityDeath implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void on(EntityDeathEvent event) {
		Entity killedE = event.getEntity();
		if (killedE == null) {
			return;
		}
		if(!(killedE instanceof Player)) {
			if(killedE.getLastDamageCause() instanceof EntityDamageByEntityEvent){ //the dead thing was killed by an entity
                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) killedE.getLastDamageCause();
                if(entityDamageByEntityEvent.getDamager() instanceof Player){ //the killer was a player
                    Player _player = (Player)entityDamageByEntityEvent.getDamager();
                    if(_player.getGameMode() == GameMode.CREATIVE){
    					return;
    				}
    				
    				if(!MyTokens.getAPI().getConfig().getBoolean("modes.pve")){
    					return;
    				}
    				if(MyTokens.getAPI().getMathHelper().ShouldDropOnKill()){
    					SQLhandler sql = MyTokens.getAPI().getSqlHandler();
    					int Drop = MyTokens.getAPI().getMathHelper().randInt(MyTokens.getAPI().getDropConfig().getConfig().getInt("Drop.kills.min"), MyTokens.getAPI().getDropConfig().getConfig().getInt("Drop.kills.max"));
    					
    					ConfigurationSection dropitem = MyTokens.getAPI().getConfig().getConfigurationSection("dropitem");
    					ConfigurationSection dropmsg = MyTokens.getAPI().getConfig().getConfigurationSection("dropmsg");
    					
    					if(dropitem.getBoolean("drop")){
    						List<String> msgs = new ArrayList<String>();
    						
    						for(String msg : dropitem.getStringList("item.lore")){
    							String f = msg;
    							f = MyTokens.getAPI().getMessageHelper().format(_player, f, Drop + "");
    							msgs.add(f);
    						}
    						
    						ItemStack droppedItem = Item.CreateItem(dropitem.getString("item.id"), dropitem.getString("item.name") + "  [" + Drop + "]", msgs, 0, true);
    						org.bukkit.entity.Item i = _player.getWorld().dropItem(_player.getLocation(), droppedItem);
    						i.setPickupDelay(dropitem.getInt("item.delay"));
    						_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("prefix")) + " " + MyTokens.getAPI().getMessageHelper().format(_player, dropitem.getString("alert"), Drop + ""));
    					}else if(dropmsg.getBoolean("say")){
    						int Current = sql.GetBalance(_player);
    						Current = Current + Drop;
    						sql.SetBalance(_player, Current);
    						for(String msg : dropmsg.getStringList("messages")){
    							String f = msg;
    							f = MyTokens.getAPI().getMessageHelper().format(_player, f, Drop + "");
    							_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("prefix")) + " " + f);
    						}
    					}
    				}
                }
            }
		} else {// killedE is instance of Player 
			Player killed = (Player) killedE;
			//String dead = killed.getDisplayName();
			Player _player = killed.getKiller();
			
			if(_player == null || _player.getGameMode() == GameMode.CREATIVE){
				return;
			}
			
			if(!MyTokens.getAPI().getConfig().getBoolean("modes.pvp")){
				return;
			}
			if(MyTokens.getAPI().getMathHelper().ShouldDropOnKill()){
				SQLhandler sql = MyTokens.getAPI().getSqlHandler();
				if(MyTokens.getAPI().getConfig().getBoolean("settings.pvp.enablethrottle")){
					int allowed = MyTokens.getAPI().getConfig().getInt("settings.pvp.killthrottle");
					int timeout = MyTokens.getAPI().getConfig().getInt("settings.pvp.timeout");
					
					int totalTimeOut = (timeout * 1000) + MyTokens.getAPI().getMathHelper().currentTimeMillis();
					int kills = sql.GetKillCount(_player, killed);

					int tmp = sql.GetTimeOut(_player, killed);
					if(kills < allowed){
						if(MyTokens.getAPI().getMathHelper().currentTimeMillis() < tmp || tmp == 0){
							kills = kills + 1;
							sql.SetKillCount(_player, killed, kills, totalTimeOut);
						}else{
							sql.SetKillCount(_player, killed, 1, totalTimeOut);
						}
					}else{
						if(MyTokens.getAPI().getMathHelper().currentTimeMillis() < tmp){
							return;
						}else{
							sql.SetKillCount(_player, killed, 1, totalTimeOut);
						}
					}
				}
				int Drop = MyTokens.getAPI().getMathHelper().randInt(MyTokens.getAPI().getDropConfig().getConfig().getInt("Drop.kills.min"), MyTokens.getAPI().getDropConfig().getConfig().getInt("Drop.kills.max"));
				
				ConfigurationSection dropitem = MyTokens.getAPI().getConfig().getConfigurationSection("dropitem");
				ConfigurationSection dropmsg = MyTokens.getAPI().getConfig().getConfigurationSection("dropmsg");
				
				if(dropitem.getBoolean("drop")){
					List<String> msgs = new ArrayList<String>();
					
					for(String msg : dropitem.getStringList("item.lore")){
						String f = msg;
						f = MyTokens.getAPI().getMessageHelper().format(_player, f, Drop + "");
						msgs.add(f);
					}
					
					ItemStack droppedItem = Item.CreateItem(dropitem.getString("item.id"), dropitem.getString("item.name") + "  [" + Drop + "]", msgs, 0, true);
					org.bukkit.entity.Item i = _player.getWorld().dropItem(_player.getLocation(), droppedItem);
					i.setPickupDelay(dropitem.getInt("item.delay"));
					_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("prefix")) + " " + MyTokens.getAPI().getMessageHelper().format(_player, dropitem.getString("alert"), Drop + ""));
				}else if(dropmsg.getBoolean("say")){
					int Current = sql.GetBalance(_player);
					Current = Current + Drop;
					sql.SetBalance(_player, Current);
					for(String msg : dropmsg.getStringList("messages")){
						String f = msg;
						f = MyTokens.getAPI().getMessageHelper().format(_player, f, Drop + "");
						_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("prefix")) + " " + f);
					}
				}
				//Current = GetAmount + Current;
				
				//killer.sendMessage();
			}
		}
	}
}