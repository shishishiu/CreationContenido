package util.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import beans.Usuario;
import util.common.Common;

public class Configuracion {

	/** Properties **/
    private Properties prop = null;
	/** Nombre del archivo de config **/
	private final String CONFIG_FILE = "config.properties";
	/** Nombre del key de config **/
    private final String DB_USER_NAME = "db_crecon_userName";
	/** Nombre del key de config **/
    private final String DB_PASSWORD = "db_crecon_password";
	/** Nombre del key de config **/
    private final String DB_DBMS = "db_crecon_dbms";
	/** Nombre del key de config **/
    private final String DB_SERVER_NAME = "db_crecon_serverName";
	/** Nombre del key de config **/
    private final String DB_BASE_DE_DATOS = "db_crecon_baseDeDatos";
	/** Nombre del key de config **/
    private final String DB_DRIVER_NAME = "db_crecon_driverName";

	/** Nombre del key de config **/
    private final String DB_BAC_USER_NAME = "db_bac_userName";
	/** Nombre del key de config **/
    private final String DB_BAC_PASSWORD = "db_bac_password";
	/** Nombre del key de config **/
    private final String DB_BAC_DBMS = "db_bac_dbms";
	/** Nombre del key de config **/
    private final String DB_BAC_SERVER_NAME = "db_bac_serverName";
	/** Nombre del key de config **/
    private final String DB_BAC_BASE_DE_DATOS = "db_bac_baseDeDatos";
	/** Nombre del key de config **/
    private final String DB_BAC_DRIVER_NAME = "db_bac_driverName";
    
	/** Nombre del key de config **/
    private final String DB_LIC_USER_NAME = "db_lic_userName";
	/** Nombre del key de config **/
    private final String DB_LIC_PASSWORD = "db_lic_password";
	/** Nombre del key de config **/
    private final String DB_LIC_DBMS = "db_lic_dbms";
	/** Nombre del key de config **/
    private final String DB_LIC_SERVER_NAME = "db_lic_serverName";
	/** Nombre del key de config **/
    private final String DB_LIC_BASE_DE_DATOS = "db_lic_baseDeDatos";
	/** Nombre del key de config **/
    private final String DB_LIC_DRIVER_NAME = "db_lic_driverName";
    
    /** Nombre del key de config **/
    private final String PATHFILE_ROOT = "pathFile_root";
	/** Nombre del key de config **/
    private final String PATHFILE_MATERIA = "pathFile_materia";
	/** Nombre del key de config **/
    private final String PATHFILE_USUARIO = "pathFile_usuario";
	/** Nombre del key de config **/
    private final String PATHFILE_CONF = "pathFile_conf";
	/** Nombre del key de config **/
//    private static final String USERFILES_ABSOLUTE_PATH= "userfiles_absolute_path";
	/** Nombre del key de config **/
    private final String ABSOLUTE_PATH= "absolute_path";
	/** Nombre del key de config **/
    private final String USERFILES = "userfiles";
	/** Nombre del key de config **/
    private final String USERFILES_PATH= "userfiles_path";
	/** Nombre del key de config **/
    private final String PRUEBA_URL= "url_prueba_lic";
	/** Nombre del key de config **/
    private final String PLANTILLAS = "plantillas";

    
    /** Nombre del key de config **/
    private final String FTP_LIC_SERVER = "ftp_lic_server";
    /** Nombre del key de config **/
    private final String FTP_LIC_PORT = "ftp_lic_port";
    /** Nombre del key de config **/
    private final String FTP_LIC_USER = "ftp_lic_user";
    /** Nombre del key de config **/
    private final String FTP_LIC_PASSWORD = "ftp_lic_password";

    public Configuracion(){
    	Initialize();
    }
    
	public String getDbUserName(){
		return prop.getProperty(DB_USER_NAME);
	}
	public String getDbPassword(){
		return prop.getProperty(DB_PASSWORD);
	}
	public String getDbDbms(){
		return prop.getProperty(DB_DBMS);
	}
	public String getDbServerName(){
		return prop.getProperty(DB_SERVER_NAME);
	}
	public String getDbBaseDeDatos(){
		return prop.getProperty(DB_BASE_DE_DATOS);
	}

	public String getDbDriverName(){
		return prop.getProperty(DB_DRIVER_NAME);
	}
	
