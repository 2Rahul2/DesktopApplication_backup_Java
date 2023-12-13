package org.openjfx.hellofx;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class getSettingsFXML {
	private static getSettingsFXML instance = new getSettingsFXML();
	public settingsController settingController;
	public FXMLLoader settingLoader;
	private getSettingsFXML() {
		try {
			settingLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
			Parent root = settingLoader.load();
			settingController = settingLoader.getController();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public FXMLLoader getSettingLoader() {
        return settingLoader;
    }

    public settingsController getSettingController() {
        return settingController;
    }
	public static getSettingsFXML getInstance() {
		return instance;
	}
}
