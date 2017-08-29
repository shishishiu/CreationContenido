package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.db.DbUtil;
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
	/** Usuario **/
	private Usuario usuario;
	/** Fecha del acceso **/
	private String fecLogInicial = "";
	/** Fecha del acceso **/
	private String fecLogTerminal = "";
	/** Fecha de la salida **/
	private String ultLogActInicial = "";
	/** Fecha de la salida **/
	private String ultLogActTerminal = "";
	

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
	public String getFecLogInicial(){
		return fecLogInicial;
	}
	public void setFecLogInicial(String fecLogInicial){
		this.fecLogInicial = fecLogInicial;
	}
	public String getFecLogTerminal(){
		return fecLogTerminal;
	}
	public void setFecLogTerminal(String fecLogTerminal){
		this.fecLogTerminal = fecLogTerminal;
	}
	public String getUltLogAct(){
		return ultLogAct;
	}
	public void setUltLogAct(String ultLogAct){
		this.ultLogAct = ultLogAct;
	}
	public String getUltLogActInicial(){
		return ultLogActInicial;
	}
	public void setUltLogActInicial(String ultLogActInicial){
		this.ultLogActInicial = ultLogActInicial;
	}
	public String getUltLogActTerminal(){
		return ultLogActTerminal;
	}
	public void setUltLogActTerminal(String ultLogActTerminal){
		this.ultLogActTerminal = ultLogActTerminal;
	}
	public int getBanUsu(){
		return banLogAcc;
	}
	public void setBanLogAcc(int banLogAcc){
		this.banLogAcc = banLogAcc;
	}
	public Usuario getUsuario(){
		return this.usuario;
	}
	public void setUsuario(Usuario usuario){
		this.usuario = usuario;
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
			String query = "UPDATE tbllogacc SET UltLogAcs =? WHERE CveUsu =? AND IdSession = ?";
	
			Date fechaLog = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, sdf.format(fechaLog));
			preparedStatement.setString(2, this.cveUsu);
			preparedStatement.setString(3, this.idSession);
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
	
	public List<LogAcc> Buscar() throws Exception{
		List<LogAcc> list = new ArrayList<LogAcc>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "SELECT ac.CveUsu, ac.IpUsu, ac.FecLog, ac.UltLogacs, u.NomUsu, u.NomPatUsu, u.NomMatUsu, cd.NomCode"
					+ " FROM tbllogacc ac LEFT OUTER JOIN tblusuario u ON ac.CveUsu = u.CveUsu AND u.BanUsu = 1"
					+ " LEFT OUTER JOIN mstcode cd ON cd.cvecode ='" + MstCode.CVE_CODE_PERMISO +"' AND code = u.PerUsu AND cd.BanCode = 1"
					+ " WHERE banlogacc = 1 ";
		
			String condicion = CreateCondition(con);
			if(condicion.length() > 0){
				query += condicion.toString();
			}

			query += " ORDER BY FecLog";

			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			SetPreparedStatement(preparedStatement);

			
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				LogAcc bean = new LogAcc();
				bean.cveUsu = rs.getString("CveUsu");
				bean.ipUsu = rs.getString("IpUsu");
				bean.fecLog = rs.getString("FecLog");
				bean.ultLogAct = rs.getString("UltLogacs");
				
				Usuario usu = Usuario.Buscar(bean.getCveUsu());
				bean.usuario = usu;
				
				list.add(bean);
 			}
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}
		
		return list;
	}
	private void SetPreparedStatement(PreparedStatement preparedStatement) throws Exception {
		int count = 0;
		try{	
			if(this.fecLogInicial != null && this.fecLogInicial.length() > 0){
				count++;
				preparedStatement.setString(count, this.fecLogInicial);
			}
			if(this.fecLogTerminal != null && this.fecLogTerminal.length() > 0){
				count++;
				preparedStatement.setString(count, this.fecLogTerminal);
			}
			if(this.ultLogActInicial != null && this.ultLogActInicial.length() > 0){
				count++;
				preparedStatement.setString(count, this.ultLogActInicial);
			}
			if(this.ultLogActTerminal != null && this.ultLogActTerminal.length() > 0){
				count++;
				preparedStatement.setString(count, this.ultLogActTerminal);
			}
			if(this.cveUsu != null && this.cveUsu.length() > 0){
				count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.cveUsu) + "%");
				count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.cveUsu.replaceAll(" ", "").toUpperCase()) + "%");
			}

		}catch(Exception e){
			throw e;
		}
		
	}
	private String CreateCondition(Connection con) {
		StringBuilder condicion = new StringBuilder();
		if(this.fecLogInicial != null && this.fecLogInicial.length() > 0){
			condicion.append(" AND STR_TO_DATE(SUBSTR(FecLog,1,10), '%d/%m/%Y') >= STR_TO_DATE(?,'%d/%m/%Y') ");
		}
		if(this.fecLogTerminal != null && this.fecLogTerminal.length() > 0){
			condicion.append(" AND STR_TO_DATE(SUBSTR(FecLog,1,10), '%d/%m/%Y') <= STR_TO_DATE(?,'%d/%m/%Y') ");
		}
		if(this.ultLogActInicial != null && this.ultLogActInicial.length() > 0){
			condicion.append(" AND STR_TO_DATE(SUBSTR(ultLogAcs,1,10), '%d/%m/%Y') >= STR_TO_DATE(?,'%d/%m/%Y') ");
		}
		if(this.ultLogActTerminal != null && this.ultLogActTerminal.length() > 0){
			condicion.append(" AND STR_TO_DATE(SUBSTR(ultLogAcs,1,10), '%d/%m/%Y') <= STR_TO_DATE(?,'%d/%m/%Y') ");
		}
		if(this.cveUsu != null && this.cveUsu.length() > 0){
			condicion.append(" AND (u.cveUsu LIKE ? OR REPLACE(CONCAT(IFNULL(u.NomUsu, ''), IFNULL(u.NomPatUsu, ''), IFNULL(u.NomMatUsu,'')),' ','') like ?) ");
		}
		return condicion.toString();
	}
	
	
}
