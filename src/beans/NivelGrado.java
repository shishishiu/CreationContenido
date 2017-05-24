package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.db.MySqlConnector;


public class NivelGrado implements Serializable {
	
	/** Clave del nivel grado **/
	private int cveNivelGrado;
	/** Clave del nivel **/
	private int cveNivel;
	/** Nombre del nombre de nivel grado **/
	private String nomNivelGrado = "";
	/** Nombre del grado **/
	private String nomGrado = "";
	/** Bandera del estado **/
	private int banNivelGrado;
	/** selected **/
	private String selected = "";
	
	private String tieneMateria = "";
	
	public int getCveNivelGrado(){
		return cveNivelGrado;
	}
	public void setCveNivelGrado(int cveNivelGrado){
		this.cveNivelGrado = cveNivelGrado;
	}
	public int getCveNivel(){
		return cveNivel;
	}
	public void setCveNivel(int cveNivel){
		this.cveNivel = cveNivel;
	}
	public String getNomNivelGrado(){
		return nomNivelGrado;
	}
	public void setNomNivelGrado(String nomNivelGrado){
		this.nomNivelGrado = nomNivelGrado;
	}
	public String getNomGrado(){
		return nomGrado;
	}
	public void setNomGrado(String nomGrado){
		this.nomGrado = nomGrado;
	}
	public int getBanNivelGrado(){
		return banNivelGrado;
	}
	public void setBanNivelGrado(int banNivelGrado){
		this.banNivelGrado = banNivelGrado;
	}
	public String getSelected(){
		return selected;
	}
	public void setSelected(String selected){
		this.selected = selected;
	}
	public String getTieneMateria(){
		return tieneMateria;
	}
	public void setTieneMateria(String tieneMateria){
		this.tieneMateria = tieneMateria;
	}
	
	
	/**
	 * Buscar todos del nivel grado
	 * @return
	 * @throws Exception
	 */
	public static List<NivelGrado> BuscarTodo() throws Exception{
		List<NivelGrado> listBean = new ArrayList<NivelGrado>();
		Connection con = MySqlConnector.getConnection();
		
		try {
			
			String query = "SELECT CveNiv_Grado ,CveNiv ,NomNiv_Grado ,NomGrado ,BanGrado"
					+ " FROM tblniv_Grado WHERE BanGrado = 1 ORDER BY CveNiv";
	 
			Statement st = con.createStatement();
   
			ResultSet rs = st.executeQuery(query.toString());
   
			while (rs.next())
			{
				NivelGrado bean = new NivelGrado();
				bean.cveNivelGrado = rs.getInt("CveNiv_Grado");
				bean.cveNivel = rs.getInt("CveNiv");
				bean.nomNivelGrado = rs.getString("NomGrado");
				bean.nomGrado = rs.getString("NomGrado");
				bean.banNivelGrado = rs.getInt("BanGrado");
				
				listBean.add(bean);
 			}
			st.close();
	
		} catch (SQLException e) {
			throw e;
		} finally{
			if (con != null) {
				con.close();
	        }
		}
		return listBean;
	}

