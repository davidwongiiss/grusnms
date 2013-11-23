package com.grus.nms.daemon.monitor.nsg9000;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.grus.nms.daemon.monitor.nsg9000.pojo.EventValue;
import com.grus.nms.daemon.monitor.nsg9000.pojo.GbeValue;
import com.grus.nms.daemon.monitor.nsg9000.pojo.QamValue;
import com.grus.nms.daemon.monitor.util.XmlManager;

public class XmlParser {
	public static GbeValue gbeXmlParser(String xmlContent, com.grus.nms.daemon.monitor.nsg9000.pojo.Node deviceNode)
			throws Exception {
		GbeValue gbe = new GbeValue();
		gbe.setNodeId(deviceNode.getId());

		Document document = XmlManager.parse(new ByteArrayInputStream(xmlContent.getBytes()));
		NodeList list = document.getElementsByTagName("IpInpCfg");
		System.out.println(list.getLength());
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i).getAttributes().getNamedItem("Id");
			if ("1".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate1(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices1(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if ("2".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate2(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices2(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if ("3".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate3(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices3(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if ("4".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate4(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices4(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if ("5".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate5(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices5(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if ("6".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate6(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices6(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if ("7".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate7(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices7(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if ("8".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate8(Long.parseLong(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices8(Long.parseLong(numOfServices.getNodeValue()));
			}
		}

		return gbe;
	}
	
	/**
	 * 避免1条
	 * 
	 * @param xmlContent
	 * @param deviceNode
	 * @param blade
	 * @return
	 * @throws Exception
	 */
	public static QamValue qamXmlParser1(String xmlContent, com.grus.nms.daemon.monitor.nsg9000.pojo.Node deviceNode)
			throws Exception {

		Document document = XmlManager.parse(new ByteArrayInputStream(xmlContent.getBytes()));

		QamValue qam = new QamValue();
		qam.setNodeId(deviceNode.getId());
		
		NodeList root = document.getElementsByTagName("BLADETRAFFIC");
		if (root.getLength() == 0)
			return null;
		
		Node bladeNode = root.item(0).getAttributes().getNamedItem("Blade");
		int blade = Integer.parseInt(bladeNode.getNodeValue());
		
		qam.setBlade(blade);

		NodeList list = document.getElementsByTagName("TsOut");
		System.out.println(list.getLength());
		for (int i = 0; i < list.getLength(); i++) {
			NamedNodeMap map = list.item(i).getAttributes();

			Node node = map.getNamedItem("TsId");
			if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 1) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam1(true);//map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate1(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices1(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 2) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam2(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate2(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices2(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 3) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam3(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));;
				qam.setBitrate3(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices3(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 4) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam4(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate4(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices4(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 5) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam5(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate5(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices5(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 6) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam6(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate6(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices6(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 7) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam7(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate7(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices7(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 8) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam8(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate8(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices8(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 9) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam9(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate9(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices9(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 10) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam10(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate10(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices10(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 11) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam11(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate11(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices11(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 12) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam12(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate12(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices12(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 13) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam13(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate13(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices13(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 14) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam14(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate14(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices14(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 15) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam15(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate15(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices15(Long.parseLong(numOfServices.getNodeValue()));
			}
			else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 16) {
				Node multicastBitrate = map.getNamedItem("Bitrate");
				Node numOfServices = map.getNamedItem("NumOfServices");
				qam.setQam16(true);//(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
				qam.setBitrate16(Long.parseLong(multicastBitrate.getNodeValue()));
				qam.setNumOfServices16(Long.parseLong(numOfServices.getNodeValue()));
			}
		}

		return qam;
	}
	

	/**
	 * 分析全部，避免重复解析
	 * 
	 * @param xmlContent
	 * @param deviceNode
	 * @param blade
	 * @return
	 * @throws Exception
	 */
	public static List<QamValue> qamXmlParser(String xmlContent, com.grus.nms.daemon.monitor.nsg9000.pojo.Node deviceNode)
			throws Exception {

		List<QamValue> values = new ArrayList<QamValue>();

		Document document = XmlManager.parse(new ByteArrayInputStream(xmlContent.getBytes()));

		// slot固定9
		for (int n = 0; n < 9; n++) {
			QamValue qam = new QamValue();
			qam.setNodeId(deviceNode.getId());
			int blade = n;
			qam.setBlade(blade);

			NodeList list = document.getElementsByTagName("TsOut");
			System.out.println(list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				NamedNodeMap map = list.item(i).getAttributes();

				Node node = map.getNamedItem("TsId");
				if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 1) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam1(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate1(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices1(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 2) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam2(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate2(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices2(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 3) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam3(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));;
					qam.setBitrate3(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices3(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 4) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam4(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate4(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices4(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 5) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam5(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate5(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices5(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 6) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam6(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate6(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices6(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 7) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam7(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate7(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices7(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 8) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam8(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate8(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices8(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 9) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam9(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate9(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices9(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 10) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam10(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate10(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices10(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 11) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam11(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate11(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices11(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 12) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam12(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate12(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices12(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 13) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam13(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate13(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices13(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 14) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam14(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate14(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices14(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 15) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam15(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate15(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices15(Long.parseLong(numOfServices.getNodeValue()));
				}
				else if (Long.parseLong(node.getNodeValue()) - ((blade - 1) * 16) == 16) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam16(map.getNamedItem("Enabled").getNodeValue().toString().equals("1"));
					qam.setBitrate16(Long.parseLong(multicastBitrate.getNodeValue()));
					qam.setNumOfServices16(Long.parseLong(numOfServices.getNodeValue()));
				}
			}

			values.add(qam);
		}

		return values;
	}

	public static List<EventValue> eventXmlParser(String xmlContent, com.grus.nms.daemon.monitor.nsg9000.pojo.Node n) throws Exception {
		List<EventValue> events = new ArrayList<EventValue>();
		// 取得XML文档
		Document document = XmlManager.parse(new ByteArrayInputStream(xmlContent.getBytes()));
		NodeList list = document.getElementsByTagName("Alarms");
		String seqNo = list.item(0).getAttributes().getNamedItem("SeqNo").getNodeValue();
		NodeList alarms = document.getElementsByTagName("Alarm");
		for (int i = 0; i < alarms.getLength(); i++) {
			EventValue event = new EventValue();
			String eventId = alarms.item(i).getAttributes().getNamedItem("Id").getNodeValue();
			String eventObject = alarms.item(i).getAttributes().getNamedItem("Object").getNodeValue();
			String physIdx = alarms.item(i).getAttributes().getNamedItem("PhysIdx").getNodeValue();
			String description = alarms.item(i).getAttributes().getNamedItem("Description").getNodeValue();
			String severity = alarms.item(i).getAttributes().getNamedItem("Severity").getNodeValue();
			String eventTime = alarms.item(i).getAttributes().getNamedItem("Time").getNodeValue();
			// 转换日期
			event.setSeqNo(seqNo);
			event.setEventId(eventId);
			event.setEventObject(eventObject);
			event.setPhysIdx(physIdx);
			event.setDescription(description);
			event.setSeverity(severity);
			
			Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(eventTime);
			event.setEventTime(new Timestamp(date.getTime()));
			
			event.setCreateTime(new Timestamp(new Date().getTime()));
			event.setNodeId(n.getId());
			// 此处有待调查
			event.setUser("system");
			event.setHandled(false);
			events.add(event);
		}
		System.out.println(events.size() + "事件个数");
		return events;
	}
}
