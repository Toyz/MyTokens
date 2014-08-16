package com.toyz.MyTokens.Utils;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.toyz.MyTokens.MyTokens;
import com.toyz.MyTokens.sql.SQLhandler;

public class MessageHelper {
	private static Hashtable<String, String> _replace = null;
	
	public static String Format(Player player, String msg){
		//Make Hash and build list
		_replace = new Hashtable<String, String>();
		BuildReplaceTable(player);
		
		//Fix string
		Enumeration<String> e = _replace.keys();
	    while (e.hasMoreElements()) {
	    	String key = (String) e.nextElement();
	        msg = msg.replace(key,_replace.get(key));
	    }
	    
	    //Add our color support
	    msg = ChatColor.translateAlternateColorCodes('&', msg);
	    
	    //return our fixed string
		return msg;
	}
	
	private static void BuildReplaceTable(Player _player){
		if(_player != null){
			SQLhandler sql = MyTokens.sql;
			_replace.put("%player", _player.getName());
			_replace.put("%total", sql.GetBalance(_player) + "");
		}
		_replace.put("%amount", "0");
	}
	
	public static String Format(Player player, String msg, String Dropped){
		//Make Hash and build list
		_replace = new Hashtable<String, String>();
		BuildReplaceTable(player, Dropped);
		
		//Fix string
		Enumeration<String> e = _replace.keys();
	    while (e.hasMoreElements()) {
	    	String key = (String) e.nextElement();
	        msg = msg.replace(key,_replace.get(key));
	    }
	    
	    //Add our color support
	    msg = ChatColor.translateAlternateColorCodes('&', msg);
	    
	    //return our fixed string
		return msg;
	}
	
	private static void BuildReplaceTable(Player _player, String dropped){
		if(_player != null){
			SQLhandler sql = MyTokens.sql;
			_replace.put("%player", _player.getName());
			_replace.put("%total", sql.GetBalance(_player) + "");
		}
		_replace.put("%amount", dropped);
	}
}
