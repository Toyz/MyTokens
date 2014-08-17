package com.toyz.MyTokens.Tools;

import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Utils.*;

public class Item {
	private Hashtable<Integer, ItemStack> items = null;
	public static Player player = null;
	
	public Item(){
		items = new Hashtable<Integer, ItemStack>();
	}
	
	public Hashtable<Integer, ItemStack> BuildItems(){
		ConfigurationSection cs = MyTokens.TokenShop.getConfig().getConfigurationSection("Shop");
		
		for (String key : cs.getKeys(false)){
			int place = 0;
			try{
				place = Integer.valueOf(key).intValue() - 1;
			}catch (Exception e){
				MyTokens.logger.warning("Failed to create item with ID of " + cs.getString(key + ".id") + " @ place " + key);
			}
			ItemStack item = CreateItem(cs.getString(key + ".id"), 
					cs.getString(key + ".name"),
					cs.getStringList(key + ".lore"), 
					cs.getInt(key + ".amount"),
					cs.getBoolean(key + ".glow"));
			
			if(item != null && place < 53)
				items.put(place, item);
		}
		return items;
	}
	
	public static ItemStack CreateItem(String id, String name, List<String> lore, int amount, boolean glow){
		String[] data = id.split(":");
		int mit = 0;
		try{
			mit = Integer.valueOf(data[0]).intValue();
		}catch (Exception e){
			MyTokens.logger.warning("Failed to create item with ID of " + id);
			return null;
		}
		ItemStack is = new ItemStack(Material.getMaterial(mit));
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		
		if(data.length == 2){
			is.setDurability(Short.valueOf(data[1]).shortValue());
		}
		
		if(amount > 0){
			is.setAmount(amount);
		}
		
		if(lore != null){
			List<String> Lores = new ArrayList<String>();
			for(String l : lore){
				Lores.add(MessageHelper.Format(player, l));
			}
			im.setLore(Lores);
		}
		is.setItemMeta(im);
		if(glow)
			NMSUtils.addGlow(is); 
		
		//System.out.println(is);
		return is;
	}
}
