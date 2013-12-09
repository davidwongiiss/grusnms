package com.grus.nms.daemon.monitor.nsg9000;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public final class HttpConnection {
	private PoolingHttpClientConnectionManager connManager = null;
	private CloseableHttpClient httpclient = null;
	private HttpClientContext context = null;
	
	private String ip;

	private HttpConnection() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param ip
	 * @param user
	 * @param password
	 * @return
	 */
	public static HttpConnection connect(String ip, String user, String password, int timeout) {
		HttpConnection conn = new HttpConnection();
		if (conn.createConnect(ip, user, password, timeout))
			return conn;
		
		return null;
	}	

	public String send(String data) {
		String responseBody = "";
		CloseableHttpResponse response = null;

		try {
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					}
					else {
						throw new ClientProtocolException("Unexpected response status: "
								+ status);
					}
				}
			};

			StringBuffer sb = new StringBuffer();
			sb.append("http://").append(this.ip).append("/BrowseConfig");
			
			HttpPost httppost = new HttpPost(sb.toString());
			
			HttpEntity entity = new org.apache.http.entity.StringEntity(data, "UTF-8");
			httppost.setEntity(entity);

			responseBody = httpclient.execute(httppost, responseHandler);
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (response != null)
				try {
					response.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return responseBody;
	}

	public void disconnect() {
		try {
			this.httpclient.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean createConnect(String ip, String user, String password, int timeout) {
		boolean ok = false;
		
		this.connManager = new PoolingHttpClientConnectionManager();
		this.connManager.setMaxTotal(2);

		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE)
				.setCharset(Consts.UTF_8).build();
		// Configure the connection manager to use connection configuration either
		// by default or for a specific host.
		this.connManager.setDefaultConnectionConfig(connectionConfig);

		// Use custom cookie store if necessary.
		CookieStore cookieStore = new BasicCookieStore();
		// Use custom credentials provider if necessary.
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(ip, 80),
				new UsernamePasswordCredentials(user, password));

		// Create global request configuration
		RequestConfig defaultRequestConfig = RequestConfig
				.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH)
				.setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true)
				.setTargetPreferredAuthSchemes(
						Arrays.asList(AuthSchemes.BASIC, AuthSchemes.NTLM,
								AuthSchemes.DIGEST)).build();

		// Create an HttpClient with the given custom dependencies and
		// configuration.
		this.httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
				.setDefaultCredentialsProvider(credentialsProvider)
				.setDefaultRequestConfig(defaultRequestConfig).build();

		try {
			StringBuffer sb = new StringBuffer();
			sb.append("http://").append(ip).append("/AUTH/a");			
			HttpPost httpPost = new HttpPost(sb.toString());

			// Request configuration can be overridden at the request level.
			// They will take precedence over the one set at the client level.
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(5000).build();
			// httpget.setConfig(requestConfig);
			httpPost.setConfig(requestConfig);

			// Execution context can be customized locally.
			this.context = HttpClientContext.create();
			// Contextual attributes set the local context level will take
			// precedence over those set at the client level.
			this.context.setCookieStore(cookieStore);
			this.context.setCredentialsProvider(credentialsProvider);

			// System.out.println("executing request " + httpget.getURI());
			// CloseableHttpResponse response = httpclient
			// .execute(httpget, this.context);
			CloseableHttpResponse response = httpclient.execute(httpPost,
					this.context);

			try {
				boolean debug = false;
				if (debug) {
					int statusCode = response.getStatusLine().getStatusCode();
					System.out.println("statucCode: " + statusCode);
					HttpEntity entity = response.getEntity();

					System.out.println("----------------------------------------");
					System.out.println(response.getStatusLine());
					if (entity != null) {
						System.out.println("Response content length: "
								+ entity.getContentLength());
					}
					System.out.println("----------------------------------------");

					// Once the request has been executed the local context can
					// be used to examine updated state and various objects affected
					// by the request execution.
					// Last executed request
					context.getRequest();
					// Execution route
					context.getHttpRoute();
					// Target auth state
					context.getTargetAuthState();
					// Proxy auth state
					context.getTargetAuthState();
					// Cookie origin
					context.getCookieOrigin();
					// Cookie spec used
					context.getCookieSpec();

					// User security token
					context.getUserToken();

					List<Cookie> s = cookieStore.getCookies();
					for (int i = 0; i < s.size(); i++) {
						System.out.println(s.get(i).getName() + ": " + s.get(i).getValue());
					}
				}
				
				this.ip = ip;
				
				ok = true;
			}
			finally {
				response.close();
			}
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
		}
		
		return ok;
	}

	// 以后可以提取为request
	public String getAllData() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0'?><NSG>");
		sb.append("<IPINPCFG Action=\"GET\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"1\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"2\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"3\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"4\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"5\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"6\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"7\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"8\"/>");
		sb.append("<BLADETRAFFIC Action=\"GET\" Blade=\"9\"/>");
		sb.append("<STATUS Action='GET'/>");
		sb.append("</NSG>");
		
		return this.send(sb.toString());
	}
	
	public String getGbeData() {
		return this.send("<?xml version='1.0'?><NSG><IPINPCFG Action='GET'/></NSG>");
	}

	public String getQamData(int i) {
		return this.send("<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='" + i + "'/></NSG>");
	}
	
	public String getEvents() {
		return this.send("<?xml version='1.0'?><NSG><STATUS Action='GET'/></NSG>");
	}
	
	/**
	 * 测试main方法
	 * @param args
	 */
	public static void main(String args[]) {
		HttpConnection conn = HttpConnection.connect("192.168.11.45", "admin", "nsgadmin", 1000);

		String s = conn
			//	.send("<?xml version='1.0'?><NSG><SLOTSCFG Action='GET'/></NSG>");
				.send("<NSG><SLOTSCFG Action=\"GET_STATUS\" /></NSG>");
		System.out.println(s);
	}	
}
