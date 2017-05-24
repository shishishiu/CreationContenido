package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.db.MySqlConnector;


public class MstCode implements Serializable {
	
	/** Clave del code **/
	private String cveCode;
	/** code **/
	private String code = "";
	/** Nombre del code **/
	private String nomCode = "";
	/** Orden **/
	private int orden;
	/** Bandera del estado **/
	private int banCode;
	/** selected **/
	private String selected = "";
	/** checked **/
	private String checked = "";
		
	/** Clave de mstcode **/
	public static final String CVE_CODE_PERMISO = "Permiso";
	public static final String CVE_CODE_PERMISO_CONTENIDO = "PermisoContenido";
	public static final String CVE_CODE_SOLICITUD = "Solicitud";
	public static final String CVE_CODE_TIPO_CONTENIDO = "TipoContenido";


	
	public String getCveCode(){
		return cveCode;
	}
	public void setCveCode(String cveCode){
		this.cveCode = cveCode;
	}
	public String getCode(){
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	public String getNomCode(){
		return nomCode;
	}
	public void setNomCode(String nomCode){
		this.nomCode = nomCode;
	}
	public int getOrden(){
		return orden;
	}
	public void setOrden(int orden){
		this.orden = orden;
	}
	public int getBanCode(){
		return banCode;
	}
	public void setBanCode(int banCode){
		this.banCode = banCode;
	}
	public String getSelected(){
		return selected;
	}
	public void setSelected(String selected){
		this.selected = selected;
	}
	public String getChecked(){
		return checked;
	}
	public void setChecked(String checked){
		this.checked = checked;
	}
	
	
	/**
	 * Buscar
	 * @return
	 * @throws Exception
	 */
	public static List<MstCode> Buscar(String cveCode) throws Exception{
		List<MstCode> listBean = new ArrayList<MstCode>();
		PreparedStatement preparedStatement = null;
		Connection con = null;
		
		try {
			con = MySqlConnector.getConnection();
		
			StringBuilder query = new StringBuilder();
			query.append("SELECT CveCode ,Code ,NomCode ,Orden ,BanCode");  
			query.append(" FROM mstcode");
			query.append(" WHERE BanCode = 1 AND CveCode = ?");
			query.append(" ORDER BY Orden");
	 
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, cveCode);
			
			ResultSet rs = preparedStatement.executeQuery();
   
			while (rs.next())
			{
				MstCode bean = new MstCode();
				bean.cveCode = rs.getString("CveCode");
				bean.code = rs.getString("Code");
				bean.nomCode = rs.getString("NomCode");
				bean.orden = rs.getInt("Orden");
				bean.banCode = rs.getInt("BanCode");
				
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
	
}
