package com.security.aws.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AccessS3 {
	public static AmazonS3 s3;
	public static String bucketName = "file-store-s3-bucket";

	public AccessS3(AmazonS3 s3) {
		this.s3 = s3;
	}

	public static void downloadingFileFromS3(String bucketName, String key) {
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		try {
			displayTextInputStream(object.getObjectContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getAllObjectsFromS3(String bucketName) {

		ObjectListing objectListing = s3
				.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("file"));
		final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
		ListObjectsV2Result result;
		result = s3.listObjectsV2(req);
		for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
			System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
			downloadingFileFromS3(bucketName, objectSummary.getKey());
		}
	}

	public static void listS3Data() throws IOException {
		getAllObjectsFromS3(bucketName);

	}

	private static void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			System.out.println("    " + line);
		}
		System.out.println();
	}

}
