package com.security.aws.main;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.security.aws.access.Authentication;

public class RemoveS3Data {
	private static AmazonS3 s3;
	public static void main(String[] args) {
		AWSCredentials credentials = Authentication.validateCredentials();
		 s3= Authentication.createAmazonS3Reference(credentials);
		deleteAllBuckets();
	}
	public static void deleteAllBuckets(){
		for (Bucket bucket : s3.listBuckets()) {
			if(!bucket.getName().contains("elastic")){
				List<String> keys=getKeys(bucket.getName());
				for(int i=0;i<keys.size();i++){
					System.out.println("Deleting an object from cloud");
					deleteFileFromS3(bucket.getName(), keys.get(i));
					
				}
				deleteBucketFromS3(bucket.getName());
			}
		}
	}
	public static List<String> getKeys(String bucketName) {
		ObjectListing objectListing = s3
				.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("file"));

		final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
		ListObjectsV2Result result;

		result = s3.listObjectsV2(req);
		List<String> list = new ArrayList<String>();
		for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
			list.add(objectSummary.getKey());
			System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");

		}
		return list;
	}
	public static void deleteFileFromS3(String bucketName, String key) {
		s3.deleteObject(bucketName, key);
	}

	public static void deleteBucketFromS3(String bucketName) {
		s3.deleteBucket(bucketName);
	}

	
}
