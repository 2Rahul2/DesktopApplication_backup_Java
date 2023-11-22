package org.openjfx.hellofx;

import java.io.File;
//import java.awt.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.openjfx.hellofx.StorePathInJson.createJsonBluePrint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EditBackupController implements Initializable {
	@FXML
	private VBox displayContents;
	@FXML
	VBox mainScreen;
	@FXML
	Button refreshButton;
	public boolean loadOnce=true;
	public List<HBox> allBackUp = new ArrayList<>();
//	private 
	public void refresh() {		
		for(HBox hboxNode : allBackUp) {
			displayContents.getChildren().remove(hboxNode);			
		}
		
		String jsonFilePath = System.getProperty("user.home")+"/appFolder/user_data.json";
		File jsonFile = new File(jsonFilePath);
		if(jsonFile.exists()) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode jsonNode = objectMapper.readTree(jsonFile);
				StorePathInJson jsonDataObject = new StorePathInJson();
				UploadBackup backupObject = UploadBackup.getInstance();
				int count = 0;
				for(BackUpTask task : backupObject.ScheduledBackupTaskList) {
					addContents(jsonDataObject.getJsonList(jsonDataObject.jsonPath) ,task.totalMinutes ,task.Name ,count);
					count++;
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("Unable to get backup Status");
		}
	}
	
	public void checkNumbers(TextField TF) {
		TF.addEventFilter(KeyEvent.KEY_TYPED, event->{
			String input = event.getCharacter();
			String allInput = TF.getText()+input;
			if(!input.matches("[0-9]")) {
				event.consume();
			}else {
				if(allInput!="") {
					int wholeValue = Integer.parseInt(allInput);
					if(wholeValue>60 || wholeValue<0) {						
						event.consume();
					}					
				}
			}
		});
	}
	public static boolean miniumSetTime(int totalMinutes) {
		if(totalMinutes>=1) {
			return true;
		}
		return false;
	}
	
	public static int STI(String num) {
		if(num!="") {
			return Integer.parseInt(num);			
		}else {
			return 0;
		}
	}
	public static boolean trueNumber(int num) {
		if(num<0 || num>60) {
			return false;
		}else {
			return true;
		}		
	}
