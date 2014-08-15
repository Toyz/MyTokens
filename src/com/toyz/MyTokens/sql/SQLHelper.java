package com.toyz.MyTokens.sql;

import java.util.logging.Level;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.toyz.MyTokens.MyTokens;

public class SQLHelper {
	private MyTokens _plugin;
	private Connection conn;
	private Statement stmt;  
	private ResultSet rs;  
	private PreparedStatement pstmt;  
	    
	public SQLHelper(MyTokens plugin){
		_plugin = plugin;
		
		ConfigurationSection cs = _plugin.getConfig().getConfigurationSection("database");
		
		if(cs.getBoolean("sqlite.use") && cs.getBoolean("mysql.use")){
			_plugin.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', _plugin.getConfig().getString("prefix")) + " Bpth MySQL and SQLite can not be enabled!");
			_plugin.onDisable();
		}
		 try {  
	            stmt = getConn().createStatement();  
	            stmt.execute("CREATE TABLE IF NOT EXISTS `" + MyTokens._plugin.getConfig().getString("database.prefix") + "Balance` (`player` VARCHAR(255) NOT NULL DEFAULT 'NULL', `username` VARCHAR(255) NULL DEFAULT NULL, `bal` INTEGER NULL DEFAULT NULL, PRIMARY KEY (`player`) );"); 
	            stmt.execute("CREATE TABLE IF NOT EXISTS `" + MyTokens._plugin.getConfig().getString("database.prefix") + "Kills` ( `killer` VARCHAR(255) NULL DEFAULT NULL, `killer_name` VARCHAR(255) NULL DEFAULT NULL, `killed` VARCHAR(255) NULL DEFAULT NULL, `killed_name` VARCHAR(255) NULL DEFAULT NULL, `kcnt` INTEGER NULL DEFAULT NULL, `timeout` INTEGER NULL DEFAULT NULL, PRIMARY KEY (`killer`));");
	            if(cs.getBoolean("sqlite.use")){
	            	stmt.execute("pragma journal_mode=wal");
	            }
	            this.close();
		 } catch (SQLException e) {  
	            e.printStackTrace();  
		 } 
	}
	
	public Connection getConn(){
		ConfigurationSection cs = _plugin.getConfig().getConfigurationSection("database");
		
		if(cs.getBoolean("sqlite.use") && !cs.getBoolean("mysql.use")){
			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection(
						"jdbc:sqlite:" + _plugin.getDataFolder() + File.separatorChar + 
						((cs.getString("sqlite.file") != null) ? cs.getString("sqlite.file") : "mytokens.db"));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(!cs.getBoolean("sqlite.use") && cs.getBoolean("mysql.use")){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://" 
						+ ((cs.getString("mysql.host") != null) ? cs.getString("mysql.host") : "localhost")
						+ ":" + ((cs.getString("mysql.port") != null) ? cs.getString("mysql.port") : "3306")
						+ "/" + ((cs.getString("mysql.database") != null) ? cs.getString("mysql.database") : "mytokens"),
						((cs.getString("mysql.user") != null) ? cs.getString("mysql.user") : "root"),
						((cs.getString("mysql.password") != null) ? cs.getString("mysql.password") : ""));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return conn;
	}
	
	public ResultSet executeQuery(String sql) {// SQL?  
        rs = null;  
        try {  
            stmt = getConn().createStatement();  
            rs = stmt.executeQuery(sql);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return rs;  
    }  
  
    public int executeUpdate(String sql) {// SQL  
        int i = 0;  
        pstmt = null;  
        try {  
            pstmt = getConn().prepareStatement(sql);  
            i = pstmt.executeUpdate();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return i;  
  
    }  
  
    public PreparedStatement preparedStatement(String sql) {// SQL  
        pstmt = null;  
        try {  
            pstmt = getConn().prepareStatement(sql);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return pstmt;  
    }  
  
    public void close() {//   
        if (getConn() != null) {  
            try {  
                getConn().close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    public void setAutoCommit(Boolean b) {//   
        if (b) {  
            try {  
                getConn().commit();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        } else {  
            try {  
                getConn().rollback();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    public void commit() {//   
        try {  
            getConn().commit();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        System.out.println("");  
    }  
  
    public void rollback() {//   
        try {  
            getConn().rollback();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        System.out.println("");  
    }  
}
