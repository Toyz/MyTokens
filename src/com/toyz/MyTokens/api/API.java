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
		SQLhandler sql = MyTokens.sql;
		
		int bal = sql.GetBalance(player);
		return bal;
	}
	
	public static void SetBalance(Player player, int balance){
		SQLhandler sql = MyTokens.sql;
		
		sql.SetBalance(player, balance);
	}
	
	public static void AddKill(Player killer, Player killed, int times, int timeout){
		timeout = timeout | 30;
		SQLhandler sql = MyTokens.sql;
		
		sql.SetKillCount(killer, killed, times, timeout);
	}
	
	public static int GetTimesKilled(Player killer, Player killed){
		SQLhandler sql = MyTokens.sql;
		
		int times = sql.GetKillCount(killer, killed);
		return times;
	}
}
