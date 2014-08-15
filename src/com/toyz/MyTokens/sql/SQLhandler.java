package com.toyz.MyTokens.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.toyz.MyTokens.MyTokens;

public class SQLhandler {
	private MyTokens _plugin;
	private SQLHelper _sql;
	public SQLhandler(MyTokens plugin){
		_plugin = plugin;
		
		//_sql = new SQLHelper(_plugin);
	}
	
	public SQLHelper GetSQL(){
		if(_sql == null)
			_sql = new SQLHelper(_plugin);
			
		return _sql;
	}
	
	//get/set balance
	public int GetBalance(Player player){
		_sql = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT bal from " + MyTokens._plugin.getConfig().getString("database.prefix") + "Balance where player = ?";
			PreparedStatement statement = GetSQL().preparedStatement(sql);
			statement.setString(1, player.getUniqueId().toString());
			rs = statement.executeQuery();

			if(rs.next()){
				return rs.getInt("bal");
			}
			return 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void SetBalance(Player player, int balance){
		_sql = null;
		String sql = "REPLACE INTO " + MyTokens._plugin.getConfig().getString("database.prefix") + "Balance (player, bal) VALUES(?, ?)";
		try{
			PreparedStatement statement = GetSQL().preparedStatement(sql);
			statement.setString(1, player.getUniqueId().toString());
			statement.setInt(2, balance);
			statement.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//get/set kills
	public int GetKillCount(Player killer, Player killed){
		_sql = null;
		ResultSet rs = null;
		String sql = "SELECT kcnt from " + MyTokens._plugin.getConfig().getString("database.prefix") + "Kills where killer = ? and killed = ? limit 1";
		try{
			PreparedStatement statement = GetSQL().preparedStatement(sql);
			statement.setString(1, killer.getUniqueId().toString());
			statement.setString(2, killed.getUniqueId().toString());
			rs = statement.executeQuery();
			
			if(rs.next()){
				return rs.getInt("kcnt");
			}
			return 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void SetKillCount(Player killer, Player killed, int count, int timeout){
		_sql = null;
		String sql = "REPLACE INTO " + MyTokens._plugin.getConfig().getString("database.prefix") + "Kills (killer, killed, kcnt, timeout) VALUES(?, ?, ?, ?)";
		try{
			PreparedStatement statement = GetSQL().preparedStatement(sql);
			statement.setString(1, killer.getUniqueId().toString());
			statement.setString(2, killed.getUniqueId().toString());
			statement.setInt(3, count);
			statement.setInt(4, timeout);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int GetTimeOut(Player killer, Player killed){
		_sql = null;
		ResultSet rs = null;
		String sql = "SELECT kcnt from " + MyTokens._plugin.getConfig().getString("database.prefix") + "Kills where killer = ? and killed = ? limit 1";
		try{
			PreparedStatement statement = GetSQL().preparedStatement(sql);
			statement.setString(1, killer.getUniqueId().toString());
			statement.setString(2, killed.getUniqueId().toString());
			rs = statement.executeQuery();
			
			if(rs.next()){
				return rs.getInt("timeout");
			}
			return 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
