package com.security.aws.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class KeyGeneration {
	private static final String keyDir ="src/main/java/com/security/aws/key";
	private static final SecureRandom srand = new SecureRandom();

	public static void main(String[] args) throws Exception {
		KeyPair keypair = genKeyPair("RSA", 1024);
		saveKeyPair(keyDir, keypair);
	
	}

	public static KeyPair genKeyPair(String algorithm, int bitLength) throws NoSuchAlgorithmException {
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(algorithm);
		keyGenerator.initialize(1024, srand);
		System.out.println("Generated public and private key pair");
		return keyGenerator.generateKeyPair();
	}

	public static void saveKeyPair(String dir, KeyPair keyPair) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(dir + "/public.key");
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		fos = new FileOutputStream(dir + "/private.key");
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}

	
}
