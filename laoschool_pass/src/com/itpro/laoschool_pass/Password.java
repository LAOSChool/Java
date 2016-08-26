package com.itpro.laoschool_pass;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Password {
	 // The higher the number of iterations the more 
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 1000;//20*1000
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported. */
    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        // store the salt with the password
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
//    	return getSaltedHashMD5(password);        
        
    }

    /** Checks whether given plain text password corresponds 
        to a stored salted hash of the password. */
    public static boolean check(String password, String stored) throws Exception{
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException(
                "The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
//    	return checkMD5(password,stored);
    }

    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.encodeBase64String(key.getEncoded());
    }
    public static String getRandomPass(){
    		int randomNum = 1111 + (int)(Math.random() * 8889);// >= 1111 <10000 
    		return randomNum+"";
    }
    
    public static String getMD5Salt(){
    	return "ItPro";
    }
    
    public static String getSaltedHashMD5(String password) throws Exception {
    	byte[] bytesOfPass = password.getBytes("UTF-8");

    	MessageDigest md = MessageDigest.getInstance("MD5");
    	byte[] thedigest = md.digest(bytesOfPass);
    	return convertByteToHexa(thedigest);
    }
    
    private static String convertByteToHexa(byte[] mdbytes){
    	  //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<mdbytes.length;i++) {
    		String hex=Integer.toHexString(0xff & mdbytes[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
    }
    public static boolean checkMD5(String password, String stored) throws Exception{
        
        String hashOfInput = getSaltedHashMD5(password);
        return hashOfInput.equals(stored);
    }

    
}
