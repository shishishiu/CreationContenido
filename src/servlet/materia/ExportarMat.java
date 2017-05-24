package servlet.materia;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Materia;
import beans.TrcnMat;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class ExportarMat
 */
@WebServlet("/ExportarMat")
public class ExportarMat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "ExportarMat.jsp";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MENU_HTML = "menuHtml";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del variable **/	
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del variable **/	
	private final String KEY_TIPO_VALIDACION = "1";
	/** Nombre del variable **/	
	private final String KEY_TIPO_EXPORTAR = "2";
       
	/** Usuario **/
	public Usuario usuario;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ExportarMat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
		
    	try {
    		
    		usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			SetForm(request);
	
    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);
    		}

    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){
    				
    				String tipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
    				if(tipo.equals(KEY_TIPO_VALIDACION)){
						if(!Common.Validacion(request.getParameter(KEY_FORM_HIDDEN_CVE_MAT), response)){
    						return;
    					}
    					return;
    					
    				}else if(tipo.equals(KEY_TIPO_EXPORTAR)){
    					Common.Exportar(request,request.getParameter(KEY_FORM_HIDDEN_CVE_MAT),usuario );
    					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);
    				}

    			}
    			
    			SetForm(request);

    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);		
    		}
    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
    		
	}


//	private boolean Validacion(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Connection con;
//		String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
//		try {
//			Materia materia = Materia.Buscar(cveMat);
//			switch (materia.getNivel()) {
//				case Common.NIVEL_BACHILLERATO:
//					con = MySqlConnector.getConnectionBac();
//					break;
//				case Common.NIVEL_LICENCIATURA:
//					con = MySqlConnector.getConnectionLic();
//					break;
//				default:
//					Common.MsgJson("error", Common.MENSAJE_ERROR_EXPORTAR, response);
//					return false;
//			}
//			
//			boolean existsCont = TrcnMat.ExistsContenido(con, cveMat);
//			
//			if(existsCont){
//				Common.MsgJson("success", Common.MENSAJE_ERROR_TIENE_CONTENIDO, response);
//			}else{
//				Common.MsgJson("success", "", response);
//			}
//			return !existsCont;
//
//		} catch (Exception e) {
//			Common.Error(e);
//			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
//			return false;
//		}
//		
//	}
//
//	private void Exportar(HttpServletRequest request) throws Exception {
//		try {
//		
//			String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
//			Materia materia = Materia.Buscar(cveMat);
//			List<TrcnMat> list = TrcnMat.Buscar(cveMat);
//
//			Connection con = null;
//			Connection con2 = null;
//			String schema = "";
//			switch (materia.getNivel()) {
//				case Common.NIVEL_BACHILLERATO:
//					con = MySqlConnector.getConnectionBac();
//					schema =  Configuracion.getDbBacBaseDeDatos();
//					break;
//				case Common.NIVEL_LICENCIATURA:
//					con = MySqlConnector.getConnectionLic();
//					schema =  Configuracion.getDbLicBaseDeDatos();
//					break;
//				default:
//					return;
//			}
//
//			try {
//				
//				con.setAutoCommit(false);
//
//				TrcnMat.DarBajaConMateria(con, cveMat, 0);
//				
//				CopiarContenidos(con, schema, list, materia.getNivel());
//
//				con2 = MySqlConnector.getConnection();
//				con2.setAutoCommit(false);
//
//				Common.InsertLogAct(request, con2, usuario.getCveUsu(), 
//						MessageFormat.format(Common.TEXTO_ACTION_LOG_EXPORTAR,cveMat));
//
//				con.commit();
//				con2.commit();
//
//			} catch (SQLException e) {
//				if (con != null) {
//					con.rollback();
//				}			
//				if (con2 != null) {
//					con2.rollback();
//				}			
//				throw e;
//			} finally{
//				if (con != null) {
//					con.setAutoCommit(true);
//					con.close();
//		        }			
//				if (con2 != null) {
//					con2.setAutoCommit(true);
//					con2.close();
//				}			
//			}		
//			
//			
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//		
//	private void CopiarContenidos(Connection con, String schema, List<TrcnMat> list, int nivel) throws Exception {
//
//			//Select numero de auto increment
//			int maxItemMenu = TrcnMat.ObtenerMaxMenuItem(con, schema);
//						
//			//Hacer lista de numero nuevo y numero antes hashmap
//			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
//			int itemMenu = maxItemMenu;
//			for(TrcnMat bean : list){
//				hm.put(bean.getMenuItem(), itemMenu);
//				
//				itemMenu++;
//			}
//			
//			//insert datos 
//			for(TrcnMat bean : list){
//				
//				if(hm.containsKey(bean.getMenuItem())){
//					bean.setMenuItem(hm.get(bean.getMenuItem()));
//				}
//				if(hm.containsKey(bean.getMenuItemParentId())){
//					bean.setMenuItemParentId(hm.get(bean.getMenuItemParentId()));
//				}
//				
//				switch (nivel) {
//					case Common.NIVEL_BACHILLERATO:
//						bean.InsertarBac(con);
//						break;
//					case Common.NIVEL_LICENCIATURA:
//						bean.InsertarLic(con);
//						break;
//					default:
//						return;
//				}
//				
//			}
//
//	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
			if(cveMat == null){
				cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
			}
			
			Materia mat = Materia.Buscar(cveMat);
			request.setAttribute(KEY_VARIABLE_MATERIA, mat);
	
			String s = TrcnMat.CreaMenu(mat, 0, false, false, usuario, false);
			request.setAttribute(KEY_VARIABLE_MENU_HTML, s);

		} catch (Exception e) {
			throw e;
		}
		
	}

}
