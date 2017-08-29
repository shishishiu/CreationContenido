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

public class LogAct {
	/** Clave del usuario **/
	private String cveUsu = "";
	/** Id sesion **/
	private String idSession = "";
	/** Ip del usuario **/
	private String ipUsu = "";
	/** Fecha del acceso **/
	private String fecLog = "";
	/** Fecha del acceso **/
	private String fecLogInicial = "";
	/** Fecha del acceso **/
	private String fecLogTerminal = "";
	/** Actividad realizada en el sistema **/
	private String actLogAct = "";
	/** Bandera del estado **/
	private int banLogAct;
	/** Usuario **/
	private Usuario usuario;
	

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
	
	private String queryCommon = " FROM tbllogact act LEFT OUTER JOIN tblusuario u ON act.CveUsu = u.CveUsu AND u.BanUsu = 1"
			+ " LEFT OUTER JOIN mstcode cd ON cd.cvecode ='" + MstCode.CVE_CODE_PERMISO +"' AND code = u.PerUsu AND cd.BanCode = 1"
			+ " WHERE banlogact = 1 ";


	
	public int Count() throws Exception{
		
		int count = 0;
		
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			
			String query = "SELECT count(*) cnt"
					+ this.queryCommon;

			String condicion = CreateCondition(con);
			if(condicion.length() > 0){
				query += condicion.toString();
			}

			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			SetPreparedStatement(preparedStatement);

			
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				count = rs.getInt("cnt");
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
		
		
		return count;
		
	}
	
	public List<LogAct> Buscar() throws Exception{
		List<LogAct> list = new ArrayList<LogAct>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
		
			String query = "SELECT act.CveUsu, act.IpUsu, act.FecLog, act.ActLogact, u.NomUsu, u.NomPatUsu, u.NomMatUsu, cd.NomCode"
					+ this.queryCommon;

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
				LogAct bean = new LogAct();
				bean.cveUsu = rs.getString("CveUsu");
				bean.ipUsu = rs.getString("IpUsu");
				bean.fecLog = rs.getString("FecLog");
				bean.actLogAct = rs.getString("actLogAct");
				
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
			if(this.cveUsu != null && this.cveUsu.length() > 0){
				count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.cveUsu) + "%");
				count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.cveUsu.replaceAll(" ", "")) + "%");
			}
			if(this.actLogAct != null && this.actLogAct.length() > 0){
				count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.actLogAct.replaceAll(" ", "")) + "%");
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
		if(this.cveUsu != null && this.cveUsu.length() > 0){
			condicion.append(" AND (u.cveUsu LIKE ? OR REPLACE(CONCAT(IFNULL(u.NomUsu, ''), IFNULL(u.NomPatUsu, ''), IFNULL(u.NomMatUsu,'')),' ','') like ?) ");
		}
		if(this.actLogAct != null && this.actLogAct.length() > 0){
			condicion.append(" AND actLogAct LIKE ? ");
		}

		return condicion.toString();
	}

	
}
