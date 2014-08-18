package com.toyz.MyTokens;

import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.toyz.MyTokens.Tools.TokenBlock;
import com.toyz.MyTokens.Utils.ConfigAccessor;
import com.toyz.MyTokens.Utils.MathHelper;
import com.toyz.MyTokens.Utils.MessageHelper;
import com.toyz.MyTokens.sql.SQLhandler;

public class API {
	private static MyTokens plugin;
	private static MathHelper mathHelper = new MathHelper();
	private static MessageHelper messageHelper = new MessageHelper();
	
	public API(MyTokens plugin) {
		
		if (API.plugin != null)
			throw new AssertionError("The API has already been initialized.");
		
		API.plugin = plugin;
	}
	
	/**
	 * Gets the balance of a player
	 * @param player player to get the balance from
	 * @return the player's balance
	 */
	public int getBalance(Player player){		
		int bal = plugin.getSqlHandler().GetBalance(player);
		return bal;
	}
	
	/**
	 * Sets a player's balance
	 * @param player player to set the balance of
	 * @param balance the amount to set the balance to
	 */
	public void setBalance(Player player, int balance){
		getSqlHandler().SetBalance(player, balance);
	}
	
	/**
	 * Sets the amount of kills
	 * @param killer the player who killed
	 * @param killed the player who died
	 * @param times the amount of times the player killed
	 * @param timeout the timeout of the kill (to prevent spam)
	 */
	public void setKills(Player killer, Player killed, int times, int timeout){
		timeout = timeout | 30;
		getSqlHandler().SetKillCount(killer, killed, times, timeout);
	}
	
	/**
	 * Gets the amount of timees a player was killed
	 * @param player player to get the amount of deaths from
	 * @return the player's death count
	 */
	public int getTimesKilled(Player killer, Player killed){
		int times = getSqlHandler().GetKillCount(killer, killed);
		return times;
	}

	/**
	 * Gets the logger
	 * @return logger
	 */
	public Logger getLogger() {
		return plugin.getLogger();
	}

	/**
	 * Gets the configuration file for the token shop
	 * @return token shop config file
	 */
	public ConfigAccessor getTokenShop() {
		return plugin.getTokenShop();
	}

	/**
	 * Gets the drop configuration file
	 * @return drop config file
	 */
	public ConfigAccessor getDropConfig() {
		return plugin.getDropConfig();
	}

	/**
	 * Gets the plugin's default configuration file
	 * @return config
	 */
	public FileConfiguration getConfig() {
		return plugin.getConfig();
	}
	
	/**
	 * Gets the list of items
	 * @return item list
	 */
	public Hashtable<Integer, ItemStack> getItems() {
		return plugin.getItems();
	}

	/**
	 * Gets the list of items that can be broken
	 * @return breakable item list
	 */
	public Hashtable<Integer, ItemStack> getBreakableItems() {
		return plugin.getBreakableItems();
	}

	/**
	 * Sets the breakable items
	 * @param breakableItems list of items that can be broken
	 */
	public void setBreakableItems(Hashtable<Integer, ItemStack> breakableItems) {
		plugin.setBreakableItems(breakableItems);
	}
	
	/**
	 * Gets the list of TokenBlock (registered with MyTokens)
	 * @return TokenBlock list
	 */
	public List<TokenBlock> getDropBlocks() {
		return plugin.getDropBlocks();
	}

	/**
	 * Sets the blocks that drop tokens
	 * @param dropBlocks list of blocks that drop items
	 */
	public void setDropBlocks(List<TokenBlock> dropBlocks) {
		plugin.setDropBlocks(dropBlocks);
	}
	
	/**
	 * Gets the console
	 * @return console
	 */
	public ConsoleCommandSender getConsole() {
		return plugin.getConsole();
	}

	/**
	 * Gets the class for handling SQL tasks
	 * @return SQL handler
	 */
	public SQLhandler getSqlHandler() {
		return plugin.getSqlHandler();
	}
	
	/**
	 * Reloads the files
	 */
	public void reload() {
		plugin.reload();
	}
	
	/**
	 * Gets the MathHelper class
	 * @return MathHelper
	 */
	public MathHelper getMathHelper() {
		return mathHelper;
	}
	
	/**
	 * Gets the MessageHelper class
	 * @return MessageHelper
	 */
	public MessageHelper getMessageHelper() {
		return messageHelper;
	}
}