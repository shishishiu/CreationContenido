package servlet.conf;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;

/**
 * Servlet implementation class ModificaPlantilla
 */
@WebServlet("/ModificaConfiguracion")
public class ModificaConfiguracion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "ModificaConfiguracion.jsp";
	/** Nombre del form **/
	private final String KEY_VARIABLE_PROP_LIST = "propList";
	/** Nombre del form **/
	private final String KEY_VARIABLE_PUEDE_MOSTRAR = "puedeMostrar";
	/** Nombre del form **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del param **/
	private final String KEY_VARIABLE_KEY = "key";
	/** Nombre del param **/
	private final String KEY_VARIABLE_VAL = "val";
	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificaConfiguracion() {
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

    			if(usuario.isAdministradorGeneral()){
        			
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, true);
        			SetForm(request);

    			}else{
   					request.setAttribute(KEY_VARIABLE_PUEDE_MOSTRAR, false);
   					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_ERROR_AUTENTIFICA);
    			}


    			Configuracion config = new Configuracion();
    			rd = getServletConfig().getServletContext().getRequestDispatcher(
    					config.getPathConf() + NOMBRE_DE_PAGINA);
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
    			if(usuario.isAdministradorGeneral()){
        			
        			Modificar(request, response);

    			}else{
    				Common.MsgJson(Common.JSON_STATUS_NO_AUTORIZADO, Common.MENSAJE_ERROR, response);
    			}
				
				
    		}
    		
    	} catch (Exception e) {
			Common.Error(e);
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
		}
	}
	
	

	private void Modificar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = request.getParameter(KEY_VARIABLE_KEY);
		String val = request.getParameter(KEY_VARIABLE_VAL);
		
		Configuracion conf = new Configuracion();
		try {
			
			conf.Write(key, val, usuario);
			
			Common.MsgJson("success", Common.MENSAJE_TERMINAR_PROCESO, response);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {
			
			Configuracion conf = new Configuracion();
			List<String[]> list = conf.getList();
			
			request.setAttribute(KEY_VARIABLE_PROP_LIST, list);


		} catch (Exception e) {
			throw e;
		}
		
	}



}
