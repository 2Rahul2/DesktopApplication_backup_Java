package org.openjfx.hellofx;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//import main.FileExplorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
public class JsonToObject {
	public List<FileExplorer> listOfFileExplorer;
	public JsonToObject() throws ClientProtocolException, IOException{
		String djangoUrl = "http://127.0.0.1:8000/data/"; // Replace '1' with the actual file ID
		
		// Create an HTTP client
		HttpClient httpClient = HttpClients.createDefault();
		
		// Create an HTTP GET request
		HttpGet httpGet = new HttpGet(djangoUrl);
		// Execute the GET request
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		listOfFileExplorer = new ArrayList();
		if(response.getStatusLine().getStatusCode() == 200) {
			ObjectMapper objectmapper = new ObjectMapper();
			String jsonResponse = EntityUtils.toString(entity);
			JsonNode jsonNode = objectmapper.readTree(jsonResponse);
			Iterator<String> jsonKeys = jsonNode.fieldNames();
//		   allKey(jsonNode, "");
			// Iterate through the keys and print them
			while (jsonKeys.hasNext()) {
				String key = jsonKeys.next();
//               FileExplorer fileE = new FileExplorer();
				JsonNode nextJsonNode = jsonNode.get(key);
				int UnderScoreIndex = key.indexOf("_");
				if(UnderScoreIndex != -1) {
					String rootKey = key.substring(0 ,UnderScoreIndex);
					String RootFolderName = key.substring(UnderScoreIndex+1);
					FileExplorer fileExplorer = new FileExplorer(RootFolderName ,rootKey);
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
				JsonNode valueNode = jsonNode.get(fieldName);
				if(valueNode.isObject()) {
					int indexOfUnderScore = fieldName.indexOf('_');
					if(indexOfUnderScore != -1) {
						String key = fieldName.substring(0 ,indexOfUnderScore);
						String result = fieldName.substring(indexOfUnderScore+1);
//						FE.addFolder(result , key);
						FileExplorer Sub_FE = new FileExplorer(result , key);
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
						FE.addFile(keyValue.toString(), key ,FE);
//						System.out.println(keyValue+"    is a file");						
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
