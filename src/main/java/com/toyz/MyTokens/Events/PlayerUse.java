package com.toyz.MyTokens.Events;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.block.Sign;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.sql.SQLhandler;

public class PlayerUse implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void on(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
				Sign s = (Sign) e.getClickedBlock().getState();

				if (s.getLines().length >= 4) {
					if (s.getLine(0).equalsIgnoreCase(MyTokens.getAPI().getConfig().getString("settings.sign-shop.title"))) {
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
							} else {
								String message = MyTokens.getAPI().getConfig().getString("settings.command-messages.cant-afford");
								e.getPlayer().sendMessage(MyTokens.getAPI().getMessageHelper().format(e.getPlayer(), message, cs.getInt("cost") + ""));
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
			
			ItemStack itemStack = e.getPlayer().getInventory().getItemInHand();
			if (itemStack.getType() == Material.getMaterial(id) && itemStack.getAmount() > 0) {
				ItemMeta itemMeta = itemStack.getItemMeta();
				List<String> itemLore = itemMeta.getLore();
				
				// get configuration key FIXME: compile the regex only on configuration reload
				List<String> configLore = dropitem.getStringList("item.lore");
				String configKey = null;
				for(String loreLine : configLore) {
					if (loreLine.contains("%amount")) {
						configKey = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', loreLine));
						break;
					}
				}
				if (configKey == null) {
					String message = MyTokens.getAPI().getConfig().getString("settings.command-messages.missingKey", "Invalid plugin configuration: %amount needs to be defined in the lore");
					e.getPlayer().sendMessage(MyTokens.getAPI().getMessageHelper().format(e.getPlayer(), message));
					return;
				}
				String regex = Pattern.quote(configKey).replace("%amount", "\\E(\\d+)\\Q");
				// MyTokens.getAPI().getLogger().warning("regex is '" + regex + "'");
				Pattern p = Pattern.compile(regex);
				
				// extract tokens amount from the current item
				int got = 0;
				try {
					for(String loreLine : itemLore) {
						Matcher m = p.matcher(ChatColor.stripColor(loreLine));
						if (m.matches() && m.groupCount() >= 1) {
							got = Integer.valueOf(m.group(1)).intValue();
							break;
						}
					}
				} catch (Exception ex) {
					MyTokens.getAPI().getLogger().warning("Failed to extract token amount from item");
					ex.printStackTrace();
					return;
				}
				
				if (got > 0) {
					SQLhandler sql = MyTokens.getAPI().getSqlHandler();
					int tokens = sql.GetBalance(e.getPlayer());
					tokens = tokens + got;
					
					sql.SetBalance(e.getPlayer(), tokens);
					
					e.getPlayer().sendMessage(MyTokens.getAPI().getMessageHelper().format(e.getPlayer(), dropitem.getString("used"), got + ""));
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