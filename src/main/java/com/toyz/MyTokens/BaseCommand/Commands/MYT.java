package com.toyz.MyTokens.BaseCommand.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.BaseCommand.BaseCommand;
import com.toyz.MyTokens.BaseCommand.Handler.IssueCommands;
import com.toyz.MyTokens.Tools.*;
import com.toyz.MyTokens.sql.SQLhandler;

public class MYT extends BaseCommand{
	private static IssueCommands _cmd = null;
	private static String _Permission = "mytokens.myt";
	private static int _minArgs = 0;
	private static String _invaidUsage = "Invalid Args - usage:";
	
	public static Boolean Fire(IssueCommands info){
		_cmd = info;
		
		if(_cmd.isPlayer()){
			Tigger();
		}
		return true;
	}
	
	private static void Tigger(){
		if(_cmd.getArgs().length <= _minArgs){
			if(!_cmd.getPlayer().hasPermission(_Permission + ".open")){
				if(!_cmd.getPlayer().isOp()){
					sendMessage(MyTokens.getAPI().getMessageHelper().format(_cmd.getPlayer(), "&4You do not have permission to use this command"));
					return;
				}
			}
			Inventory invy = new Inventory(_cmd.getPlayer(),
					MyTokens.getAPI().getItems(), true, ChatColor.translateAlternateColorCodes('&', MyTokens.getAPI().getConfig().getString("title"))
					, true);
			invy.Open();
			return;
		}
		if(_cmd.getArg(0).equalsIgnoreCase("?") || _cmd.getArg(0).equalsIgnoreCase("help")){
			if(!_cmd.getPlayer().hasPermission(_Permission + ".help")){
				if(!_cmd.getPlayer().isOp()){
					sendMessage(MyTokens.getAPI().getMessageHelper().format(_cmd.getPlayer(), "&4You do not have permission to use this command"));
					return;
				}
			}
			for(String msg : MyTokens.HELP_COMMANDS_PUBLIC){
				sendMessage(MyTokens.getAPI().getMessageHelper().format(null, msg));
			}
		}
		if(_cmd.getArg(0).equalsIgnoreCase("breakable")){
			if(!_cmd.getPlayer().hasPermission(_Permission + ".breakable")){
				if(!_cmd.getPlayer().isOp()){
					sendMessage(MyTokens.getAPI().getMessageHelper().format(_cmd.getPlayer(), "&4You do not have permission to use this command"));
					return;
				}
			}
			Inventory invy = new Inventory(_cmd.getPlayer(), MyTokens.getAPI().getBreakableItems(), false, "Breakable Blocks", false);
			if(!_cmd.getPlayer().hasPermission("mytokens.admin.enableblocks"))
				if(!_cmd.getPlayer().isOp())
					invy.getInventory().setItem(53, null);
			invy.Open();
			return;
		}
		if(_cmd.getArg(0).equalsIgnoreCase("bal")){
			if(!_cmd.getPlayer().hasPermission(_Permission + ".bal")){
				if(!_cmd.getPlayer().isOp()){
					sendMessage(MyTokens.getAPI().getMessageHelper().format(_cmd.getPlayer(), "&4You do not have permission to use this command"));
					return;
				}
			}
			sendMessage(MyTokens.getAPI().getMessageHelper().format(_cmd.getPlayer(), "Your current Token Balance is &a%total"));
			return;
		}
		if(_cmd.getArg(0).equalsIgnoreCase("give")){
			if(!_cmd.getPlayer().hasPermission(_Permission + ".give")){
				if(!_cmd.getPlayer().isOp()){
					sendMessage(MyTokens.getAPI().getMessageHelper().format(_cmd.getPlayer(), "&4You do not have permission to use this command"));
					return;
				}
			}
			if(_cmd.getArgs().length >= 3){
				Player Getter = Bukkit.getPlayer(_cmd.getArg(1));
				if(Getter == null){
					sendMessage(MyTokens.getAPI().getMessageHelper().format(null, "&4User is offline"));
					return;
				}else{
					SQLhandler sql = MyTokens.getAPI().getSqlHandler();
					int GivingTokens = sql.GetBalance(Getter);//MyTokens.UserTokens.getConfig().getInt(Getter.getUniqueId().toString());
					int TakingTokens = sql.GetBalance(_cmd.getPlayer());//MyTokens.UserTokens.getConfig().getInt(_cmd.getPlayer().getUniqueId().toString());
					
					int Giving = 0;
					try{
						Giving = Integer.valueOf(_cmd.getArg(2)).intValue();
					}catch (Exception e){
						sendMessage("Must be a number");
						return;
					}
					
					if(Giving <= 0){
						sendMessage(MyTokens.getAPI().getMessageHelper().format(null, "&4You must send more then 0 tokens!"));
						return;
					}
					if(TakingTokens < Giving){
						sendMessage(MyTokens.getAPI().getMessageHelper().format(null, "&4You do not have enough tokens"));
						return;
					}
					
					GivingTokens = GivingTokens + Giving;
					TakingTokens = TakingTokens - Giving;
					
					sql.SetBalance(Getter, GivingTokens);
					sql.SetBalance(_cmd.getPlayer(), TakingTokens);
					 //MyTokens.UserTokens.getConfig().set(Getter.getUniqueId().toString(), GivingTokens);
					 //MyTokens.UserTokens.getConfig().set(_cmd.getPlayer().getUniqueId().toString(), TakingTokens);
					 
					 Getter.sendMessage(MyTokens.getAPI().getMessageHelper().format(null, "You got " + Giving + " tokens from " + _cmd.getPlayer().getName()));
					 sendMessage(MyTokens.getAPI().getMessageHelper().format(null, "You sent " + Giving + " tokens to " + Getter.getName()));
				}
			}else{
				sendMessage(MyTokens.getAPI().getMessageHelper().format(null, "&4" + _invaidUsage + " &f /myt give username amount"));
				return;
			}
		}
	}
}