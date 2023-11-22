package org.openjfx.hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;

public class sample implements Initializable{

    private HBox hi;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		HBox.setHgrow(hi, Priority.ALWAYS);
		// TODO Auto-generated method stub
		
	}
    
}
