package org.openjfx.hellofx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class jsonCredentials {
	private  String Name="";
	private  String Password="";
	private String userHomePath = System.getProperty("user.home");
	private String HiddenFolderPath = userHomePath+"/appFolder";
	public String jsonPath = HiddenFolderPath+"/user_credentials.json";
//	public String jsonPath = "C:\\Users\\Rahul\\appFolder\\user_credentials.json";
	
	public static void main(String[] args) {
//		jsonCredentials jsonObject = new jsonCredentials("rahul" ,"hhehehe");
//		jsonObject.StoreCredentials();
		System.setProperty("user.home", "C:\\Users\\Rahul");
		System.out.println(System.getProperty("user.home"));
//		System.getenv().forEach((key, value) -> System.out.println(key + ": " + value));
	}
	public jsonCredentials(String Name , String Password) {
		this.Name = Name;
		this.Password = Password;
	}
	public jsonCredentials() {
		
	}
	public String getUserName() {
		if(checkFolderFile()) {
			File jsonfile = new File(jsonPath);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode node = objectMapper.readTree(jsonfile);
				String username = node.get("name").asText();
				return username;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}
	public String getUserPassword() {
		if(checkFolderFile()) {
			File jsonfile = new File(jsonPath);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode node = objectMapper.readTree(jsonfile);
				String userpassword = node.get("password").asText();
				return userpassword;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}
	public void StoreCredentials() {
		if(checkFolderFile()) {
			try(FileWriter storeJson = new FileWriter(jsonPath)){
				ObjectMapper objectMapper = new ObjectMapper();
				System.out.println(userHomePath);
				 Path jsonFilePath = Paths.get(jsonPath);
				System.out.println("Writing to: " + jsonFilePath);
				CredentialsMapper credentialMapper = new CredentialsMapper(Name ,Password);
				objectMapper.writeValue(storeJson ,credentialMapper);
				System.out.println(jsonPath);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public class CredentialsMapper{
		private  String Name;
		private  String Password;
		public CredentialsMapper(String Name ,String Password) {
			this.Name =  Name;
			this.Password = Password;
		}
		 public CredentialsMapper() {
		    }
		public String getName() {
			return Name;
		}
		public void setName(String Name) {
			this.Name=Name;
		}
		public String getPassword() {
			return Password;
		}
		public void setPassword(String Password) {
			this.Password=Password;
		}
	}
	
	public boolean checkFolderFile() {
//		StorePathInJson jsonCheckObject = new StorePathInJson();
		Path getFolder = Paths.get(HiddenFolderPath);
		if(!Files.exists(getFolder)) {
			try {
				Files.createDirectories(getFolder);
				if(checkFile()) {
					return true;
				}else {
					return false;
				}
			}catch(Exception e) {
				return false;
			}
		}else {
			System.out.println("Folder exists!"+HiddenFolderPath);
			if(checkFile()) {
				return true;
			}else {
				return false;
			}
		}	
	}
	private boolean checkFile() {
		File jsonFile = new File(jsonPath);
		if(jsonFile.exists()) {
			System.out.println("File exists"+jsonPath);
			return true;
		}else {
			try {
				System.out.println("File creating");
				jsonFile.createNewFile();
				StoreCredentials();
				System.out.println("File created");
				return true;
			}catch(IOException e) {
				return false;
			}
		}		
	}
}
