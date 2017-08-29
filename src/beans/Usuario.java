package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.common.Common;
import util.common.Crypto;
import util.db.DbUtil;
import util.db.MySqlConnector;

public class Usuario {

	public Usuario(){
		
	}
	
	/**
	 * Constractor. Revisar el usuarioy
	 * @param request
	 * @param response
	 */
	public Usuario(HttpServletRequest request, HttpServletResponse response){
		UsuarioInicializar(request, response, false);
	}

	public Usuario(HttpServletRequest request, HttpServletResponse response, boolean flgChild){
		UsuarioInicializar(request, response, flgChild);
	}

	
	/** Clave del usuario **/
	private String cveUsu = "";
	/** Nombre del usuario **/
	private String nomUsu = "";
	/** Nombre del padrino **/
	private String nomPatUsu = "";
	/** Nombre de la madrino **/
	private String nomMatUsu = "";
	/** Nombre del permiso del usuario **/
	private String perUsuNombre;
	/** Permiso del usuario **/
	private int perUsu;
	/** Password del usuario **/
	private String pwsUsu = "";
	/** Bandera del estado **/
	private int banUsu;
	private String nomCompletoUsu;
	
	public String getCveUsu(){
		return cveUsu;
	}
	public void setCveUsu(String cveUsu){
		this.cveUsu = cveUsu;
	}

	public String getNomCompletoUsu(){
		nomCompletoUsu = nomUsu + " " + nomPatUsu + " " + nomMatUsu; 
		return nomCompletoUsu;
	}
	
	public String getNomUsu(){
		return nomUsu;
	}
	public void setNomUsu(String nomUsu){
		this.nomUsu = nomUsu;
	}
	public String getNomPatUsu(){
		return nomPatUsu;
	}
	public void setNomPatUsu(String nomPatUsu){
		this.nomPatUsu = nomPatUsu;
	}
	public String getNomMatUsu(){
		return nomMatUsu;
	}
	public void setNomMatUsu(String nomMatUsu){
		this.nomMatUsu = nomMatUsu;
	}
	public int getPerUsu(){
		return perUsu;
	}
	public void setPerUsu(int perUsu){
		this.perUsu = perUsu;
	}
	public String getPwsUsu(){
		return pwsUsu;
	}
	public void setPwsUsu(String pwsUsu){
		this.pwsUsu = pwsUsu;
	}
	public int getBanUsu(){
		return banUsu;
	}
	public void setBanUsu(int banUsu){
		this.banUsu = banUsu;
	}
	public String getPerUsuNombre(){
		return this.perUsuNombre;
	}
	public void setPerUsuNombre(String perUsuNombre){
		this.perUsuNombre = perUsuNombre;
	}
	public boolean isAdministradorGeneral(){
		return this.perUsu == Common.PERMISO_ADMINISTRADOR_GENERAL;
	}
	public boolean isAdministrador(){
		return this.perUsu == Common.PERMISO_ADMINISTRADOR;
	}
	public boolean isAsesor(){
		return this.perUsu == Common.PERMISO_ASESOR;
	}
	public boolean isRevisor(){
		return this.perUsu == Common.PERMISO_REVISOR;
	}
	public boolean isUsuarioNormal(){
		return this.perUsu == Common.PERMISO_USUARIO_NORMAL;
	}

	private void UsuarioInicializar(HttpServletRequest request, HttpServletResponse response, boolean flgChild){
		try{
			HttpSession sesion = request.getSession(true);
						
			if(sesion.getAttribute(Common.SESSION_NAME_USUARIO) != null){
				this.cveUsu = sesion.getAttribute(Common.SESSION_NAME_USUARIO).toString();
				try {
					if(!ExistirUsuario(this.cveUsu)){
						if(flgChild){
							response.sendRedirect(Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR_CHILD);
							return;
						}else{

							if(Common.IsAjax(request)){
								
								Common.MsgJson(Common.JSON_STATUS_NO_AUTORIZADO, Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR, response);
								
							}else{

								response.sendRedirect(Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR);
							}
							return;
						}
					} 
				} catch (SQLException e) {
					Common.Error(e);
				}
		
			} else{
				
				if(Common.IsAjax(request)){
					
					Common.MsgJson(Common.JSON_STATUS_NO_AUTORIZADO, Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR, response);
					
				}else{
				
					if(flgChild){
						response.sendRedirect(Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR_CHILD);
						return;
					}else{
						response.sendRedirect(Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR);
						return;
					}
				}
			}	
		}catch(Exception e){
			Common.Error(e);
		}
	}

	
	
	/**
	 * Entrar al sistema
	 * @return
	 * @throws SQLException
	 */
	public boolean Login(HttpServletRequest request) throws Exception{
		try{
			
			if(ExistirUsuario(this.cveUsu, this.pwsUsu)){
			
				LogAcc l = new LogAcc();
				HttpSession sesion = request.getSession(true);
				l.setCveUsu(this.cveUsu);
				l.setIdSession(sesion.getId());
				l.setIpUsu(request.getRemoteAddr());
				l.CrearEntrada();
				
     			sesion.setAttribute(Common.SESSION_NAME_USUARIO,this.cveUsu);
				
				return true;
			}else{
	
				return false;
			}
		}catch(Exception e){
			throw e;
		}

	}	

