package com.toyz.MyTokens;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.*;

import com.toyz.MyTokens.BaseCommand.BaseCommand;
import com.toyz.MyTokens.Events.*;
import com.toyz.MyTokens.Tools.*;
import com.toyz.MyTokens.Utils.ConfigAccessor;
import com.toyz.MyTokens.Utils.MetricsLite;
import com.toyz.MyTokens.sql.SQLhandler;

public class MyTokens extends JavaPlugin {
	private static API api;
	private ConfigAccessor tokenShop, dropConfig;
	private Hashtable<Integer, ItemStack> items, breakableItems;
	private List<TokenBlock> dropBlocks;
	private ConsoleCommandSender console;
	private SQLhandler sqlHandler;
	
	public static final List<String> HELP_COMMANDS_ADMIN = Arrays.asList(
			"&b/MyTokens reload &f- Reloads the plugin", 
			"&b/MyTokens give username amount &f- Gives amount of tokens to a user",
			"&b/MyTokens take username amount &f- Takes said amount of tokens from user",
			"&b/MyTokens bal user &f- View a users token balance",
			"&b/MyTokens reset username &f- Resets users tokens to 0");
	
	public static final List<String> HELP_COMMANDS_PUBLIC = Arrays.asList(
			"&b/myt &f- Opens Up the Tokens Shop", 
			"&b/myt breakable&f- Opens Up the window listing all blocks that drop tokens",
			"&b/myt give username amount &f- Gives said user a amount of tokens",
			"&b/myt bal &f- Shows your current token balance");
	
	//Enable Plugin
	@Override
	public void onEnable() {
		console = Bukkit.getServer().getConsoleSender();
		api = new API(this);
        try {
		        MetricsLite metrics = new MetricsLite(this);
		        metrics.start();
		    } catch (IOException e) {
		        // Failed to submit the stats :-(
		    }
		 
		//Load some Configs!
		tokenShop = new ConfigAccessor(this, "shop.yml");
		dropConfig = new ConfigAccessor(this, "dropsettings.yml");
		
		//Save Defaults if needed
		tokenShop.saveDefaultConfig();
		dropConfig.saveDefaultConfig();
		saveDefaultConfig();
		
		//Update configs if needed
		checkConfig();
		
		//Build our list of items!
		Item I = new Item();
		items = I.BuildItems();
		
		//Build are block list
		dropBlocks = BuildTokenBlocks.BlocksThatDrop(); 
		breakableItems = BuildTokenBlocks.BuildBreakAbleList();
		
		//Load Our Commands
		getCommand("myt").setExecutor(new BaseCommand());
		getCommand("mytokens").setExecutor(new BaseCommand());
		
		//Register the listeners
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new InventoryClick(), this);
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new PlayerUse(), this);
		pm.registerEvents(new EntityDeath(), this);
		pm.registerEvents(new PlayerJoin(), this);
        //pm.registerEvents(new SignChange(), this);

		sqlHandler = new SQLhandler(this);
		sqlHandler.GetSQL().getConn();
	}
	
	public void checkConfig(){
		if (this.getConfig().getString("prefix") == null) {
			this.getConfig().set("prefix", "&a[&bMyTokens&a]&f");
			this.saveConfig();
			this.reloadConfig();
		}
		
		if (this.getConfig().getString("settings.pvp.killthrottle") == null && this.getConfig().getString("settings.pvp.enablethrottle") == null) {
			this.getConfig().set("settings.pvp.killthrottle", 3);
			this.getConfig().set("settings.pvp.enablethrottle", true);
			this.saveConfig();
			this.reloadConfig();
		}
	}
	
	//Disable Plugin
	@Override
	public void onDisable() {
		//TokenShop.saveConfig();
		
		tokenShop = null;
		dropConfig = null;
	}
	
	public void reload(){
		//TokenShop.saveConfig();
		
		tokenShop = null;
		dropConfig = null;
		
		//Load some Configs!
		tokenShop = new ConfigAccessor(this, "shop.yml");
		dropConfig = new ConfigAccessor(this, "dropsettings.yml");
		reloadConfig();
		
		//Save Defaults if needed
		tokenShop.saveDefaultConfig();
		dropConfig.saveDefaultConfig();
		saveDefaultConfig();
				
		//Build our list of items!
		Item I = new Item();
		items = I.BuildItems();
				
		//Build are block list
		dropBlocks = BuildTokenBlocks.BlocksThatDrop(); 
		breakableItems = BuildTokenBlocks.BuildBreakAbleList();
	}

	public ConfigAccessor getTokenShop() {
		return tokenShop;
	}

	public ConfigAccessor getDropConfig() {
		return dropConfig;
	}

	public Hashtable<Integer, ItemStack> getItems() {
		return items;
	}

	public Hashtable<Integer, ItemStack> getBreakableItems() {
		return breakableItems;
	}

	public void setBreakableItems(Hashtable<Integer, ItemStack> breakableItems) {
		this.breakableItems = breakableItems;
	}
	
	public List<TokenBlock> getDropBlocks() {
		return dropBlocks;
	}

	public void setDropBlocks(List<TokenBlock> dropBlocks) {
		this.dropBlocks = dropBlocks;
	}
	
	public ConsoleCommandSender getConsole() {
		return console;
	}

	public SQLhandler getSqlHandler() {
		return sqlHandler;
	}
	
	/**
	 * Gets the MyTokens API
	 * @return api
	 */
	public static API getAPI() {
		return api;
	}
}