package com.toyz.MyTokens.BaseCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.BaseCommand.Handler.IssueCommands;
import com.toyz.MyTokens.BaseCommand.Commands.*;

public class BaseCommand implements CommandExecutor {
	static IssueCommands Info;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Info = new IssueCommands(sender, cmd.getName(), args, cmd);
		
		if(cmd.getName().equalsIgnoreCase("myt")){
			MYT.Fire(Info);
		}
		
		if(cmd.getName().equalsIgnoreCase("mytokens")){
			MyTokensAdmin.Fire(Info);
		}
		return false;
	}
	
	protected static void sendMessage(String Message){
		if(Info.isPlayer()){
			Info.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens._plugin.getConfig().getString("prefix")) + " " + Message);
		}
		if(Info.isConsole()){
			Info.getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', MyTokens._plugin.getConfig().getString("prefix")) + " " + Message);
		}
	}
}
