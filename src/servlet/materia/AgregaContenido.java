package servlet.materia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class BusContenido
 */
@WebServlet("/AgregaContenido")
public class AgregaContenido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "AgregaContenido.jsp";
	/** Nombre del variable **/	
	private final String KEY_REQUEST_PARAM_PADRE_ID = "padreId";
	/** Nombre del variable **/	
	private final String KEY_VALIABLE_CVE_MAT = "cveMat";
	/** Nombre del variable **/	
	private final String KEY_VALIABLE_PADRE_ID = "padreId";
	/** Nombre del form **/
	private final String KEY_FORM_CVE_MAT = "cveMat";
	/** Nombre del form **/
	private final String KEY_FORM_PADRE_ID = "padreId";
	/** Nombre del form **/
	private final String KEY_FORM_CAPTION = "caption";
	/** Nombre del form **/
	private final String KEY_FORM_UNIDAD = "unidad";
	/** Nombre del form **/
	private final String KEY_FORM_APARTADO = "apartado";
	/** Nombre del key **/
	private final String KEY_VARIABLE_MATERIA_UNIDAD = "materiaUnidad";
	/** Nombre del key **/
	private final String KEY_VARIABLE_UNIDAD = "unidad";
	/** Nombre del key **/
	private final String KEY_VARIABLE_APARTADO = "apartado";

	
	/** Usuario **/
	public Usuario usuario;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AgregaContenido() {
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
			Error(response, Common.MENSAJE_ERROR);
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
    			
				if(Agregar(request, response)){    			
	   				Configuracion config = new Configuracion();
					rd = getServletConfig().getServletContext().getRequestDispatcher(
							config.getPathMateria() + NOMBRE_DE_PAGINA);
				    rd.forward(request,response);	
				}
    		}
    	} catch (Exception e) {
			Common.Error(e);
			Error(response, Common.MENSAJE_ERROR);
		}
	}

	private boolean Agregar(HttpServletRequest request, HttpServletResponse response) throws Exception{

		int parentId = Integer.parseInt(request.getParameter(KEY_FORM_PADRE_ID));
		String cveMat = request.getParameter(KEY_FORM_CVE_MAT);
		String caption = request.getParameter(KEY_FORM_CAPTION);
		String strUnidad = request.getParameter(KEY_FORM_UNIDAD);
		int unidad = 0;
		if(StringUtil.isNumber(strUnidad) && Integer.parseInt(strUnidad)>0){
			unidad = Integer.parseInt(strUnidad);
		}
		String strApartado = request.getParameter(KEY_FORM_APARTADO);
		int apartado = 0;
		if(StringUtil.isNumber(strApartado)){
			apartado = Integer.parseInt(strApartado);
		}
		String path = "";
		
		//Validacion
		TrcnMat bean;
		bean = new TrcnMat();
		bean.setCveMat(cveMat);
		bean.setMenuItemParentId(parentId);
		bean.setCaption(caption);
		List<TrcnMat> list = bean.Buscar();

		if(list.size() > 0){
			Error(response, Common.MENSAJE_ERROR_MISMA_CAPTION);
			return false;
		}

		TrcnMat bean2 = TrcnMat.Buscar(parentId);
		String parentName = bean2.getParentContName() + Common.HYPHEN + bean2.getCaption();

		
		//Insartar
		Connection con = MySqlConnector.getConnection();
		try {
			con.setAutoCommit(false);
			
			bean = new TrcnMat();
			bean.setCveMat(cveMat);
			bean.setCaption(caption);
			bean.setMenuItemParentId(parentId);
			if(unidad > 0){
				bean.setUnidad(unidad);

				if(parentName.indexOf(Common.CAPTION_CONTENIDO_CONTENIDO_MATERIA) >= 0){
					if(caption.indexOf(Common.CAPTION_CONTENIDO_UNIDAD) >= 0){
						bean.setOrden(unidad);
					}
				}
				
			}
			if(apartado >0){
				bean.setApartado(apartado);
			}
			bean.setHandler(Common.GetHandler(bean));
						
			int cveContenido = bean.Insertar(con);
			
			Common.InsertLogAct(request, con, usuario.getCveUsu(), MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_AGREGAR, cveContenido));			

			// Si se agrega una unidad, modifica la materia
			if(parentName.indexOf(Common.CAPTION_CONTENIDO_CONTENIDO_MATERIA) >= 0){
				if(caption.indexOf(Common.CAPTION_CONTENIDO_UNIDAD) >= 0){
					Materia matbean = Materia.Buscar(cveMat);
					matbean.setUnidad(unidad);
					matbean.Modificar(con);
				}
			}

			
			con.commit();			

		} catch (Exception e) {
			if (con != null) {
				con.rollback();
			}			
			DeleteArchivo(path);
			
			throw e;
		} finally{
			if (con != null) {
				con.setAutoCommit(true);
				con.close();
	        }			
		}
		return true;
	}

	private void DeleteArchivo(String path) {
		File file = new File(path);

	    if (file.exists()){
	      if (!file.delete()){
	    	  
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

	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			String padreId = request.getParameter(KEY_REQUEST_PARAM_PADRE_ID);
			TrcnMat contenido = TrcnMat.Buscar(Integer.parseInt(padreId));
			String cveMat = contenido.getCveMat();
			Materia bean = Materia.Buscar(cveMat);
			request.setAttribute(KEY_VALIABLE_CVE_MAT, cveMat);
			request.setAttribute(KEY_VALIABLE_PADRE_ID, padreId);
			request.setAttribute(KEY_VARIABLE_MATERIA_UNIDAD, bean.getUnidad());			
			
			request.setAttribute(KEY_VARIABLE_UNIDAD, contenido.getUnidad());

			int countUnidad = TrcnMat.ContarUnidad(cveMat);
			
			request.setAttribute("countUnidad", countUnidad);
			request.setAttribute("padreCaption", contenido.getCaption());
			request.setAttribute("contenidoMateria", Common.CAPTION_CONTENIDO_CONTENIDO_MATERIA);
			request.setAttribute("captionUnidad", Common.CAPTION_CONTENIDO_UNIDAD);

			
			int apartado = 0;
			if(contenido.getUnidad()>0 && contenido.getApartado() == 0){
				apartado = contenido.GetMaxApartado();

			} else{
				apartado = contenido.getApartado();
			}
			
			request.setAttribute(KEY_VARIABLE_APARTADO, apartado);
			
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
