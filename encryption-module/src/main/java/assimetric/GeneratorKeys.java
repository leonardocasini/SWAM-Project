package assimetric;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GeneratorKeys {
	private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    
    public GeneratorKeys(int keylength){ 
        try {
			this.keyGen = KeyPairGenerator.getInstance("RSA");
			this.keyGen.initialize(keylength);
			this.pair = this.keyGen.generateKeyPair();
	        this.privateKey = pair.getPrivate();
	        this.publicKey = pair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
       
    }
    public boolean saveKeys(String pathPub,String pathPriv) {
    	try {
			writeToFile(pathPub, publicKey.getEncoded());
			writeToFile(pathPriv, privateKey.getEncoded());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        
    }
    
    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }
    public void writeStringToFile(String path, String key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();
        
        FileOutputStream fos = new FileOutputStream(f);
        DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
        outStream.writeUTF(key);
        outStream.close();
    }
	
}

