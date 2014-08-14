package com.toyz.MyTokens.BaseCommand.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.BaseCommand.BaseCommand;
import com.toyz.MyTokens.BaseCommand.Handler.IssueCommands;
import com.toyz.MyTokens.Tools.*;
import com.toyz.MyTokens.Utils.MessageHelper;
import com.toyz.MyTokens.sql.SQLhandler;
public class MyTokensAdmin extends BaseCommand{
	private static IssueCommands _cmd = null;
	private static String _Permission = "mytokens.admin";
	private static int _minArgs = 0;
	private static String _invaidUsage = "Invalid Args - usage:";
	
	public static Boolean Fire(IssueCommands info){
		_cmd = info;
		if(_cmd.isPlayer()){
			if(_cmd.getPlayer().hasPermission(_Permission))
			{
				Trigger();
				return true;
			}else{
				sendMessage("You do not have permission to run this command");
			}
		}
		if(_cmd.isConsole()){
			Trigger();
		}
		return true;
	}
	
	private static void Trigger(){
		if((_cmd.getArgs().length <= 0 || _cmd.getArg(0).equalsIgnoreCase("?") || _cmd.getArg(0).equalsIgnoreCase("help"))){
			for(String msg : MyTokens.AdminHelpCommands){
				sendMessage(MessageHelper.Format(null, msg));
			}
			return;
		}
		if(_cmd.getArg(0).equalsIgnoreCase("reload")){
			if(_cmd.isPlayer()){
				if(!_cmd.getPlayer().hasPermission(_Permission + ".reload")){
					if(!_cmd.getPlayer().isOp()){
						sendMessage("You do not have permission to run this command");
						return;
					}
				}
			}
			sendMessage("Reloading MyTokens Config");
			MyTokens._plugin.Reload();
			sendMessage("MyTokens Config reloaded");
		}
		
		if(_cmd.getArg(0).equalsIgnoreCase("give")){
			if(_cmd.isPlayer()){
				if(!_cmd.getPlayer().hasPermission(_Permission + ".give")){
					if(!_cmd.getPlayer().isOp()){
						sendMessage("You do not have permission to run this command");
						return;
					}
				}
			}
				if(_cmd.getArgs().length >= 3){
					Player givee = Bukkit.getPlayer(_cmd.getArg(1));
					if(givee != null){
						SQLhandler sql = new SQLhandler(MyTokens._plugin);
						int Giving = 0;
						try{
							Giving = Integer.valueOf(_cmd.getArg(2)).intValue();
						}catch (Exception e){
							sendMessage("Must be a number");
							return;
						}
						
						if(Giving <= 0){
							sendMessage(MessageHelper.Format(null, "&4You must send more then 0 tokens!"));
							return;
						}
						
						int Current = sql.GetBalance(givee);
						sql.GetSQL().close();//MyTokens.UserTokens.getConfig().getInt(givee.getUniqueId().toString());
						Current = Giving + Current;
						
						//System.out.println(givee.getUniqueId().toString() + " = " + givee.getName() + " - Giving: " + Giving + " - Total: " + Current + "");
						//MyTokens.UserTokens.getConfig().set(givee.getUniqueId().toString(), Current);
						sql.SetBalance(givee, Current);
						sql.GetSQL().close();
						givee.sendMessage(MessageHelper.Format(null, "You were given %amount tokens!", Giving + ""));
					}else{
						sendMessage(MessageHelper.Format(null, "&4Player is currently offline"));
					}
				}else{
					sendMessage(_invaidUsage + " /mytokens give user amount");
				}
			}
		
		if(_cmd.getArg(0).equalsIgnoreCase("reset")){
			if(_cmd.isPlayer()){
				if(!_cmd.getPlayer().hasPermission(_Permission + ".reset")){
					if(!_cmd.getPlayer().isOp()){
						sendMessage("You do not have permission to run this command");
						return;
					}
				}
			}
				if(_cmd.getArgs().length >= 2){
					SQLhandler sql = new SQLhandler(MyTokens._plugin);
					Player givee = Bukkit.getPlayer(_cmd.getArg(1));
					
					//MyTokens.UserTokens.getConfig().set(givee.getUniqueId().toString(), 0); 
					sql.SetBalance(givee, 0);
					sql.GetSQL().close();
					givee.sendMessage(MessageHelper.Format(null, "Your Tokens have been reset to 0"));
				}
		}
	}
}
