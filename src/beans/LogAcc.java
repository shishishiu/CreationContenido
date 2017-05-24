package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.db.MySqlConnector;

public class LogAcc {
	/** Clave del usuario **/
	private String cveUsu = "";
	/** Id sesion **/
	private String idSession = "";
	/** Ip del usuario **/
	private String ipUsu = "";
	/** Fecha del acceso **/
	private String fecLog = "";
	/** Fecha de la salida **/
	private String ultLogAct = "";
	/** Bandera del estado **/
	private int banLogAcc;
	

	public String getCveUsu(){
		return cveUsu;
	}
	public void setCveUsu(String cveUsu){
		this.cveUsu = cveUsu;
	}
	public String getIdSession(){
		return idSession;
	}
	public void setIdSession(String idSession){
		this.idSession = idSession;
	}
	public String getIpUsu(){
		return ipUsu;
	}
	public void setIpUsu(String ipUsu){
		this.ipUsu = ipUsu;
	}
	public String getFecLog(){
		return fecLog;
	}
	public void setFecLog(String fecLog){
		this.fecLog = fecLog;
	}
	public String getUltLogAct(){
		return ultLogAct;
	}
	public void setUltLogAct(String ultLogAct){
		this.ultLogAct = ultLogAct;
	}
	public int getBanUsu(){
		return banLogAcc;
	}
	public void setBanLogAcc(int banLogAcc){
		this.banLogAcc = banLogAcc;
	}
	

	/**
	 * Ingresar datos de entrada
	 * @throws SQLException
	 */
	public void CrearEntrada() throws SQLException{
		Connection con = MySqlConnector.getConnection();
		con.setAutoCommit(false);
		try{
			CrearEntrada(con);
			con.commit();
		}catch(Exception e){
			if (con != null) {
				con.rollback();
			}			
		}finally{
			if (con != null) {
				con.setAutoCommit(true);
	        }
		}
	}
	
	/**
	 * Ingresar datos de entrada
	 * @param con
	 * @throws SQLException
	 */
	public void CrearEntrada(Connection con) throws SQLException{
		PreparedStatement preparedStatement = null;
		try {			
			String query = "UPDATE tbllogacc SET BanLogAcc = 0 WHERE CveUsu = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.cveUsu);
			preparedStatement.executeUpdate();
			
			
			query = "INSERT INTO tbllogacc (CveUsu, IdSession, IpUsu, FecLog, BanLogAcc) VALUES(?, ?, ?, ?, ?)";
			Date fechaLog = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	        preparedStatement.close();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.cveUsu);
			preparedStatement.setString(2, this.idSession);
			preparedStatement.setString(3, this.ipUsu);
			preparedStatement.setString(4, sdf.format(fechaLog));
			preparedStatement.setInt(5, 1);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
	}	
	
	/**
	 * 
	 * @throws SQLException
	 */
	public void CrearSalida() throws SQLException{
		Connection con = MySqlConnector.getConnection();
		try{
			con.setAutoCommit(false);
			CrearSalida(con);
			con.commit();
		}catch(Exception e){
			if (con != null) {
				con.rollback();
			}			
		}finally{
			if (con != null) {
				con.setAutoCommit(true);
	        }
		}
	}
	/**
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void CrearSalida(Connection con) throws SQLException{
		PreparedStatement preparedStatement = null;
		try {			
			String query = "UPDATE tbllogacc SET UltLogAcs = CURRENT_TIMESTAMP WHERE CveUsu =? AND IdSession = ?";
	
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.cveUsu);
			preparedStatement.setString(2, this.idSession);
			preparedStatement.executeUpdate();
	        con.commit();
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}
	}
}
