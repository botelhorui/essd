package pt.ulisboa.tecnico.essd.crypto;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Arrays;


public class MACCipher {

	private static Mac _cipher;

	public MACCipher() throws NoSuchAlgorithmException, InvalidParameterSpecException,
	NoSuchPaddingException{
		_cipher = Mac.getInstance("HmacSHA256");
	}

	public byte[] makeMAC(byte[] data, SecretKey key) throws Exception{
		_cipher.init(key);
		byte[] cipherDigest = _cipher.doFinal(data);

		return cipherDigest;
	}

	public boolean verifyMAC(byte[] cipherDigest, byte[] data, SecretKey key) throws Exception {
		_cipher.init(key);
		byte[] cipheredBytes = _cipher.doFinal(data);
		
		return Arrays.equals(cipherDigest, cipheredBytes);
	}
}