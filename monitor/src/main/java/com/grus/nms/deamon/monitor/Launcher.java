package com.grus.nms.deamon.monitor;

import com.grus.nms.deamon.monitor.nsg9000.Monitor;

/**
 * 监视类
 * 
 * 以后拓展的话，可以在外部进行sender/receiver的配置，然后在run方法上进行handler的挂接（职责链模式）。
 * 
 * @author ipqam
 *
 */
public class Launcher {
	/**
	 * 
	 * 增加新设备和修改，需要重新启动，没有事件通知。
	 * 后期可以在数据库或者外部文件加标志，进行轮询
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Monitor.main(args);
	}
}
