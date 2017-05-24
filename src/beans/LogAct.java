package beans;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogAct {
	/** Clave del usuario **/
	private String cveUsu = "";
	/** Id sesion **/
	private String idSession = "";
	/** Ip del usuario **/
	private String ipUsu = "";
	/** Fecha del acceso **/
	private String fecLog = "";
	/** Actividad realizada en el sistema **/
	private String actLogAct = "";
	/** Bandera del estado **/
	private int banLogAct;
	

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
	public String getActLogAct(){
		return actLogAct;
	}
	public void setActLogAct(String actLogAct){
		this.actLogAct = actLogAct;
	}
	public int getBanUsu(){
		return banLogAct;
	}
	public void setBanLogAct(int banLogAct){
		this.banLogAct = banLogAct;
	}
	
	/**
	 * Ingresar datos de entrada
	 * @throws SQLException
	 */
	public int Insert(Connection con) throws SQLException{
		PreparedStatement preparedStatement = null;
		try {			
			String query = "INSERT INTO tbllogact (CveUsu, IdSession, IpUsu, FecLog, ActLogAct, BanLogAct) VALUES(?, ?, ?, ?, ?, ?)";
			
			preparedStatement = con.prepareStatement(query);
			
			Date fechaLog = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			preparedStatement.setString(1, this.cveUsu);
			preparedStatement.setString(2, this.idSession);
			preparedStatement.setString(3, this.ipUsu);
			preparedStatement.setString(4, sdf.format(fechaLog));
			preparedStatement.setString(5, this.actLogAct);
			preparedStatement.setInt(6, 1);
			return preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			
		}
	}	
}
