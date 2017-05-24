package util.db;

import java.sql.*;
import java.util.Properties;

import util.conf.Configuracion;

public class MySqlConnector {

	/**
	 * Conect MySQL
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
	    Properties connectionProps = new Properties();
	    Configuracion config = new Configuracion();
	    connectionProps.put("user", config.getDbUserName());
	    connectionProps.put("password", config.getDbPassword());
	    
	    try{
	    	Class.forName(config.getDbDriverName());
	    }catch(Exception e){
	    	
	    }
	    
	    try {
			conn = DriverManager.getConnection(
					"jdbc:" + config.getDbDbms() + 
					"://" + config.getDbServerName() + 
					"/" + config.getDbBaseDeDatos(), connectionProps);
		} catch (SQLException e) {
			throw e;
		}
        
        return conn;
    }	

	public static Connection getConnectionBac() throws SQLException {
		Connection conn = null;
	    Configuracion config = new Configuracion();
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", config.getDbBacUserName());
	    connectionProps.put("password", config.getDbBacPassword());
	    
	    try{
	    	Class.forName(config.getDbBacDriverName());
	    }catch(Exception e){
	    	
	    }
	    
	    try {
			conn = DriverManager.getConnection(
					"jdbc:" + config.getDbBacDbms() + 
					"://" + config.getDbBacServerName() + 
					"/" + config.getDbBacBaseDeDatos(), connectionProps);
		} catch (SQLException e) {
			throw e;
		}
        
        return conn;
    }	

	public static Connection getConnectionLic() throws SQLException {
		Connection conn = null;
	    Configuracion config = new Configuracion();
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", config.getDbLicUserName());
	    connectionProps.put("password", config.getDbLicPassword());
	    
	    try{
	    	Class.forName(config.getDbLicDriverName());
	    }catch(Exception e){
	    	
	    }
	    
	    try {
			conn = DriverManager.getConnection(
					"jdbc:" + config.getDbLicDbms() + 
					"://" + config.getDbLicServerName() + 
					"/" + config.getDbLicBaseDeDatos(), connectionProps);
		} catch (SQLException e) {
			throw e;
		}
        
        return conn;
    }	

}