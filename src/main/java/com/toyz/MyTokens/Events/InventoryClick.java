package com.toyz.MyTokens.Events;

import java.text.DecimalFormat;
import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.Tools.BuildTokenBlocks;
import com.toyz.MyTokens.Tools.Item;
import com.toyz.MyTokens.Tools.TokenBlock;
import com.toyz.MyTokens.Utils.*;
import com.toyz.MyTokens.sql.SQLhandler;

public class InventoryClick  implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	 public void on(InventoryClickEvent e){
		 String title = ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("title"));
		 if((e.getInventory().getTitle() != null) && (e.getInventory().getTitle().equalsIgnoreCase("Breakable Blocks"))){
			e.setCancelled(true);
			if(((Player)e.getWhoClicked()).hasPermission("mytokens.admin.enableblocks") || ((Player)e.getWhoClicked()).isOp()){
				ConfigurationSection cs = MyTokens.getAPI().getDropConfig().getConfig().getConfigurationSection("Drop.break.blocks");

				if(e.getRawSlot() == 53){
					MyTokens.getAPI().getDropConfig().saveConfig();
					((Player)e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("prefix")) + " Shops.yml has been updated");
					return;
				}	
				
				
				if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType() == Material.AIR)) {
					 return;
				 }
				
				TokenBlock tb = MyTokens.getAPI().getDropBlocks().get(e.getRawSlot());
				
				if(e.getCurrentItem().getType() == Material.REDSTONE_BLOCK)
					tb.SetEnabled(true);
				else
					tb.SetEnabled(false);
				
				cs.set(tb.getType().getId() + ".enabled", tb.enabled());
				MyTokens.getAPI().setBreakableItems(BuildTokenBlocks.BuildBreakAbleList());
				
				int id = 0;
				for(TokenBlock block: MyTokens.getAPI().getDropBlocks()){
					DecimalFormat df = new DecimalFormat("#%");
					
					List<String> lore = Arrays.asList(
							ChatColor.WHITE + "Max: " + ChatColor.GOLD + block.maxDrop() + "",
							ChatColor.WHITE + "Min: " + ChatColor.GOLD + block.minDrop() + "",
							ChatColor.WHITE + "Percent of Drop: " + ChatColor.GOLD + df.format(block.getChance()),
							ChatColor.WHITE + "Drops Enabled: " + ChatColor.GOLD + (block.enabled() ? "Yes" : "No")
					);
					int BlockID = block.enabled() ? block.getType().getId() : 152;
					e.getInventory().setItem(id, Item.CreateItem(BlockID + "", block.getType().name().replace("_", " ").toLowerCase(), lore, 1, false));
					id++;
				}
				((Player)e.getWhoClicked()).updateInventory();
				
				return;
			}else{
				e.getInventory().setItem(53, null);
				((Player)e.getWhoClicked()).updateInventory();
			}
			//if(e.getCurrentItem().getType() == Material.REDSTONE_BLOCK){
				
			//}
		 }
		 if ((e.getInventory().getTitle() != null) && (e.getInventory().getTitle().equalsIgnoreCase(title)))
		 {
			 e.setCancelled(true);
			 if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType() == Material.AIR)) {
				 return;
			 }
			 int index = e.getRawSlot() + 1;
			 int _size = MyTokens.getAPI().getConfig().getInt("settings.shop.rows");
			 int _rowLength = _size * 9;
			 if(index < _rowLength){
				 SQLhandler sql = MyTokens.getAPI().getSqlHandler();
				 ConfigurationSection cs = MyTokens.getAPI().getTokenShop().getConfig().getConfigurationSection("Shop." + index);
				 
				 int Tokens = sql.GetBalance((Player)e.getWhoClicked());
				 if(Tokens >= cs.getInt("cost") || (e.getWhoClicked()).hasPermission("mytokens.admin.nopay") ||(e.getWhoClicked()).isOp()){
					 for (String cmd : cs.getStringList("commands")){
                         if (cmd.indexOf('/') == 0) {
                             cmd = cmd.substring(1);
                         }

                         cmd = cmd.replace("%player", e.getWhoClicked().getName());
                         Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', cmd));
                     }
					 if(!(e.getWhoClicked()).hasPermission("mytokens.admin.nopay")){
						 if(!(e.getWhoClicked()).isOp()){
							 Tokens = Tokens - cs.getInt("cost");
							 sql.SetBalance((Player)e.getWhoClicked(), Tokens);
						 }
					 }

                     String message = MyTokens.getAPI().getConfig().getString("settings.command-messages.thank-you");
                     ((Player) e.getWhoClicked()).sendMessage(MyTokens.getAPI().getMessageHelper().format((Player) e.getWhoClicked(), message, cs.getInt("cost") + ""));

					 ItemStack i = e.getInventory().getItem(_rowLength - 1);
					 ConfigurationSection cs1 = MyTokens.getAPI().getConfig().getConfigurationSection("infoitem");
					 ItemMeta l = i.getItemMeta();
					 List<String> lore = new ArrayList<String>();
					 for(String msg : cs1.getStringList("lore")){
						 msg = MyTokens.getAPI().getMessageHelper().format(((Player)e.getWhoClicked()), msg);
						 lore.add(msg);
					 }
					 l.setLore(lore);
					 i.setItemMeta(l);
					 if(cs1.getBoolean("glow"))
						NMSUtils.addGlow(i); 
					 ((Player)e.getWhoClicked()).updateInventory();
				 }else{
                     String message = MyTokens.getAPI().getConfig().getString("settings.command-messages.cant-afford");
                     ((Player) e.getWhoClicked()).sendMessage(MyTokens.getAPI().getMessageHelper().format((Player) e.getWhoClicked(), message, cs.getInt("cost") + ""));
				 }
			 }
			 //System.out.println(e.getRawSlot());
		 }
	 }
}