//	public void addContents(JsonNode rootNode , List<createJsonBluePrint> jsonData ,List<BackUpTask> tasks) {
	public void addContents(List<createJsonBluePrint> jsonData ,int totalMin, String Name ,int backupIndex) {
		String folderName;
			int count = 0;
//			System.out.println("from edit backup:  "+tasks.get(i).Name);
//		for(JsonNode node: rootNode) {
//			for(int i=0;i<tasks.size();i++) {
			final int indexPosition =  count;
			final int scheduleIndex = backupIndex;
//			int totalMinutes = node.get("totalMinutes").asInt();
			int totalMinutes = totalMin;
			int hour = totalMinutes/60;
			int Min = 0;
			Min = (totalMinutes%60);				
//			if(hour<=0) {
//			}else {
//				Min = (totalMinutes%60) * 10;
//			}
//			final int min = Min;
			final HBox hbox = new HBox();
			hbox.setPrefHeight(55);
			hbox.setPrefWidth(890);
			hbox.setMaxWidth(Double.MAX_VALUE);
			hbox.setMaxHeight(Double.MAX_VALUE);
			hbox.setId("mainHbox");
			hbox.setAlignment(Pos.CENTER);
			
			
			Label folderNameLabel = new Label();
			folderNameLabel.setId("labelText");
			folderNameLabel.setPrefHeight(46);
			folderNameLabel.setPrefWidth(210);
			folderNameLabel.setMaxHeight(Double.MAX_VALUE);
			folderNameLabel.setMaxWidth(Double.MAX_VALUE);
			
			Image folderImage = new Image(getClass().getResourceAsStream("folder.png"));
			ImageView folderImageView = new ImageView(folderImage);
			folderImageView.setFitHeight(20);
			folderImageView.setFitWidth(20);
			folderNameLabel.setGraphic(folderImageView);
			folderName = Name;
//			folderName = node.get("name").asText();
			folderNameLabel.setText(folderName);
			final String fileName = folderName;
			
			
			Label hourLabel = new Label();
			hourLabel.setText("hour");
			hourLabel.setPrefHeight(32);
			hourLabel.setPrefWidth(79);
			hourLabel.setId("labelText");
			
			VBox hourBox = new VBox();
			hourBox.setPrefHeight(66);
			hourBox.setPrefWidth(40);
			hourBox.setMaxHeight(Double.MAX_VALUE);
			hourBox.setMaxWidth(Double.MAX_VALUE);
			hourBox.setId("timeBox");
			
			
			Button upButton = new Button();
			upButton.setText("");
			upButton.setPrefHeight(20);
			upButton.setPrefWidth(52);
			upButton.setId("vButton");
			
			Image upArrow = new Image(getClass().getResourceAsStream("arrowUp.png"));
			Image downArrow = new Image(getClass().getResourceAsStream("arrow.png"));
			
			ImageView upArrowView = new ImageView(upArrow);
			upArrowView.setFitHeight(20);
			upArrowView.setFitWidth(20);
			ImageView downArrowView = new ImageView(downArrow);
			downArrowView.setFitHeight(20);
			downArrowView.setFitWidth(20);
			
			final TextField hourTextInput = new TextField();
			
			Button downButton = new Button();
			downButton.setText("");
			downButton.setPrefHeight(20);
			downButton.setPrefWidth(52);
			downButton.setId("vButton");
			
			upButton.setGraphic(upArrowView);
			downButton.setGraphic(downArrowView);
			
			hourBox.getChildren().addAll(upButton , hourTextInput ,downButton);
			
			Label MinLabel = new Label();
			MinLabel.setText("Minutes");
			MinLabel.setPrefHeight(32);
			MinLabel.setPrefWidth(79);
			MinLabel.setId("labelText");
			
			
			VBox MinBox = new VBox();
			MinBox.setPrefHeight(66);
			MinBox.setPrefWidth(40);
			MinBox.setMaxHeight(Double.MAX_VALUE);
			MinBox.setMaxWidth(Double.MAX_VALUE);
			MinBox.setId("timeBox");
			
			
			Button MinupButton = new Button();
			MinupButton.setText("");
			MinupButton.setPrefHeight(20);
			MinupButton.setPrefWidth(52);
			MinupButton.setId("vButton");
			
			Image MinupArrow = new Image(getClass().getResourceAsStream("arrowUp.png"));
			Image MindownArrow = new Image(getClass().getResourceAsStream("arrow.png"));
			
			ImageView MinupArrowView = new ImageView(MinupArrow);
			MinupArrowView.setFitHeight(20);
			MinupArrowView.setFitWidth(20);
			ImageView MindownArrowView = new ImageView(MindownArrow);
			MindownArrowView.setFitHeight(20);
			MindownArrowView.setFitWidth(20);
			
			final TextField MinhourTextInput = new TextField();
			
			Button MindownButton = new Button();
			MindownButton.setText("");
			MindownButton.setPrefHeight(20);
			MindownButton.setPrefWidth(52);
			MindownButton.setId("vButton");
			
			MinupButton.setGraphic(MinupArrowView);
			MindownButton.setGraphic(MindownArrowView);
			
			MinBox.getChildren().addAll(MinupButton , MinhourTextInput ,MindownButton);
			
			Button crossButton = new Button();
			crossButton.setText("");
			
			Button correctButton = new Button();
			correctButton.setText("");
			
			Image crossImage = new Image(getClass().getResourceAsStream("cross.png"));
			Image correctImage = new Image(getClass().getResourceAsStream("correct.png"));
			
			ImageView corssImageView = new ImageView(crossImage);
			ImageView correctImageView = new ImageView(correctImage);
			
			corssImageView.setFitHeight(40);
			corssImageView.setFitWidth(40);
			
			correctImageView.setFitHeight(40);
			correctImageView.setFitWidth(40);
			
			crossButton.setGraphic(corssImageView);
			correctButton.setGraphic(correctImageView);
//			Font ButtonFont = new Font(1);
			crossButton.setStyle("-fx-font-size: 1px;");
			crossButton.setStyle("-fx-background-color:none;");
//			crossButton.setId("crossBtn");
			DropShadow dropShadow = new DropShadow();
			dropShadow.setWidth(14.0);
			dropShadow.setHeight(14.0);
//			dropShadow.setSpread(0);
//			dropShadow.setColor(Color.DARKGRAY);
			crossButton.setEffect(dropShadow);
			correctButton.setEffect(dropShadow);
			
			correctButton.setStyle("-fx-font-size: 1px;");
			correctButton.setStyle("-fx-background-color:none;");
			
			
			crossButton.setOnAction(e-> {
				jsonData.get(scheduleIndex).Name = "changed";
				jsonData.remove(scheduleIndex);
//				jsonData.remove(indexPosition);
				StorePathInJson SaveObject = new StorePathInJson();
				SaveObject.updateJsonData(jsonData);
				UploadBackup uploadObject = UploadBackup.getInstance();
				uploadObject.cancelTask(scheduleIndex);
				displayContents.getChildren().remove(hbox);
				System.out.println("index: "+scheduleIndex);
				System.out.println(fileName+"Delete button clicked");
				
			});
			
			correctButton.setOnAction(e->{
				jsonData.get(scheduleIndex).Name = fileName;
//				int totalM = (hour*60)+Min;
				jsonData.get(scheduleIndex).totalMinutes = (Integer.parseInt(hourTextInput.getText())*60) + Integer.parseInt(MinhourTextInput.getText());
				StorePathInJson SaveObject = new StorePathInJson();
				SaveObject.updateJsonData(jsonData);
				System.out.println("index: "+String.valueOf(scheduleIndex));
				System.out.println("correct button clicked");
				
			});
			
			hourTextInput.setAlignment(Pos.CENTER);
			hourTextInput.setText(String.valueOf(hour));
			MinhourTextInput.setText(String.valueOf(Min));
			MinhourTextInput.setAlignment(Pos.CENTER);
			checkNumbers(hourTextInput);
			checkNumbers(MinhourTextInput);
			
			MinupButton.setOnAction(e->{
				int currentInt = STI(MinhourTextInput.getText())+1;
				if(trueNumber(currentInt)) {
					MinhourTextInput.setText(String.valueOf(currentInt));				
				}
			});
			MindownButton.setOnAction(e->{
				int currentInt = STI(MinhourTextInput.getText())-1;
				if(trueNumber(currentInt)) {
					MinhourTextInput.setText(String.valueOf(currentInt));				
				}
			});
			
			upButton.setOnAction(e->{
				int currentInt = STI(hourTextInput.getText())+1;
				if(trueNumber(currentInt)) {
					hourTextInput.setText(String.valueOf(currentInt));				
				}
			});
			downButton.setOnAction(e->{
				int currentInt = STI(hourTextInput.getText())-1;
				if(trueNumber(currentInt)) {
					hourTextInput.setText(String.valueOf(currentInt));				
				}
			});
			
			count += 1;
			hbox.getChildren().addAll(folderNameLabel ,hourLabel ,hourBox ,MinLabel ,MinBox ,crossButton ,correctButton);
			allBackUp.add(hbox);
			displayContents.getChildren().add(hbox);
	}
		
	
	public void saveEditStuff() {
		
	}
	
	
	
	public void initialize(URL location, ResourceBundle resources) {	
	}
}
