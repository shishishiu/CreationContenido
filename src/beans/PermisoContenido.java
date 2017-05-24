package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.common.Common;
import util.db.MySqlConnector;

public class PermisoContenido {

	public PermisoContenido(){
		
	}
	
	
	/** Clave del usuario **/
	private String cveUsu = "";
	/** Clave de la materia **/
	private String cveMat = "";
	/** Permiso del contenido **/
	private int permiso = -1;
	/** Permiso del contenido **/
	private String nomPermiso = "";
	/** Bandera del estado **/
	private int banPermisoContenido;
	
	private Usuario usuario = new Usuario();
	

	public String getCveUsu(){
		return cveUsu;
	}
	public void setCveUsu(String cveUsu){
		this.cveUsu = cveUsu;
	}
	public String getCveMat(){
		return cveMat;
	}
	public void setCveMat(String cveMat){
		this.cveMat = cveMat;
	}
	public int getPermiso(){
		return permiso;
	}
	public void setPermiso(int permiso){
		this.permiso = permiso;
	}
	public String getNomPermiso(){
		return nomPermiso;
	}
	public void setNomPermiso(String nomPermiso){
		this.nomPermiso = nomPermiso;
	}
	public int getBanPermisoContenido(){
		return banPermisoContenido;
	}
	public void setBanPermisoContenido(int banPermisoContenido){
		this.banPermisoContenido = banPermisoContenido;
	}
	public Usuario getUsuario(){
		return usuario;
	}
	public void setUsuario(Usuario u){
		this.usuario = u;
	}

	
	
	String queryBuscar = 
	" FROM tblPermisoContenido pc"
	+ " INNER JOIN mstcode c"
	+ " ON pc.Permiso = c.Code AND c.CveCode = '" + MstCode.CVE_CODE_PERMISO_CONTENIDO + "' AND BanCode=1"
	+ " INNER JOIN tblusuario u ON pc.cveUsu = u.cveUsu AND u.BanUsu = 1"
	+ " INNER JOIN mstcode perUsu ON u.PerUsu = perUsu.Code AND perUsu.CveCode = '" + MstCode.CVE_CODE_PERMISO + "' AND perUsu.BanCode=1"
	+ " WHERE BanPermisoContenido = 1 AND pc.Permiso != " + Common.PERMISO_CONTENIDO_NINGUNA;
	
	
	public int CountUsuariosContenidos() throws Exception{
		int cnt = 0;
		
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = "select count(*) cnt "
					+ queryBuscar + " AND pc.cveMat = ? ";

			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setString(1, this.cveMat);
						
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
	public List<PermisoContenido> Buscar() throws Exception{
		return BuscarPerCont(-1, -1);
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PermisoContenido> Buscar(int limitfrom, int numeromostrar) throws Exception{
		return BuscarPerCont(limitfrom, numeromostrar);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<PermisoContenido> BuscarPerCont(int limitfrom, int numeromostrar) throws Exception{
		List<PermisoContenido> list = new ArrayList<PermisoContenido>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			String query = 
			"SELECT pc.CveUsu, pc.CveMat, pc.Permiso, c.Code, c.NomCode, u.*, perUsu.NomCode AS PerUsuNombre"
			+ queryBuscar;

			StringBuilder condicion = new StringBuilder();
			if(this.cveMat != null && this.cveMat.length() > 0){
				condicion.append(" AND pc.cveMat = ? ");
			}
			if(this.cveUsu != null && this.cveUsu.length() > 0){
				condicion.append(" AND pc.cveUsu = ? ");
			}
			if(this.permiso > 0){
				condicion.append(" AND pc.permiso = ? ");
			}
			
			if(condicion.length() > 0){
				query += condicion.toString();
			}

			query += " ORDER BY pc.CveUsu ";
			
			if(limitfrom >= 0 && numeromostrar >= 0){
				query += " LIMIT ?, ?";

			}
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query.toString());

			int count = 0;
			if(this.cveMat != null && this.cveMat.length() > 0){
				count++;
				preparedStatement.setString(count, this.cveMat);
			}
			if(this.cveUsu != null && this.cveUsu.length() > 0){
				count++;
				preparedStatement.setString(count, this.cveUsu);
			}
			if(this.permiso > 0){
				count++;
				preparedStatement.setInt(count, this.permiso);
			}
			
			if(limitfrom >= 0 && numeromostrar >= 0){
				count++;
				preparedStatement.setInt(count, limitfrom);
				count++;
				preparedStatement.setInt(count, numeromostrar);

			}

			
			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				PermisoContenido bean = new PermisoContenido();
				bean.cveUsu = rs.getString("cveUsu");
				bean.cveMat = rs.getString("cveMat");
				bean.permiso = rs.getInt("permiso");
				bean.nomPermiso = rs.getString("nomCode");
				
				Usuario u = new Usuario();
				u.SetUsuario(rs);
				bean.usuario = u;
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
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int Buscar(String cveMat, String cveUsuario) throws Exception{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		
		try {			
			StringBuilder query = new StringBuilder();
			query.append("SELECT pc.CveUsu, pc.CveMat, pc.Permiso ");  
			query.append(" FROM tblPermisoContenido pc");
			query.append(" WHERE BanPermisoContenido = 1 AND pc.cveMat = ?  AND pc.cveUsu = ? ");
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, cveMat);
			preparedStatement.setString(2, cveUsuario);

			ResultSet rs = preparedStatement.executeQuery();   
			while (rs.next())
			{
				return rs.getInt("Permiso");
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
		return -1;
	}
	

	
	
	/**
	 * Modificar 
	 * @param con
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static int Modificar(Connection con, PermisoContenido bean) throws SQLException{
		int count = 0;
		PreparedStatement preparedStatement = null;
		String query = "";

		try {			

			if(bean.permiso == Common.PERMISO_CONTENIDO_NINGUNA){
				//delete
				query = "DELETE FROM tblpermisocontenido WHERE cveUsu = ? AND cveMat = ?";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, bean.cveUsu);
				preparedStatement.setString(2, bean.cveMat);

			}else{
				//update
				query = "UPDATE tblpermisocontenido SET permiso = ? WHERE cveUsu = ? AND cveMat = ?";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, bean.permiso);
				preparedStatement.setString(2, bean.cveUsu);
				preparedStatement.setString(3, bean.cveMat);
			}


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
	
	/**
	 * Insertar
	 * @param con
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static int Insertar(Connection con, PermisoContenido bean) throws SQLException{
		int count = 0;
		PreparedStatement preparedStatement = null;
		String query = "";

		try {			

			if(bean.permiso != Common.PERMISO_CONTENIDO_NINGUNA){
				//insertar
				query = "INSERT INTO tblpermisocontenido (cveUsu, cveMat, Permiso, BanPermisoContenido) VALUES (?,?,?,?)";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, bean.cveUsu);
				preparedStatement.setString(2, bean.cveMat);
				preparedStatement.setInt(3, bean.permiso);
				preparedStatement.setInt(4, 1);
			}

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
