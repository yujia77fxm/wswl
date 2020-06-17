package com.wswl.util;

import com.sun.jersey.core.util.Base64;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * 加密算法类
 */
public class EncryptUtils {
	public static final String KEY_ALGORITHM = "RSA";

    /* ==MD5加密算法======================================================== */
	public static String md5(String paramString) {
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			return toHexString(localMessageDigest.digest());
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
		return paramString;
	}

	private static final char[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55,
			56, 57, 97, 98, 99, 100, 101, 102 };

	private static String toHexString(byte[] paramArrayOfByte) {
		int length = paramArrayOfByte.length;
		StringBuilder localStringBuilder = new StringBuilder(2 * length);
		for (int i = 0;; ++i) {
			if (i >= length)
				return localStringBuilder.toString().toUpperCase();
			localStringBuilder
					.append(HEX_DIGITS[((0xF0 & paramArrayOfByte[i]) >>> 4)]);
			localStringBuilder.append(HEX_DIGITS[(0xF & paramArrayOfByte[i])]);
		}
	}

	/* ================================================================== */
	/**
	 * 加密<br>
	 * 用公钥加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(byte[] data, String key)
			throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encodeData  = cipher.doFinal(data);

		return new BigInteger(1, encodeData).toString(16);
	}


	public static byte[] decryptBASE64(String key) throws Exception {
		return Base64.decode(key);//, Base64.DEFAULT);
	}

	/**
	 * url签名方法
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */

	public static String getSignData(Map<String, Object> params) {
		StringBuffer content = new StringBuffer();

		// 按照key做排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		int length=keys.size();
		for (int i = 0; i < length; i++) {
			String key = (String) keys.get(i);
			if ("sign".equals(key)) {
				continue;
			}
			String value =params.get(key).toString();
			if (value != null) {
				content.append(key + "=" + value).append("&");
			}
		}
		String signKey = "1234";//盐值
        content.append("key="+signKey);

		return MD5Util.md5(content.toString());
	}

}
