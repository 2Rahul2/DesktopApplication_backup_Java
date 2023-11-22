package org.openjfx.hellofx;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
public class controller implements Initializable{

	@FXML
	private VBox existingVBox;
	@FXML
	private VBox rootNode;
	@FXML
	private HBox hi;
	@FXML 
	private VBox vi;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		VBox.setVgrow(vi, Priority.ALWAYS);
		HBox.setHgrow(hi, Priority.ALWAYS);
		
//		Label label1 = new Label("Label 1");
//        Label label2 = new Label("Label 2");
//
//        label1.setMaxWidth(Double.MAX_VALUE);
//        label2.setMaxWidth(Double.MAX_VALUE);
//
//        hi.getChildren().addAll(label1, label2);
		// TODO Auto-generated method stub
		// Assuming you already have a reference to the existing VBox

		for (int i = 0; i < 0; i++) {
//		    HBox hbox = new HBox();
//		    Label label = new Label();
//		    label.setText("hello");
//		    hbox.getChildren().add(label);

			
			HBox hbox = new HBox();
			hbox = createHBox(hbox);
			
			HBox.setHgrow(hbox, Priority.ALWAYS);
			VBox.setVgrow(existingVBox, Priority.ALWAYS);
			VBox.setVgrow(rootNode, Priority.ALWAYS);

		    existingVBox.getChildren().add(hbox);
//		    existingVBox.setMargin(hbox, new Insets(10 ,0 ,0 ,10));
		}
	}
	
	
	private HBox createHBox(HBox currentHBox) {
		HBox Hbox = new HBox();
		String [] labelText = {"S.NO" ,"Name" ,"Size"};
		int[] width = {54 ,318 ,137};
		int[] height = {74 ,74 ,74};
		
		Font font_16 = new Font(16);
		for(int i=0;i<3;i++) {
			Label newLabel = new Label();
			newLabel.setFont(font_16);
			newLabel.setText(labelText[i]);
			newLabel.setAlignment(Pos.CENTER);
			newLabel.setPrefHeight(height[i]);
			newLabel.setPrefWidth(width[i]);
			newLabel.setMaxWidth(Double.MAX_VALUE);
			newLabel.setMaxHeight(Double.MAX_VALUE);
			Line line = new Line(0, 0, 0, 50); 
	        line.setStyle("-fx-stroke: black;");
			Hbox.getChildren().addAll(line ,newLabel);
		}
		
		
		Button downloadButtonLabel = new Button();
		downloadButtonLabel.setPrefHeight(32);
		downloadButtonLabel.setPrefWidth(90);
		downloadButtonLabel.setMaxWidth(Double.MAX_VALUE);
		
		downloadButtonLabel.setAlignment(Pos.CENTER);
		Line line = new Line(0, 0, 0, 50); 
        line.setStyle("-fx-stroke: black;");
		Hbox.getChildren().addAll(line ,downloadButtonLabel);
//		HBox.setHgrow(Hbox, Priority.ALWAYS);
		//Label one -> S.No
//		Label Sno_Label = new Label();
//		Sno_Label.setAlignment(Pos.CENTER);
//		Sno_Label.setFont(font_16);
//		
//		Sno_Label.setPrefWidth(55);
//		Sno_Label.setPrefHeight(74);
//		
//		Label NameLabel = new Label();
//		NameLabel.setAlignment(Pos.CENTER);
		return Hbox;
	}


}
