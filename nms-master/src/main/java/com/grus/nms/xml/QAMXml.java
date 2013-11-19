package com.grus.nms.xml;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.grus.nms.factory.ServicesFactory;
import com.grus.nms.pojo.GbeValues;
import com.grus.nms.pojo.Nodes;
import com.grus.nms.pojo.QamValues;

public class QAMXml implements Runnable {
	
	
	private Nodes node;
	
	public QAMXml(Nodes node){
		this.node = node;
	}

	@Override
	public synchronized void run() {

		//session :8ced02b04f8653f76036eef04c1e438940901f2f
		String url = "http://"+this.node.getIp()+"/XmlConfig";
		String gbeData = "<?xml version='1.0'?><NSG><IPINPCFG Action='GET'/></NSG>";
		String session = "b644ae0720178c3fa1b5db18ca2d0c8c76db196a";
		
		saveXml(gbeData,url,this.node,session,"gbe");
		
		String qam1Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='1'/></NSG>";
		saveXml(qam1Data,url,this.node,session,"qam1");
		
		String qam2Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='2'/></NSG>";
		saveXml(qam2Data,url,this.node,session,"qam2");
		
		String qam3Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='3'/></NSG>";
		saveXml(qam3Data,url,this.node,session,"qam3");
		
		String qam4Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='4'/></NSG>";
		saveXml(qam4Data,url,this.node,session,"qam4");
		
		String qam5Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='5'/></NSG>";
		saveXml(qam5Data,url,this.node,session,"qam5");
		
		String qam6Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='6'/></NSG>";
		saveXml(qam6Data,url,this.node,session,"qam6");
		
		String qam7Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='7'/></NSG>";
		saveXml(qam7Data,url,this.node,session,"qam7");
		
		String qam8Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='8'/></NSG>";
		saveXml(qam8Data,url,this.node,session,"qam8");
		
		String qam9Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='9'/></NSG>";
		saveXml(qam9Data,url,this.node,session,"qam9");
	}
	
	
	/*public static void main(String args[]){
		String data = "<?xml version='1.0'?><NSG><IPINPCFG Action='GET'/></NSG>";
		String url = "http://192.168.11.45/XmlConfig";
		
		//new QAMXml("192.168.11.45").saveXml(data, url, "192.168.11.45", "8ced02b04f8653f76036eef04c1e438940901f2f", "gbe");
		Nodes n = new Nodes();
		n.setId("e15117e2-4842-11e3-8317-c3f7b0cf833f");
		n.setIp("192.168.11.45");
		String qam9Data = "<?xml version='1.0'?><NSG><BLADETRAFFIC Action='GET' Blade='9'/></NSG>";
		String url = "http://"+n.getIp()+"/XmlConfig";
		new QAMXml(null).saveXml(qam9Data,url,n,"b644ae0720178c3fa1b5db18ca2d0c8c76db196a","qam9");
	}*/
	
	public void saveXml(String data,String url,Nodes node,String session,String dataType){
		/*String data = "<?xml version='1.0'?><NSG><SLOTSCFG Action='GET'/></NSG>";  
		String url = "http://192.168.11.45/XmlConfig";  */
		  
		HttpClient httpclient = new HttpClient();  
		        PostMethod post  = new PostMethod(url);  
		        String info = null;  
		        try {  
		            RequestEntity entity = new StringRequestEntity(data, "text/xml",  
		            "utf-8");  
		            post.setRequestEntity(entity);
		            post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		            post.setRequestHeader("Host", node.getIp());
		            post.setRequestHeader("Connection", "keep-alive");
		            post.setRequestHeader("Content-Length", data.length()+"");
		            post.setRequestHeader("Accept", "application/xml, text/xml, */*");
		            post.setRequestHeader("Origin", "http://"+node.getIp());
		            post.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		            post.setRequestHeader("Cookie", "webpy_session_id="+session);
		            httpclient.executeMethod(post);   
		            int code = post.getStatusCode(); 
		            if(code == HttpStatus.SC_TEMPORARY_REDIRECT){
		            	Header header = post.getResponseHeader("location");
		            	if(header != null){
		            		String newUrl = header.getValue();
		            		System.out.println("新的请求地址："+newUrl);
		            		GetMethod redirect = new GetMethod(newUrl);
		            		httpclient.executeMethod(redirect);
		            	}
		            }
		            if (code == HttpStatus.SC_OK)  {
		            	if("gbe".equals(dataType)){
		            		//取得到XML文件內容
			            	info = post.getResponseBodyAsString(); 
			            	
			            	//进行XML解析，保存到对应的对象，并进行数据的增加
			            	GbeValues gbe = XmlParser.gbeXmlParser(info,node);
			            	
			            	//将取得到的数据保存到数据库
			            	ServicesFactory.getIGbeValuesServicesInstance().insertGbeValuesForGbeDay(gbe);
		            	}
		            	if("qam1".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 1);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam2".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 2);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam3".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 3);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam4".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 4);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam5".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 5);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam6".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 6);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam7".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 7);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam8".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 8);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            	if("qam9".equals(dataType)){
		            		info = post.getResponseBodyAsString();
		            		QamValues qam = XmlParser.qamXmlParser(info, node, 9);
		            		ServicesFactory.getIQamValuesServicesInstance().insertQamValuesForDay(qam);
		            	}
		            }
		                
		        } catch (Exception ex) {  
		            ex.printStackTrace();  
		        } finally {  
		            post.releaseConnection();
		        }  
		        
		        
	}
	}

