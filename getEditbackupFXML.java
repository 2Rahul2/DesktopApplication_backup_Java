package org.openjfx.hellofx;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class getEditbackupFXML {
	private static getEditbackupFXML instance = new getEditbackupFXML();
	EditBackupController editbackupObject;
	private getEditbackupFXML() {
		try {
			FXMLLoader editLoader = new FXMLLoader(getClass().getResource("EditBackUp.fxml"));
			Parent root = editLoader.load();
			editbackupObject = editLoader.getController();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static getEditbackupFXML getinstance() {
		return instance;
	}
}
