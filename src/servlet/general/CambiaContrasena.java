package servlet.general;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Usuario;
import util.common.Common;
import util.common.Crypto;
import util.conf.Configuracion;
import util.db.MySqlConnector;

/**
 * Servlet implementation class CambiaContrasena
 */
@WebServlet("/CambiaContrasena")
public class CambiaContrasena extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de esta página **/
	private final String NOMBRE_DE_PAGINA = "CambiaContrasena.jsp";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_CONTRASENA = "pwsUsu";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_USU = "usu";

	Configuracion config = new Configuracion();
	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiaContrasena() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		try{
			usuario = new Usuario(request, response);
			if(usuario.IsAutorizado()){
				
				try {
		    		
					SetForm(request);

					rd = getServletConfig().getServletContext().getRequestDispatcher(
							config.getPathFileRoot() + NOMBRE_DE_PAGINA);
				    rd.forward(request,response);
					

					
				} catch (Exception e) {
					Common.Error(e);
					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
					return;
				}
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
				
				if(ModificaContrasena(request)){
					
					
				}else{
				}
				
				SetForm(request);
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathFileRoot() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);
			    return;

			}
			
		} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
	}

	private boolean ModificaContrasena(HttpServletRequest request) throws Exception {

		if(request.getParameter(KEY_FORM_CONTRASENA) != null || !request.getParameter(KEY_FORM_CONTRASENA).equals("")){
			if(request.getParameter(KEY_FORM_CONTRASENA).length() > 21){
				String message = "Ingresa menos de 21 letras en la contraseña, por favor<br>";
				request.setAttribute(KEY_VARIABLE_MESSAGE, message);
				return false;
			}
		}
		
		
		Connection con = MySqlConnector.getConnection();
		try {

			String password = request.getParameter(KEY_FORM_CONTRASENA);
			String passwordCrypto = "";
			
			HttpSession sesion = request.getSession(true);
			String cveUsu = sesion.getAttribute(Common.SESSION_NAME_USUARIO).toString();
			
			con.setAutoCommit(false);

			if(password != null && !password.equals("")){

				Usuario bean = Usuario.Buscar(cveUsu);
				passwordCrypto = Crypto.getStretchedPassword(password, cveUsu);
				bean.setPwsUsu(passwordCrypto);
				bean.Modificar(con);
				
			}
			
			con.commit();

			request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);

			return true;

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

	private void SetForm(HttpServletRequest request) throws Exception {
		HttpSession sesion = request.getSession(true);
		String cveUsu = sesion.getAttribute(Common.SESSION_NAME_USUARIO).toString();

			try {

				Usuario bean = Usuario.Buscar(cveUsu);
				request.setAttribute(KEY_VARIABLE_USU, bean);			

			} catch (Exception e) {
				throw e;
			}		
		
	}


}
