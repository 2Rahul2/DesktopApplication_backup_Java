package org.openjfx.hellofx;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class getHomePageFXML {
	private static getHomePageFXML instance = new getHomePageFXML();
	public homepage homepageController;
	public FXMLLoader homeLoader;
	private getHomePageFXML() {
		try {
			homeLoader = new FXMLLoader(getClass().getResource("test.fxml"));
			Parent root = homeLoader.load();
			homepageController = homeLoader.getController();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static getHomePageFXML getInstance() {
		return instance;
	}
	public homepage getController() {
		return homepageController;
	}
	public BorderPane getMainPane() {
        return homepageController.mainPane;
    }

}
