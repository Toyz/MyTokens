package com.toyz.MyTokens.api;

import org.bukkit.entity.Player;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.sql.SQLhandler;

public class API {
	private static MyTokens _plugin;
	
	public static MyTokens Plugin(){
		return _plugin;
	}
	
	public static int GetBalance(Player player){
		SQLhandler sql = new SQLhandler(_plugin);
		
		int bal = sql.GetBalance(player);
		sql.GetSQL().close();
		return bal;
	}
	
	public static void SetBalance(Player player, int balance){
		SQLhandler sql = new SQLhandler(_plugin);
		
		sql.SetBalance(player, balance);
		sql.GetSQL().close();
	}
	
	public static void AddKill(Player killer, Player killed, int times, int timeout){
		timeout = timeout | 30;
		SQLhandler sql = new SQLhandler(_plugin);
		
		sql.SetKillCount(killer, killed, times, timeout);
	}
	
	public static int GetTimesKilled(Player killer, Player killed){
		SQLhandler sql = new SQLhandler(_plugin);
		
		int times = sql.GetKillCount(killer, killed);
		sql.GetSQL().close();
		return times;
	}
}
