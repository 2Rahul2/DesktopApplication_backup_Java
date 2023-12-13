package org.openjfx.hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

public class settingsController implements Initializable {
	@FXML
	VBox mainPage;
	@FXML
	CheckBox IstBox;
	@FXML
	CheckBox UtcBox;
	@FXML
	CheckBox runBackground;
	@FXML
	CheckBox startLaunch;
	@FXML
	Button runBackup;
	@FXML
	Button exit;
	@FXML
	Button save;
	@FXML
	Button saveExit;
	public void getTimeSettings() {
		jsonSettings getSettings = new jsonSettings();
		IstBox.setSelected(getSettings.getISTvalue());
		UtcBox.setSelected(getSettings.getUTCvalue());
		runBackground.setSelected(getSettings.getruninbackgroundvalue());
		startLaunch.setSelected(getSettings.getbackupatlaunch());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		save.setOnMouseEntered(e->{
			
			save.setStyle("-fx-font-size:22;-fx-background-color:#512873;-fx-text-fill:white;-fx-border-color: #d99d1c;-fx-border-width: 2px 2px 2px 2px;-fx-background-radius:15;-fx-border-radius:11");
		});
		save.setOnMouseExited(e->{
			 
			save.setStyle("-fx-font-size:20;-fx-background-color:#5c427d;-fx-text-fill:white;-fx-border-color: #d99d1c;-fx-border-width: 2px 2px 2px 2px;-fx-background-radius:15;-fx-border-radius:11");
		});
		
		saveExit.setOnMouseEntered(e->{
			saveExit.setStyle("-fx-font-size:22;-fx-background-color:#512873;-fx-text-fill:white;-fx-border-color: #d99d1c;-fx-border-width: 2px 2px 2px 2px;-fx-background-radius:15;-fx-border-radius:11");
		});
		saveExit.setOnMouseExited(e->{
			saveExit.setStyle("-fx-font-size:20;-fx-background-color:#5c427d;-fx-text-fill:white;-fx-border-color: #d99d1c;-fx-border-width: 2px 2px 2px 2px;-fx-background-radius:15;-fx-border-radius:11");
		});
		IstBox.selectedProperty().addListener((obs ,oldValue ,newValue)->{
			if(newValue) {
				UtcBox.setSelected(false);	
			}
		});
		UtcBox.selectedProperty().addListener((obs ,oldValue ,newValue)->{
			if(newValue) {
				IstBox.setSelected(false);	
			}
		});
		jsonSettings jsonSetting = new jsonSettings();
    	if(!jsonSetting.getbackupatlaunch()) {
    		runBackup.setOnAction(e->{
    			UploadBackup Uobject = UploadBackup.getInstance();
    			Uobject.getDataFromJson();
    			Uobject.runListOfBackup();  
    			runBackup.setDisable(true);
    		});    		
    	}else {
    		runBackup.setDisable(true);
    	}
//		if(IstBox.isFocused()) {
//			UtcBox.setSelected(false);
//		}
//		if(UtcBox.isFocused()) {
//			IstBox.setSelected(false);
//		}
		getTimeSettings();
		exit.setOnAction(e->{
			exitFunction();
		});
		save.setOnAction(e->{
			saveFunction();
		});
		saveExit.setOnAction(e->{
			saveFunction();
			exitFunction();
		});
	}
	public void exitFunction() {
		getHomePageFXML homeFXML = getHomePageFXML.getInstance();
		homepage HomePage = homeFXML.homepageController;
		HomePage.menuStage.hide();
	}
	public void saveFunction() {
		if(!IstBox.isSelected() && !UtcBox.isSelected()) {
			IstBox.setSelected(true);
		}
		jsonSettings jsonSetting = new jsonSettings(IstBox.isSelected() ,UtcBox.isSelected(),runBackground.isSelected(),startLaunch.isSelected());
		jsonSetting.StoreCredentials();
	}
	
}
