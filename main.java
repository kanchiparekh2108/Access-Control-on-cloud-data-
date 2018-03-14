package com.security.aws.main;

import java.io.IOException;
import java.util.Scanner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.s3.AmazonS3;
import com.security.aws.access.Authentication;
import com.security.aws.access.UserAuthentication;
import com.security.aws.storage.AccessS3;
import com.security.aws.xacml.AccessPolicyEnforcementPoint;

public class main {

	public static void main(String[] args) throws IOException {

		UserAuthentication auth = new UserAuthentication();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter login id ");
		String userid = sc.nextLine();
		System.out.println("Enter password ");
		String password = sc.nextLine();
		System.out.println("Executing policies to fetch user attributes and permisions...");
		Item item = auth.loginAuthentication(userid, password);

		if (item == null) {
			System.out.println("not authenticated");
			System.exit(1);
		} else {
			String passwordFromDB = (String) item.get("password");

			if (password.equals(passwordFromDB)) {

				System.out.println("Role fetched for the user is : "+item.get("role"));
				System.out.println("Department fetched for the user is : "+item.get("department"));
				String policyResult = AccessPolicyEnforcementPoint.request((String) item.get("role"),
						(String) item.get("department"));
				
				if ("Permit".equals(policyResult)) {
					System.out.println(policyResult);
					AWSCredentials credentials = Authentication.validateCredentials();
					System.out.println("validated......");
					AmazonS3 s3 = Authentication.createAmazonS3Reference(credentials);
					AccessS3 s3access = new AccessS3(s3);
					s3access.listS3Data();
					
				} else {
					System.out.println("User is not authenticated");
					System.exit(1);
				}

			}else{
				System.out.println("User is not authenticated");
				System.exit(1);
			}

		}

	}
}