	/**
	 * Confirmar que si el usuario existe con id y contraseña
	 * @param idUsuario
	 * @param password
	 * @return
	 * @throws SQLException 
	 */
	public boolean ExistirUsuario(String idUsuario, String password) throws SQLException{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		String passwordDescrypto = Crypto.getStretchedPassword(password, idUsuario);
		
		try {			
			String query = this.queryBuscar1 + " AND PwsUsu =?";  
				 
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, idUsuario);
			preparedStatement.setString(2, passwordDescrypto);
			ResultSet rs = preparedStatement.executeQuery();

			if(rs.next()){
				rs.beforeFirst();
				while (rs.next())
				{
					SetUsuario(rs);
				}
			} else {
				return false;
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
		return true;
	}
	
	/**
	 * Confirmar que si el usuario existe con id.
	 * @param idUsuario
	 * @return
	 * @throws SQLException
	 */
	public boolean ExistirUsuario(String idUsuario) throws SQLException{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {			
				 
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(this.queryBuscar1);
			preparedStatement.setString(1, idUsuario);
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()){
				rs.beforeFirst();
				while (rs.next())
				{
					SetUsuario(rs);
				}
			} else {
				return false;
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
		return true;
	}
	
	/**
	 * Conjuntar datos del usuario
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public void SetUsuario(ResultSet rs) throws SQLException {
		try {
			this.cveUsu = rs.getString("cveUsu");
			this.nomUsu = rs.getString("nomUsu");
			this.nomPatUsu = rs.getString("nomPatUsu");
			this.nomMatUsu = rs.getString("nomMatUsu");
			this.perUsu = rs.getInt("perUsu");
			this.pwsUsu = rs.getString("pwsUsu");
			this.perUsuNombre = rs.getString("perUsuNombre");		
			this.banUsu = rs.getInt("banUsu");
		} catch (SQLException e) {
			throw e;
		}
	}

	public boolean IsAutorizado(){
		if(this.cveUsu.equals("")){
			return false;
		} else{
			return true;
		}
	}
	
	String queryBuscar1 = "SELECT CveUsu, NomUsu, NomPatUsu, NomMatUsu, PerUsu, PwsUsu, c.NomCode AS PerUsuNombre, BanUsu FROM tblUsuario u INNER JOIN mstcode c ON u.PerUsu = c.Code AND c.CveCode = '" + MstCode.CVE_CODE_PERMISO + "' AND BanCode=1 WHERE CveUsu = ? AND BanUsu=1";  
	
	String queryBuscar2 = "SELECT CveUsu, NomUsu, NomPatUsu, NomMatUsu, PerUsu, PwsUsu, c.NomCode, BanUsu "
			+ " FROM tblUsuario u"
			+ " INNER JOIN mstcode c"
			+ " ON u.PerUsu = c.Code AND c.CveCode = '" + MstCode.CVE_CODE_PERMISO + "' AND BanCode=1";
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Usuario Buscar(String cveUsu) throws Exception{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		Usuario u = new Usuario();
		
		try {			
			String query = "SELECT CveUsu, NomUsu, NomPatUsu, NomMatUsu, PerUsu, "
					+ " PwsUsu, c.NomCode AS PerUsuNombre, BanUsu "
					+ " FROM tblUsuario u "
					+ " INNER JOIN mstcode c ON u.PerUsu = c.Code "
					+ "  AND c.CveCode = '" + MstCode.CVE_CODE_PERMISO + "' AND BanCode=1 "
					+ " WHERE CveUsu = ? AND BanUsu=1";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cveUsu);
						
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				u.cveUsu = rs.getString("cveUsu");
				u.nomUsu = rs.getString("nomUsu");
				u.nomPatUsu = rs.getString("nomPatUsu");
				u.nomMatUsu = rs.getString("nomMatUsu");
				u.perUsu = rs.getInt("perUsu");
				u.perUsuNombre = rs.getString("PerUsuNombre");
				u.pwsUsu = rs.getString("pwsUsu");
				u.banUsu = rs.getInt("banUsu");	
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
		
		return u;
	}
	
	public int CountUsuario() throws Exception{
		int cnt = 0;
		
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "select count(*) cnt from tblusuario u";

			String condicion = CreateCondition(con);
			if(condicion.length() > 0){
				query  += " WHERE " + condicion.toString();
			}
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			SetPreparedStatement(preparedStatement);
						
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				cnt = rs.getInt("cnt");
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
		
		return cnt;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Usuario> Buscar(int limitfrom, int numeromostrar) throws Exception{
		List<Usuario> list = new ArrayList<Usuario>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			

			String condicion = CreateCondition(con);
			if(condicion.length() > 0){
				this.queryBuscar2  += " WHERE " + condicion.toString();
			}

			this.queryBuscar2 += " ORDER BY cveUsu ";
			this.queryBuscar2  += " LIMIT ?, ?";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(this.queryBuscar2);

			int cnt = SetPreparedStatement(preparedStatement);

			cnt++;
			preparedStatement.setInt(cnt, limitfrom);
			cnt++;
			preparedStatement.setInt(cnt, numeromostrar);

			
			
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				Usuario u = new Usuario();
				u.cveUsu = rs.getString("cveUsu");
				u.nomUsu = rs.getString("nomUsu");
				u.nomPatUsu = rs.getString("nomPatUsu");
				u.nomMatUsu = rs.getString("nomMatUsu");
				u.perUsu = rs.getInt("perUsu");
				u.pwsUsu = rs.getString("pwsUsu");
				u.perUsuNombre = rs.getString("NomCode");		
				u.banUsu = rs.getInt("banUsu");	
				list.add(u);
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
	
	private String CreateCondition(Connection con) throws Exception{
		try{
			
			StringBuilder condicion = new StringBuilder();
			if(this.cveUsu != null && this.cveUsu.length() > 0){
				condicion.append(" u.cveUsu LIKE ? ");
			}
			
			String nombreCondi = "";
			if(this.nomUsu != null && this.nomUsu.length() > 0){
				if(condicion.length() > 0){ condicion.append(" AND ");}
				condicion.append(" (REPLACE(CONCAT(IFNULL(u.NomUsu, ''), IFNULL(u.NomPatUsu, ''), IFNULL(u.NomMatUsu,'')),' ','') like ?) ");

			}
			
			if(this.perUsu > 0){
				if(condicion.length() > 0){ condicion.append(" AND ");}
				condicion.append(" u.perUsu = ? ");
			}
			
			return condicion.toString();
		}catch(Exception e){
			throw e;
		}
		
	}
	private int SetPreparedStatement(PreparedStatement preparedStatement) throws Exception{
		int count = 0;
		try{	
			if(this.cveUsu != null && this.cveUsu.length() > 0){
				count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.cveUsu) + "%");
			}
			if(this.nomUsu != null && this.nomUsu.length() > 0){
				count++;
				preparedStatement.setString(count, "%" + DbUtil.EscapeLike(this.nomUsu) + "%");
			}
			if(this.perUsu > 0){
				count++;
				preparedStatement.setInt(count, this.perUsu);
			}

		}catch(Exception e){
			throw e;
		}
		return count;
		
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Usuario> BuscarUsuarioSinPermisoContenido(String cveMat) throws Exception{
		List<Usuario> list = new ArrayList<Usuario>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			

			this.queryBuscar2 += " WHERE BanUsu=1 AND NOT EXISTS("
					+ "SELECT cveMat FROM tblpermisocontenido c WHERE c.cveUsu = u.cveUsu AND c.cveMat='" + cveMat + "')"
					+ " AND u.perUsu NOT IN(" + Common.PERMISO_ADMINISTRADOR + "," + Common.PERMISO_ADMINISTRADOR_GENERAL + "," + Common.PERMISO_USUARIO_NORMAL + ")";

			String condicion = CreateCondition(con);
			if(condicion.length() > 0){
				this.queryBuscar2  += " AND " + condicion.toString();
			}

			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(this.queryBuscar2);

			SetPreparedStatement(preparedStatement);

			this.queryBuscar2 += " ORDER BY cveUsu ";

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				Usuario u = new Usuario();
				u.cveUsu = rs.getString("cveUsu");
				u.nomUsu = rs.getString("nomUsu");
				u.nomPatUsu = rs.getString("nomPatUsu");
				u.nomMatUsu = rs.getString("nomMatUsu");
				u.perUsu = rs.getInt("perUsu");
				u.pwsUsu = rs.getString("pwsUsu");
				u.perUsuNombre = rs.getString("NomCode");		
								
				list.add(u);
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

	public static int DarBajaLogical(Connection con, String cveUsu, int bandera) throws SQLException {
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE tblusuario SET BanUsu = ? WHERE CveUsu = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, bandera);
			preparedStatement.setString(2, cveUsu);
			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		
		return count;
	}

	public int Insertar(Connection con) throws Exception {
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "INSERT INTO tblusuario (CveUsu, NomUsu, NomPatUsu, NomMatUsu, PerUsu, PwsUsu, BanUsu) "
					+ "VALUES (?,?,?,?,?,?,?)";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.cveUsu);
			
			preparedStatement.setString(2, this.nomUsu);
			preparedStatement.setString(3, this.nomPatUsu);
			preparedStatement.setString(4, this.nomMatUsu);
			preparedStatement.setInt(5, this.perUsu);
			preparedStatement.setString(6, this.pwsUsu);
			preparedStatement.setInt(7, 1);
			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		return count;
	}
	
	public int Modificar(Connection con) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE tblusuario "
					+ " SET NomUsu = ?, NomPatUsu = ?, NomMatUsu = ?, PerUsu = ?, PwsUsu = ?"
					+ " WHERE CveUsu = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.nomUsu);
			preparedStatement.setString(2, this.nomPatUsu);
			preparedStatement.setString(3, this.nomMatUsu);
			preparedStatement.setInt(4, this.perUsu);
			preparedStatement.setString(5, this.pwsUsu);
			preparedStatement.setString(6, this.cveUsu);
			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		
		return count;
	}

	
}
