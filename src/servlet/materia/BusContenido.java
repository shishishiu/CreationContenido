package servlet.materia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.TrcnMat;
import beans.Materia;
import beans.PermisoContenido;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class BusContenido
 */
@WebServlet("/BusContenido")
public class BusContenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "BusContenido.jsp";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_NIVEL_LICEN = "nivelLicen";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_NIVEL_BACHI = "nivelBachi";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_PERMISO_MODIFICAR = "permisoModificar";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISO_ADMINISTRADOR_GENERAL = "perAdminGeneral";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PERMISO_ADMINISTRADOR = "perAdmin";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MENU_HTML = "menuHtml";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_CONT = "hiddenCveCont";
	/** Nombre del form **/
	private final String VALUE_HIDDEN_TIPO_ORDENAR = "1";
	/** Nombre del form **/
	private final String VALUE_HIDDEN_TIPO_BAJAR = "2";

	
	/** Usuario **/
	public Usuario usuario;
	/** Materia **/
	public Materia materia;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BusContenido() {
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
   				Inicializar();
   				if(!TieneAutoridad(request)){
   					response.sendRedirect(Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR);
   					return;
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


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher rd = null;	
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			
    			String hiddenTipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
    			
    			if(hiddenTipo.equals(VALUE_HIDDEN_TIPO_BAJAR)){
    				Bajar(request);
    				this.materia = null;
    			}    			
    			else if(hiddenTipo.equals(VALUE_HIDDEN_TIPO_ORDENAR)){
    				Ordenar(request);
    			}    			

    			SetForm(request);

    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);		
    		}
    	} catch (Exception e) {
			Common.Error(e);
			if(Common.IsAjax(request)){
				Error(response, Common.MENSAJE_ERROR);
			}else{
				response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
			}
    	}
    		
	}

	private void Ordenar(HttpServletRequest request) throws Exception {
		String[] str = request.getParameterValues("menuOrdens[]");
		if(str != null){
			Connection con;
			con = MySqlConnector.getConnection();

			try {

				for(int i=0; i<str.length; i++){
				
					String menuOrden = str[i];
					String menuId = menuOrden.substring(menuOrden.indexOf("_")+1);
					
	
					con.setAutoCommit(false);
					TrcnMat bean = new TrcnMat();
					bean.setMenuItem(Integer.parseInt(menuId));
					bean.setOrden(i+1);
					
					bean.Modificar(con);
	
					Common.InsertLogAct(request, con, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_MODIFICAR,menuId));
						
				}
			
				con.commit();

			} catch (SQLException e) {
				if (con != null) {
					con.rollback();
				}			
				throw e;
			} finally{
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
		        }			
			}		

		}
		
	}

	/**
	 * Regresar a la pantalla con error
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	private void Error(HttpServletResponse response, String msg) throws IOException {
		String json = "{\"status\":\"error\",\"message\": \"" + msg + "\"}";
		response.setContentType("application/json;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();
		out.print(json);
		
	}

	/**
	 * Dar baja del contenido
	 * @param request
	 * @throws Exception
	 */
	private void Bajar(HttpServletRequest request) throws Exception{
		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);

			String strCveCont = request.getParameter(KEY_FORM_HIDDEN_CVE_CONT);
			int cveCont = Integer.parseInt(strCveCont);
			TrcnMat bean = TrcnMat.Buscar(cveCont);
			bean.DarBaja(con);
			bean.EliminarContArchivo();
			
			if(bean.getParentContName().indexOf(Common.CAPTION_CONTENIDO_CONTENIDO_MATERIA) >= 0){
				if(bean.getCaption().indexOf(Common.CAPTION_CONTENIDO_UNIDAD) >= 0){

					int countUnidad = TrcnMat.ContarUnidad(bean.getCveMat());

					Materia matbean = Materia.Buscar(bean.getCveMat());
					matbean.setUnidad(countUnidad-1);
					matbean.Modificar(con);
				}
			}
			
		
			Common.InsertLogAct(request, con, usuario.getCveUsu(), MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_DAR_BAJA, cveCont));
			con.commit();
		} catch (Exception e) {
			if (con != null) {
				con.rollback();
			}			
			throw e;
		} finally{
			if (con != null) {
				con.setAutoCommit(true);
				con.close();
	        }			
		}
		
	}


	private void Inicializar() {
		this.materia = null;
		
	}

	/**
	 * Confirmar si tenga acceso
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean TieneAutoridad(HttpServletRequest request) throws Exception {
		
		String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
		if(cveMat == null){
			cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		}
		PermisoContenido bean = new PermisoContenido();
		bean.setCveUsu(usuario.getCveUsu());
		bean.setCveMat(cveMat);
		try {
			if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR 
					|| usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){

				request.setAttribute(KEY_VARIABLE_PERMISO_MODIFICAR, true);				
				return true;
			}
			
			List<PermisoContenido> list = bean.Buscar();
			
			if(list.size() <= 0){
				return false;
			}else if(list.get(0).getPermiso() == Common.PERMISO_CONTENIDO_MODIFICAR){				
				request.setAttribute(KEY_VARIABLE_PERMISO_MODIFICAR, true);				
				return true;
				
			}else if(list.get(0).getPermiso() == Common.PERMISO_CONTENIDO_VER){
				request.setAttribute(KEY_VARIABLE_PERMISO_MODIFICAR, false);
				return true;
				
			}else{
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
		
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
			if(cveMat == null){
				cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
			}
	
			if(this.materia == null){
					this.materia = Materia.Buscar(cveMat);
			}
			request.setAttribute(KEY_VARIABLE_MATERIA, this.materia);

			request.setAttribute(KEY_VARIABLE_NIVEL_LICEN, Common.NIVEL_LICENCIATURA);
			request.setAttribute(KEY_VARIABLE_NIVEL_BACHI, Common.NIVEL_BACHILLERATO);
			
			request.setAttribute(KEY_VARIABLE_PERMISO_ADMINISTRADOR_GENERAL, Common.PERMISO_ADMINISTRADOR_GENERAL);
			request.setAttribute(KEY_VARIABLE_PERMISO_ADMINISTRADOR, Common.PERMISO_ADMINISTRADOR);
			
			boolean puedever = false;
			boolean puedemodificar = false;
			if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR
				||usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
				puedever = true;
				puedemodificar = true;
						
			}else{
				int per = PermisoContenido.Buscar(cveMat, usuario.getCveUsu());
				if(per == 1) {
					puedever = true;
					puedemodificar = false;
				}
				if(per== 2){
					puedever = true;
					puedemodificar = true;
				}
			}
			
			int estadoSolicitud = this.materia.getMatSol().getEstadoSolicitud();
			switch (usuario.getPerUsu()) {
				case Common.PERMISO_ASESOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_PENDIENTE){
						puedemodificar = false;
					}
					break;
				case Common.PERMISO_REVISOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_SOLICITADO){
						puedemodificar = false;
					}					
					break;
				case Common.PERMISO_ADMINISTRADOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_REVISADO && 
					estadoSolicitud != Common.ESTADO_DE_SOLICITUD_VALIDADO){
						puedemodificar = false;
					}					
					break;
				default:
					break;
			}
			
			String s = TrcnMat.CreaMenu(this.materia, 0, puedever, puedemodificar, usuario, false);
			request.setAttribute(KEY_VARIABLE_MENU_HTML, s);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
