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


public class Nivel implements Serializable {
	
	/** Clave del nivel **/
	private int cveNivel;
	/** Nombre del nivel **/
	private String nomNivel = "";
	/** Nombre del directorio **/
	private String nomDir = "";
	/** Bandera del estado **/
	private int banNivel;
	/** selected **/
	private String selected = "";
	/** Lista **/
	private List<NivelGrado> listNivGrado = new ArrayList<NivelGrado>();
	
	public int getCveNivel(){
		return cveNivel;
	}
	public void setCveNivel(int cveNivel){
		this.cveNivel = cveNivel;
	}
	public String getNomNivel(){
		return nomNivel;
	}
	public void setNomNivel(String nomNivel){
		this.nomNivel = nomNivel;
	}
	public String getNomDir(){
		return nomDir;
	}
	public void setNomDir(String nomDir){
		this.nomDir = nomDir;
	}
	public int getBanNivel(){
		return banNivel;
	}
	public void setBanNivel(int banNivel){
		this.banNivel = banNivel;
	}
	public String getSelected(){
		return selected;
	}
	public void setSelected(String selected){
		this.selected = selected;
	}
	public List<NivelGrado> getListNivGrado(){
		return listNivGrado;
	}
	public void setListNivGrado(List<NivelGrado> s){
		this.listNivGrado = s;
	}
	
	
	/**
	 * Buscar todos del nivel
	 * @return
	 * @throws Exception
	 */
	public static List<Nivel> BuscarTodo() throws Exception{
		List<Nivel> listBean = new ArrayList<Nivel>();
		Connection con = MySqlConnector.getConnection();
		
		try {
			
			String query = "SELECT CveNiv ,NomNiv ,NomDir ,BanNiv FROM tblniv"
					+ " WHERE BanNiv = 1 ORDER BY CveNiv";
	 
			Statement st = con.createStatement();
   
			ResultSet rs = st.executeQuery(query.toString());
   
			while (rs.next())
			{
				Nivel bean = new Nivel();
				bean.cveNivel = rs.getInt("CveNiv");
				bean.nomNivel = rs.getString("NomNiv");
				bean.nomDir = rs.getString("NomDir");
				bean.banNivel = rs.getInt("BanNiv");
				
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

	/**
	 * Buscar todos del nivel
	 * @return
	 * @throws Exception
	 */
	public static List<Nivel> BuscarNivelNivGrado() throws Exception{
		List<Nivel> listBean = new ArrayList<Nivel>();
		Connection con = MySqlConnector.getConnection();
		
		try {
			
			String query = "SELECT CveNiv ,NomNiv ,NomDir ,BanNiv FROM tblniv"
					+ " WHERE BanNiv = 1 ORDER BY CveNiv";
	 
			Statement st = con.createStatement();
   
			ResultSet rs = st.executeQuery(query.toString());
   
			while (rs.next())
			{
				Nivel bean = new Nivel();
				bean.cveNivel = rs.getInt("CveNiv");
				bean.nomNivel = rs.getString("NomNiv");
				bean.nomDir = rs.getString("NomDir");
				bean.banNivel = rs.getInt("BanNiv");
				
				listBean.add(bean);
				
				List<NivelGrado> list = NivelGrado.BuscarParaVerNivel(bean.cveNivel);
				for(NivelGrado ng : list){
					bean.listNivGrado.add(ng);
				}
				
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


	
	/**
	 * Buscar todos del nivel
	 * @return
	 * @throws Exception
	 */
	public static Nivel Buscar(int cveNivel) throws Exception{
		
		Connection con = MySqlConnector.getConnection();
		PreparedStatement preparedStatement = null;
		Nivel bean = new Nivel();

		try {
			
			String query = "SELECT CveNiv ,NomNiv ,NomDir ,BanNiv FROM tblniv"
					+ " WHERE BanNiv = 1 AND cveNiv = ?";
	 
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, cveNivel);
   
			ResultSet rs = preparedStatement.executeQuery();   
   
			while (rs.next())
			{
				bean.cveNivel = rs.getInt("CveNiv");
				bean.nomNivel = rs.getString("NomNiv");
				bean.nomDir = rs.getString("NomDir");
				bean.banNivel = rs.getInt("BanNiv");
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

}
