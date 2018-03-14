//Citation: https://www.ibm.com/developerworks/library/x-xacml/ 
package com.security.aws.xacml;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import com.sun.xacml.Indenter;
import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.FilePolicyModule;
public class AccessPolicyDecisionPoint {

	public void evaluatePolicy()  {
		try {
			String requestFile = null;
		requestFile = "src/main/java/com/security/aws/xacml/request.xml";
		String policyFiles;
		policyFiles = "src/main/java/com/security/aws/xacml/policy.xml";
		FilePolicyModule fpm = new FilePolicyModule();
		fpm.addPolicy(policyFiles);
		//  Setup the PolicyFinder that this PDP will use
		PolicyFinder policyFinder = new PolicyFinder();
		Set policyMod = new HashSet();
		policyMod.add(fpm);
		policyFinder.setModules(policyMod);
		// create the PDP
		PDP pdp = new PDP(new PDPConfig(null, policyFinder, null));
		RequestCtx request = RequestCtx.getInstance(new FileInputStream(requestFile));
		// evaluate the request
		ResponseCtx response = pdp.evaluate(request);
		OutputStream out = new FileOutputStream("src/main/java/com/security/aws/xacml/result.xml");
		response.encode( out, new Indenter());
		}
		catch(Exception e )
		{
		}
		
		}
				
	}



