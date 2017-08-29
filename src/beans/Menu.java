package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import util.db.MySqlConnector;

public class Menu {

	/** Clave del menu **/
	private int menuItem;
	/** Caption **/
	private String caption = "";
	/** Dirección **/
	private String direccion = "";
	/** Clave del padre del menu **/
	private int menuItemParent;
	/** Bandera del estado **/
	private int menuItemBan;
	/** Permiso **/
	private String permisos;
	
	public int getMenuItem(){
		return menuItem;
	}
	public void setMenuItem(int menuItem){
		this.menuItem = menuItem;
	}
	public String getCaption(){
		return caption;
	}
	public void setCaption(String caption){
		this.caption = caption;
	}
	public String getDireccion(){
		return direccion;
	}
	public void setDireccion(String direccion){
		this.direccion = direccion;
	}

	public int getMenuItemParent(){
		return menuItemParent;
	}
	public void setMenuItemParent(int menuItemParent){
		this.menuItemParent = menuItemParent;
	}

	public int getMenuItemBan(){
		return menuItemBan;
	}
	public void setMenuItemBan(int menuItemBan){
		this.menuItemBan = menuItemBan;
	}
	public String getPermisos(){
		return permisos;
	}
	public void setPermisos(String permisos){
		this.permisos = permisos;
	}

	/**
	 * Buscar menu
	 * @param idPadre
	 * @return
	 * @throws SQLException
	 */
	public String BuscarMenu(int idPadre, int PerUsu) throws SQLException {
		Connection con = null;
		PreparedStatement preparedStatement = null;

		StringBuilder menuStr = new StringBuilder();
		try {			
			StringBuilder query = new StringBuilder();
			query.append("SELECT A.menuItem, A.caption, A.direccion, A.menuItemParentId, A.permiso ");
			query.append(" FROM trcn_menu A WHERE A.menuItemParentId =? AND A.menuItemBan = 1");
			query.append(" ORDER BY A.menuItemParentId, A.Orden");
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, idPadre);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				Menu m = new Menu();
				m.menuItem = rs.getInt("menuItem");
				m.caption = rs.getString("caption");
				m.direccion = rs.getString("direccion");
				m.permisos = rs.getString("permiso");
				
				String[] permiso = null;
				if(m.permisos != null){
					permiso = m.permisos.split(",", 0);
				}

				if(permiso == null || 
						(permiso != null && Arrays.asList(permiso).contains(Integer.toString(PerUsu)))){

					if(m.direccion == null || m.direccion.equals("")){
						menuStr.append("<li><a href=\"#\"> " + m.caption + "</a><ul class=\"dl-submenu\"><li class=\"dl-back\"><a href=\"#\">atrás</a></li> " + BuscarMenu(m.menuItem, PerUsu) + " </ul></li>");
						
					}else{
	                    menuStr.append(" <li><a href='" + m.direccion + "'> " + m.caption + "</a>  </li>");    
						
					}

				}
				
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
		}		return menuStr.toString();
	}

}
