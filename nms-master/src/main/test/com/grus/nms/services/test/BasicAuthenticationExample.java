package com.grus.nms.services.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;


public class BasicAuthenticationExample {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws HttpException 
	 */
	public static void main(String[] args) throws HttpException, Exception {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		client.getState().setCredentials(
				 
	            new AuthScope("http://192.168.11.45", 80, "realm"),
	 
	            new UsernamePasswordCredentials("admin", "nsgadmin")
	 
	        );
		GetMethod get = new GetMethod("http://192.168.11.45");
		get.setDoAuthentication( true );
		 
		 
		 
        try {          

            // execute the GET
 
            int status = client.executeMethod( get );
 
 
 
            // print the status and response
 
            System.out.println(status + "\n" + get.getResponseBodyAsString());
 
 
 
        } finally {
 
            // release any connection resources used by the method
 
            get.releaseConnection();
 
        }
	}

}
