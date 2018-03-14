//Citation: https://www.ibm.com/developerworks/library/x-xacml/ 
package com.security.aws.xacml;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.Indenter;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.RFC822NameAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.Subject;
import java.io.*;
public class AccessPolicyEnforcementPoint {
	public static String request(String role, String dept)  {
		try
		{
			System.out.println(role);
			HashSet subjects = new HashSet();
			HashSet attributes = new HashSet();
			HashSet att= new HashSet();
			HashSet resource = new HashSet();
			HashSet action = new HashSet();
			URI subjectId = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
			RFC822NameAttribute value = new RFC822NameAttribute("rajani1@umbc.edu");
			attributes.add(new Attribute(subjectId, null, null, value));
			URI groupId = new URI(dept);
			StringAttribute stringAttribValue = new StringAttribute(role);
			attributes.add(new Attribute(groupId,null,null,stringAttribValue));
			att.add(dept);
			subjects.add(new Subject(attributes));
					AnyURIAttribute resID =
						new AnyURIAttribute(new URI("Student.txt"));
					resource.add(
						new Attribute(
							new URI(EvaluationCtx.RESOURCE_ID),
							null,
							null,
							resID));
					URI actionId = new URI("urn:oasis:names:tc:xacml:1.0:action:action-id");
					
					action.add(
						new Attribute(actionId, null, null, new StringAttribute("open")));
		RequestCtx request =
			new RequestCtx(
				subjects,
				resource,
				action,
				new HashSet());
		OutputStream out = new FileOutputStream("src/main/java/com/security/aws/xacml/request.xml");
		request.encode( out, new Indenter());
		AccessPolicyDecisionPoint a = new AccessPolicyDecisionPoint();
		a.evaluatePolicy();
		ReadXMLfile b = new ReadXMLfile();
		return b.readXML();
		}
		catch(Exception e)
		{
				System.out.println("Erorr execption in readinf xml"+e);
		}
		return " ";
		}
			
	}




