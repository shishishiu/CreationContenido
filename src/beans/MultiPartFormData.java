package beans;

import java.util.Hashtable;

public class MultiPartFormData {

private Hashtable<String, String> parameters;
private Hashtable<String, String> uploadedFiles;

	public MultiPartFormData()
	{
	    this.parameters = new Hashtable<String, String>();
	    this.uploadedFiles = new Hashtable<String, String>();
	}
	
	public Hashtable<String, String> getParameters() {
	    return parameters;
	}
	public void setParameters(Hashtable<String, String> parameters) {
	    this.parameters = parameters;
	}
	
    public String getParameter(String paramName) {
	  if(this.parameters.containsKey(paramName)){
		  return this.parameters.get(paramName);
	  }
	  return null;
    }
    public void addFile(String key, String filename) {
        uploadedFiles.put(key, filename);
    }
    public void getFilename(String key) {
        uploadedFiles.get(key);
    }
} 