	public static List<NivelGrado> Buscar(int cveNivel) throws Exception{
		List<NivelGrado> listBean = new ArrayList<NivelGrado>();
		PreparedStatement preparedStatement = null;
		Connection con = null;
		
		try {
			con = MySqlConnector.getConnection();

			String query = "SELECT CveNiv_Grado ,CveNiv ,NomNiv_Grado ,NomGrado ,BanGrado"
					+ " FROM tblniv_Grado"
					+ " WHERE BanGrado = 1 AND CveNiv = ?"
					+ " ORDER BY CveNiv_Grado";
	 
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, cveNivel);
			ResultSet rs = preparedStatement.executeQuery();
   
			while (rs.next())
			{
				NivelGrado bean = new NivelGrado();
				bean.cveNivelGrado = rs.getInt("CveNiv_Grado");
				bean.cveNivel = rs.getInt("CveNiv");
				bean.nomNivelGrado = rs.getString("NomNiv_Grado");
				bean.nomGrado = rs.getString("NomGrado");
				bean.banNivelGrado = rs.getInt("BanGrado");
				
				listBean.add(bean);
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
		return listBean;
	}

	public static NivelGrado BuscarConCveNivGrado(int cveNivGrado) throws Exception{
		NivelGrado bean = new NivelGrado();
		PreparedStatement preparedStatement = null;
		Connection con = null;
		
		try {
			con = MySqlConnector.getConnection();

			String query = "SELECT CveNiv_Grado ,CveNiv ,NomNiv_Grado ,NomGrado ,BanGrado"
					+ " FROM tblniv_Grado"
					+ " WHERE BanGrado = 1 AND CveNiv_Grado = ?";
	 
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, cveNivGrado);
			ResultSet rs = preparedStatement.executeQuery();
   
			while (rs.next())
			{
				bean.cveNivelGrado = rs.getInt("CveNiv_Grado");
				bean.cveNivel = rs.getInt("CveNiv");
				bean.nomNivelGrado = rs.getString("NomNiv_Grado");
				bean.nomGrado = rs.getString("NomGrado");
				bean.banNivelGrado = rs.getInt("BanGrado");
				
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
		return bean;
	}

	
	public static List<NivelGrado> BuscarParaVerNivel(int cveNivel) throws Exception{
		List<NivelGrado> listBean = new ArrayList<NivelGrado>();
		PreparedStatement preparedStatement = null;
		Connection con = null;
		
		try {
			con = MySqlConnector.getConnection();

			String query = "SELECT ng.CveNiv_Grado ,CveNiv ,NomNiv_Grado ,NomGrado ,BanGrado, "
					+ " CASE WHEN COUNT(m.cvemat) > 0 THEN 'true' ELSE 'false' END AS TieneMateria" 
					+ " FROM tblniv_Grado ng"
					+ " LEFT JOIN tblmat m ON ng.CveNiv_Grado = m.CveNiv_Grado "
					+ " WHERE CveNiv = ? AND BanGrado = 1"
					+ " GROUP BY ng.CveNiv_Grado ,CveNiv ,NomNiv_Grado ,NomGrado ,BanGrado"
					+ " ORDER BY ng.CveNiv_Grado";
	 
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, cveNivel);
			ResultSet rs = preparedStatement.executeQuery();
   
			while (rs.next())
			{
				NivelGrado bean = new NivelGrado();
				bean.cveNivelGrado = rs.getInt("CveNiv_Grado");
				bean.cveNivel = rs.getInt("CveNiv");
				bean.nomNivelGrado = rs.getString("NomNiv_Grado");
				bean.nomGrado = rs.getString("NomGrado");
				bean.banNivelGrado = rs.getInt("BanGrado");
				bean.tieneMateria = rs.getString("TieneMateria");
				
				listBean.add(bean);
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
		return listBean;
	}

	
	public static boolean TieneMismoNomGrado(int cveNivGrado, int cveNiv, String nomGrado) throws Exception{
		PreparedStatement preparedStatement = null;
		Connection con = null;
		int cnt = 0;
		
		try {
			con = MySqlConnector.getConnection();

			String query = "SELECT Count(CveNiv_Grado) cnt"
					+ " FROM tblniv_Grado"
					+ " WHERE BanGrado = 1 AND CveNiv = ? AND NomGrado = ? AND CveNiv_Grado != ?";
	 
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, cveNiv);
			preparedStatement.setString(2, nomGrado);
			preparedStatement.setInt(3, cveNivGrado);
			ResultSet rs = preparedStatement.executeQuery();
   
			while (rs.next())
			{
				cnt = rs.getInt("cnt");
				
 			}
			
			if(cnt>0){
				return true;
				
			}else {
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
	}


	
	
	public int Modificar(Connection con) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE tblniv_Grado "
					+ " SET NomNiv_Grado = ? ";
			
			
			String condition= "";
			if(this.nomGrado != null && this.nomGrado.length() > 0){
				condition += " ,NomGrado = ? ";
			}
			query += condition + " WHERE CveNiv_Grado = ?";

			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.nomNivelGrado);
			
			
			int condCount = 1;
			if(this.nomGrado != null && this.nomGrado.length() > 0){
				condCount++;
				preparedStatement.setString(condCount, this.nomGrado);
			}

			condCount++;
			preparedStatement.setInt(condCount, this.cveNivelGrado);
			
			
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
		int autoIncKey = 0;
		ResultSet rs = null;
		try {			
			String query = "INSERT INTO tblniv_Grado (CveNiv_Grado, CveNiv, NomNiv_Grado, NomGrado, BanGrado) "
					+ "VALUES (?,?,?,?,?)";
			
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, this.cveNivelGrado);
			preparedStatement.setInt(2, this.cveNivel);
			preparedStatement.setString(3, this.nomNivelGrado);
			preparedStatement.setString(4, this.nomGrado);
			preparedStatement.setInt(5, 1);
			preparedStatement.executeUpdate();
			
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
			  autoIncKey = rs.getInt(1);
			}


		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		return autoIncKey;
	}
	
	public int DarBajaLogical(Connection con) throws SQLException {
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE tblniv_Grado SET BanGrado = ? WHERE CveNiv_Grado = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, this.cveNivelGrado);
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
