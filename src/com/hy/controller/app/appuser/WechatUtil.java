//package com.hy.controller.app.appuser;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class WechatUtil {
//	/**
//	 * 用SHA1算法生成安全签名
//	 * 
//	 * @param token
//	 *            票据
//	 * @param timestamp
//	 *            时间戳
//	 * @param nonce
//	 *            随机字符串
//	 * @param encrypt
//	 *            密文
//	 * @return 安全签名
//	 * @throws NoSuchAlgorithmException
//	 */
//	public static String getSHA1(String token, String timestamp, String nonce)
//			throws NoSuchAlgorithmException {
//		String[] array = new String[] { token, timestamp, nonce };
//		StringBuffer sb = new StringBuffer();
//		// 字符串排序
//		Arrays.sort(array);
//		for (int i = 0; i < 3; i++) {
//			sb.append(array[i]);
//		}
//		String str = sb.toString();
//		// SHA1签名生成
//		MessageDigest md = MessageDigest.getInstance("SHA-1");
//		md.update(str.getBytes());
//		byte[] digest = md.digest();
//
//		StringBuffer hexstr = new StringBuffer();
//		String shaHex = "";
//		for (int i = 0; i < digest.length; i++) {
//			shaHex = Integer.toHexString(digest[i] & 0xFF);
//			if (shaHex.length() < 2) {
//				hexstr.append(0);
//			}
//			hexstr.append(shaHex);
//		}
//		return hexstr.toString();
//	}
//
//	/**
//	 * 转换格式为微信的
//	 * 
//	 * @param value
//	 *            值
//	 * @param color
//	 *            背景色
//	 * @return
//	 */
//	public static Map<String, String> getMapParam(String value, String color) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("color", color);
//		map.put("value", value);
//		return map;
//	}
//
//	/**
//	 * 根据code获取链接
//	 * 
//	 * @param authCode
//	 * @return
//	 */
//	public static String getUserInfoByCode(String authCode) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=");
//		sb.append(WechatTaskService.getAccessToken());
//		sb.append("&code=");
//		sb.append(authCode);
//		return sb.toString();
//	}
//
//	/**
//	 * 根据用户id获取链接
//	 * 
//	 * @param userId
//	 * @return
//	 */
//	public static String getUserInfoByUserId(String userId) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=");
//		sb.append(WechatTaskService.getAccessToken());
//		sb.append("&userid=");
//		sb.append(userId);
//		return sb.toString();
//	}
//
//	/**
//	 * 发送消息链接
//	 * 
//	 * @return
//	 */
//	public static String getSendUrl() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=");
//		sb.append(WechatTaskService.getAccessToken());
//		return sb.toString();
//	}
//}
