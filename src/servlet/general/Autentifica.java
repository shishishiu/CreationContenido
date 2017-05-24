package servlet.general;

import java.io.IOException;

import java.sql.SQLException;

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
 * Servlet implementation class Autentifica
 */
@WebServlet("/Autentifica")
public class Autentifica extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de esta página **/
	private final String NOMBRE_DE_PAGINA = "Autentifica.jsp";
	/** Nombre del form de IdUsu **/
	private final String NOMBRE_DE_FORM_ID_USU = "IdUsu";
	/** Nombre del form del password **/
	private final String NOMBRE_DE_FORM_PASSWORD_USU = "PasswordUsu";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Autentifica() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		try{
			Configuracion config = new Configuracion();
			rd = getServletConfig().getServletContext().getRequestDispatcher(
					config.getPathFileRoot() + NOMBRE_DE_PAGINA);
		    rd.forward(request,response);
			
		} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		try {
			if(Login(request)){
				response.sendRedirect(getServletConfig().getServletContext().getContextPath() 
						+ Common.SLASH + Common.NOMBRE_DE_PAGINA_BUS_MATERIA);
			}else{
				response.sendRedirect(getServletConfig().getServletContext().getContextPath()
						+ Common.SLASH + Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR);
			}
		} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
		
	}

	/**
	 * Entrar al sistema
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private boolean Login(HttpServletRequest request) throws Exception {
		Usuario u = new Usuario();
		
		u.setCveUsu(request.getParameter(NOMBRE_DE_FORM_ID_USU));
		u.setPwsUsu(request.getParameter(NOMBRE_DE_FORM_PASSWORD_USU));
		try {
			if(u.Login(request)){
				
				return true;
			}else{
				return false;
			}
			
		} catch (SQLException e) {
			throw e;
		}
	}

}
