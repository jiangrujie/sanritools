package test.mini;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TVoice {
	public static void main(String[] args) {

		try {
			String voiceIp = "192.168.0.206";
			String voicePort = "7788";
			//prisonNames.subSequence(0, prisonNames.length() - 1);
			DatagramSocket ds = new DatagramSocket();
			String armMsg = "";
			armMsg = "请注意,小强要出山了";
			String strs = "play str="+armMsg;
			DatagramPacket dp = new DatagramPacket(strs.getBytes("GB2312"),
					strs.getBytes("GB2312").length,
					InetAddress.getByName(voiceIp),
					Integer.parseInt(voicePort));
			ds.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
