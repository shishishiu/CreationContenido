package util.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beans.LogAct;
import beans.Materia;
import beans.TrcnMat;
import beans.Usuario;
import servlet.general.Ftp;
import util.conf.Configuracion;
import util.db.MySqlConnector;
 
public class Common {
	/** log4j **/		
	private static final Logger logger = LogManager.getLogger(Common.class);
	/** Extensión **/
	public static final String EXTENSION_JSP = ".jsp";
	/** Slash **/
	public static final String SLASH = "/";
	/** Hyphen **/
	public static final String HYPHEN = " - ";
	/** Guion bajo **/
	public static final String GUION_BAJO = "_";
	/** Nombre de la página de buscar materia **/
	public static final String NOMBRE_DE_PAGINA_BUS_MATERIA = "BusMat";
	/** Nombre de la página de autentificar **/
	public static final String NOMBRE_DE_PAGINA_AUTENTIFICA = "Autentifica";
	/** Nombre de la página de error de autentificación **/
	public static final String NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR = "AutentificaError";
	/** Nombre de la página de error de autentificación **/
	public static final String NOMBRE_DE_PAGINA_AUTENTIFICA_ERROR_CHILD = "AutentificaErrorChild";
	/** Nombre de la página de autentificar **/
	public static final String NOMBRE_DE_PAGINA_MODIFICA_PERMISO_CONTENIDO = "ModificaPermisoContenido";
	/** Nombre de la página de error del sistema **/
	public static final String NOMBRE_DE_PAGINA_ERROR = "Error";
	/** Key de sesión de usuario **/		
	public static final String SESSION_NAME_USUARIO = "CreationContenido_Usuario";
	/** Nombre del contenido **/		
	public static final String CAPTION_CONTENIDO_UNIDAD = "Unidad";
	/** Nombre del contenido **/		
	public static final String CAPTION_CONTENIDO_INTRODUCCION = "Introducción";
	/** Nombre del contenido **/		
	public static final String CAPTION_CONTENIDO_CONTENIDO_MATERIA = "Contenido Materia";
	/** Nombre del contenido **/		
	public static final String CAPTION_CONTENIDO_GENERALIDADES = "Generalidades";
	/** New line**/
	public static final String NEW_LINE = System.getProperty("line.separator");
	/** Nombre del carpeta **/		
	public static final String CARPETA_IMG = "Img";
	/** Nombre del carpeta **/		
	public static final String CARPETA_IMG_INT = "Int";
	/** Nombre del carpeta **/		
	public static final String CARPETA_HTML5 = "HTML5";
	/** Nombre del carpeta **/		
	public static final String CARPETA_SONIDO = "SND";
	/** Nombre del carpeta **/		
	public static final String CARPETA_ACTIVIDADES = "ACT";
	/** Nombre del carpeta **/		
	public static final String CARPETA_ACTIVIDADES_COMPLEMENTARIAS = "ActCmp";
	/** Nombre del carpeta **/		
	public static final String CARPETA_GNERALIDADES = "GNR";

	/** Prefix del archivo **/		
	public static final String PREFIX_GENERALIDADES = "Met";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_CONTENIDO_MATERIA = "Cont";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_CONTENIDO = "Cont";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_SONIDO = "Snd";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_ACTIVIDADES = "Act";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS = "ActCmp";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_CONTENIDO_IMPRIMIBLE = "ProgCont";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_PROGRAMA_IMPRIMIBLE = "MetProg";
	/** Prefix del archivo **/		
	public static final String PREFIX_ARCHIVO_FORMULARIO = "Form";

		
	/** Nivel **/
	public static final int NIVEL_BACHILLERATO = 1;
	/** Nivel **/
	public static final int NIVEL_LICENCIATURA = 2;
	/** Nivel **/
	public static final int NIVEL_MAESTORIA = 3;
	/** Nivel **/
	public static final int NIVEL_DOCTORADO = 4;
	/** Nivel **/
	public static final String NIVEL_BACHILLERATO_STR = "Bachillerato";
	/** Nivel **/
	public static final String NIVEL_LICENCIATURA_STR = "Licenciatura";
	/** Nivel **/
	public static final String NIVEL_MAESTRIA_STR = "Maestria";
	/** Nivel **/
	public static final String NIVEL_DOCTORADO_STR = "Doctorado";

	/** Permiso administrador general **/
	public static final int PERMISO_ADMINISTRADOR_GENERAL = 1;
	/** Permiso administrador **/
	public static final int PERMISO_ADMINISTRADOR = 2;
	/** Permiso asesor **/
	public static final int PERMISO_ASESOR = 3;
	/** Permiso revisor **/
	public static final int PERMISO_REVISOR = 4;
	/** Permiso usuario normal **/
	public static final int PERMISO_USUARIO_NORMAL = 5;

