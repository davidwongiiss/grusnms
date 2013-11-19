package com.grus.nms.deamon.monitor.nsg9000;

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

import com.grus.nms.deamon.monitor.pojo.EventValue;
import com.grus.nms.deamon.monitor.pojo.GbeValue;
import com.grus.nms.deamon.monitor.pojo.QamValue;
import com.grus.nms.deamon.monitor.util.XmlManager;

public class XmlParser {
	public static GbeValue gbeXmlParser(String xmlContent, com.grus.nms.deamon.monitor.pojo.Node deviceNode)
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
				gbe.setMulticastBitrate1(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices1(Integer.parseInt(numOfServices.getNodeValue()));
			}
			else if ("2".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate2(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices2(Integer.parseInt(numOfServices.getNodeValue()));
			}
			else if ("3".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate3(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices3(Integer.parseInt(numOfServices.getNodeValue()));
			}
			else if ("4".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate4(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices4(Integer.parseInt(numOfServices.getNodeValue()));
			}
			else if ("5".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate5(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices5(Integer.parseInt(numOfServices.getNodeValue()));
			}
			else if ("6".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate6(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices6(Integer.parseInt(numOfServices.getNodeValue()));
			}
			else if ("7".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate7(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices7(Integer.parseInt(numOfServices.getNodeValue()));
			}
			else if ("8".equals(node.getNodeValue())) {
				Node multicastBitrate = list.item(i).getAttributes().getNamedItem("MulticastBitrate");
				Node numOfServices = list.item(i).getAttributes().getNamedItem("NumOfServices");
				gbe.setMulticastBitrate8(Integer.parseInt(multicastBitrate.getNodeValue()));
				gbe.setNumberOfServices8(Integer.parseInt(numOfServices.getNodeValue()));
			}
		}

		return gbe;
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
	public static List<QamValue> qamXmlParser(String xmlContent, com.grus.nms.deamon.monitor.pojo.Node deviceNode)
			throws Exception {

		List<QamValue> values = new ArrayList<QamValue>();

		Document document = XmlManager.parse(new ByteArrayInputStream(xmlContent.getBytes()));

		// slot固定8
		for (int n = 0; n < 8; n++) {
			QamValue qam = new QamValue();
			qam.setNodeId(deviceNode.getId());
			int blade = n;
			qam.setBlade(blade);

			NodeList list = document.getElementsByTagName("TsOut");
			System.out.println(list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				NamedNodeMap map = list.item(i).getAttributes();

				Node node = map.getNamedItem("TsId");
				if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 1) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam1(1);
					qam.setBitrate1(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices1(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 2) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam2(2);
					qam.setBitrate2(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices2(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 3) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam3(3);
					qam.setBitrate3(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices3(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 4) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam4(4);
					qam.setBitrate4(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices4(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 5) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam5(5);
					qam.setBitrate5(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices5(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 6) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam6(6);
					qam.setBitrate6(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices6(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 7) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam7(7);
					qam.setBitrate7(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices7(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 8) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam8(8);
					qam.setBitrate8(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices8(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 9) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam9(9);
					qam.setBitrate9(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices9(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 10) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam10(10);
					qam.setBitrate10(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices10(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 11) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam11(11);
					qam.setBitrate11(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices11(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 12) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam12(12);
					qam.setBitrate12(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices12(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 13) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam13(13);
					qam.setBitrate13(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices13(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 14) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam14(14);
					qam.setBitrate14(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices14(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 15) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam15(15);
					qam.setBitrate15(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices15(Integer.parseInt(numOfServices.getNodeValue()));
				}
				else if (Integer.parseInt(node.getNodeValue()) - ((blade - 1) * 16) == 16) {
					Node multicastBitrate = map.getNamedItem("Bitrate");
					Node numOfServices = map.getNamedItem("NumOfServices");
					qam.setQam16(16);
					qam.setBitrate16(Integer.parseInt(multicastBitrate.getNodeValue()));
					qam.setNumOfServices16(Integer.parseInt(numOfServices.getNodeValue()));
				}
			}

			values.add(qam);
		}

		return values;
	}

	public static List<EventValue> eventXmlParser(String xmlContent, com.grus.nms.deamon.monitor.pojo.Node n) throws Exception {
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
			String description = alarms.item(i).getAttributes().getNamedItem("Description").getNodeValue();
			String severity = alarms.item(i).getAttributes().getNamedItem("Severity").getNodeValue();
			String eventTime = alarms.item(i).getAttributes().getNamedItem("Time").getNodeValue();
			// 转换日期
			Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(eventTime);
			event.setSeqNo(seqNo);
			event.setEventId(eventId);
			event.setEventObject(eventObject);
			event.setDescription(description);
			event.setSeverity(severity);
			event.setEventTime(new Timestamp(date.getTime()));
			event.setCreateTime(new Timestamp(new Date().getTime()));
			event.setNodeId(n.getId());
			// 此处有待调查
			event.setUser("admin");
			event.setHandled(false);
			events.add(event);
		}
		System.out.println(events.size() + "事件个数");
		return events;
	}
}
