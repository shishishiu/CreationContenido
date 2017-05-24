package servlet.general;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import util.common.Common;

public class Ftp {

	private String hostName;
	private int port;
	private String userName;
	private String password;
	private boolean isConnected;
	private boolean binaryTransfer;
	private boolean usePassiveMode;
	private String hostPath;
	private String localPath;
	private String encoding;
    private FTPClient client = new FTPClient();
    
    public boolean getIsConnected(){
    	return this.isConnected;
    }

    public Ftp(String hostName, int port, String userName, String password, 
    		boolean binaryTransfer, boolean usePassiveMode, String hostPath, 
    		String localPath, String encoding) {
	    this.hostName = hostName;
	    this.port = port;
	    this.userName = userName;
	    this.password = password;
	    this.binaryTransfer = binaryTransfer;
	    this.usePassiveMode = usePassiveMode;
	    this.hostPath = hostPath;
	    this.localPath = localPath;
	    this.encoding = encoding;
	    this.isConnected = false;
    }

    public Ftp() {
	    this.isConnected = false;
    }

    public boolean Connect() throws Exception {
	    boolean success = Check();
	    if ( !success )
	      return false;
	    this.client = new FTPClient();

	    this.client.setControlEncoding(this.encoding);
	    client.connect(this.hostName, this.port);

	    this.client.login(this.userName, this.password);

	    if ( !FTPReply.isPositiveCompletion(this.client.getReplyCode()) ) {
	      System.out.println("Login Failed");
	      this.client.disconnect();
	      return false;

	    } else {
	      this.isConnected = true;
	    }

	    if ( this.binaryTransfer ) {
	      this.client.setFileType(FTP.BINARY_FILE_TYPE);
	    }

	    if ( this.usePassiveMode ) {
	    	this.client.enterLocalPassiveMode();
	    } else {
	        this.client.enterLocalActiveMode();
	    }

//	    success = this.client.changeWorkingDirectory(this.hostPath);
//	    if ( !success ) {
//	    	Common.Error("Server Directory Failed");
//	      this.client.disconnect();
//	      return false;
//	    }

	    success = FTPReply.isPositiveCompletion(this.client.getReplyCode());
	    return success;
    }
    	 
    
    public boolean Disconnect() throws Exception {
	    if ( this.isConnected ) {
	      client.logout();
	      if ( client.isConnected() ) client.disconnect();
	    }
	    return true;
    }

    private boolean IsEmpty(String value) {
	    if ( value == null || value.length() == 0 )
	      return true;
	    else
        return false;
    }

    private boolean Check() {
    	boolean success = true;

    	if (IsEmpty(this.hostName) ) {
    		Common.Error("hostName Parameter Failed");
    	    success = false;
    	}

    	if (this.port == 0 ) {
    		Common.Error("port Parameter Failed");
    	    success = false;
    	}

    	if (IsEmpty(this.userName) ) {
    		Common.Error("userName Parameter Failed");
    	    success = false;
    	}

    	if ( IsEmpty(this.password) ) {
    		Common.Error("password Parameter Failed");
    	    success = false;
    	}

    	if ( IsEmpty(this.hostPath) ) {
    		Common.Error("hostPath Parameter Failed");
    	      success = false;
    	}

    	if ( IsEmpty(this.localPath) ) {
    		Common.Error("localPath Parameter Failed");
    	    success = false;
    	}
    	if ( IsEmpty(this.encoding) ) {
    		Common.Error("encoding Parameter Failed");
    	    success = false;
    	}
    	    return success;
    }

    public boolean Put(boolean createDir) throws Exception{
    	
        boolean success = true;
        if (!this.isConnected){
            success = Connect();
        }
        if (success) {
        	success = this.client.changeWorkingDirectory(this.hostPath);
        	if (!success) {
        		
        		if(createDir){

        			success = this.client.makeDirectory(this.hostPath);
        			if(!success){
    		            Common.Error("Make Directory Failed");
    		            this.client.disconnect();
        				return false;
        			}
        			
        		}else{
		            Common.Error("Server Directory Failed");
		            this.client.disconnect();
		            return false;
        		}
        	}
          
        	return PutFiles(new File(this.localPath), 
        			this.hostPath + (this.hostPath.endsWith("/") ? "" : "/"));
        }
        
        return success;
      }
    
    private boolean PutFiles(File file, String hostPath) throws Exception {
    	boolean success = true;
    	
    	if (file.isFile()) {
    		FileInputStream inputSt = null;
    	    try {
    	    	inputSt = new FileInputStream(file);

    	        this.client.storeFile(hostPath + file.getName(), inputSt);
    	        inputSt.close();

    	    } catch ( Exception e ) {
    	    	Common.Error("FTP PUT Failed: " + file.getName() + " " + e.getMessage());
    	        success = false;
    	    } finally {
    	        if (inputSt != null )
    	        	inputSt.close();
    	      }
    	} else if (file.isDirectory() ) {
    		String dirName = hostPath + file.getName() + "/";

    	    success = this.client.makeDirectory(dirName);
    	    if (success) {
    	        success = this.client.changeWorkingDirectory(dirName);
    	    } else {
    	        success = this.client.changeWorkingDirectory(dirName);
    	        if ( !success ) {
    	        	Common.Error("Server Directory Failed: " + dirName);
    	          return success;
    	        }
   	        }

    	    File[] files = file.listFiles();
    	    for ( File f : files ) {
    	    	success = PutFiles(f, dirName);
    	        if ( !success )
    	          return success;
    	    }
    	}
    	return success;
    }
}
