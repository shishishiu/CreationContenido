package servlet.general;

import java.io.File;
import ch.ethz.ssh2.*;
import ch.ethz.ssh2.Session;

import java.text.MessageFormat;

import util.common.Common;

public class Sftp {

	private String hostName;
	private int port;
	private String userName;
	private String password;
	private boolean isConnected;
	private boolean binaryTransfer;
	private boolean usePassiveMode;
//	private String hostPath;
	private String localPath;
	private String encoding;
	private Session session;
	private Connection conn;
    
    public boolean getIsConnected(){
    	return this.isConnected;
    }

    public Sftp(String hostName, int port, String userName, String password, 
    		boolean binaryTransfer, boolean usePassiveMode, String encoding) {
	    this.hostName = hostName;
	    this.port = port;
	    this.userName = userName;
	    this.password = password;
	    this.binaryTransfer = binaryTransfer;
	    this.usePassiveMode = usePassiveMode;
//	    this.hostPath = hostPath;
//	    this.localPath = localPath;
	    this.encoding = encoding;
	    this.isConnected = false;
    }

    public Sftp() {
	    this.isConnected = false;
    }

    public boolean Connect() throws Exception {

    	
//    	JSch jsch=new JSch();
//    	// connect session
//    	com.jcraft.jsch.Session sessionJsch=jsch.getSession(this.userName, this.hostName, 22);
//    	sessionJsch.setPassword(this.password);
//    	sessionJsch.connect();
    	
    	
    	this.conn = new Connection(this.hostName);
		conn.connect();
		this.isConnected = conn.authenticateWithPassword(this.userName, this.password);
		
		return this.isConnected;
		
    }
    	 
    
    public boolean Disconnect() throws Exception {
	    if ( this.isConnected ) {
			this.conn.close();
		}
	    return true;
    }

    public boolean Delete(String remotePath) throws Exception{
    	
  	  
	    boolean success = true;
	    if (!this.isConnected){
	        success = Connect();
	    }
	    if (success) {
	    	
	        String command = "rm -rf {0}";
	        command = MessageFormat.format(command, remotePath) ;
	        Session sess = conn.openSession();
	        sess.execCommand(command);
	        sess.close();
	    	
	    }
	    
	    return true;
    }

    
    
    public boolean Put(String localPath, String targetRemotePath) throws Exception{
	
	  
	    boolean success = true;
	    if (!this.isConnected){
	        success = Connect();
	    }
	    if (success) {
	    	
	    	return PutFiles(new File(localPath), targetRemotePath );
	    }
	    
	    return true;
    }
    
    
    private boolean PutFiles(File file, String remotePath) throws Exception {
    	boolean success = true;
  	  	SCPClient scp = this.conn.createSCPClient();
	
		if (file.isFile()) {
		    try {
	
			  scp.put(file.getAbsolutePath(), remotePath);
			  
		    	
		    } catch ( Exception e ) {
		    	Common.Error("FTP PUT Failed: " + file.getName() + " " + e.getMessage());
		        success = false;
		    }
		
		} else if (file.isDirectory() ) {
			
			String dirName = file.getName();
			
	        String command = "cd {0};mkdir {1}";
	        command = MessageFormat.format(command, remotePath, dirName) ;
	        Session sess = conn.openSession();
	        sess.execCommand(command);
	        sess.close();
	
		    File[] files = file.listFiles();
		    for ( File f : files ) {
		    	success = PutFiles(f, remotePath + "/" + dirName);
		        if ( !success )
		          return success;
		    }
		}
	 	return success;
	  } 
    
    public boolean Get(String remotePath, String targetLocalPath) throws Exception{
  	  
	    boolean success = true;
  	  	
  	  	
	    if (!this.isConnected){
	        success = Connect();
	    }
	    if (success) {

	        String command = "scp -r licusrcont@192.168.1.220:Pscl/1/LPSBC01APS01 /server/tomcat7/webapps/CreationContenido/userfiles/Lic";
	        command = MessageFormat.format(command, remotePath) ;
	        Session sess = conn.openSession();
	        sess.execCommand(command);
	        
	        sess.close();

	    	
	  	  	SCPClient scp = this.conn.createSCPClient();
	  	  	scp.get(remotePath, targetLocalPath);

	    }
	    
	    return true;
    }
 
  
}
