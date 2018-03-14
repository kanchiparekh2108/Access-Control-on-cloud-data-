package com.security.aws.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.security.aws.access.Authentication;
import com.security.aws.storage.AccessS3;

public class UploadFilesToS3 {

	public static String bucketName = "file-store-s3-bucket";
	public static AmazonS3 s3;
	public static void main(String[] args) {
		AWSCredentials credentials = Authentication.validateCredentials();
		s3 = Authentication.createAmazonS3Reference(credentials);
		UploadFilesToS3 upload=new UploadFilesToS3();
		upload.prepareS3Data();
	}
	public static void createBucket(String bucketName) {
		System.out.println("Creating bucket " + bucketName + "\n");
		s3.createBucket(bucketName);
	}
	public  static File createSampleFile() throws IOException {
		File file = File.createTempFile("Student", ".txt");
		file.deleteOnExit();

		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write("Zhe Robert: XYZ \n");
		writer.write("CS department \n");
		writer.write("GPA: 3.7 \n");
		writer.write("GRE score: 313 \n");
		writer.write("Toefl score : 97\n");
		writer.write("Robert Sue: XYZ \n");
		writer.write("CS department \n");
		writer.write("GPA: 3.5 \n");
		writer.write("GRE score: 310 \n");
		writer.write("Toefl score : 90\n");
		writer.close();

		return file;
	}

	private static File createSampleFile1() throws IOException {
		File file = File.createTempFile("Events", ".txt");
		file.deleteOnExit();

		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write("Seminar Date: 04/12/2017 \n");
		writer.write("Art Exhibition 05/12/2017 \n");
		writer.write("Career Fare 06/12/2017\n");
		writer.close();

		return file;
	}
	public static void uploadFileInS3(String bucketName, String key, File file) {
		s3.putObject(new PutObjectRequest(bucketName, key, file));
	}
	private void prepareS3Data() {

		try {
			System.out.println("===========================================");
			System.out.println("Creating bucket");
			System.out.println("===========================================\n");
			createBucket(bucketName);
			File file;
			file = createSampleFile();
			String key = "Student.txt";
			System.out.println("Uploading file "+key);
			uploadFileInS3(bucketName, key, file);
			file = createSampleFile1();
			String key2 = "Events.txt";
			System.out.println("Uploading file "+key2);
			uploadFileInS3(bucketName, key2, file);
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
