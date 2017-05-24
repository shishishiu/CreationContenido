package servlet.materia;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.TrcnMat;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.string.StringUtil;

/**
 * Servlet implementation class BusContenido
 */
@WebServlet("/Contenido")
public class Contenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "Contenido.jsp";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_CVE_CONTENIDO = "cveCont";
	/** Nombre del variable **/	
	private final String KEY_VARIABLE_CONTENIDO_HTML = "contenidoHtml";

	
	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Contenido() {
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
    			SetForm(request);
	
    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);
    		}

    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void Inicializar() {
		
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			String cveCont = request.getParameter(KEY_REQUEST_PARAM_CVE_CONTENIDO);
			if(StringUtil.isNumber(cveCont)){
				TrcnMat bean = TrcnMat.Buscar(Integer.parseInt(cveCont));
				
				String strClob = bean.getContenido();
				request.setAttribute(KEY_VARIABLE_CONTENIDO_HTML, strClob);
			}

		} catch (Exception e) {
			throw e;
		}
		
	}

}
