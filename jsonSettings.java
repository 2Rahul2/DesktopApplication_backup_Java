package org.openjfx.hellofx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openjfx.hellofx.jsonCredentials.CredentialsMapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class jsonSettings {
	public boolean IST=true ,UTC ,runinBackground=true ,backupAtLaunch=true;
	private String userHomePath = System.getProperty("user.home");
	private String HiddenFolderPath = userHomePath+"/appFolder";
	public String jsonPath = HiddenFolderPath+"/user_settings.json";
//	public String jsonPath = "C:\\Users\\Rahul\\appFolder\\user_settings.json";
	public jsonSettings(boolean IST ,boolean UTC ,boolean runinBackground ,boolean backupAtLaunch) {
		this.IST=IST;
		this.UTC=UTC;
		this.runinBackground=runinBackground;
		this.backupAtLaunch=backupAtLaunch;
	}
	public jsonSettings() {
		
	}
	
	public boolean getISTvalue() {
		if(checkFolder()) {
			File jsonfile = new File(jsonPath);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode node = objectMapper.readTree(jsonfile);
				boolean ist = node.get("IST").asBoolean();
				return ist;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	public boolean getUTCvalue() {
		if(checkFolder()) {
			File jsonfile = new File(jsonPath);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode node = objectMapper.readTree(jsonfile);
				boolean utc = node.get("UTC").asBoolean();
				return utc;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	public boolean getruninbackgroundvalue() {
		if(checkFolder()) {
			File jsonfile = new File(jsonPath);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode node = objectMapper.readTree(jsonfile);
				boolean rib = node.get("runinBackground").asBoolean();
				return rib;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	public boolean getbackupatlaunch() {
		if(checkFolder()) {
			File jsonfile = new File(jsonPath);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode node = objectMapper.readTree(jsonfile);
				boolean bal = node.get("backupAtLaunch").asBoolean();
				return bal;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	public void StoreCredentials() {
		if(checkFolder()) {
			try(FileWriter storeJson = new FileWriter(jsonPath)){
				ObjectMapper objectMapper = new ObjectMapper();
				System.out.println(userHomePath);
				 Path jsonFilePath = Paths.get(jsonPath);
				System.out.println("Writing to: " + jsonFilePath);
				settingMapper settingMapperObject = new settingMapper(IST ,UTC ,runinBackground ,backupAtLaunch);
				
				objectMapper.writeValue(storeJson ,settingMapperObject);
				System.out.println(jsonPath);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public boolean checkFile() {
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
	public boolean checkFolder() {
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
	public class settingMapper{
		public boolean IST ,UTC ,runinBackground ,backupAtLaunch;
		public settingMapper(boolean IST ,boolean UTC ,boolean runinBackground ,boolean backupAtLaunch) {
			this.IST=IST;
			this.UTC=UTC;
			this.runinBackground=runinBackground;
			this.backupAtLaunch=backupAtLaunch;
		}
		
		public boolean getIST() {
			return IST;
		}
		
		public boolean getUTC() {
			return UTC;
		}
		public boolean getruninBackground() {
			return runinBackground;
		}
		public boolean getbackupAtLaunch() {
			return backupAtLaunch;
		}
		
		public void setIST(boolean IST) {
			this.IST=IST;
		}
		public void setUTC(boolean UTC) {
			this.UTC=UTC;
		}
		public void setruninBackground(boolean runinBackground) {
			this.runinBackground=runinBackground;
		}
		public void setbackupAtLaunch(boolean backupAtLaunch) {
			this.backupAtLaunch=backupAtLaunch;
		}
	}
}
