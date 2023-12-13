package org.openjfx.hellofx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class StorePathInJson {
	private String Name;
	private String Path;
	private int TotalMinutes;
	String userHome = System.getProperty("user.home");
	String hiddenFolderPath = userHome + "/appFolder";
	public String jsonPath = hiddenFolderPath+"/user_data.json";
	public StorePathInJson(String Name ,String Path ,int TotalMinutes) {
		this.Name = Name;
		this.Path = Path;
		this.TotalMinutes = TotalMinutes;
	}
	public StorePathInJson() {
		
	}
	public static void main(String[] args) {
		
		
		

	}
	public void updateJsonData(List<createJsonBluePrint> updatedJsonData) {
		if(CheckFolderFile(hiddenFolderPath)) {
//			List<createJsonBluePrint> ToBackUpData = getJsonList(jsonPath);
//			WriteDataIntoClass(ToBackUpData ,Name ,Path);
			WriteDataIntoJson(updatedJsonData , jsonPath);			
		}else {
			System.out.println("Unable To Locate Saved Folder");
		}
	}
	public void storeJsonData() {
		if(CheckFolderFile(hiddenFolderPath)) {
			List<createJsonBluePrint> ToBackUpData = getJsonList(jsonPath);
			WriteDataIntoClass(ToBackUpData ,Name ,Path , TotalMinutes);
			WriteDataIntoJson(ToBackUpData , jsonPath);			
		}else {
			System.out.println("Unable To Locate Saved Folder");
		}
	}
	public static boolean CheckFolderFile(String hiddenApplicationFolder) {		
		Path getFolder = Paths.get(hiddenApplicationFolder);
		if(!Files.exists(getFolder)) {
			try {
				Files.createDirectories(getFolder);
				if(CheckForJsonFile(hiddenApplicationFolder+"/user_data.json")) {
					return true;
				}else {
					return false;
				}
			} catch (IOException e) {
				return false;				
			}
		}else {
			if(CheckForJsonFile(hiddenApplicationFolder+"/user_data.json")) {
				return true;
			}else {
				return false;
			}
		}
	}
	public static boolean CheckForJsonFile(String jPath) {
		File checkForJson = new File(jPath);
		if(checkForJson.exists()) {
			return true;
		}else {
			try {
				checkForJson.createNewFile();
				return true;
			} catch (IOException e) {
				return false;								
			}			
		}
	}
	public JsonNode getJsonData() {
		JsonNode rootNode = null;
		if(CheckForJsonFile(jsonPath)) {
			ObjectMapper objectMapper = new ObjectMapper();
			File JsonFile = new File(jsonPath);
			try {
				rootNode = objectMapper.readTree(JsonFile);
				return rootNode;
			} catch (IOException e) {			
				e.printStackTrace();
			}
			return rootNode;			
		}else {
			return rootNode;
		}
	}
	public static void WriteDataIntoClass(List<createJsonBluePrint> ListDataClass ,String Name ,String Path , int totalMin) {
		createJsonBluePrint userData = new createJsonBluePrint(Name ,Path ,totalMin);
		ListDataClass.add(userData);
	}
	public static void WriteDataIntoJson(List<createJsonBluePrint> writeData , String JsonPath) {
		try(FileWriter storeJson = new FileWriter(JsonPath)){
			//objectMapper.writeValue:
			//This method is specifically used for serializing (converting) a Java object into its JSON representation and writing that JSON data to an output destination, which can be a file, an output stream, or other types of destinations.
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(storeJson ,writeData);
			System.out.println("create at :"+JsonPath);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public  List<createJsonBluePrint> getJsonList(String jsonPath){
//		object mapper use to serialize and deserialize 
//		converts class object to json string and vice versa
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			File file = new File(jsonPath);
			if(file.exists()) {
				System.out.println("hehe");
				CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, createJsonBluePrint.class);
				return objectMapper.readValue(file ,collectionType);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();	
	}
	public static class createJsonBluePrint{
		String Name;
		String Path;
		int totalMinutes;
		@JsonCreator
		public createJsonBluePrint(@JsonProperty("Name") String Name ,@JsonProperty("Path") String Path ,@JsonProperty("totalMinutes") int totalMinutes) {
			this.Name = Name;
			this.Path = Path;
			this.totalMinutes = totalMinutes;
		}
		public int gettotalMinutes() {
			return totalMinutes;
		}
		public void settotalMinutes(int totalMinutes) {
			this.totalMinutes = totalMinutes;
		}
		public String getName() {
	        return Name;
	    }

	    public void setName(String Name) {
	        this.Name = Name;
	    }

	    public String getPath() {
	        return Path;
	    }

	    public void setPath(String Path) {
	        this.Path = Path;
	    }
	}
}
