package com.security.aws.access;

import java.io.File;
import java.io.FileInputStream;
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

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.EncryptionMaterials;

public class Authentication {

	public static AWSCredentials validateCredentials(){
		AWSCredentials credentials = null;
		System.out.println("validating credentials");
        try {
        	credentials = new BasicAWSCredentials("", "");

        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. ",e);
        }
        return credentials;
	}
	
	public static AmazonS3 createAmazonS3Reference(AWSCredentials credentials){
		String keyDir ="src/main/java/com/security/aws/key";
		try {
			KeyPair myKeyPair;
			myKeyPair = loadKeyPair(keyDir,"RSA");			
			EncryptionMaterials encryptionMaterials = new EncryptionMaterials(myKeyPair);			
			AmazonS3EncryptionClient s3 = new AmazonS3EncryptionClient(credentials, encryptionMaterials);
			 return s3;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			System.out.println("Encryption decryption keys not set");
		}catch( IOException e){
			System.out.println("IO exception");
		}
		
		return null;
	}
	public static KeyPair loadKeyPair(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// read public key from file
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis = new FileInputStream(filePublicKey);
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();

		// read private key from file
		File filePrivateKey = new File(path + "/private.key");
		fis = new FileInputStream(filePrivateKey);
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		// Convert them into KeyPair
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		return new KeyPair(publicKey, privateKey);
	}

}
