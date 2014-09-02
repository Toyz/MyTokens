package com.toyz.MyTokens.Tools;

import java.text.DecimalFormat;
import java.util.*;

import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.toyz.MyTokens.MyTokens;

public class BuildTokenBlocks {
	private static TokenBlock block;

	@SuppressWarnings("deprecation")
	public static List<TokenBlock> BlocksThatDrop(){
		List<TokenBlock> _blocks = new ArrayList<TokenBlock>();
		
		ConfigurationSection cs = MyTokens.getAPI().getDropConfig().getConfig().getConfigurationSection("Drop.break.blocks");
		
		for (String key : cs.getKeys(false)){
			int id = 0;
			try{
				id = Integer.valueOf(key).intValue();
			}catch (Exception e){
				MyTokens.getAPI().getLogger().warning("Failed to block item with ID of " + id);
				continue;
			}
			if(id > 0){
				block = new TokenBlock(Material.getMaterial(id), 
						cs.getDouble(key + ".percent"), 
						cs.getInt(key + ".min"),
						cs.getInt(key + ".max"),
						cs.getBoolean(key + ".enabled"));
				MyTokens.getAPI().getConsole().sendMessage(ChatColor.GREEN + "[Loaded]" +ChatColor.WHITE + " Block " + block.getType().toString() +" with drop rate of " + cs.getDouble(key + ".percent"));
				_blocks.add(block);
			}
		}
		
		return _blocks;
	}
	
	public static Hashtable<Integer, ItemStack> BuildBreakAbleList(){
		Hashtable<Integer, ItemStack> items = new Hashtable<Integer, ItemStack>();
		int id = 0;
		for(TokenBlock block : MyTokens.getAPI().getDropBlocks()){
			DecimalFormat df = new DecimalFormat("#%");
			
			List<String> lore = Arrays.asList(
					ChatColor.WHITE + "Max: " + ChatColor.GOLD + block.maxDrop() + "",
					ChatColor.WHITE + "Min: " + ChatColor.GOLD + block.minDrop() + "",
					ChatColor.WHITE + "Percent of Drop: " + ChatColor.GOLD + df.format(block.getChance()),
					ChatColor.WHITE + "Drops Enabled: " + ChatColor.GOLD + (block.enabled() ? "Yes" : "No")
			);
			int BlockID = block.enabled() ? block.getType().getId() : 152;
			items.put(id, Item.CreateItem(BlockID + "", WordUtils.capitalize(block.getType().name().replace("_", " ").toLowerCase()), lore, 1, false));
			id++;
		}
		return items;
	}
}