	/** Permiso del contenido de la materia **/
	public static final int PERMISO_CONTENIDO_NINGUNA = 0;
	/** Permiso del contenido de la materia **/
	public static final int PERMISO_CONTENIDO_VER = 1;
	/** Permiso del contenido de la materia **/
	public static final int PERMISO_CONTENIDO_MODIFICAR = 2;

	/** Estado de solicitud **/
	public static final int ESTADO_DE_SOLICITUD_PENDIENTE = 0;
	/** Estado de solicitud **/
	public static final int ESTADO_DE_SOLICITUD_SOLICITADO = 1;
	/** Estado de solicitud **/
	public static final int ESTADO_DE_SOLICITUD_REVISADO = 2;
	/** Estado de solicitud **/
	public static final int ESTADO_DE_SOLICITUD_VALIDADO = 3;
	/** Estado de solicitud **/
	public static final int ESTADO_DE_SOLICITUD_LIBERADO = 4;
	/** Estado de solicitud **/
	public static final int ESTADO_DE_SOLICITUD_PRODUCCION = 5;
	
	/** Tipo del contenido **/
	public static final String TIPO_CONTENIDO_INTRODUCCION = "1";
	/** Tipo del contenido **/
	public static final String TIPO_CONTENIDO_GENERALIDAD = "2";
	/** Tipo del contenido **/
	public static final String TIPO_CONTENIDO_MATERIA = "3";

	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_IMAGEN = "1";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_ANIMACION = "2";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_SONIDO = "3";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_ACTIVIDADES = "4";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS = "5";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_CONTENIDO_IMPRIMIBLE = "6";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_PROGRAMA_IMPRIMIBLE = "7";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_FORMULARIO = "8";
	/** Tipo de Archivo **/
	public static final String VALUE_TIPO_ARCHIVO_HTML5 = "9";

	
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_AUTENTIFICA = "No tiene acceso.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_MISMA_CAPTION = "Hay misma caption. Ingresa diferente nombre por favor.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_MISMA_CLAVE_MATERIA = "Hay misma clave de la materia. Ingresa diferente clave de la materia por favor.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_FILE_NO_CREAR = "No puede crear la carpeta o el archivo.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_FILE_NO_PUEDE_BORRAR = "No puede borrar la carpeta o el archivo.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_FILE_NO_EXIST = "No se encuentró carpeta o el archivo.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_FILE_EXIST = "Hay carpeta o archivo que tiene mismo nombre. No puede crear la carpeta o el archivo";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_FILE_NO_ESCRIBIR = "No puede escribir en el archivo: {0}";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_FILE_GRANDE = "El archivo es más grande que el limite";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_NO_CAMBIAR_NOMBRE_FILE_FOLDER = "No se puede cambiar el nombre de los archivos o las carpetas de siguientes. Podría cambiar los nombres de 'Antes' a 'Después', por favor.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_NO_CAMBIAR_NOMBRE_FOLDER = "No se puede cambiar el nombre de la carpeta.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_TIENE_CONTENIDO = "Ya está contenido. ¿Deseas sobreescribir?";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_NO_HAY_DATOS = "No hay datos";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_EXPORTAR = "Se Puede exportar la de Bachillerato y Licenciatura.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_IMPORTAR = "Se puede importar la de Bachillerato y Licenciatura.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_IMPORTAR_NO_HAY_MATERIA = "Materia \"{0}\" no existe en el sistema {1}.<br>";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_MISMO_NOM_GRADO = "Cambia el nombre de directorio, por favor. Lo que escribió ya se está usando.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR_MOVER_CARPETA = "Se produje error cuando movía la carpeta de la materia. No se ha cambiado lo que modificó usted.";
	/** Mensaje de error **/
	public static final String MENSAJE_ERROR = "Se produjo un error";
	/** Mensaje de error **/
	public static final String MENSAJE_NO_HAY_CAMBIO = "No hay cambio. Modifica algún campo, por favor";
	/** Mensaje de error **/
	public static final String MENSAJE_SOLICITAR_HA_SOLICITADO = "No puedo solicitar porque ya se ha solicitado la materia";
	/** Mensaje de error **/
	public static final String MENSAJE_SOLICITAR_HA_REVISADO = "No puedo solicitar porque ya se ha revisado la materia";
	/** Mensaje de error **/
	public static final String MENSAJE_SOLICITAR_HA_VALIDADO = "No puedo solicitar porque ya se ha validado la materia";
	/** Mensaje de error **/
	public static final String MENSAJE_SOLICITAR_HA_LIBERADO = "No puedo solicitar porque ya se ha liberado la materia";
	/** Mensaje de error **/
	public static final String MENSAJE_SOLICITAR_HA_PRODUCCION = "No puedo solicitar porque ya está producción";
	/** Mensaje de error **/
	public static final String MENSAJE_NO_MODIFICAR = "No puede modificar.";
	/** Mensaje de error **/
	public static final String MENSAJE_NO_MODIFICAR_CONTENIDO_POR_UNIDAD_APARTADO = "Siguiente archivos no son de esta unidad o no son de este apartado. Cambia los archivos o sube los archivos nuevamente, por favor.";
	/** Mensaje de error **/
	public static final String MENSAJE_NO_SE_ELIGE_NIVEL = "No se elige Nivel. Elige Nivel, por favor.";
	/** Mensaje de error **/
	public static final String MENSAJE_NO_SE_ENCUENTRA_ARCHIVO = "No se encuentra archivo {0}.";
	/** Mensaje de error **/
	public static final String MENSAJE_LIMIT_DE_NUMERO_DE_CARACTER = "Escribir dentro del límite del número de caracteres {0}.";
	/** Mensaje de error **/
	public static final String MENSAJE_SE_ENCUENTRA_DEMACIADOS_DATOS = "Se encontraron más de 1000 datos. Busca con más condiciones por favor.";

	
	/** Mensaje **/
	public static final String MENSAJE_CONFIRMAR_IMPORTAR = "Materia \"{0}\" está en este sistema. Seguro que la borra y agrrega? ";
	/** Mensaje **/
	public static final String MENSAJE_TERMINAR_PROCESO = "Terminó el proceso.";
	/** Mensaje **/
	public static final String MENSAJE_URL_PRUEBA = " URL de prueba es <a target='_blank' href={0}>{0}</a>";

	
	
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_MATERIA_DAR_BAJA = "Siguiente materia dio de baja: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_MATERIA_DAR_ALTA = "Siguiente materia dio de alta: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_MATERIA_BORRAR = "Siguiente materia se borró para que pueda importar: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_MATERIA_MODIFICAR = "Siguiente materia se modificó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_MATERIA_AGREGAR = "Siguiente materia se agregó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_MATERIA_IMPORTAR = "Siguiente materia se importó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_DAR_PERMISO_CONTENIDO_MODIFICAR = "Siguiente permiso se modificó: materia:{0} usuario:{1}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_DAR_PERMISO_CONTENIDO_AGREGAR = "Siguiente permiso se agregó: materia:{0} usuario:{1}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_DAR_BAJA = "Siguiente contenido dio de baja: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_AGREGAR = "Siguiente contenido agregó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_MODIFICAR = "Siguiente contenido modificó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_ARCHIVO_AGREGAR = "Siguiente archivo agregó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_CARPETA_AGREGAR = "Siguiente carpeta agregó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_CARPETA_ARCHIVO_BORRAR = "Siguiente carpeta o arachivo se borró: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_BORRAR = "Siguiente contenidos de la materia se borró para que pueda importar: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_CONTENIDO_IMPORTAR = "Siguiente contenidos de la materia se importó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_EXPORTAR = "Siguiente materia exportó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_SOLICITAR = "Siguiente materia se solicitó la revisión: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_REVISAR = "Siguiente materia se revisó la revisión: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_VALIDAR = "Siguiente materia se validó la revisión: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_LIBERAR = "Siguiente materia se liberó la revisión: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_PRODUCCION = "Siguiente materia se subió a producción: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_REGRESAR_PENDIENTE = "Siguiente materia se regresó a 'Pendiente' de 'Está Producción': {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_USUARIO_DAR_BAJA = "Siguiente usuario dio de baja: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_USUARIO_DAR_ALTA = "Siguiente usuario dio de alta: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_USUARIO_MODIFICAR = "Siguiente usuario se modificó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_USUARIO_AGREGAR = "Siguiente usuario se agregó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_PLANTILLA_MODIFICAR = "Siguiente plantilla se modificó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_NIVEL_GRADO_AGREGAR = "Siguiente nivel grado se agregó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_NIVEL_GRADO_MODIFICAR = "Siguiente nivel grado se modificó: {0}";
	/** Texto de action log **/
	public static final String TEXTO_ACTION_LOG_NIVEL_GRADO_BORRAR = "Siguiente nivel grado se borró: {0}";
	
