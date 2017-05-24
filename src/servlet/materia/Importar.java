package servlet.materia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.sql.Clob;
import java.sql.Connection;
import java.text.MessageFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Materia;
import beans.Nivel;
import beans.NivelGrado;
import beans.NivelGradoCombos;
import beans.TrcnMat;
import beans.Usuario;
import util.common.Common;
import util.conf.Configuracion;
import util.db.MySqlConnector;
import util.string.StringUtil;

/**
 * Servlet implementation class Importar
 */
@WebServlet("/Importar")
public class Importar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Nombre de la página **/
	private final String NOMBRE_DE_PAGINA = "Importar.jsp";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_NIVELES = "niveles";
	/** Nombre del form del permiso **/
	private final String KEY_VARIABLE_MESSAGE = "message";
	/** Nombre del form del nombre **/
	private final String KEY_VARIABLE_NOMBRE = "nombre";
	/** Nombre del form de niveles **/
	private final String KEY_VARIABLE_NIVELES_GRADO = "nivelGrados";
	/** Nombre del form de nivel **/
	private final String KEY_FORM_NIVEL = "nivel";
	/** Nombre del form del nombre **/
	private final String KEY_FORM_NOMBRE = "cveMat";
	/** Nombre del form del nombre de la materia **/
	private final String KEY_FORM_NIVEL_GRADO = "nivelGrado";
	/** Nombre del variable **/	
	private final String KEY_FORM_HIDDEN_TIPO = "hiddenTipo";
	/** Nombre del variable **/	
	private final String KEY_TIPO_VALIDACION = "1";
	/** Nombre del variable **/	
	private final String KEY_TIPO_IMPORTAR = "2";

	/** Lista del nivel **/
	private List<Nivel> listaNivel;
	/** Lista del nivel grado **/
	private NivelGradoCombos ngc;

	/** Usuario **/
	public Usuario usuario;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Importar() {
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
			response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;	
		String tipo = request.getParameter(KEY_FORM_HIDDEN_TIPO);
		
		try {
    	
			usuario = new Usuario(request, response);
    		if(usuario.IsAutorizado()){
    			if(usuario.isAdministrador() || usuario.isAdministradorGeneral()){

    				String cveMat = request.getParameter(KEY_FORM_NOMBRE);
    				String nivel = request.getParameter(KEY_FORM_NIVEL);
    				if(tipo.equals(KEY_TIPO_VALIDACION)){
    					Validacion(request, response, cveMat, nivel);
   						return;
    					
    				}else if(tipo.equals(KEY_TIPO_IMPORTAR)){
        				ImportarMateria(cveMat, nivel, request);
    					request.setAttribute(KEY_VARIABLE_MESSAGE, Common.MENSAJE_TERMINAR_PROCESO);
    				}
    				
    			}else{
 
					Common.Error(Common.MENSAJE_ERROR_AUTENTIFICA);

					if(Common.IsAjax(request)){
        				Common.MsgJson(Common.JSON_STATUS_NO_AUTORIZADO, Common.NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR, response);
    					
    				} else{
    					response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
    					
    				}
    				
        			return;
        		}
    			
    			SetForm(request);

    			Configuracion config = new Configuracion();
				rd = getServletConfig().getServletContext().getRequestDispatcher(
						config.getPathMateria() + NOMBRE_DE_PAGINA);
			    rd.forward(request,response);		
    		
    		}
    	} catch (Exception e) {
			Common.Error(e);

			if(Common.IsAjax(request)){
				Common.MsgJson("error", Common.MENSAJE_ERROR, response);
				
			} else{
				response.sendRedirect(getServletConfig().getServletContext().getContextPath() + Common.getErrorPage());
				
			}

		}
    		
	}
	
	private boolean ImportarMateria(String cveMat, String nivel, HttpServletRequest request) throws Exception{

		//importar base de datos
		Insertar(cveMat, nivel, request);
		
		return true;
	}

	private void Insertar(String cveMat, String strNivel, HttpServletRequest request) throws Exception {
		Connection conConVirt = null;
		Connection con = null;
		
		try{
			
			int nivel = Integer.parseInt(strNivel);

			switch (nivel) {
				case Common.NIVEL_BACHILLERATO:
					con = MySqlConnector.getConnectionBac();
					break;
				case Common.NIVEL_LICENCIATURA:
					con = MySqlConnector.getConnectionLic();
					break;
				default:
					break;
			}

			conConVirt = MySqlConnector.getConnection();
			conConVirt.setAutoCommit(false);

			String[] arrCveMat = cveMat.split(",");
			for(String materia : arrCveMat){
				
				//Si hay misma materia en este sistema, se la borra
				Materia beanExistMat = Materia.Buscar(materia);
				if(!beanExistMat.getCveMat().equals("")){
					Materia.Delete(conConVirt, materia);
					Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_MATERIA_BORRAR, materia));
				}
				
				//Si hay contenidos de la misma materia en este sistema, se los borra
				List<TrcnMat> listExistCont = TrcnMat.Buscar(materia);
				if(listExistCont.size()>0){
					TrcnMat.Delete(conConVirt, materia);
					Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_BORRAR, materia));
				}
				

				Nivel beanNiv = Nivel.Buscar(nivel);
				int nivGrado = Integer.parseInt(request.getParameter(KEY_FORM_NIVEL_GRADO + nivel));
				NivelGrado beanNivGrado = NivelGrado.BuscarConCveNivGrado(nivGrado);
				
				//Insertar materia
				Materia beanMat = Materia.BuscarDeExterno(con, materia);
				beanMat.setNivelGrado(nivGrado);
				beanMat.setNomNivelDir(beanNiv.getNomDir());
				beanMat.setNomGrado(beanNivGrado.getNomGrado());
				
				beanMat.Insertar(conConVirt);
				Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
						MessageFormat.format(Common.TEXTO_ACTION_LOG_MATERIA_IMPORTAR, materia));
				
				//Insertar contenidos
				List<TrcnMat> listCont = TrcnMat.BuscarDeExterno(con, materia);
				for(TrcnMat beanCont : listCont){
					String handler = beanCont.getHandler();
					beanCont.setContenido(GetHtml(beanMat, handler, conConVirt));
					int cveCont = beanCont.Insertar(conConVirt);
					Common.InsertLogAct(request, conConVirt, usuario.getCveUsu(), 
							MessageFormat.format(Common.TEXTO_ACTION_LOG_CONTENIDO_IMPORTAR, cveCont));
				}	
			}
			
			conConVirt.commit();

		} catch (Exception e) {
			if (conConVirt != null) {
				conConVirt.rollback();
			}			
			throw e;
		} finally{
			if (con != null) {
				con.close();
	        }			
			if (conConVirt != null) {
				conConVirt.setAutoCommit(true);
				conConVirt.close();
			}			
		}		
		
	}

	private Clob GetHtml(Materia materia, String handler, Connection conConVirt) throws Exception {
		Clob myClob;

		try {

			if(handler != null && !handler.equals("") && handler.indexOf(Common.EXTENSION_JSP)>=0){
			
				String pathMat = materia.GetPathMateriaAbsolute();
	        	String path = pathMat + File.separator + handler;
	        	FileReader fr = new FileReader(path);
	        	String PalRes="<!-- InstanceBeginEditable name=\"Trabajo\" -->";
	        	String PalRes2 = "<!-- InstanceEndEditable -->";
	        	
	            String Cadena = "";
	               
	            BufferedReader br = new BufferedReader(fr);
	         
	            String linea;
	            int Ban =0;
	            int resultado =  0;
	                 
	                
	            while((linea = br.readLine()) != null){
	            	// VERIFICA DONDE INICIA  EL TEXTO A TRABAJAR
	            	resultado = linea.indexOf(PalRes);  
				    if(resultado != -1)                  
				    {
				    	resultado = resultado + PalRes.length();
				        Ban =1;
				        linea = linea.substring(resultado, linea.length());
				    }
				          
				    if (Ban == 1 && linea.indexOf(PalRes2) != -1)
				    {
				    	linea = linea.substring(0, linea.indexOf(PalRes2));
			            Ban = 2;
				    }
				    if (Ban == 1 || Ban == 2)
				    {
				    	Cadena = Cadena + linea;
				        if ( Ban == 2)
				        {
				            Ban = 0;
				        }
				    }
	 
	            }
	         
	            fr.close();
	         	
				myClob = conConVirt.createClob();
								
				
//				Cadena = Cadena.replaceAll(Common.CARPETA_IMG + "/",
//								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_IMG + "/"));
//				Cadena = Cadena.replaceAll(Common.CARPETA_IMG_INT + "/",
//								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_IMG_INT + "/"));
//				Cadena = Cadena.replaceAll(Common.CARPETA_HTML5 + "/",
//								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_HTML5 + "/"));
//				Cadena = Cadena.replaceAll(Common.CARPETA_SONIDO + "/",
//								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_SONIDO + "/"));
//				Cadena = Cadena.replaceAll(Common.CARPETA_ACTIVIDADES + "/",
//								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_ACTIVIDADES + "/"));
//				Cadena = Cadena.replaceAll(Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS + "/",
//								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS + "/"));
//				Cadena = Cadena.replaceAll(Common.CARPETA_GNERALIDADES + "/",
//								Matcher.quoteReplacement(pathMat + "/" + Common.CARPETA_GNERALIDADES + "/"));

						
		        myClob.setString(1, Cadena);			
				return myClob;
			}

		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private boolean Validacion(HttpServletRequest request, HttpServletResponse response, String cveMat, String strNivel) throws Exception {
		
		String message = "";
		int nivel = 0;
		Connection con = null;
		
		try {
			if(!StringUtil.isNumber(strNivel)){
				message += "Ingresa nivel, por favor.<br>";
			}
			if(cveMat == null || cveMat.equals("")){
				message += "Ingresa materia, por favor.<br>";
			}
			if(!message.equals("")){
				Common.MsgJson("error", message, response);
				return false;
			}
	
			String[] arrCveMat = cveMat.split(",");
			nivel = Integer.parseInt(strNivel);
			String nomSistema = "";
			
			// no hay materia en el sistema de bac o lic
			switch (nivel) {
			case Common.NIVEL_BACHILLERATO:
				con = MySqlConnector.getConnectionBac();
				nomSistema = Common.NIVEL_BACHILLERATO_STR;
				break;
			case Common.NIVEL_LICENCIATURA:
				con = MySqlConnector.getConnectionLic();
				nomSistema = Common.NIVEL_LICENCIATURA_STR;
				break;
			default:
				message = Common.MENSAJE_ERROR_IMPORTAR;
				Common.MsgJson("error", message, response);
				return false;
			}
		
			for(String materia : arrCveMat){
				boolean existMateria = Materia.ExistsMateria(con, materia);
				if(!existMateria){
					message += MessageFormat.format(Common.MENSAJE_ERROR_IMPORTAR_NO_HAY_MATERIA, materia, nomSistema);
				}
			}
			
			if(!message.equals("")){
				Common.MsgJson("error", message, response);
				return false;
			}

			con = MySqlConnector.getConnection();
			for(String materia : arrCveMat){
				boolean existMateria = Materia.ExistsMateria(con, cveMat);
				if(existMateria){
					message += MessageFormat.format(Common.MENSAJE_CONFIRMAR_IMPORTAR, materia);
				}
			}
			
			Common.MsgJson("success", message, response);
						
		} catch (Exception e) {
			Common.Error(e);
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
			return false;
		} finally{
			if (con != null) {
				con.close();
	        }
		}

		return true;
	}

	private void SetForm(HttpServletRequest request) throws Exception {
		try {

			if(this.listaNivel == null){
					this.listaNivel = CrearComboNivel();
			}
			request.setAttribute(KEY_VARIABLE_NIVELES, this.listaNivel);
			request.setAttribute(KEY_VARIABLE_NIVELES_GRADO, CrearComboNivelGrado());
			
			String cveMat = request.getParameter(KEY_FORM_NOMBRE);
			String nivel = request.getParameter(KEY_FORM_NIVEL);
			String nivelGrado = request.getParameter(KEY_FORM_NIVEL_GRADO + nivel);
			request.setAttribute(KEY_VARIABLE_NOMBRE, cveMat);
			SetSelected(nivel);
			if(StringUtil.isNumber(nivel) && StringUtil.isNumber(nivelGrado)){
	    		SetSelectedNivelGrado(Integer.parseInt(nivel), Integer.parseInt(nivelGrado));  
			}
		
		} catch (Exception e) {
			throw e;
		}
		
	}

	/**
	 * Colocar "selected" en Combo de Nivel
	 * @param nivel
	 */
	private void SetSelected(String nivel){
		if(StringUtil.isNumber(nivel)){
			for(int i=0; i <= listaNivel.size()-1; i++){
				if(((Nivel)listaNivel.get(i)).getCveNivel() == Integer.parseInt(nivel)){
					((Nivel)listaNivel.get(i)).setSelected("selected");
				} else {
					((Nivel)listaNivel.get(i)).setSelected("");
				}
			}
		}
		
	}

	/**
	 * Crear Combo de nivel
	 * @return
	 * @throws Exception 
	 */
	private List<Nivel> CrearComboNivel() throws Exception {
		try {
			this.listaNivel = Nivel.BuscarTodo();
			return this.listaNivel;
		} catch (Exception e) {
			throw e;
		}		
	}
	
	/**
	 * Crear Combo de nivel grado
	 * @return
	 * @throws Exception 
	 */
	private NivelGradoCombos CrearComboNivelGrado() throws Exception {
		List<NivelGrado> listaNivelGrado;
		ngc = new NivelGradoCombos();

		try {
			
			for (Nivel nivel : listaNivel) {
				listaNivelGrado = NivelGrado.Buscar(nivel.getCveNivel());
				ngc.add(listaNivelGrado);
			}

			return ngc;

		} catch (Exception e) {
			throw e;
		}		
	}

	/**
	 * Colocar "selected" en Combo de Nivel Grado
	 * @param nivel
	 */
	private void SetSelectedNivelGrado(int nivel ,int nivelGrado){
		for(int i=0; i <= ngc.size()-1; i++){
			
			List<NivelGrado> comboNivelGrado = (List<NivelGrado>)ngc.get(i);
			
			if(comboNivelGrado.size() > 0 && 
					comboNivelGrado.get(0).getCveNivel() == nivel){
				
				for(int j=0; j<=comboNivelGrado.size()-1; j++ ){
					NivelGrado ng = comboNivelGrado.get(j);
					if(ng.getCveNivelGrado() == nivelGrado){
						ng.setSelected("selected");						

					} else{
						ng.setSelected("");
					}
				}
			}
		}
	}

}
