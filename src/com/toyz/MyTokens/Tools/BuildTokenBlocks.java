package com.toyz.MyTokens.Tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.toyz.MyTokens.MyTokens;

public class BuildTokenBlocks {
	private static TokenBlock block;

	public static List<TokenBlock> BlocksThatDrop(){
		List<TokenBlock> _blocks = new ArrayList<TokenBlock>();
		
		ConfigurationSection cs = MyTokens.DropConfig.getConfig().getConfigurationSection("Drop.break.blocks");
		
		for (String key : cs.getKeys(false)){
			int id = 0;
			try{
				id = Integer.valueOf(key).intValue();
			}catch (Exception e){
				MyTokens.logger.warning("Failed to block item with ID of " + id);
				continue;
			}
			if(id > 0){
				block = new TokenBlock(Material.getMaterial(id), 
						cs.getDouble(key + ".percent"), 
						cs.getInt(key + ".min"),
						cs.getInt(key + ".max"));
				MyTokens.console.sendMessage(ChatColor.GREEN + "[Loaded]" +ChatColor.WHITE + " Block " + block.getType().toString() +" with drop rate of " + cs.getDouble(key + ".percent"));
				_blocks.add(block);
			}
		}
		
		return _blocks;
	}
}
