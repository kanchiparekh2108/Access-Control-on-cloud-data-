package com.security.aws.access;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class UserAuthentication {

	public  Item loginAuthentication(String userid, String password){
		AWSCredentials credentials = Authentication.validateCredentials();		
		AmazonDynamoDBClient client = new AmazonDynamoDBClient(Authentication.validateCredentials()).withRegion(Regions.US_EAST_1);
		DynamoDB dynamoDB = new DynamoDB(client);
		Table table = dynamoDB.getTable("Login");
		Item item = table.getItem("userId", userid);
		return item;
	}
}
