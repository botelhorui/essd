package pt.ulisboa.tecnico.essd.crypto;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;


public class AESCipher {
	
	private static final byte[] _salt = "xptoleao".getBytes();
	private static final int _iterations = 65536;
	private static final int _keyLength = 256;

	private static Cipher _cipher;
	private static byte[] _iv;

	public AESCipher() throws NoSuchAlgorithmException, InvalidParameterSpecException,
	NoSuchPaddingException{
		_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		_iv = _cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
	}

	byte[] createKeyFromPassword(String password) throws NoSuchAlgorithmException,
	InvalidKeySpecException, InvalidKeyException {
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), _salt, _iterations, _keyLength);

		SecretKey tmp = factory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

		return secret.getEncoded();
	}

	byte[] encrypt(byte[] toCipher, byte[] key) throws InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException{
		//Recria chave
		SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
		//Inicia cifra em modo encrypt
		_cipher.init(Cipher.ENCRYPT_MODE, originalKey);
		//Cifra data
		byte[] cipherBytes = _cipher.doFinal(toCipher);
		//Retorna data cifrada
		return cipherBytes;
	}

	byte[] decrypt(byte[] toDecrypt, byte[] key) throws InvalidKeyException,
	IllegalBlockSizeException, InvalidAlgorithmParameterException,
	BadPaddingException{
		//Recria chave
		SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
		//Inicia cifra em modo decrypt
		_cipher.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(_iv));
		//Descifra data
		byte[] clearBytes = _cipher.doFinal(toDecrypt);
		//Retorna data nao cifrada
		return clearBytes;
	}

	byte[] generateKey() throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(_keyLength);
		SecretKey secretKey = keyGen.generateKey();
		return secretKey.getEncoded();
	}

}