	/** Json status **/
	public static final String JSON_STATUS_NO_AUTORIZADO = "noAutorizado";
	


	/**
	 * log
	 * @param e
	 */
	public static void LogInfo(String s){
		logger.info(s);

	}
	/**
	 * Entrar error log
	 * @param e
	 */
	public static void Error(Exception e){
		logger.error(getStackTrace(e));

	}
	/**
	 * Entrar error log
	 * @param e
	 */
	public static void Error(String message){
		logger.error(message);

	}
	/**
	 * Obtener StackTrace
	 * @param e
	 * @return
	 */
	public static String getStackTrace(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return e.getMessage() + " " + sw.toString();
	}

	/**
	 * Obtener nombre de la página del error
	 * @return
	 */
	public static String getErrorPage(){
		return "/" + NOMBRE_DE_PAGINA_ERROR;
	}
	

	public static String htmlEscape(String str){
		if(str != null && !str.equals("")){
			StringBuffer result = new StringBuffer();
			for(char c : str.toCharArray()) {
				switch (c) {
				case '&' :
					result.append("%26amp;");
					break;
				case '<' :
					result.append("%26lt;");
					break;
				case '>' :
					result.append("%26gt;");
					break;
				case '"' :
					result.append("%26quot;");
					break;
				case '\'' :
					result.append("%26#39;");
					break;
				case ' ' :
					result.append("%26nbsp;");
					break;
				default :
					result.append(c);
					break;
				}
			}
			return result.toString();
		}
		return null;
	}
	
