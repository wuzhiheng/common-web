package com.wonders.commonweb.util;

import java.security.MessageDigest;

/**
 * <p> Title :MD5 Operate Tools </p>
 * <p> Description:java MD5 处理工具 </p>
 * <p> Created on 2014-7-29 </p>
 * <p> Company : </p>
 * 
 * @author : xuxs
 * @version : 1.0
 */
public class MD5Utils {
	private static byte[] hexBase = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', };

	public static String encrypt(String s) {
		return encrypt(s, "utf-8");
	}
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		String d = encrypt("1001v2.1.0MD5a1b2c31385345938378");
//		String a = encrypt("123456");
		System.out.println(d);
//		System.out.println(d.equals("1f82c942befda29b6ed487a51da199f78fce7f05"));
//		System.out.println(a);
//		System.out.println(a.equals("1f82c942befda29b6ed487a51da199f78fce7f05"));
		/*for(int i=1000;i<999999;i++){
			String e = encrypt(i+"");
			if(e.equals("1f82c942befda29b6ed487a51da199f78fce7f05")){
				System.out.println(i);
				break;
			}
		}*/
		
	}
	public static String encrypt(String s, String charsetName) {
		if (s == null)
			return "";
		try {
			byte buff[] = s.getBytes(charsetName);
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(buff);
			byte result[] = messagedigest.digest();
			return byte2Hex(result);
		} catch (Exception e) {
			return "";
		}
	}


	public static String byte2Hex(byte b[]) {
		if (b == null)
			return "";
		StringBuffer tmp = new StringBuffer();
		int len = b.length;
		for (int i = 0; i < len; i++) {
			tmp.append((char) hexBase[(b[i] & 0xF0) >> 4]);
			tmp.append((char) hexBase[b[i] & 0x0F]);
		}
		while (tmp.length() < 16)
			tmp.append("00");

		return tmp.toString();
	}
}