	public String getDbBacUserName(){
		return prop.getProperty(DB_BAC_USER_NAME);
	}
	public String getDbBacPassword(){
		return prop.getProperty(DB_BAC_PASSWORD);
	}
	public String getDbBacDbms(){
		return prop.getProperty(DB_BAC_DBMS);
	}
	public String getDbBacServerName(){
		return prop.getProperty(DB_BAC_SERVER_NAME);
	}
	public String getDbBacBaseDeDatos(){
		return prop.getProperty(DB_BAC_BASE_DE_DATOS);
	}

	public String getDbBacDriverName(){
		return prop.getProperty(DB_BAC_DRIVER_NAME);
	}

	public String getDbLicUserName(){
		return prop.getProperty(DB_LIC_USER_NAME);
	}
	public String getDbLicPassword(){
		return prop.getProperty(DB_LIC_PASSWORD);
	}
	public String getDbLicDbms(){
		return prop.getProperty(DB_LIC_DBMS);
	}
	public String getDbLicServerName(){
		return prop.getProperty(DB_LIC_SERVER_NAME);
	}
	public String getDbLicBaseDeDatos(){
		return prop.getProperty(DB_LIC_BASE_DE_DATOS);
	}

	public String getDbLicDriverName(){
		return prop.getProperty(DB_LIC_DRIVER_NAME);
	}
	
	public String getPathFileRoot(){
		return prop.getProperty(PATHFILE_ROOT);
	}
	
	public String getPathMateria(){
		return getPathFileRoot() + prop.getProperty(PATHFILE_MATERIA);
	}
	public String getPathUsuario(){
		return getPathFileRoot() + prop.getProperty(PATHFILE_USUARIO);
	}
	public String getPathConf(){
		return getPathFileRoot() + prop.getProperty(PATHFILE_CONF);
	}
	public String getAbsolutePath(){
		return prop.getProperty(ABSOLUTE_PATH);
	}
	public String getUserfilesAbsolutePath(){
		return prop.getProperty(ABSOLUTE_PATH) + File.separator + prop.getProperty(USERFILES);
	}
	public String getUserfilesPath(){
		return prop.getProperty(USERFILES_PATH);
	}
	public String getPruebaUrl(){
		return prop.getProperty(PRUEBA_URL);
	}
	public String getFtpLicServer(){
		return prop.getProperty(FTP_LIC_SERVER);
	}
	public int getFtpLicPort(){
		return Integer.parseInt(prop.getProperty(FTP_LIC_PORT));
	}
	public String getFtpLicUser(){
		return prop.getProperty(FTP_LIC_USER);
	}
	public String getFtpLicPassword(){
		return prop.getProperty(FTP_LIC_PASSWORD);
	}
	public String getPlantillas(){
		return prop.getProperty(PLANTILLAS);
	}

	private void Initialize() {
	    prop = new Properties();
	    
		try {
			prop.load(util.conf.Configuracion.class.getClassLoader().getResourceAsStream(CONFIG_FILE));

		} catch (FileNotFoundException e1) {
			Common.Error(e1);
		} 
	        
	    catch (IOException e) {
			Common.Error(e);
	    }
	}
	
	public List<String[]> getList(){
		
		List<String> list = new ArrayList<String>();
		List<String[]> listArr = new ArrayList<String[]>();
		Enumeration e = prop.propertyNames();
		while(e.hasMoreElements()){
			
			String key = e.nextElement().toString();
			if(key.equals(PATHFILE_CONF) || key.equals(PATHFILE_MATERIA) || key.equals(PATHFILE_ROOT) || key.equals(PATHFILE_USUARIO)
					|| key.equals(USERFILES) || key.equals(USERFILES_PATH) || key.equals(ABSOLUTE_PATH) || key.equals(PLANTILLAS)){
				continue;
			}
			
			list.add(key);
			
		}
		
		Collections.sort(list);
		
		for(String s : list){
			
			String[] arr = new String[2];
			arr[0] = s;
			arr[1] = getProperty(s);
			
			listArr.add(arr);
			
		}
		
		
		return listArr;
		
	}
	
	public String getProperty(String key){
		
		return prop.getProperty(key);
		
	}
	
	
	public void Write(String key, String val, Usuario usuario) throws Exception{
		prop.setProperty(key, val);
		
		String confpath = util.conf.Configuracion.class.getClassLoader().getResource(CONFIG_FILE).getPath();
		
		File f = new File(confpath);
        OutputStream out = new FileOutputStream( f );
        prop.store(out, "Actualizado por " + usuario.getCveUsu() + usuario.getNomCompletoUsu());
        out.close();
    }	
	
	
}
