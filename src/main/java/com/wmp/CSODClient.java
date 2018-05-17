package com.wmp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.wmp.util.CSODUtil;
import com.wmp.util.DateTimeUtil;



public class CSODClient {

	public static String KEY_HEADER_NAME = "x-csod-api-key";
	public static String DATE_HEADER_NAME = "x-csod-date";
	public static String SIGNATURE_HEADER_NAME = "x-csod-signature";

	public static void main(String[] args) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {

		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime utc = DateTimeUtil.toUtc(now);

		System.out.println("Now: " + now);
		System.out.println("UTC: " + utc);
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
		headers.setContentType(MediaType.APPLICATION_XML);
		headers.set(KEY_HEADER_NAME, CSODUtil.API_KEY);
		headers.set(DATE_HEADER_NAME, utc.toString());

		String stringToSign = CSODUtil.ConstructStringToSign("POST",headers,"/services/api/sts/Session");
		
		String singedKey = CSODUtil.SignString512(stringToSign,CSODUtil.API_SECRET);
		
		System.out.println("singedKey :"+ singedKey);
		
		headers.set(SIGNATURE_HEADER_NAME, singedKey);
		
		// HttpEntity<Employee[]>: To get result as Employee[].
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		try{
			ResponseEntity<String> response = restTemplate.exchange(CSODUtil.API_URL, //
					HttpMethod.POST, entity, String.class);
			
			HttpStatus statusCode = response.getStatusCode();
			System.out.println("Response Satus Code: " + statusCode);
			
			if (statusCode == HttpStatus.OK) {
				// Response Body Data
				String token = response.getBody();

				System.out.println("Token :"+ token);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error Message :"+e.getMessage());
		}
				

		

	}
}
