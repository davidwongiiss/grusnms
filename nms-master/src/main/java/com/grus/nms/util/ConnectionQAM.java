package com.grus.nms.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;

public class ConnectionQAM {
	


	

	public static String getSession(String ip, String username, String password)
			throws Exception {
		HttpHost targetHost = new HttpHost(ip, 80, "http");

		DefaultHttpClient httpclient = new DefaultHttpClient();

		httpclient.getCredentialsProvider().setCredentials(
				new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials(username, password));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
		

		String url = "http://" + ip + "/XmlConfig";
		HttpPost httpPost = new HttpPost(url);
		// httpPost.setHeader("Content-Type",
		// "application/x-www-form-urlencoded");
		String data = "<?xml version='1.0'?><NSG><SLOTSCFG Action='GET'/></NSG>"; 
		HttpEntity entity = new StringEntity(data,"UTF-8");
		httpPost.setEntity(entity);

		HttpResponse response = httpclient.execute(targetHost, httpPost,
				localcontext);
		HttpEntity result = response.getEntity();
		Header h[] = response.getAllHeaders();
		for(int i=0;i<h.length;i++){
			System.out.println(h[i].getValue());
		}
        BufferedReader reader =  new BufferedReader(new InputStreamReader(result.getContent()));
        String text = null;
		while((text = reader.readLine()) != null){
			System.out.println(text);
		}
		return "";
	}
	
	public static void main(String args[]) throws Exception{
		ConnectionQAM.getSession("192.168.11.45", "admin", "nsgadmin");
	}
}
