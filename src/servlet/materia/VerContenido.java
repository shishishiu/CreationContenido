package servlet.materia;

import java.io.IOException;
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
import util.string.StringUtil;

/**
 * Servlet implementation class BusContenido
 */
@WebServlet("/VerContenido")
public class VerContenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String NOMBRE_DE_PAGINA_VER_CONTENIDO = "VerContenido.jsp";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_MATERIA = "materia";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_CONTENIDO = "contenido";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_CVE_CONTENIDO = "cveContenido";
	/** Nombre del form de materia **/
	private final String KEY_VARIABLE_PERMISO_MODIFICAR = "permisoModificar";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_MAT = "hiddenCveMat";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_CVE_CONTENIDO = "hiddenCveCont";
	/** Nombre del form **/
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";

	
	/** Usuario **/
	public Usuario usuario;
	/** Materia **/
	public Materia materia;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerContenido() {
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
   					return;
   				}
   				
   				if(request.getParameter(KEY_FORM_HIDDEN_TIPO) != null
   						&& request.getParameter(KEY_FORM_HIDDEN_TIPO).equals("1")){
   					DarBajaContenido(request);
   				}
   				
    			SetForm(request);
	
    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA_VER_CONTENIDO);
			    rd.forward(request,response);
    		}

    	} catch (Exception e) {
			Common.Error(e);
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}	
    }

	/**
	 * Dar baja un contenido
	 * @param request
	 * @throws Exception
	 */
	private void DarBajaContenido(HttpServletRequest request) throws Exception {
		String cveContenido = request.getParameter(KEY_FORM_HIDDEN_CVE_CONTENIDO);

		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);
			TrcnMat bean = TrcnMat.Buscar(Integer.parseInt(cveContenido));
			bean.DarBaja(con);
			bean.EliminarContArchivo();
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), 
					MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_DAR_BAJA,cveContenido));
			
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void Inicializar() {
		this.materia = null;
		
	}

	private boolean TieneAutoridad(HttpServletRequest request) throws Exception {
		request.setAttribute(KEY_VARIABLE_PERMISO_MODIFICAR, true);

		String cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
		PermisoContenido bean = new PermisoContenido();
		bean.setCveUsu(usuario.getCveUsu());
		bean.setCveMat(cveMat);
		try {
			if(usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR 
					|| usuario.getPerUsu() == Common.PERMISO_ADMINISTRADOR_GENERAL){
				return true;
			}
			
			List<PermisoContenido> list = bean.Buscar();
			Materia matBean = Materia.Buscar(cveMat);
			
			if(list.size() <= 0){
				return false;
			}else if(list.get(0).getPermiso() == Common.PERMISO_CONTENIDO_MODIFICAR){				

				int estadoSolicitud = matBean.getMatSol().getEstadoSolicitud();
				switch (usuario.getPerUsu()) {
				case Common.PERMISO_ASESOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_PENDIENTE){
						request.setAttribute(KEY_VARIABLE_PERMISO_MODIFICAR, false);				
					}
					break;
				case Common.PERMISO_REVISOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_SOLICITADO){
						request.setAttribute(KEY_VARIABLE_PERMISO_MODIFICAR, false);				
					}					
					break;
				case Common.PERMISO_ADMINISTRADOR:
					if(estadoSolicitud != Common.ESTADO_DE_SOLICITUD_REVISADO){
						request.setAttribute(KEY_VARIABLE_PERMISO_MODIFICAR, false);				
					}					
					break;
				default:
					break;
				}
				
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

			String cveMat;
			cveMat = request.getParameter(KEY_FORM_HIDDEN_CVE_MAT);
			String cveContenido;
			cveContenido = request.getParameter(KEY_FORM_HIDDEN_CVE_CONTENIDO);
	
			if(this.materia == null){
				this.materia = Materia.Buscar(cveMat);
			}
			
			if(StringUtil.isNumber(cveContenido)){
				TrcnMat bean = TrcnMat.Buscar(Integer.parseInt(cveContenido));
				request.setAttribute(KEY_VARIABLE_CONTENIDO, bean);
			}
			
			request.setAttribute(KEY_VARIABLE_MATERIA, this.materia);
			request.setAttribute(KEY_VARIABLE_CVE_CONTENIDO, cveContenido);

			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
