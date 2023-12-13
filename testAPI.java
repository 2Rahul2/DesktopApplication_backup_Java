package org.openjfx.hellofx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class testAPI {

	public static void main(String[] args) {
		
//		HttpPost tempPost = new HttpPost("http://127.0.0.1:8000/test_token/");
		getServerUrl rootUrl = new getServerUrl();
		HttpGet tempPost = new HttpGet(rootUrl.getRootUrl()+"test_token/");
//		tempPost.setHeader("Content-Type" ,"application/json");
		JSONObject json = new JSONObject();
//		json.put("username", "rahul");
//		json.put("password", "op");
		
		StringEntity tempEntity;
		try {
			tempEntity = new StringEntity(json.toString());
			tempPost.setHeader("Authorization", "token "+ "6f2ad8ebebaff25939f8355faf7a210de8e8a16d");
//			tempPost.setEntity(tempEntity);
			try {
				HttpClient tempClient = HttpClients.createDefault();
				HttpResponse httpResponse = tempClient.execute(tempPost);
				HttpEntity entity = httpResponse.getEntity();
				String responseString = EntityUtils.toString(entity);
				System.out.println("Request Headers: " + Arrays.toString(tempPost.getAllHeaders()));
				System.out.println(responseString);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
