package org.openjfx.hellofx;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javafx.stage.Stage;

public class Logout {
	public  Logout() {
		
	}
	public void logoutUser() {
		jsonCredentials jsonCred = new jsonCredentials();
		if(jsonCred.checkFolderFile()) {
			ObjectMapper objectMapper = new ObjectMapper();
			File jsonFile = new File(jsonCred.jsonPath);
			try {
				
				JsonNode node = objectMapper.readTree(jsonFile);
				if(node.has("name") && node.has("password")) {
					jsonCredentials newCredentials = new jsonCredentials("" ,"");
					newCredentials.StoreCredentials();
							
//					System.out.println(node.get("name").s);
//					((ObjectNode) node).remove("name");
//					((ObjectNode) node).remove("password");
//					objectMapper.writeValueAsString(node);
				}
				App app = App.getInstance();
				Login login =Login.getInstance();
				login.currentStage.hide();
				Stage stage = new Stage();
				app.loadLoginPage(stage);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
