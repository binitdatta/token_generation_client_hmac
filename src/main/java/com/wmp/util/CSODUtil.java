package com.wmp.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpHeaders;

import com.wmp.CSODClient;

public class CSODUtil {


	public static String API_SECRET = "";

	private static final String HMAC_SHA512 = "HMACSHA512";

	public static String API_KEY = "";
	public static String API_URL = "";

	public static String API_USER_ID = "";

	
	public static String calculateHMAC(String data, String key)
		    throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
		{
		    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA512);
		    Mac mac = Mac.getInstance(HMAC_SHA512);
		    mac.init(secretKeySpec);
		    //return toHexString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
		    return toHexString(mac.doFinal(data.getBytes()));

		}
		
		public static LocalDateTime toUtc(final LocalDateTime time) {
			return DateTimeUtil.toUtc(time, ZoneId.systemDefault());
		}
		
		private static String toHexString(byte[] bytes) {
			Base64.Encoder encoder = Base64.getEncoder();
			return encoder.encodeToString(bytes);
		}
		
		public static String ConstructStringToSign(String httpMethod, HttpHeaders headers, String pathAndQuery ){
			
			StringBuilder stringToSign = new StringBuilder();
			
			String httpVerb = httpMethod.trim() + "\n";
			String apiKey = headers.get(CSODClient.KEY_HEADER_NAME).get(0);
			String utcDate = headers.get(CSODClient.DATE_HEADER_NAME).get(0);
			
			apiKey = apiKey.trim() + "\n";
			utcDate = utcDate.trim() + "\n";
			
			stringToSign.append(httpVerb);
			stringToSign.append(CSODClient.KEY_HEADER_NAME);
			stringToSign.append(":");
			stringToSign.append(apiKey);
			stringToSign.append(CSODClient.DATE_HEADER_NAME);
			stringToSign.append(":");
			stringToSign.append(utcDate);
			stringToSign.append(pathAndQuery);
			
			System.out.println("ConstructStringToSign :");
			System.out.println(stringToSign );

			return stringToSign.toString();
		}
		
		public static String SignString512(String stringToSign, String secretKey) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException{
			String signedVal = CSODUtil.calculateHMAC(stringToSign,secretKey);
			return signedVal;
		}
}
