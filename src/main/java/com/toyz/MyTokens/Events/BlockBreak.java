package com.toyz.MyTokens.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Tools.Item;
import com.toyz.MyTokens.Tools.TokenBlock;
import com.toyz.MyTokens.sql.SQLhandler;

public class BlockBreak implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void on(BlockBreakEvent e){
		Player _player = e.getPlayer();

		if(!MyTokens.getAPI().getConfig().getBoolean("modes.blockbreak")){
			return;
		}
		
		if(_player.getGameMode() == GameMode.CREATIVE){
			return;
		}
		
		if(e.isCancelled()){
			return;
		}
		
		//Handle config values
		ConfigurationSection dropitem = MyTokens.getAPI().getConfig().getConfigurationSection("dropitem");
		ConfigurationSection dropmsg = MyTokens.getAPI().getConfig().getConfigurationSection("dropmsg");
		
		//Let's do some checking now
		for (TokenBlock b : MyTokens.getAPI().getDropBlocks()) {
			if ((b.getType() == e.getBlock().getType()) && (b.shouldDrop()))
			{
				if(!b.enabled()){
					return;
				}
				int Drop = b.calculateDropAmount();
				
				if(dropitem.getBoolean("drop")){
					List<String> msgs = new ArrayList<String>();
					
					for(String msg : dropitem.getStringList("item.lore")){
						String f = msg;
						f = MyTokens.getAPI().getMessageHelper().format(_player, f, Drop + "");
						msgs.add(f);
					}
					
					ItemStack droppedItem = Item.CreateItem(dropitem.getString("item.id"), dropitem.getString("item.name") + "  [" + Drop + "]", msgs, 0, true);
					org.bukkit.entity.Item i = _player.getWorld().dropItem(e.getBlock().getLocation(), droppedItem);
					i.setPickupDelay(dropitem.getInt("item.delay"));
					_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("prefix")) + " " + MyTokens.getAPI().getMessageHelper().format(_player, dropitem.getString("alert"), Drop + ""));
				}else if(dropmsg.getBoolean("say")){
					SQLhandler sql = MyTokens.getAPI().getSqlHandler();
					int Current = sql.GetBalance(_player);//MyTokens.UserTokens.getConfig().getInt(_player.getUniqueId().toString());
					Current = Current + Drop;
					sql.SetBalance(_player, Current);
					//sql.GetSQL().commit();
					//MyTokens.UserTokens.getConfig().set(_player.getUniqueId().toString(), Current);
					for(String msg : dropmsg.getStringList("messages")){
						String f = msg;
						f = MyTokens.getAPI().getMessageHelper().format(_player, f, Drop + "");
						_player.sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("prefix")) + " " + f);
					}
				}
			}
		}
	}
}