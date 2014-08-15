package com.toyz.MyTokens.Events;

import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Utils.*;
import com.toyz.MyTokens.sql.SQLhandler;

public class InventoryClick  implements Listener {
	 @EventHandler
	 public void onInventoryClick(InventoryClickEvent e){
		 String title = ChatColor.translateAlternateColorCodes('&', MyTokens._plugin.getConfig().getString("title"));
		 if ((e.getInventory().getTitle() != null) && (e.getInventory().getTitle().equalsIgnoreCase(title)))
		 {
			 if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType() == Material.AIR)) {
				 return;
			 }
			 if (e.getSlot() == -1) {
				 e.setCancelled(true);
			 }
			 e.setCancelled(true);
			 int index = e.getSlot() + 1;
			 int _size = MyTokens._plugin.getConfig().getInt("settings.shop.rows");
			 int _rowLength = _size * 9;
			 if(index < _rowLength){
				 SQLhandler sql = new SQLhandler(MyTokens._plugin);
				 ConfigurationSection cs = MyTokens.TokenShop.getConfig().getConfigurationSection("Shop." + index);
				 System.out.println(cs.getStringList("commands"));
				 
				 int Tokens = sql.GetBalance((Player)e.getWhoClicked());
				 sql.GetSQL().close();//MyTokens.UserTokens.getConfig().getInt(e.getWhoClicked().getUniqueId().toString());
				 if(Tokens >= cs.getInt("cost")){
					 for (String cmd : cs.getStringList("commands")){
						 if (cmd.indexOf('/') == 0) {
							 cmd = cmd.substring(1);
						 }
						 
						 cmd = cmd.replace("%player", e.getWhoClicked().getName());
						 Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', cmd));
					 }
					 Tokens = Tokens - cs.getInt("cost");
					 sql.SetBalance((Player)e.getWhoClicked(), Tokens);
					 //MyTokens.UserTokens.getConfig().set(e.getWhoClicked().getUniqueId().toString(), Tokens);
					 
					 //Update Last Object
					 ItemStack i = e.getInventory().getItem(_rowLength - 1);
					 ConfigurationSection cs1 = MyTokens._plugin.getConfig().getConfigurationSection("infoitem");
					 ItemMeta l = i.getItemMeta();
					 List<String> lore = new ArrayList<String>();
					 for(String msg : cs1.getStringList("lore")){
						 msg = MessageHelper.Format(((Player)e.getWhoClicked()), msg);
						 lore.add(msg);
					 }
					 l.setLore(lore);
					 i.setItemMeta(l);
					 if(cs1.getBoolean("glow"))
						NMSUtils.addGlow(i); 
					 ((Player)e.getWhoClicked()).updateInventory();
				 }else{
					 ((Player)e.getWhoClicked()).sendMessage("You cannot afford this item!");
				 }
			 }
			 //System.out.println(e.getRawSlot());
		 }
	 }
}
