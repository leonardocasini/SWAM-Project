package simmetric;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SimmetricCryptography {

	private byte[] output;
	private Key key;
	private Cipher cipher;
	
	public SimmetricCryptography(String pw) {
		try {
			setKey(pw);
			setCipher();
		} catch (GeneralSecurityException e) {
			
			e.printStackTrace();
		}
	}
	public SimmetricCryptography() {
		String pw = "default";
		try {
			setKey(pw);
			setCipher();
		} catch (GeneralSecurityException e) {
			
			e.printStackTrace();
		}
	}
	public String getEncriptedText(String s) {
		String tmp  = new String();
		try {
			tmp = encriptText(s);
		} catch (GeneralSecurityException e) {
			
			e.printStackTrace();
		}
		return tmp;
	}
	public String getDecryptedText(String s) {
		String tmp  = new String();
		try {
			tmp = decriptText(s);
		} catch (GeneralSecurityException e) {
			
			e.printStackTrace();
		}
		return tmp;
	}
	
	
	public void setKey(String pw) throws GeneralSecurityException{
		byte[] salt = // for learning purposes
            { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
              (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
		char[] password = pw.toCharArray();
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		key = secret;
	}
	
	
	public void setCipher() throws GeneralSecurityException{
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	}
	
	
	public String encriptText(String s) throws GeneralSecurityException {
		
		byte[] input;		
		input = s.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE,key);
		byte[] cyperText = cipher.doFinal(input);
		return Base64.getMimeEncoder().encodeToString(cyperText);
			
	 }
	
	
	public String decriptText(String s) throws GeneralSecurityException {
		
		cipher.init(Cipher.DECRYPT_MODE,key);		
		byte[] output2 = cipher.doFinal(Base64.getMimeDecoder().decode(s));		
		return new String(output2);
		
	    }

}
