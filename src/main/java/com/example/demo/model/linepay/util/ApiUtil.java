package com.example.demo.model.linepay.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiUtil {
	public static JsonNode sendPost(String channelId, String nonce, String signature, String httpsUrl,
			String mapperData) {
		RestTemplate restTemplate = new RestTemplate();
		// Header設定
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-LINE-ChannelId", channelId);
		headers.add("X-LINE-Authorization-Nonce", nonce);
		headers.add("X-LINE-Authorization", signature);

		HttpEntity<String> request = new HttpEntity<String>(mapperData, headers);
		String responseBody = restTemplate.postForObject(httpsUrl, request, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = null;
		try {
			json = mapper.readTree(responseBody);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static ResponseEntity<JsonNode> sendGet(String channelId, String nonce, String signature, String httpsUrl) {
		RestTemplate restTemplate = new RestTemplate();
		// Header設定
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-LINE-ChannelId", channelId);
		headers.add("X-LINE-Authorization-Nonce", nonce);
		headers.add("X-LINE-Authorization", signature);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<JsonNode> responseBody = restTemplate.exchange(httpsUrl, HttpMethod.GET, request,
				JsonNode.class);
		return responseBody;
	}
}
