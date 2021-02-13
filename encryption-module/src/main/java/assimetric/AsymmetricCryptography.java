package assimetric;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AsymmetricCryptography {
	private Cipher cipher;
	private PrivateKey privateKey;
    private PublicKey publicKey;

    public AsymmetricCryptography() {
        try {
			this.cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void loadPublicKeyFromByte(byte[] keyBytes) {
    	try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    this.publicKey = kf.generatePublic(spec);
		    System.out.println("CHIAVE PUBBLICA: " + "" +  this.publicKey.getEncoded() );
		}  catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }
    
    public void loadPrivateKeyFromByte(byte[] keyBytes) {
    	try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    this.privateKey = kf.generatePrivate(spec);
		    System.out.println("CHIAVE Privata: " + "" +  this.privateKey.getEncoded() );
		}  catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }
    
    public void  loadPublicKey(String filename){
        byte[] keyBytes;
		try {
			keyBytes = Files.readAllBytes(new File(filename).toPath());
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    this.publicKey = kf.generatePublic(spec);
		    System.out.println("CHIAVE PUBBLICA DA FILE: " + "" +  this.publicKey.getEncoded() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }
    public void  loadPrivateKey(String filename){
        byte[] keyBytes;
		try {
			keyBytes = Files.readAllBytes(new File(filename).toPath());
			//X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    this.privateKey = kf.generatePrivate(spec);
		    System.out.println("CHIAVE PRIVATA DA FILE: " + "" +  this.privateKey.getEncoded() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

    public String encryptText(String msg){
        try {
			this.cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
			//return Base64.getMimeEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
        
    }

    public String decryptText(String msg){
        try {
			this.cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(msg));
			return new String(decryptedBytes,"UTF-8");
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
        
        //return new String(cipher.doFinal(), "UTF-8");
    }
}

