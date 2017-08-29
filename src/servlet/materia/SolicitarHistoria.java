package servlet.materia;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.MateriaSolicitud;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;

/**
 * Servlet implementation class SolicitarHistoria
 */
@WebServlet("/SolicitarHistoria")
public class SolicitarHistoria extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "SolicitarHistoria.jsp";
	/** Nombre del variable **/	
	private final String KEY_VALIABLE_SOLICITUDES = "solicitudes";
	/** Nombre del form del cveMat **/
	private final String KEY_REQUEST_PARAM_CVE_MAT = "cveMat";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";

	
	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SolicitarHistoria() {
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
			Error(response, "Error");
//			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
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
    			
				Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);	
    		}
    	} catch (Exception e) {
			Common.Error(e);
			Error(response, "Error");
//			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
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
	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			String cveMat = request.getParameter(KEY_REQUEST_PARAM_CVE_MAT);
			if(cveMat == null || cveMat.equals("")){
				cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
			}
			
			MateriaSolicitud bean = new MateriaSolicitud();
			bean.setCveMat(cveMat);
			List<MateriaSolicitud> list = bean.Buscar();
			
			if(list.size() <= 0){
				request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_NO_HAY_DATOS);
			} else{
				request.setAttribute(KEY_VALIABLE_SOLICITUDES, list);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
