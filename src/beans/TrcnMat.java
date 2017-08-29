package beans;

import java.io.File;
import java.io.Serializable;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mysql.jdbc.Statement;

import util.common.Common;
import util.db.DbUtil;
import util.db.MySqlConnector;


public class TrcnMat implements Serializable {
	
	/** Clave de la materia **/
	private String cveMat ="";
	/** Clave del contenido **/
	private int menuItem = 0;
	/** Clave del contenido padre **/
	private int menuItemParentId = 0;
	/** Nombre del contenido **/
	private String caption ="";
	/** Nombre del url **/
	private String handler ="";
	/** Nombre del url **/
	private String handlerLike ="";
	/** Contenido **/
	private Clob contenido;
	/** Estado **/
	private int menuItemBan = 0;
	/** Unidad **/
	private int unidad = 0;
	/** Apartado **/
	private int apartado = 0;
	/** Orden **/
	private int orden = 0;

	
	public String getCveMat(){
		return cveMat;
	}
	public void setCveMat(String cveMat){
		this.cveMat = cveMat;
	}
	public String getCaption(){
		return caption;
	}
	public void setCaption(String caption){
		this.caption = caption;
	}
	public String getHandler(){
		return handler;
	}
	public void setHandler(String handler){
		this.handler = handler;
	}
	public String getHandlerLike(){
		return handlerLike;
	}
	public void setHandlerLike(String handlerLike){
		this.handlerLike = handlerLike;
	}
	public int getMenuItem(){
		return menuItem;
	}
	public void setMenuItem(int menuItem){
		this.menuItem = menuItem;
	}
	public int getMenuItemParentId(){
		return menuItemParentId;
	}
	public void setMenuItemParentId(int menuItemParentId){
		this.menuItemParentId = menuItemParentId;
	}
	public String getContenido(){
		
		String strClob = "";
		Clob clob = this.contenido;
		try {
			if(clob != null){
				int len;
				len = (int)clob.length();
				strClob = clob.getSubString(1, len);
			}
		} catch (Exception e) {
			Common.Error(e);
		}
		
		return strClob;
	}
	public void setContenido(Clob contenido){
		this.contenido = contenido;
	}
	public int getMenuItemBan(){
		return menuItemBan;
	}
	public void setMenuItemBan(int menuItemBan){
		this.menuItemBan = menuItemBan;
	}
	public String getParentContName(){
		String str = "";
		try {
			str = BuscarParentName(this.cveMat, this.menuItem);
			int i = str.lastIndexOf(Common.HYPHEN);
			if(i>=0){
				str = str.substring(0, i+Common.HYPHEN.length());
			} else{
				str = "";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	public int getUnidad(){
		return unidad;
	}
	public void setUnidad(int unidad){
		this.unidad = unidad;
	}
	public int getApartado(){
		return apartado;
	}
	public void setApartado(int apartado){
		this.apartado = apartado;
	}
	public int getOrden(){
		return orden;
	}
	public void setOrden(int orden){
		this.orden = orden;
	}

	
	/**
	 * Crear el arbol de los contenidos
	 * @param idPadre
	 * @return
	 * @throws Exception 
	 */

	final static String queryMenu = "SELECT A.* ,"
			+ "            (SELECT COUNT(menu_item) FROM trcn_materia WHERE menu_item_parent_id = A.menu_item) AS numChild "
			+ "     FROM "
			+ "      ( SELECT A.menu_item, A.caption, A.handler, A.menu_item_parent_id, A.menu_item_ban, A.Materia, A.unidad, A.apartado, A.orden"
			+ "        FROM trcn_materia A WHERE A.menu_item_parent_id =? AND A.menu_item_ban = 1 AND A.Materia = ? ) AS A"
			+ "	    ORDER BY CASE WHEN A.orden <= 0 THEN A.menu_item"
			+ "                   WHEN A.orden > 0 THEN A.orden END";

	public static String CreaMenu(Materia beanMat, int idPadre, boolean puedeVer, boolean puedeModificar, Usuario usuario, boolean sortable) throws Exception {
		Connection con = null;
		PreparedStatement preparedStatement = null;

		StringBuilder str = new StringBuilder();
		try {			
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(queryMenu);
			preparedStatement.setInt(1, idPadre);
			preparedStatement.setString(2, beanMat.getCveMat());
			ResultSet rs = preparedStatement.executeQuery();   

			String display = "block";
			String strSortable = "class='sortable sortable_" + idPadre + "'";
			if(sortable){
				if(idPadre == 0){
					strSortable = "";
				}
			} else{
				strSortable = "";
			}
			
			str.append("<ul id='tabla_" + idPadre + "' " + strSortable + " style='display: " + display + "' >");
			
			while (rs.next())
			{
				TrcnMat bean = new TrcnMat();
				bean.menuItem = rs.getInt("menu_item");
				bean.cveMat = rs.getString("Materia");
				bean.caption = rs.getString("caption");
				bean.handler = rs.getString("handler");
				bean.menuItemParentId = rs.getInt("menu_item_parent_id");
				bean.unidad = rs.getInt("unidad");
				bean.apartado = rs.getInt("apartado");

				String enlaceVer = "  <a class='btn' href='javascript:void(0)' onclick='funcVer(\"" +bean.menuItem+ "\")'>Ver</a> / ";
				String enlaceVerClose = "";
				String enlaceAgregar = "";
				String enlaceModificar = "";
				String enlaceExport = "<i class='material-icons'>file_upload</i>";
				String enlaceOrdenar = "<a class='btn' href='javascript:void(0);' onclick='funcOrdenar(\"" +bean.menuItem+ "\")' title='ordenar'><i class='material-icons'>swap_vert</i></a> / ";
				enlaceAgregar = "  <a class='btn' href='javascript:void(0);' onclick='funcAgregar(\"" +bean.menuItem+ "\")' title='agregar'><i class='material-icons'>add</i></a> / ";
				enlaceModificar = "  <a class='btn' href='javascript:void(0)' onclick='funcModificar(\"" +bean.menuItem+ "\")' title='modificar'><i class='material-icons'>edit</i></a> / ";

				String enlaceBaja = "<a class='btn' href='javascript:void(0)' onclick='funcBajar(\"" +bean.menuItem+ "\")' title='bajar'><i class='material-icons'>delete</i></a> / ";
				
				str.append("<li id='menu_" + bean.menuItem + "'>");

				enlaceVer = "";
				if(bean.handler != null && !bean.handler.equals("")){
					enlaceVer = "<a class='caption' href='javascript:void(0)' onclick='funcVer(\"" +bean.menuItem+ "\")'>";
					enlaceVerClose = "</a>";
				}
				str.append("  - " + enlaceVer + bean.caption + enlaceVerClose + " ("+bean.handler+")");



				if(puedeModificar){

					if(bean.caption.equals(Common.CAPTION_CONTENIDO_INTRODUCCION)){
						str.append(enlaceModificar);
					} else if(bean.caption.equals(Common.CAPTION_CONTENIDO_GENERALIDADES)){
						str.append(enlaceAgregar);
						str.append(enlaceOrdenar);
					} else if(bean.caption.equals(Common.CAPTION_CONTENIDO_CONTENIDO_MATERIA)){
						str.append(enlaceAgregar);
						str.append(enlaceOrdenar);
					} else{
						str.append(enlaceModificar);
						str.append(enlaceAgregar);
						if((bean.unidad > 0 && bean.apartado != 0) || 
								(bean.menuItemParentId != 0 && bean.unidad <= 0) || 
								(bean.unidad > 0 && bean.caption.equals(Common.CAPTION_CONTENIDO_UNIDAD + beanMat.getUnidad()))){
							str.append(enlaceBaja);
						}
					}
					
				}
				
				if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
//					str.append(enlaceExport);
				}

				str.append(CreaMenu(beanMat, bean.menuItem, puedeVer, puedeModificar, usuario, sortable));
				str.append("</li>");
           }

			str.append("</ul>");
				
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
			if (con != null) {
				con.close();
	        }
		}		return str.toString();
	}

	public static void CreaMenuPDF(Materia beanMat, int idPadre, int jerarquía, PdfPTable table) throws Exception {
		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {			
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(queryMenu);
			preparedStatement.setInt(1, idPadre);
			preparedStatement.setString(2, beanMat.getCveMat());
			ResultSet rs = preparedStatement.executeQuery();   

			jerarquía++;

			while (rs.next())
			{
				TrcnMat bean = new TrcnMat();
				bean.menuItem = rs.getInt("menu_item");
				bean.cveMat = rs.getString("Materia");
				bean.caption = rs.getString("caption");
				bean.handler = rs.getString("handler");
				bean.menuItemParentId = rs.getInt("menu_item_parent_id");
				bean.unidad = rs.getInt("unidad");
				bean.apartado = rs.getInt("apartado");

				String blank = "";
				for(int i=0;i<jerarquía; i++){
					blank += "  ";
				}
				
				PdfPCell cell = new PdfPCell(new Phrase(blank + "  - " + bean.caption + "("+bean.handler+") \r\n"));
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(cell);
				
				CreaMenuPDF(beanMat, bean.menuItem, jerarquía, table);
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

	public static String CreaMenuPDF(Materia beanMat, int idPadre, int jerarquía) throws Exception {
		Connection con = null;
		PreparedStatement preparedStatement = null;

		StringBuilder str = new StringBuilder();
		try {			
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(queryMenu);
			preparedStatement.setInt(1, idPadre);
			preparedStatement.setString(2, beanMat.getCveMat());
			ResultSet rs = preparedStatement.executeQuery();   

			jerarquía++;

			while (rs.next())
			{
				TrcnMat bean = new TrcnMat();
				bean.menuItem = rs.getInt("menu_item");
				bean.cveMat = rs.getString("Materia");
				bean.caption = rs.getString("caption");
				bean.handler = rs.getString("handler");
				bean.menuItemParentId = rs.getInt("menu_item_parent_id");
				bean.unidad = rs.getInt("unidad");
				bean.apartado = rs.getInt("apartado");

				String blank = "";
				for(int i=0;i<jerarquía; i++){
					blank += "  ";
				}
				
				str.append(blank + "  - " + bean.caption + "("+bean.handler+") \r\n");
				
				str.append(CreaMenuPDF(beanMat, bean.menuItem, jerarquía));
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
		return str.toString();
	}

	
	public int GetMaxApartado() throws SQLException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {			
			String query = "SELECT MAX(apartado)+1 AS apartado FROM trcn_materia "
					+ " WHERE materia = ? AND UNIDAD = ? AND menu_item_ban = 1 ";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, this.cveMat);
			preparedStatement.setInt(2, this.unidad);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				return rs.getInt("apartado");

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
		return 0;
	}

	private static final String BUS_QUERY = 
			"SELECT A.menu_item, A.caption, A.handler, A.menu_item_parent_id, A.menu_item_ban, A.Materia, A.contenido, A.unidad, A.apartado, A.orden"
			+ " FROM trcn_materia A ";

	
	public List<TrcnMat> Buscar() throws SQLException{
		List<TrcnMat> list = new ArrayList<TrcnMat>();
		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {			
			String query = BUS_QUERY + " WHERE menu_item_ban = 1";
			
			if(!this.cveMat.equals("")){
				query += " AND Materia = ?";
			}
			if(this.menuItem > 0){
				query += " AND menu_item = ?";
			}
			if(this.menuItemParentId > 0){
				query += " AND menu_item_parent_id = ?";
			}
			if(!this.caption.equals("")){
				query += " AND caption = ?";
			}
			if(!this.handler.equals("")){
				query += " AND handler = ?";
			}
			if(this.contenido != null){
				query += " AND contenido = ?";
			}
			if(this.unidad > 0){
				query += " AND unidad = ?";
			}
			if(this.apartado > 0){
				query += " AND apartado = ?";
			}
			
			query += " ORDER BY menu_item_parent_id, "
//					+ "menu_item";
					+ "	    CASE WHEN A.orden <= 0 THEN A.menu_item"
					+ "          WHEN A.orden > 0 THEN A.orden END";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);

			int count = 0;
			if(!this.cveMat.equals("")){
				count++;
				preparedStatement.setString(count, this.cveMat);
			}
			if(this.menuItem > 0){
				count++;
				preparedStatement.setInt(count, this.menuItem);
			}
			if(this.menuItemParentId > 0){
				count++;
				preparedStatement.setInt(count, this.menuItemParentId);
			}
			if(!this.caption.equals("")){
				count++;
				preparedStatement.setString(count, this.caption);
			}
			if(!this.handler.equals("")){
				count++;
				preparedStatement.setString(count, this.handler);
			}
			if(!this.handlerLike.equals("")){
				count++;
				preparedStatement.setString(count,  DbUtil.EscapeLike(this.handlerLike) + "%");
			}
			if(this.contenido != null){
				count++;
				preparedStatement.setClob(count, this.contenido);
			}
			if(this.unidad > 0){
				count++;
				preparedStatement.setInt(count, this.unidad);
			}
			if(this.apartado > 0){
				count++;
				preparedStatement.setInt(count, this.apartado);
			}
					
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				TrcnMat bean = new TrcnMat();
				SetBean(bean, rs);
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
	 * Buscar el nombre del padre del contenido
	 * @param cveMat
	 * @param menuItem
	 * @return
	 * @throws Exception
	 */
	private static String BuscarParentName(String cveMat, int menuItem) throws Exception{
		StringBuilder name = new StringBuilder();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {			
			String query = "SELECT menu_item_parent_id, caption FROM trcn_materia "
					+ "WHERE Materia =? AND menu_item =? AND menu_item_ban = 1";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cveMat);
			preparedStatement.setInt(2, menuItem);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				int pid = Integer.parseInt(rs.getString("menu_item_parent_id"));
				if(pid <= 0){
					name.append(rs.getString("caption"));
				}else{
				name.append(
						BuscarParentName(cveMat, Integer.parseInt(rs.getString("menu_item_parent_id"))) + 
						Common.HYPHEN + rs.getString("caption"));
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
		}
		return name.toString();
		
	}	

	/**
	 * Buscar el contenido con clave del contenido
	 * @param cveCont
	 * @return
	 * @throws Exception
	 */
	public static TrcnMat Buscar(int cveCont) throws Exception{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		TrcnMat bean = new TrcnMat();
		try {			
			String query = BUS_QUERY + " WHERE A.menu_item =? AND menu_item_ban = 1 ";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, cveCont);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				SetBean(bean, rs);

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

	/**
	 * Contar unidad
	 * @param cveCont
	 * @return
	 * @throws Exception
	 */
	public static int ContarUnidad(String cveMat) throws Exception{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "SELECT count(*) countUnidad FROM trcn_materia where Materia = ? AND unidad > 0 AND apartado = 0 AND menu_item_ban = 1";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cveMat);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				count = rs.getInt("countUnidad");

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

	/**
	 * Buscar el contenido con clave de la materia
	 * @param cveMat
	 * @return
	 * @throws Exception
	 */
	public static List<TrcnMat> Buscar(String cveMat) throws Exception{
		
		return Buscar(cveMat, false);
		
	}
	
	public static List<TrcnMat> Buscar(String cveMat, boolean conDeleted) throws Exception{
		List<TrcnMat> list = new ArrayList<TrcnMat>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {			
			
			String queryItemBan = " AND menu_item_ban =1 ";
			if(conDeleted){
				queryItemBan = "";
			}
			
			
			String query = BUS_QUERY + " WHERE A.Materia =? " + queryItemBan
					+ "ORDER BY menu_item_parent_id,"
					+ "	    CASE WHEN A.orden <= 0 THEN A.menu_item"
					+ "          WHEN A.orden > 0 THEN A.orden END";

			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cveMat);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				TrcnMat bean = new TrcnMat();
				SetBean(bean, rs);
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

	private static void SetBean(TrcnMat bean, ResultSet rs) throws SQLException {
		bean.menuItem = rs.getInt("menu_item");
		bean.cveMat = rs.getString("Materia");
		bean.caption = rs.getString("caption");
		bean.menuItemParentId = rs.getInt("menu_item_parent_id");
		bean.contenido = rs.getClob("contenido");
		bean.handler = rs.getString("handler");
		bean.unidad = rs.getInt("unidad");
		bean.apartado = rs.getInt("apartado");
		
	}
	/**
	 * Buscar el contenido con clave del contenido
	 * @param cveCont
	 * @return
	 * @throws Exception
	 */
	public static String BuscarSequencia(int unidad, int apartado, String cveMat) throws Exception{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		String sequencia="";
		try {			
			String query = "SELECT COUNT(menu_item) cnt FROM trcn_materia "
					+ "	WHERE materia = ? AND unidad = ? AND apartado = ? AND menu_item_ban = 1";

			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cveMat);						
			preparedStatement.setInt(2,  unidad);
			preparedStatement.setInt(3,  apartado);
			ResultSet rs = preparedStatement.executeQuery();   

			if(rs.next()){
				sequencia = Integer.toString(rs.getInt("cnt"));
				
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
		return sequencia;
	}
	
	/**
	 * Insertar el contenido
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int Insertar(Connection con) throws Exception {
		PreparedStatement preparedStatement = null;
		int autoIncKey = 0;
		ResultSet rs = null;
		try {			
			String query = "INSERT INTO trcn_materia ("
					+ "menu_item, caption, handler, menu_item_parent_id, menu_item_ban, Materia, contenido, unidad, apartado, orden)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
			
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, this.menuItem);
			preparedStatement.setString(2, this.caption);
			preparedStatement.setString(3, this.handler);
			preparedStatement.setInt(4, this.menuItemParentId);
			preparedStatement.setInt(5, 1);
			preparedStatement.setString(6, this.cveMat);
			preparedStatement.setClob(7, this.contenido);
			preparedStatement.setInt(8, this.unidad);
			preparedStatement.setInt(9, this.apartado);
			preparedStatement.setInt(10, this.orden);
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
			if (rs != null) {
				rs.close();
	        }			
		}
		return autoIncKey;
	}

	/**
	 * Buscar las materias en Bachillerato o Licenciatura o etc.
	 * @return
	 * @throws Exception
	 */
	public static boolean ExistsContenido(Connection con, String cveMat) throws Exception{
		PreparedStatement preparedStatement = null;
		
		try {			
			StringBuilder query = new StringBuilder();
			query.append("SELECT menu_item");  
			query.append(" FROM trcn_materia ");
			query.append(" WHERE Materia = ? AND menu_item_ban = 1");
			
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setString(1, cveMat);

			ResultSet rs = preparedStatement.executeQuery();   
			return rs.next();

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

	public static int ObtenerMaxMenuItem(Connection con, String schema) throws SQLException{
		int menuItem = 0;

		PreparedStatement preparedStatement = null;
		try {			
			String query = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES"
					+ " WHERE TABLE_SCHEMA = '" + schema + "' AND TABLE_NAME = 'trcn_materia'";

			preparedStatement = con.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();   

			if(rs.next()){
				menuItem = rs.getInt("AUTO_INCREMENT");
				
			}
			
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}

		return menuItem;
	}

	public static List<TrcnMat> BuscarChild(String cveMat, int menuId) throws Exception{
		List<TrcnMat> list = new ArrayList<TrcnMat>();
		Connection con = null;
		PreparedStatement preparedStatement = null;

		
		try {			
			String query = "SELECT * FROM trcn_materia where Materia = ? AND menu_item_parent_id = ? AND menu_item_ban = 1";
			
			con = MySqlConnector.getConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cveMat);
			preparedStatement.setInt(2, menuId);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				TrcnMat bean = new TrcnMat();

				SetBean(bean, rs);

				list.add(bean);

			}
				
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}
		return list;
	}
	
	public static List<TrcnMat> BuscarDeExterno(Connection con, String cveMat) throws Exception{
		List<TrcnMat> list = new ArrayList<TrcnMat>();
		PreparedStatement preparedStatement = null;
		try {			
			String query = "SELECT A.menu_item, A.caption, A.handler, A.menu_item_padre_id, A.menu_item_ban, A.Materia "
					+ " FROM trcn_materia A WHERE A.Materia =? AND menu_item_ban = 1";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cveMat);
			ResultSet rs = preparedStatement.executeQuery();   

			while (rs.next())
			{
				TrcnMat bean = new TrcnMat();

				bean.menuItem = rs.getInt("menu_item");
				bean.cveMat = rs.getString("Materia");
				bean.caption = rs.getString("caption");
				bean.menuItemParentId = rs.getInt("menu_item_padre_id");
				bean.handler = rs.getString("handler");

				list.add(bean);

			}
				
		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }
		}
		return list;
	}

	/**
	 * Insertar el contenido
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int InsertarBac(Connection con) throws Exception {
		PreparedStatement preparedStatement = null;
		int autoIncKey = 0;
		ResultSet rs = null;
		try {			
			String query = "INSERT INTO trcn_materia ("
					+ "menu_item, caption, handler, menu_item_padre_id, menu_item_ban, Materia)"
					+ " VALUES (?,?,?,?,?,?)";
			
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, this.menuItem);
			preparedStatement.setString(2, this.caption);
			preparedStatement.setString(3, this.handler);
			preparedStatement.setInt(4, this.menuItemParentId);
			preparedStatement.setInt(5, 1);
			preparedStatement.setString(6, this.cveMat);
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
			if (rs != null) {
				rs.close();
	        }			
		}
		return autoIncKey;
	}
	
	/**
	 * Insertar el contenido
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int InsertarLic(Connection con) throws Exception {
		PreparedStatement preparedStatement = null;
		int autoIncKey = 0;
		ResultSet rs = null;
		try {			
			String query = "INSERT INTO trcn_materia ("
					+ "menu_item, caption, handler, menu_item_padre_id, menu_item_ban, Materia)"
					+ " VALUES (?,?,?,?,?,?)";
			
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, this.menuItem);
			preparedStatement.setString(2, this.caption);
			preparedStatement.setString(3, this.handler);
			preparedStatement.setInt(4, this.menuItemParentId);
			preparedStatement.setInt(5, 1);
			preparedStatement.setString(6, this.cveMat);
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
			if (rs != null) {
				rs.close();
	        }			
		}
		return autoIncKey;
	}
	
	/**
	 * Modificar el contenido
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int Modificar(Connection con) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int ret = 0;
		try {			
			String query = "UPDATE trcn_materia SET ";
			String query2 = "";
			if(!this.caption.equals("")){
				query2 += " caption = ?";
			}
			if(this.handler != null && !this.handler.equals("")){
				if(query2.length()>0)query2 += ",";
				query2 += " handler = ?";
			}
			if(this.contenido != null && !this.contenido.equals("")){
				if(query2.length()>0)query2 += ",";
				query2 += " contenido = ?";
			}
			if(this.orden > 0){
				if(query2.length()>0)query2 += ",";
				query2 += " orden = ?";
			}
			if(this.unidad > 0){
				if(query2.length()>0)query2 += ",";
				query2 += " unidad = ?";
			}
			if(this.apartado > 0){
				if(query2.length()>0)query2 += ",";
				query2 += " apartado = ?";
			}
			
			query = query + query2 + " WHERE menu_item = ?";
			
			preparedStatement = con.prepareStatement(query);
			int count = 0;
			if(!this.caption.equals("")){
				count++;
				preparedStatement.setString(count, this.caption);
			}
			if(this.handler != null && !this.handler.equals("")){
				count++;
				preparedStatement.setString(count, this.handler);
			}
			if(this.contenido != null && !this.contenido.equals("")){
				count++;
				preparedStatement.setClob(count, this.contenido);
			}
			if(this.orden > 0){
				count++;
				preparedStatement.setInt(count, this.orden);
			}
			if(this.unidad > 0){
				count++;
				preparedStatement.setInt(count, this.unidad);
			}
			if(this.apartado > 0){
				count++;
				preparedStatement.setInt(count, this.apartado);
			}

			count++;
			preparedStatement.setInt(count, this.menuItem);
			
			ret = preparedStatement.executeUpdate();			

		} catch (SQLException e) {
			throw e;
		} finally{
			if (preparedStatement != null) {
				preparedStatement.close();
	        }			
		}
		
		return ret;
	}
	
	public static int DarBajaConMateria(Connection con, String cveMat, int bandera) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE trcn_materia SET menu_item_ban = ? WHERE materia = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, bandera);
			preparedStatement.setString(2, cveMat);
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

	public int DarBaja(Connection con) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "UPDATE trcn_materia SET menu_item_ban = ? WHERE (menu_item = ? OR menu_item_parent_id = ?) AND Materia =? ";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, this.menuItem);
			preparedStatement.setInt(3, this.menuItem);
			preparedStatement.setString(4, this.cveMat);
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
	
	public void EliminarContArchivo() throws Exception {
		
		Materia materia = Materia.Buscar(this.cveMat);			
    	String path = materia.GetPathMateriaAbsolute() + File.separator;
    	String handlerPath = path + this.handler;
    	File file = new File(handlerPath);
    	if(file.exists()){
    		file.delete();
    	}
		
	}

	
	public static int Delete(Connection con, String cvemat) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {			
			String query = "DELETE FROM trcn_materia WHERE Materia = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cvemat);
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
