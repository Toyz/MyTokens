package com.toyz.MyTokens.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Tools.Item;
import com.toyz.MyTokens.Utils.MathHelper;
import com.toyz.MyTokens.Utils.MessageHelper;

public class EntityDeath implements Listener {
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Entity killedE = event.getEntity();
		if (killedE instanceof Player) {
			Player killed = (Player) killedE;
			//String dead = killed.getDisplayName();
			Player _player = killed.getKiller();
			
			if(_player.getGameMode() == GameMode.CREATIVE){
				return;
			}
			
			if(!MyTokens._plugin.getConfig().getBoolean("modes.pvp")){
				return;
			}
			if(MathHelper.ShouldDropOnKill()){
				int Drop = MathHelper.randInt(MyTokens._plugin.getConfig().getInt("Drop.kills.min"), MyTokens._plugin.getConfig().getInt("Drop.kills.max"));
				
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
					_player.sendMessage(MessageHelper.Format(_player, dropitem.getString("alert"), Drop + ""));
				}else if(dropmsg.getBoolean("say")){
					int Current = MyTokens.UserTokens.getConfig().getInt(_player.getUniqueId().toString());
					Current = Current + Drop;
					MyTokens.UserTokens.getConfig().set(_player.getUniqueId().toString(), Current);
					for(String msg : dropmsg.getStringList("messages")){
						String f = msg;
						f = MessageHelper.Format(_player, f, Drop + "");
						_player.sendMessage(f);
					}
				}
				//Current = GetAmount + Current;
				
				//killer.sendMessage();
			}
			/*if(!(killedE instanceof Player)){ //if we get this far, we know the dead thing is not a player
	            if(killedE.getLastDamageCause() instanceof EntityDamageByEntityEvent){ //the dead thing was killed by an entity
	                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) killedE.getLastDamageCause();
	                if(entityDamageByEntityEvent.getDamager() instanceof Player){ //the killer was a player
	                    Player killer = (Player)entityDamageByEntityEvent.getDamager();
	                    //do stuff to killer
	                    System.out.println(killer.getName());
	                }
	            }
	        }*/
		}
	}
}
