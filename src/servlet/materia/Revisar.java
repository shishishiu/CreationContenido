package servlet.materia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.*;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class Revision
 */
@WebServlet("/Revisar")
public class Revisar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "Revisar.jsp";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form de materias **/
	private final String KEY_VARIABLE_FECHA_SOLICITUD = "fechaSolicitud";
	/** Nombre del form del usuario **/
	private final String KEY_VARIABLE_USUARIO = "usuario";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_TIENE_AUTORIDAD = "tieneAutoridad";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_RADIO_REVISADO = "radioRevisado";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_RADIO_PENDIENTE = "radioPendiente";
	/** Nombre del form del cveMat **/
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del form **/
	private final String KEY_FORM_NOTA = "nota";
	/** Nombre del form **/
	private final String KEY_FORM_RESULTADO = "resultado";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";

	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServletHttpServlet()
     */
    public Revisar() {
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
    		if(usuario.IsAutorizado()) {
				if(usuario.getPerUsu() == Common.PERMISO_REVISOR ||
				 usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
	    			Iniciar();
	    			SetForm(request);
		    		
		    		} else{
		
		    			request.setAttribute(KEY_VARIABLE_TIENE_AUTORIDAD, false);
		    			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
		    		}

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
	 * Iniciar
	 */
	private void Iniciar() {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
				if(usuario.getPerUsu() == Common.PERMISO_REVISOR ||
	    		 usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
	 								
					if(request.getParameter(KEY_FORM_HIDDEN_TIPO).equals("1")){
						RevisarMateria(request);
					}

					SetForm(request);
					
	    		} else{
	
	    			request.setAttribute(KEY_VARIABLE_TIENE_AUTORIDAD, false);
	    			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
	    		}

    		
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
	

	private void RevisarMateria(HttpServletRequest request) throws Exception {
		String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		
		if(Validaction(request, cveMat)){

			InsertarSolicitud(request, cveMat);
			
		}
		
		
	}

	private boolean Validaction(HttpServletRequest request, String cveMat) throws Exception {
		List<MateriaSolicitud> list = null;
		
		try {
			
			MateriaSolicitud bean = new MateriaSolicitud();
			bean.setCveMat(cveMat);
			list = bean.Buscar();
			if(list.size()>0 && 
					list.get(0).getEstadoSolicitud() != Common.ESTADO_DE_SOLICITUD_SOLICITADO){
				
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_SOLICITAR_HA_REVISADO);
				return false;
			
			}
			
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	private void InsertarSolicitud(HttpServletRequest request, String cveMat) throws Exception {

		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);

			int resultado = Integer.parseInt(request.getParameter(KEY_FORM_RESULTADO));
			
			MateriaSolicitud bean = new MateriaSolicitud();
			bean.setCveMat(cveMat);
			bean.setNota(request.getParameter(KEY_FORM_NOTA));
			bean.setResultado(resultado);
			bean.setEstadoSolicitud(resultado);
			bean.setUsuarioSolicitud(usuario.getCveUsu());
			
			bean.Insertar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_REVISAR,cveMat));
	
			
			con.commit();
			
			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);
			
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

	/**
	 * Set form de la pantalla
	 * @param request
	 * @throws Exception 
	 */
	private void SetForm(HttpServletRequest request) throws Exception {
		try {
	
   			request.setAttribute(KEY_VARIABLE_USUARIO, usuario);

			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
			if(cveMat == null || cveMat.equals("")){
				cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
			}
			
			Materia bean = Materia.Buscar(cveMat);
			
			request.setAttribute(KEY_VARIABLE_MATERIA, bean);
			
			Date date = new Date();
	        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

	        request.setAttribute(KEY_VARIABLE_FECHA_SOLICITUD, sdf1.format(date));
	
   			request.setAttribute(KEY_VARIABLE_TIENE_AUTORIDAD, true);

			request.setAttribute(KEY_VARIABLE_RADIO_REVISADO, Common.ESTADO_DE_SOLICITUD_REVISADO);
			request.setAttribute(KEY_VARIABLE_RADIO_PENDIENTE, Common.ESTADO_DE_SOLICITUD_PENDIENTE);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	

}
