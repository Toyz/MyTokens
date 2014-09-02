package com.toyz.MyTokens.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Sign;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.sql.SQLhandler;

public class PlayerUse implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void on(PlayerInteractEvent e){
         if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
             if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
                 Sign s = (Sign) e.getClickedBlock().getState();

                 if (s.getLines().length >= 4) {
                     if (s.getLine(0).equalsIgnoreCase("[MyTokens]") || s.getLine(0).equalsIgnoreCase("[MYT]")) {
                         if (e.getPlayer().hasPermission("mytokens.sign.use")) {
                             e.setCancelled(true);
                             String[] last = s.getLine(3).split(":");
                             int id = Integer.parseInt(last[1].replace(" ", ""));

                             SQLhandler sql = MyTokens.getAPI().getSqlHandler();
                             ConfigurationSection cs = MyTokens.getAPI().getTokenShop().getConfig().getConfigurationSection("Shop." + id);

                             int Current_Balance = sql.GetBalance(e.getPlayer());

                             if (Current_Balance >= cs.getInt("cost") || (e.getPlayer()).hasPermission("mytokens.admin.nopay") || (e.getPlayer()).isOp()) {
                                 for (String cmd : cs.getStringList("commands")) {
                                     if (cmd.indexOf('/') == 0) {
                                         cmd = cmd.substring(1);
                                     }

                                     cmd = cmd.replace("%player", e.getPlayer().getName());
                                     Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', cmd));
                                 }
                                 if (!(e.getPlayer()).hasPermission("mytokens.admin.nopay")) {
                                     if (!(e.getPlayer()).isOp()) {
                                         Current_Balance = Current_Balance - cs.getInt("cost");
                                         sql.SetBalance(e.getPlayer(), Current_Balance);
                                     }
                                 }

                                 String message = MyTokens.getAPI().getConfig().getString("settings.command-messages.thank-you");
                                 e.getPlayer().sendMessage(MyTokens.getAPI().getMessageHelper().format(e.getPlayer(), message, cs.getInt("cost") + ""));
                             }else{
                                 String message = MyTokens.getAPI().getConfig().getString("settings.command-messages.cant-afford");
                                 e.getPlayer().sendMessage(MyTokens.getAPI().getMessageHelper().format((Player) e.getPlayer(), message, cs.getInt("cost") + ""));
                             }
                         }
                     }
                 }

                 return;
             }
         }
		 ConfigurationSection dropitem = MyTokens.getAPI().getConfig().getConfigurationSection("dropitem");
		 if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			 int id = 0;
			 try{
				 id = Integer.valueOf(dropitem.getString("item.id")).intValue();
			 }catch (Exception ex){
				 MyTokens.getAPI().getLogger().warning("Failed to create item with ID of " + id);
				 return;
		     }
			 
			 if(e.getPlayer().getInventory().getItemInHand().getType() == Material.getMaterial(id)){
				 String name = ChatColor.stripColor(e.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName());
				 if(name.contains(dropitem.getString("item.name"))){
					 name = name.replace(dropitem.getString("item.name"), "");
					 String amount = name.replace("[", "").replace("]", "");
					 amount = amount.trim();
					 int got = 0;
					 try{
						 got = Integer.valueOf(amount.trim()).intValue();
					 }catch (Exception ex){
						 MyTokens.getAPI().getLogger().warning("Failed to load token amount");
						 return;
				     }
					 
					 SQLhandler sql = MyTokens.getAPI().getSqlHandler();
					 int tokens = sql.GetBalance(e.getPlayer());
					 tokens = tokens + got;
					 
					 sql.SetBalance(e.getPlayer(), tokens);
					 
					 e.getPlayer().sendMessage(MyTokens.getAPI().getMessageHelper().format(e.getPlayer(), dropitem.getString("used"), amount));
					 int index = e.getPlayer().getInventory().getHeldItemSlot();
					 ItemStack i = e.getPlayer().getInventory().getItemInHand();
					 int count = i.getAmount();
					 if(count >= 1){
						 i.setAmount(count - 1);
						 e.getPlayer().getInventory().setItem(index, i);
					 }else{
						 e.getPlayer().getInventory().setItem(index, null);
					 }
					 e.getPlayer().updateInventory();
				 }
			 }
		 }
	 }
}