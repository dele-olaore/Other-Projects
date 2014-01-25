package com.dexter.nextzon.ogas.util;

import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;
import org.cryptonode.jncryptor.JNCryptorFactory;

public class Secure
{
	private String password = "deXter";
	
	public String Decrypt(String stringToDecrypt)
	{
		String returnvalue;
		
		JNCryptor cryptor = JNCryptorFactory.getCryptor();
		byte[] ciphertext = stringToDecrypt.getBytes();
		
		try
		{
			byte[] plaintext = cryptor.decryptData(ciphertext, password.toCharArray());
			returnvalue = new String(plaintext);
		}
		catch(CryptorException e)
		{
			e.printStackTrace();
			returnvalue = null;
		}
		
	    return returnvalue;
	  }

	public String Encrypt(String stringToEncrypt)
	{
		String returnvalue = "";
		
		JNCryptor cryptor = JNCryptorFactory.getCryptor();
		byte[] plaintext = stringToEncrypt.getBytes();
		
		try
		{
			byte[] ciphertext = cryptor.encryptData(plaintext, password.toCharArray());
			returnvalue = new String(ciphertext);
		}
		catch(CryptorException e)
		{
			e.printStackTrace();
			returnvalue = null;
		}

		return returnvalue;
	}
}
