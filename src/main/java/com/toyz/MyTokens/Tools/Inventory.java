package com.toyz.MyTokens.Tools;

import java.util.*;

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
	private int _size = 0;
	private int _rowLength = 54;
	public Inventory(Player p, Hashtable<Integer, ItemStack> items, Boolean useRow, String title, Boolean ShowTokenBal){
		_player = p;
		_items = items;
		
		_size = MyTokens.getAPI().getConfig().getInt("settings.shop.rows");
		if(useRow)
			_rowLength = _size * 9;
		_Inventory = Bukkit.createInventory(null, _rowLength, title);
		
		Build(ShowTokenBal);
	}
	
	public org.bukkit.inventory.Inventory getInventory(){
		return this._Inventory;
	}
	
	private void Build(Boolean ShowTokenBal){
		Iterator<Map.Entry<Integer, ItemStack>> it = _items.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry<Integer, ItemStack> entry = it.next();
			if(entry.getKey() < _rowLength){
				_Inventory.setItem(entry.getKey(), entry.getValue());
			}
			//MyTokens.console.sendMessage(MessageHelper.Format(null, "&Loaded item @ index " + entry.getKey()));
		}
		if(ShowTokenBal){
			ConfigurationSection cs = MyTokens.getAPI().getConfig().getConfigurationSection("infoitem");
			Item.player = _player;
			ItemStack lastItem = Item.CreateItem(cs.getString("id"), cs.getString("name"), cs.getStringList("lore"), 0, cs.getBoolean("glow"));
			_Inventory.setItem((_rowLength - 1), lastItem);
		}else{
			ItemStack lastItem = Item.CreateItem(347 + "", "Save Changes", Arrays.asList("Save your settings to DropSettings.yml", ChatColor.RED + "If you don't save DropSettings.yml", ChatColor.RED + "Settings will not stay on reload!"), 0, true);
			_Inventory.setItem((_rowLength - 1), lastItem);
		}
	}
	
	public void Open(){
		_player.openInventory(_Inventory);
	}
}