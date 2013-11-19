package com.grus.nms.xml;

import java.io.ByteArrayInputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.grus.nms.pojo.GbeValues;
import com.grus.nms.pojo.Nodes;
import com.grus.nms.pojo.QamValues;
import com.grus.nms.util.XmlManager;

public class XmlParser {

	public static GbeValues gbeXmlParser(String xmlContent,Nodes n) throws Exception{
		GbeValues gbe = new GbeValues();
		gbe.setNodeId(n.getId());
		Document document = XmlManager.parse(new ByteArrayInputStream(xmlContent.getBytes()));
		NodeList list = document.getElementsByTagName("IpInpCfg");
		System.out.println(list.getLength());
		for(int i=0;i<list.getLength();i++){
			Node node = list.item(i).getAttributes().getNamedItem("Id");
			if("1".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate1(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices1(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if("2".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate2(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices2(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if("3".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate3(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices3(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if("4".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate4(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices4(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if("5".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate5(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices5(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if("6".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate6(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices6(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if("7".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate7(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices7(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if("8".equals(node.getNodeValue())){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate8(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices8(Integer.parseInt(numOfServices.getNodeValue()));
			}
			
		}
		return gbe;
	}
	
	
	public static QamValues qamXmlParser(String xmlContent,Nodes n,int blade) throws Exception{
		QamValues qam = new QamValues();
		qam.setNodeId(n.getId());
		qam.setBlade(blade);
		Document document = XmlManager.parse(new ByteArrayInputStream(xmlContent.getBytes()));
		NodeList list = document.getElementsByTagName("TsOut");
		System.out.println(list.getLength());
		for(int i=0;i<list.getLength();i++){
			Node node = list.item(i).getAttributes().getNamedItem("TsId");
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 1){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam1(1);
				qam.setBitrate1(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices1(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 2){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam2(2);
				qam.setBitrate2(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices2(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 3){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam3(3);
				qam.setBitrate3(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices3(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 4){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam4(4);
				qam.setBitrate4(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices4(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 5){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam5(5);
				qam.setBitrate5(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices5(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 6){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam6(6);
				qam.setBitrate6(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices6(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 7){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam7(7);
				qam.setBitrate7(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices7(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 8){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam8(8);
				qam.setBitrate8(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices8(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 9){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam9(9);
				qam.setBitrate9(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices9(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 10){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam10(10);
				qam.setBitrate10(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices10(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 11){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam11(11);
				qam.setBitrate11(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices11(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 12){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam12(12);
				qam.setBitrate12(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices12(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 13){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam13(13);
				qam.setBitrate13(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices13(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 14){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam14(14);
				qam.setBitrate14(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices14(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 15){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam15(15);
				qam.setBitrate15(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices15(Integer.parseInt(numOfServices.getNodeValue()));
			}
			if(Integer.parseInt(node.getNodeValue())-((blade-1)*16) == 16){
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("Bitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				qam.setQam16(16);
				qam.setBitrate16(Integer.parseInt(multicastBitrate.getNodeValue()));
				qam.setNumOfServices16(Integer.parseInt(numOfServices.getNodeValue()));
			}
		}
		return qam;
	}
}
