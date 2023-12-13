package org.openjfx.hellofx;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class getOngoingFXML {
	private static getOngoingFXML instance = new getOngoingFXML();
	OngoingBackupController ongoingObject;
	private getOngoingFXML(){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
            scene.getStylesheets().add("ongoingbackup.css");
			ongoingObject = loader.getController();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static getOngoingFXML getInstance() {
		return instance;
	}
//	public void storeElement(List<BackUpTask> allTask) {
//		loader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
//		try {
//			Pane newPane = loader.load();
//			OngoingBackupController object = loader.getController();
//			object.fun(allTask);
//			
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
	public VBox getRootNode() {
		VBox root = ongoingObject.ParentNode;
//		Scene rootScene = root.getScene();
//		rootScene.getStylesheets().add("ongoingbackup.css");
		return root;
	}
//	public FXMLLoader getFXMLfile() {
//		return loader;
//	}
}
