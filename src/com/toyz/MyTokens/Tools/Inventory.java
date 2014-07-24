package com.toyz.MyTokens.Tools;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Utils.MessageHelper;

public class Inventory {
	private Player _player = null;
	private Hashtable<Integer, ItemStack> _items = null;
	private org.bukkit.inventory.Inventory _Inventory = null;
	
	public Inventory(Player p, Hashtable<Integer, ItemStack> items){
		_player = p;
		_items = items;
		
		_Inventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', MyTokens._plugin.getConfig().getString("title")));
		
		Build();
	}
	
	private void Build(){
		int i = 0;
		Iterator<Map.Entry<Integer, ItemStack>> it = _items.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry<Integer, ItemStack> entry = it.next();
			_Inventory.setItem(entry.getKey(), entry.getValue());
			//MyTokens.console.sendMessage(MessageHelper.Format(null, "&Loaded item @ index " + entry.getKey()));
		}
		ConfigurationSection cs = MyTokens._plugin.getConfig().getConfigurationSection("infoitem");
		Item.player = _player;
		ItemStack lastItem = Item.CreateItem(cs.getString("id"), cs.getString("name"), cs.getStringList("lore"), 0, cs.getBoolean("glow"));
		_Inventory.setItem(53, lastItem);
	}
	
	public void Open(){
		_player.openInventory(_Inventory);
	}
}
