package com.toyz.MyTokens.sql;

import java.util.logging.Level;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import com.toyz.MyTokens.MyTokens;

import javax.sql.DataSource;

public class SQLHelper {
    private GenericObjectPool connectionPool = null;
	private MyTokens _plugin;
	private Connection conn;
	private Statement stmt;  
	private ResultSet rs;  
	private PreparedStatement pstmt;  
	    
	public SQLHelper(MyTokens plugin){
		_plugin = plugin;

        connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(10);

        ConfigurationSection cs = _plugin.getConfig().getConfigurationSection("database");
		
		if(cs.getBoolean("sqlite.use") && cs.getBoolean("mysql.use")){
			_plugin.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', _plugin.getConfig().getString("prefix")) + " Bpth MySQL and SQLite can not be enabled!");
			_plugin.onDisable();
		}
		 try {  
	            stmt = getConn().createStatement();  
	            stmt.execute("CREATE TABLE IF NOT EXISTS `" + MyTokens.getAPI().getConfig().getString("database.prefix") + "Balance` (`player` VARCHAR(255) NOT NULL DEFAULT 'NULL', `username` VARCHAR(255) NULL DEFAULT NULL, `bal` INTEGER NULL DEFAULT NULL, PRIMARY KEY (`player`) );"); 
	            stmt.execute("CREATE TABLE IF NOT EXISTS `" + MyTokens.getAPI().getConfig().getString("database.prefix") + "Kills` ( `killer` VARCHAR(255) NULL DEFAULT NULL, `killer_name` VARCHAR(255) NULL DEFAULT NULL, `killed` VARCHAR(255) NULL DEFAULT NULL, `killed_name` VARCHAR(255) NULL DEFAULT NULL, `kcnt` INTEGER NULL DEFAULT NULL, `timeout` INTEGER NULL DEFAULT NULL, PRIMARY KEY (`killer`));");
	            if(cs.getBoolean("sqlite.use")){
	            	stmt.execute("pragma journal_mode=wal");
	            }
		 } catch (SQLException e) {  
	            e.printStackTrace();  
		 } 
	}
	
	public Boolean IsSQLLite(){
		ConfigurationSection cs = _plugin.getConfig().getConfigurationSection("database");
		return cs.getBoolean("sqlite.use");
	}
	
	public Connection getConn(){
		if(conn == null){
				ConfigurationSection cs = _plugin.getConfig().getConfigurationSection("database");
				if(cs.getBoolean("sqlite.use") && !cs.getBoolean("mysql.use")){
					try {
						Class.forName("org.sqlite.JDBC");
						conn = DriverManager.getConnection(
								"jdbc:sqlite:" + _plugin.getDataFolder() + File.separatorChar + 
								((cs.getString("sqlite.file") != null) ? cs.getString("sqlite.file") : "mytokens.db"));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
					}
					
				}
				
				if(!cs.getBoolean("sqlite.use") && cs.getBoolean("mysql.use")){
					/*try {
						Class.forName("com.mysql.jdbc.Driver");
						conn = DriverManager.getConnection("jdbc:mysql://"
								+ ((cs.getString("mysql.host") != null) ? cs.getString("mysql.host") : "localhost")
								+ ":" + ((cs.getString("mysql.port") != null) ? cs.getString("mysql.port") : "3306")
								+ "/" + ((cs.getString("mysql.database") != null) ? cs.getString("mysql.database") : "mytokens"),
								((cs.getString("mysql.user") != null) ? cs.getString("mysql.user") : "root"),
								((cs.getString("mysql.password") != null) ? cs.getString("mysql.password") : ""));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e){
						e.printStackTrace();
					}*/
                    try {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    ConnectionFactory cf = new DriverManagerConnectionFactory(
                            "jdbc:mysql://"
                                    + ((cs.getString("mysql.host") != null) ? cs.getString("mysql.host") : "localhost")
                                    + ":" + ((cs.getString("mysql.port") != null) ? cs.getString("mysql.port") : "3306")
                                    + "/" + ((cs.getString("mysql.database") != null) ? cs.getString("mysql.database") : "mytokens"),
                            ((cs.getString("mysql.user") != null) ? cs.getString("mysql.user") : "root"),
                            ((cs.getString("mysql.password") != null) ? cs.getString("mysql.password") : ""));

                    PoolableConnectionFactory pcf =
                            new PoolableConnectionFactory(cf, connectionPool,
                                    null, null, false, true);

                    PoolingDataSource p = new PoolingDataSource(connectionPool);

                    try {
                        conn = p.getConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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