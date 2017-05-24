package util.conf;

import javax.servlet.http.HttpServletRequest;

import com.ckfinder.connector.configuration.DefaultPathBuilder;

public class CkEditorPathBuilder extends DefaultPathBuilder{

	public static final String SESSION_CKEDITOR_CONT_ABSOLUTE_PATH = "ckEditorAbsolutePath";
	public static final String SESSION_CKEDITOR_CONT_URL = "ckEditorUrl";
	
	@Override
    public String getBaseUrl(HttpServletRequest request) {
		if(request.getSession().getAttribute(SESSION_CKEDITOR_CONT_URL) != null){
			return request.getSession().getAttribute(SESSION_CKEDITOR_CONT_URL).toString();
		}
		return "";
    }
    
    @Override
    public String getBaseDir(HttpServletRequest request) {
    	if(request.getSession().getAttribute(SESSION_CKEDITOR_CONT_ABSOLUTE_PATH) != null){
			return request.getSession().getAttribute(SESSION_CKEDITOR_CONT_ABSOLUTE_PATH).toString();
		}
    	return "";
    }
}
