package servlet.general;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.LogAcc;
import util.common.Common;
import util.conf.CkEditorPathBuilder;

/**
 * Servlet implementation class CerrarSesion
 */
@WebServlet("/CerrarSesion")
public class CerrarSesion extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CerrarSesion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Salir(request);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath()
					+ Common.SLASH + Common.NOMBRE_DE_PAGINA_AUTENTIFICA);
		} catch (Exception e) {		
			Common.Error(e);
			response.sendRedirect(
					getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
	}
	
	/**
	 * Salir del sistema
	 * @param request
	 * @throws Exception
	 */
	private void Salir(HttpServletRequest request) throws Exception {
		HttpSession sesion = request.getSession(true);
		
		if(sesion.getAttribute(Common.SESSION_NAME_USUARIO) != null){
			LogAcc l = new LogAcc();
			l.setCveUsu(sesion.getAttribute(Common.SESSION_NAME_USUARIO).toString());
			l.setIdSession(sesion.getId());
			try {
				l.CrearSalida();
			} catch (SQLException e) {
				throw e;
			}

			sesion.removeAttribute(Common.SESSION_NAME_USUARIO);
			sesion.removeAttribute(CkEditorPathBuilder.SESSION_CKEDITOR_CONT_ABSOLUTE_PATH);
			sesion.removeAttribute(CkEditorPathBuilder.SESSION_CKEDITOR_CONT_URL);
		}

	}
}
