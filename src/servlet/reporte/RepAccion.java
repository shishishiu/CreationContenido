package servlet.reporte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.LogAct;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;

/**
 * Servlet implementation class RepAcceso
 */
@WebServlet("/RepAccion")
public class RepAccion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "RepAccion.jsp";
	/** Nombre del form de nivel **/
	private final String KEY_VARIABLE_EXIST_RESULTADO = "existResultado";
	/** Nombre del form del clave **/
	private final String KEY_VARIABLE_FECHA_INICIAL = "fechaInicial";
	/** Nombre del form del clave **/
	private final String KEY_VARIABLE_FECHA_TERMINAL = "fechaTerminal";
	/** Nombre del form del clave **/
	private final String KEY_VARIABLE_USUARIO = "usuario";
	/** Nombre del form del clave **/
	private final String KEY_VARIABLE_ACCION = "accion";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_TOTAL = "total";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_LOGS_ACT = "logsAct";
	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RepAccion() {
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
    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){
        			
    	   			SetForm(request);
    	   			
    			}else{
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
   					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
    			}
	
    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathReporte() + NOMBRE_DE_PAGINA);
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
    	
			usuario = new Usuario(request, response, true);
    		if(usuario.IsAutorizado()){
 
    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){
    				
    				Buscar(request);
    				
    			}else{
					Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
					
				SetForm(request);
				
    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathReporte() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);		
    		}
    		
    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
		
	}

	private void Buscar(HttpServletRequest request) throws Exception {
		
		List<LogAct> list = new ArrayList<LogAct>();
		LogAct bean = new LogAct();
		
		bean.setFecLogInicial(request.getParameter(KEY_VARIABLE_FECHA_INICIAL));
		bean.setFecLogTerminal(request.getParameter(KEY_VARIABLE_FECHA_TERMINAL));
		bean.setCveUsu(request.getParameter(KEY_VARIABLE_USUARIO));
		bean.setActLogAct(request.getParameter(KEY_VARIABLE_ACCION));
		
		int count = bean.Count();
		if(count > 1000){
			
			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_SE_ENCUENTRA_DEMACIADOS_DATOS);			
			return;
		}
		
		list = bean.Buscar();
		request.setAttribute(KEY_VARIABLE_LOGS_ACT, list);
		request.setAttribute(KEY_VARIABLE_TOTAL, list.size());
		request.setAttribute(KEY_VARIABLE_EXIST_RESULTADO, list.size() > 0);
		
		
	}

	private void SetForm(HttpServletRequest request) {

		request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
		String fechaInicial = request.getParameter(KEY_VARIABLE_FECHA_INICIAL);
		request.setAttribute(KEY_VARIABLE_FECHA_INICIAL, fechaInicial);
		String fechaTerminal = request.getParameter(KEY_VARIABLE_FECHA_TERMINAL);
		request.setAttribute(KEY_VARIABLE_FECHA_TERMINAL, fechaTerminal);
	
		String usuario = request.getParameter(KEY_VARIABLE_USUARIO);
		request.setAttribute(KEY_VARIABLE_USUARIO, usuario);
		
		String accion = request.getParameter(KEY_VARIABLE_ACCION);
		request.setAttribute(KEY_VARIABLE_ACCION, accion);

	}

}