	/**
	 * Insertar log
	 * @param request
	 * @param con
	 * @param actMessage
	 * @throws Exception
	 */
	public static void InsertLogAct(HttpServletRequest request, Connection con, String cveUsu, String actMessage) throws Exception {
		try{
			HttpSession sesion = request.getSession(true);
			LogAct logact = new LogAct();
			logact.setCveUsu(cveUsu);
			logact.setIdSession(sesion.getId());
			logact.setIpUsu(request.getRemoteAddr());
			logact.setActLogAct(actMessage);
			logact.Insert(con);
		}catch(Exception e){
			throw e;
		}
		
	}
	public static boolean IsAjax(HttpServletRequest request)  
	{  
	    if(request.getHeader("HTTP_X_REQUESTED_WITH") != null
	    		&& (request.getHeader("HTTP_X_REQUESTED_WITH").toLowerCase()).equals("xmlhttprequest")){  
	        return true;  
	    }  
	    return false;  
	}
	public static void MsgJson(String status, String msg, HttpServletResponse response) throws IOException {

		msg = msg.replaceAll(Pattern.quote("\\"), Matcher.quoteReplacement("\\\\"));
		msg = msg.replaceAll("\"", "\\\\\"");
		msg = msg.replaceAll("\\r\\n", "\\\\r\\\\n");
		
		String json = "{\"status\":\""+ status + "\",\"message\":\"" + msg + "\"}";
		response.setContentType("application/json;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();
		out.print(json);
		
	}

	/**
	 * Crear lista de los numeros de la pagina
	 * @param request
	 * @param count
	 */
	public static final int NUMERO_DE_DATOS_PARA_MOSTRAR = 50;
	private static final int NUMERO_DE_PAGINA_PARA_MOSTRAR = 15;

	public static String CreateListNumeroPagina(HttpServletRequest request, int totaldatos, int currentPagina) {
		
		int resto = totaldatos % NUMERO_DE_DATOS_PARA_MOSTRAR;
		int numeroPagina = 0;
		if(resto>0){
			numeroPagina = totaldatos / NUMERO_DE_DATOS_PARA_MOSTRAR + 1;
		}else{
			numeroPagina = totaldatos / NUMERO_DE_DATOS_PARA_MOSTRAR;
		}

		int dispStart = currentPagina - 4;
		if(dispStart <= 0){
			dispStart = 1;
		}
		int dispEnd = dispStart + NUMERO_DE_DATOS_PARA_MOSTRAR-1;
		if(dispEnd > numeroPagina){
			dispEnd = numeroPagina;
		}
		
		if(dispEnd - dispStart + 1 < NUMERO_DE_PAGINA_PARA_MOSTRAR){
			if(dispEnd - NUMERO_DE_DATOS_PARA_MOSTRAR > 0){
				dispStart = dispEnd - NUMERO_DE_DATOS_PARA_MOSTRAR +1;
			}else{
				dispStart = 1;
			}
			
		}
		
		String listNumeroPagina = "";
		if(dispStart > 1){
			listNumeroPagina = "<a href='javascript:void(0)' onclick='funcMovePagina(1)'>primera</a>    ";
		}
		for(int i=dispStart; i<=dispEnd; i++){
			
			if(currentPagina != i){
				listNumeroPagina += "<a href='javascript:void(0)' onclick='funcMovePagina(" + i + ")'>";
			}
			
			listNumeroPagina +=  i;
			
			if(currentPagina != i){
				listNumeroPagina += " </a> ";
			}
			
			listNumeroPagina += " | ";
		}

		if(dispEnd < numeroPagina){
			listNumeroPagina += "    <a href='javascript:void(0)' onclick='funcMovePagina(" + numeroPagina + ")'>última</a>";
		}

		return listNumeroPagina;		
	}

	
	/**
	 * Obtener el nombre de la carpeta
	 * @param tipoArchivo
	 * @return
	 */
	public static String GetNombreCarpeta(String tipoArchivo, TrcnMat contenido, String separator) {
		String carpeta = "";
		String parentContName = contenido.getParentContName();
	    if(tipoArchivo.equals(VALUE_TIPO_ARCHIVO_IMAGEN)){
	    	carpeta = Common.CARPETA_IMG;
    		
    		if(contenido.getCaption().equals(Common.CAPTION_CONTENIDO_INTRODUCCION)){
    			carpeta += separator + Common.CARPETA_IMG_INT;
    		
    		}else if(parentContName.indexOf(Common.CAPTION_CONTENIDO_GENERALIDADES)>=0){
    			carpeta += separator + Common.PREFIX_GENERALIDADES 
    					+ contenido.getCaption().substring(0, 1).toUpperCase()
    					+ contenido.getCaption().substring(1, 3).toLowerCase();
    		
    		}else if(contenido.getUnidad()>0){
    			carpeta += separator + contenido.getUnidad();
    		}
	    }
	    else if(tipoArchivo.equals(VALUE_TIPO_ARCHIVO_ANIMACION)){
	    	carpeta = Common.CARPETA_HTML5;
	    }
	    else if(tipoArchivo.equals(VALUE_TIPO_ARCHIVO_SONIDO)){
	    	carpeta = Common.CARPETA_SONIDO;
	    }
	    else if(tipoArchivo.equals(VALUE_TIPO_ARCHIVO_ACTIVIDADES)){
	    	carpeta = Common.CARPETA_ACTIVIDADES;
	    }
	    else if(tipoArchivo.equals(VALUE_TIPO_ARCHIVO_ACTIVIDADES_COMPLEMENTARIAS)){
	    	carpeta = Common.CARPETA_ACTIVIDADES_COMPLEMENTARIAS;
	    }
	    else if(tipoArchivo.equals(VALUE_TIPO_ARCHIVO_CONTENIDO_IMPRIMIBLE)
	    		|| tipoArchivo.equals(VALUE_TIPO_ARCHIVO_PROGRAMA_IMPRIMIBLE)
	    		|| tipoArchivo.equals(VALUE_TIPO_ARCHIVO_FORMULARIO)){
	    	carpeta = Common.CARPETA_GNERALIDADES;
	    }

//	    return File.separator + carpeta;
	    return carpeta;
	}

	
	
	
	
	
	
	/**
	 * Crear jsp
	 * @param request
	 * @throws Exception
	 */
	/** Nombre del form **/
//	public static final String PATH_WEB_INF = "/WEB-INF/";

	public static String CrearJSP(HttpServletRequest request, Usuario usuario, String cveMat) throws Exception {
		if(usuario.isAdministradorGeneral() || usuario.isAdministrador()){
		
			TrcnMat bean = new TrcnMat();
			bean.setCveMat(cveMat);
			List<TrcnMat> list = bean.Buscar();
			if(list.size()<=0){
	        	return Common.MENSAJE_ERROR_NO_HAY_DATOS;
			}

			Materia materia = Materia.Buscar(cveMat);			
        	String path = materia.GetPathMateriaAbsolute() + File.separator;
            String encabezado = GetEncabezado(materia.getNivel());
            String pie = GetPie(materia.getNivel());
		
            File newdir = new File(path);
            if(!newdir.exists()){
            	newdir.mkdirs();
            }
            
	        try{
	        	for(TrcnMat trcnm : list){
	        		
					if(trcnm.getHandler() != null && !trcnm.getHandler().equals("")){
						//Crear JSP
			        	String handlerPath = path + trcnm.getHandler();
			        	
			        	File file = new File(handlerPath);
			        	if(file.exists()){
			        		file.delete();
			        	}
			
			        	if (!file.createNewFile()){
			        		throw new CustomException(0, String.format(Common.MENSAJE_ERROR_FILE_NO_CREAR, handlerPath));
			            }
			        	
			        	//Escribir html
			        	if (checkBeforeWritefile(file)){
			                PrintWriter pw    = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"ISO-8859-1")));

			                String piecompleto = "";
							if(materia.getNivel() == Common.NIVEL_LICENCIATURA){
								piecompleto = MessageFormat.format(pie, trcnm.getCaption());
							}else{
								piecompleto = pie;
							}
							
				  			  Common.Error(piecompleto);
							String replaceStr = (materia.GetPathMateriaRelative() + File.separator).
									replaceAll(Matcher.quoteReplacement(File.separator), SLASH);
			                String contenidoStr = trcnm.getContenido().replace(replaceStr, "");
			                pw.println(encabezado + contenidoStr + piecompleto);
	
			                pw.close();
			              }else{
			            	  throw new CustomException(0, String.format(Common.MENSAJE_ERROR_FILE_NO_ESCRIBIR, handlerPath));
			              }
			        		
		        	}

				}			

	        	return Common.MENSAJE_TERMINAR_PROCESO;
	        }catch(IOException e){
	        	throw e;
	        }
			
		}else{
			throw new CustomException(0, Common.MENSAJE_ERROR_AUTENTIFICA);
		}
	}

	public static String GetFilePathPlantillaPie(int nivel){
		Configuracion conf = new Configuracion();
		String path = conf.getAbsolutePath() + File.separator + conf.getPlantillas();
		  if(nivel == Common.NIVEL_BACHILLERATO){
			  path += File.separator + "pie_bachi.txt";
		  }else if(nivel == Common.NIVEL_LICENCIATURA){
			  path += File.separator + "pie_lic.txt";
		  }

		  return path;
	}
	public static String GetFilePathPlantillaEncabezado(int nivel){
		Configuracion conf = new Configuracion();
		String path = conf.getAbsolutePath() + File.separator + conf.getPlantillas();
		  if(nivel == Common.NIVEL_BACHILLERATO){
			  path += File.separator + "encabezado_bachi.txt";
		  }else if(nivel == Common.NIVEL_LICENCIATURA){
			  path += File.separator + "encabezado_lic.txt";
		  }

		  return path;
	}
	
	/**
	 * Obtener el texto del pie
	 * @param nivel
	 * @return
	 * @throws IOException
	 */
	private static String GetPie(int nivel) throws IOException {
		  String pie = "";
		  try{
			  String pathPie = GetFilePathPlantillaPie(nivel);
			  
			  FileReader fr = new FileReader(pathPie);
		      BufferedReader br = new BufferedReader(fr);
	
		      StringBuffer sb = new StringBuffer();
	          String line;
	          while ((line = br.readLine()) != null) {
	        	  sb.append("\n" + line);
	          }
	
	          br.close();
	          fr.close();
	
	          pie = sb.toString();
	          
		    }catch(FileNotFoundException e){
		    	throw e;
		    }catch(IOException e){
		    	throw e;
		    }
		  
		  return pie;
	}
	
	/**
	 * Obtener el texto del encabezado 
	 * @param nivel
	 * @return
	 * @throws IOException
	 */
	private static String GetEncabezado(int nivel) throws IOException {
	      StringBuffer sb = new StringBuffer();
		  
		  try{
			  String pathEncabezado = GetFilePathPlantillaEncabezado(nivel);
			  
			  FileReader fr = new FileReader(pathEncabezado);
		      BufferedReader br = new BufferedReader(fr);
	
	          String line;
	          while ((line = br.readLine()) != null) {
	        	  sb.append("\n" + line);
	          }
	
	          br.close();
	          fr.close();
	
		    }catch(FileNotFoundException e){
		      throw e;
		    }catch(IOException e){
		    	throw e;
		    }
		  
		  return sb.toString();
		}
	
	/**
	 * Confirmar si pueda escribir los textos
	 * @param file
	 * @return
	 */
	private static boolean checkBeforeWritefile(File file){
	    if (file.exists()){
	      if (file.isFile() && file.canWrite()){
	        return true;
	      }
	    }
	
	    return false;
	  }
		
	/**
	 * Exportar los contenidos a prueba
	 * @param request
	 * @param cveMat
	 * @param usuario
	 * @throws Exception
	 */
	public static void Exportar(HttpServletRequest request, String cveMat, Usuario usuario) throws Exception {
		try {
		
			Materia materia = Materia.Buscar(cveMat);
			List<TrcnMat> list = TrcnMat.Buscar(cveMat);
		    Configuracion config = new Configuracion();

			Connection con = null;
			Connection con2 = null;
			String schema = "";
			switch (materia.getNivel()) {
				case Common.NIVEL_BACHILLERATO:
					con = MySqlConnector.getConnectionBac();
					schema =  config.getDbBacBaseDeDatos();
					break;
				case Common.NIVEL_LICENCIATURA:
					con = MySqlConnector.getConnectionLic();
					schema =  config.getDbLicBaseDeDatos();
					break;
				default:
					return;
			}

			try {
				
				con.setAutoCommit(false);

				TrcnMat.DarBajaConMateria(con, cveMat, 0);
				
				CopiarContenidos(con, schema, list, materia.getNivel());

				con2 = MySqlConnector.getConnection();
				con2.setAutoCommit(false);

				Common.InsertLogAct(request, con2, usuario.getCveUsu(), 
						MessageFormat.format(Common.TEXTO_ACTION_LOG_EXPORTAR,cveMat));

				con.commit();
				con2.commit();


			} catch (SQLException e) {
				if (con != null) {
					con.rollback();
				}			
				if (con2 != null) {
					con2.rollback();
				}			
				throw e;
			} finally{
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
		        }			
				if (con2 != null) {
					con2.setAutoCommit(true);
					con2.close();
				}			
			}		
			
			
		} catch (Exception e) {
			throw e;
		}
	}
		
	private static void CopiarContenidos(Connection con, String schema, List<TrcnMat> list, int nivel) throws Exception {
			try{
				//Select numero de auto increment
				int maxItemMenu = TrcnMat.ObtenerMaxMenuItem(con, schema);
							
				//Hacer lista de numero nuevo y numero antes hashmap
				HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
				int itemMenu = maxItemMenu;
				for(TrcnMat bean : list){
					hm.put(bean.getMenuItem(), itemMenu);
					
					itemMenu++;
				}
				
				//insert datos 
				for(TrcnMat bean : list){
					
					if(hm.containsKey(bean.getMenuItem())){
						bean.setMenuItem(hm.get(bean.getMenuItem()));
					}
					if(hm.containsKey(bean.getMenuItemParentId())){
						bean.setMenuItemParentId(hm.get(bean.getMenuItemParentId()));
					}
					
					switch (nivel) {
						case Common.NIVEL_BACHILLERATO:
							bean.InsertarBac(con);
							break;
						case Common.NIVEL_LICENCIATURA:
							bean.InsertarLic(con);
							break;
						default:
							return;
					}
					
				}
			}catch(Exception e){
				throw e;
			}
	}

	
	/**
	 * Validar que pueda subir a la plataforma
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static boolean Validacion(String cveMat, HttpServletResponse response) throws Exception {
		Connection con;
		try {
			Materia materia = Materia.Buscar(cveMat);
			switch (materia.getNivel()) {
				case Common.NIVEL_BACHILLERATO:
					con = MySqlConnector.getConnectionBac();
					break;
				case Common.NIVEL_LICENCIATURA:
					con = MySqlConnector.getConnectionLic();
					break;
				default:
					Common.MsgJson("error", Common.MENSAJE_ERROR_EXPORTAR, response);
					return false;
			}
			
			boolean existsCont = TrcnMat.ExistsContenido(con, cveMat);
			
			if(existsCont){
				Common.MsgJson("success", Common.MENSAJE_ERROR_TIENE_CONTENIDO, response);
			}else{
				Common.MsgJson("success", "", response);
			}
			return !existsCont;
		} catch (Exception e) {
			Common.Error(e);
			Common.MsgJson("error", Common.MENSAJE_ERROR, response);
			return false;
		}
		
	}
	

	public static boolean Copiar(Materia materia) throws Exception{
		
		boolean result = false;
		Configuracion config = new Configuracion();

		String rootPath = "";
		switch (materia.getNivel()) {
			case Common.NIVEL_BACHILLERATO:
				rootPath = config.getAbsolutePathBac();
				break;
			case Common.NIVEL_LICENCIATURA:
				rootPath = config.getAbsolutePathLic();
				break;
			default:
				break;
		}

		String pathMat = materia.GetPathMateria(rootPath);
		if(materia.getNivel() == Common.NIVEL_LICENCIATURA){
			pathMat = pathMat.replace("/Lic/", "/lic/");
		}
		
		
		try{
			
			File srcDir = new File(materia.GetPathMateriaAbsolute());
			File destDir = new File(pathMat);
			FileUtils.copyDirectory(srcDir, destDir);
			
			result = true;
			
		}catch(Exception e){
			
			throw e;
		}
		
		
		
		
//		Configuracion config = new Configuracion();
//		String server = config.getFtpLicServer();
//		int port = config.getFtpLicPort();
//		String user = config.getFtpLicUser();
//		String password = config.getFtpLicPassword();
//
//		Sftp ftp = new Sftp(server, port, user, password, true, true, "ISO-8859-1");
//		
//		boolean result = false;
//	    
//	    try {
//
//	      ftp.Connect();
//
//	      ftp.Delete(materia.getNomGrado() + "/" + materia.getModulo() + "/" + materia.getCveMat());
//          
//          result = ftp.Put(materia.GetPathMateriaAbsolute(), materia.getNomGrado() + "/" + materia.getModulo());
//          
//	    } catch(Exception e) {
//	    	throw e;
//	    } finally {
//	    	if(ftp.getIsConnected()) {
//		        try {
//		        	ftp.Disconnect();
//		        } catch(IOException ioe) {
//	        }
//	     }
//	      
//	  }
	    return result;
	    
	}

	
	public static boolean FTPCopiar(Materia materia) throws Exception{
		Configuracion config = new Configuracion();
		String server = config.getFtpLicServer();
		int port = config.getFtpLicPort();
		String user = config.getFtpLicUser();
		String password = config.getFtpLicPassword();

		Ftp ftp = new Ftp(server, port, user, password, true, true, 
				"\\" + materia.getNomGrado() + "\\" + materia.getModulo() + "\\", 
				materia.GetPathMateriaAbsolute(), "ISO-8859-1");
		
		boolean result = false;
	    
	    try {

	      result = ftp.Connect();

	      if(!result) {
	        return false;
	      }
          
          result = ftp.Put(true);
          
	    } catch(Exception e) {
	    	throw e;
	    } finally {
	    	if(ftp.getIsConnected()) {
		        try {
		        	ftp.Disconnect();
		        } catch(IOException ioe) {
	        }
	     }
	      
	  }
	    return result;
	    
	}

	/**
	 * Hacer handler
	 * @param request
	 * @param parentId
	 * @param unidad
	 * @param apartado
	 * @return
	 * @throws Exception
	 */
	public static String GetHandler(TrcnMat bean) throws Exception{
		String handler = null;
		String cveMat = bean.getCveMat();
		String caption = bean.getCaption();
		int unidad = bean.getUnidad();
		int apartado = bean.getApartado();
		TrcnMat parentbean = TrcnMat.Buscar(bean.getMenuItemParentId());
		String parentName = parentbean.getParentContName() + Common.HYPHEN + parentbean.getCaption();

		try {
			// handler de los contenidos generaridades
			if(parentName.indexOf(Common.CAPTION_CONTENIDO_GENERALIDADES) >= 0){
				String MET = Common.PREFIX_GENERALIDADES;
				
				if(caption.length() < 3){
					handler = MET + caption.substring(0, 1).toUpperCase() + 
							caption.substring(1,caption.length()) + cveMat + Common.EXTENSION_JSP;
				}else{

					String letras = caption.substring(0, 1).toUpperCase() 
							+ caption.substring(1, 3).toLowerCase();			
					handler = MET + letras + cveMat + Common.EXTENSION_JSP;
					
					TrcnMat bean2 = new TrcnMat();
					bean2.setCveMat(cveMat);
					bean2.setHandler(handler);
					List<TrcnMat> list = bean2.Buscar();
	  				
					if(list.size() > 0){
						letras = caption.substring(0, 1).toUpperCase() 
								+ caption.substring(1, 4).toLowerCase();
						handler = MET + letras + cveMat + Common.EXTENSION_JSP;
					}

				}
			
			// handler de los Contenidos
			} else{
				if(parentbean.getUnidad() > 0){
					String sequencia = GetSequencia(cveMat, unidad, apartado);
					
					handler = Common.PREFIX_ARCHIVO_CONTENIDO_MATERIA
							+ cveMat + Common.GUION_BAJO + unidad + Common.GUION_BAJO 
							+ apartado + Common.GUION_BAJO + sequencia + Common.EXTENSION_JSP;
					
				}else{
					
					if(caption.indexOf(Common.CAPTION_CONTENIDO_UNIDAD) < 0){
											
						handler = Common.PREFIX_ARCHIVO_CONTENIDO_MATERIA
								+ cveMat + caption.substring(0, 1).toUpperCase() 
								+ caption.substring(1,caption.length()).toLowerCase() + Common.EXTENSION_JSP;
					}

				}
			}
		
		} catch (Exception e) {
			throw e;
		}
		return handler;
	}

	/**
	 * Obtener el sequencia del archivo
	 * @param cveMat
	 * @param unidad
	 * @param apartado
	 * @return
	 * @throws Exception
	 */
	private static String GetSequencia(String cveMat, int unidad, int apartado) throws Exception {
		int sequencia = 0;
		
		if(unidad <= 0 || apartado <= 0){
			
			return Integer.toString(sequencia);
		
		}else{

			try {
				return TrcnMat.BuscarSequencia(unidad, apartado, cveMat);
			
			} catch (Exception e) {
				throw e;
			}
		}
				
	}


}
