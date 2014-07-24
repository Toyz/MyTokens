package com.toyz.MyTokens.BaseCommand.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.BaseCommand.BaseCommand;
import com.toyz.MyTokens.BaseCommand.Handler.IssueCommands;
import com.toyz.MyTokens.Tools.*;
import com.toyz.MyTokens.Utils.MessageHelper;
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
			Inventory invy = new Inventory(_cmd.getPlayer(), MyTokens.Items);
			invy.Open();
			return;
		}
		if(_cmd.getArg(0).equalsIgnoreCase("?") || _cmd.getArg(0).equalsIgnoreCase("help")){
			if(!_cmd.getPlayer().hasPermission(_Permission + ".help")){
				sendMessage(MessageHelper.Format(_cmd.getPlayer(), "&4You do not have permission to use this command"));
				return;
			}
			for(String msg : MyTokens.PublicHelpCommands){
				sendMessage(MessageHelper.Format(null, msg));
			}
		}
		if(_cmd.getArg(0).equalsIgnoreCase("bal")){
			sendMessage(MessageHelper.Format(_cmd.getPlayer(), "Your current Token Balance is &a%total"));
			return;
		}
		if(_cmd.getArg(0).equalsIgnoreCase("give")){
			if(_cmd.getArgs().length >= 3){
				if(!_cmd.getPlayer().hasPermission(_Permission + ".give")){
					sendMessage(MessageHelper.Format(_cmd.getPlayer(), "&4You do not have permission to use this command"));
					return;
				}
				Player Getter = Bukkit.getPlayer(_cmd.getArg(1));
				if(Getter == null){
					sendMessage(MessageHelper.Format(null, "&4User is offline"));
					return;
				}else{
					int GivingTokens = MyTokens.UserTokens.getConfig().getInt(Getter.getUniqueId().toString());
					int TakingTokens = MyTokens.UserTokens.getConfig().getInt(_cmd.getPlayer().getUniqueId().toString());
					
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
					if(TakingTokens < Giving){
						sendMessage(MessageHelper.Format(null, "&4You do not have enough tokens"));
						return;
					}
					
					GivingTokens = GivingTokens + Giving;
					TakingTokens = TakingTokens - Giving;
					
					 MyTokens.UserTokens.getConfig().set(Getter.getUniqueId().toString(), GivingTokens);
					 MyTokens.UserTokens.getConfig().set(_cmd.getPlayer().getUniqueId().toString(), TakingTokens);
					 
					 Getter.sendMessage(MessageHelper.Format(null, "You got " + Giving + " tokens from " + _cmd.getPlayer().getName()));
					 sendMessage(MessageHelper.Format(null, "You sent " + Giving + " tokens to " + Getter.getName()));
				}
			}else{
				sendMessage(MessageHelper.Format(null, "&4" + _invaidUsage + " &f /myt give username amount"));
				return;
			}
		}
	}
}
