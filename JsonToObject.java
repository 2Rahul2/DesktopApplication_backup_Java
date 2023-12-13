package org.openjfx.hellofx;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//import main.FileExplorer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
public class JsonToObject {
	public List<FileExplorer> listOfFileExplorer;
	private String token;
	public String getToken(HttpClient tempClient) {
//		HttpClient tempClient = HttpClients.createDefault();
		getServerUrl rootUrl = new getServerUrl();
		HttpPost tempPost = new HttpPost(rootUrl.getRootUrl()+"loginsite/");
		
		tempPost.setHeader("Content-Type" ,"application/json");
		JSONObject json = new JSONObject();
		
		jsonCredentials credObject = new jsonCredentials();
		String username = credObject.getUserName();
		String userpassword = credObject.getUserPassword();
		json.put("username", username);
		json.put("password", userpassword);
		
		StringEntity tempEntity;
		try {
			tempEntity = new StringEntity(json.toString());
			tempPost.setEntity(tempEntity);
			try {
				HttpResponse httpResponse = tempClient.execute(tempPost);
				HttpEntity entity = httpResponse.getEntity();
				String responseString = EntityUtils.toString(entity);
				JSONObject jObject = new JSONObject(responseString);
				
				token = jObject.getString("token");
//				String user = jObject.getString("user");
				
				System.out.println("Token : "+token);
				return token;
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public JsonToObject() throws ClientProtocolException, IOException{
		getServerUrl rootUrl = new getServerUrl();
		String djangoUrl = rootUrl.getRootUrl()+"data/"; // Replace '1' with the actual file ID
//		send token to the client
		// Create an HTTP client
		HttpClient httpClient = HttpClients.createDefault();
		
		// Create an HTTP GET request
		HttpPost httppost = new HttpPost(djangoUrl);
		String authToken = getToken(httpClient);
		if(authToken != "") {
			httppost.setHeader("Authorization", "token "+ authToken);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			jsonCredentials credObject = new jsonCredentials();
			String username = credObject.getUserName();
			builder.addPart("username" ,new StringBody(username ,ContentType.TEXT_PLAIN));
			httppost.setEntity(builder.build());
			HttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			listOfFileExplorer = new ArrayList();
			String result = entity.toString();
			if(response.getStatusLine().getStatusCode() == 200 && !result.equals("Error")) {
				ObjectMapper objectmapper = new ObjectMapper();
				String jsonResponse = EntityUtils.toString(entity);
				System.out.println(jsonResponse);
				JsonNode jsonNode = objectmapper.readTree(jsonResponse);
				Iterator<String> jsonKeys = jsonNode.fieldNames();
//		   allKey(jsonNode, "");
				// Iterate through the keys and print them
				while (jsonKeys.hasNext()) {
					String key = jsonKeys.next();
//               FileExplorer fileE = new FileExplorer();
					JsonNode nextJsonNode = jsonNode.get(key);
					
					int UnderScoreIndex = key.indexOf("_");
					int lastUnderScorIndex = key.lastIndexOf("_");
					int LastDollarIndex = key.lastIndexOf("$");
					if(UnderScoreIndex != -1) {
						String rootKey = key.substring(0 ,UnderScoreIndex);
						String RootFolderName = key.substring(UnderScoreIndex+1 ,lastUnderScorIndex);
						String fileDate = key.substring(lastUnderScorIndex+1 ,LastDollarIndex);
						String fileSize = key.substring(LastDollarIndex+1);
						int floatSize = (int) ((Float.parseFloat(fileSize))*(1024*1024));
						fileSize = String.valueOf(floatSize);
						System.out.println("KEYSSSSSSSSS:   "+RootFolderName);
						FileExplorer fileExplorer = new FileExplorer(RootFolderName ,rootKey ,fileDate ,fileSize);
						listOfFileExplorer.add(fileExplorer);
						
//            	   System.out.println("Key: " + rootKey);
//            	   System.out.println("Name: " + RootFolderName);
						allKey(nextJsonNode ,fileExplorer);
						
//            	   System.out.println(fileExplorer.fileexplorer.get(0).subFile.get(1).FileName);
					}
//               allKey(nextJsonNode);
//               System.out.println("Key: " + rootFolderName);
				}
				
//			System.out.println(listOfFileExplorer.get(1).FileName);
			}
		}else {
			System.out.println("No token was sent!");
		}
		// Execute the GET request
		
	}
	public static void main(String[] args) throws ClientProtocolException, IOException{
//		   for(JsonNode item : jsonNode) {
//			   System.out.println(item);
//		   }
//			JsonToObject obj = new JsonToObject();
//		   System.out.println(jsonResponse);
	   }
	public static void allKey(JsonNode jsonNode , FileExplorer FE) {
//		if(jsonNode.isObject()) {
			Iterator<String> fieldNames = jsonNode.fieldNames();
			while(fieldNames.hasNext()) {
				String fieldName = fieldNames.next();
				int lastDollarIndex = fieldName.lastIndexOf('$');
				String fileSize = fieldName.substring(lastDollarIndex+1);
				int floatSize = (int) ((Float.parseFloat(fileSize))*(1024*1024));
				fileSize = String.valueOf(floatSize);
				JsonNode valueNode = jsonNode.get(fieldName);
				if(valueNode.isObject()) {
					int indexOfUnderScore = fieldName.indexOf('_');
					if(indexOfUnderScore != -1) {
						String key = fieldName.substring(0 ,indexOfUnderScore);
						String result = fieldName.substring(indexOfUnderScore+1 ,lastDollarIndex);
						
//						FE.addFolder(result , key);
						FileExplorer Sub_FE = new FileExplorer(result , key ,"" ,fileSize);
						FE.fileexplorer.add(Sub_FE);
						JsonNode childNode = jsonNode.get(fieldName);
		                allKey(childNode ,Sub_FE);
//						System.out.println(key+"    key value");
//						System.out.println(result+"   contains inner strcuture");
					}
				}else {
					String isFile = fieldName.substring(0,4);
					if(isFile.equals("file")) {
						JsonNode keyValue = jsonNode.get(fieldName);
						String key = fieldName.substring(4);
						FE.addFile(keyValue.asText(), key ,FE ,"" ,fileSize);
//						System.out.println(keyValue.asText()+"    is a file"+key);						
					}
				}
//				else if(fieldName.substring(0,4).toString() == "file") {
//					System.out.println(fieldName+"   its a file");
//				}
				
//				String currentPath = parentPath.isEmpty() ? fieldName:parentPath + "." + fieldName;
//				System.out.println("Key: " + fieldName);
                
			}
//		}
	}
}
