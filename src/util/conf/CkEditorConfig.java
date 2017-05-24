package util.conf;

import javax.servlet.ServletConfig;

import javax.servlet.http.HttpServletRequest;

import com.ckfinder.connector.configuration.Configuration;
import util.common.Common;

public class CkEditorConfig extends Configuration {
	 
    public CkEditorConfig(ServletConfig servletConfig) {
    	super(servletConfig);
    }	

    @Override
    public void init() throws Exception {
    	super.init();
    	
    }
    
    @Override
    protected Configuration createConfigurationInstance() {
    	return new CkEditorConfig(this.servletConf);
    }
    @Override
    public boolean checkAuthentication(final HttpServletRequest request) {

    	return request.getSession().getAttribute(Common.SESSION_NAME_USUARIO) != null;
    	
    }
}