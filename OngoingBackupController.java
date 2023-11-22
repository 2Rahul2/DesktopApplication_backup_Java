package org.openjfx.hellofx;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class OngoingBackupController implements Initializable {
	public HashMap<Integer , HBox> hashmap = new HashMap<>();
//	public List<HBox> HboxContents =new ArrayList<>();
	public List<BackUpTask> allTask=new ArrayList<>();
	public boolean isRendered = false;
	public UploadBackup uploadObject;
	@FXML
	VBox MainScreen;
	@FXML 
	VBox ParentNode;
	private int indexCount = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		MainScreen.setId("mainS");
//		MainScreen.setPrefWidth(890);
//		MainScreen.setPrefHeight(615);
//		MainScreen.setMaxWidth(Double.MAX_VALUE);
//		MainScreen.setMaxHeight(Double.MAX_VALUE);
		isRendered = true;		
		System.out.println("File is been display :: "+ isRendered);
//		fun();
	}
	public void fun(List<BackUpTask> allTask) {
		
		System.out.println("~~~~"+allTask);
		for(int i = 0;i<allTask.size();i++) {
			BackUpTask tempTask = allTask.get(i);
			System.out.println("*************************************");
			addContents(i ,tempTask.Name , tempTask.lastBackup , tempTask.nextBackup);
		}
	}
	
	public void addContents(int HboxIndex ,String Name , String LastBackup ,String NextBackup) {
		final int newIndex = indexCount;
		final int hboxIndex = HboxIndex;
		HBox hbox = new HBox();
		hbox.setPrefHeight(90);
		hbox.setPrefWidth(810);
		hbox.setMaxHeight(Double.MAX_VALUE);
		hbox.setMaxWidth(Double.MAX_VALUE);
		hbox.setId("nodeHbox");
//		HboxContents.add(hbox);
		VBox vboxOne = new VBox();
		Label folderName = new Label();
		Label backupTaken = new Label();
		Label nextBackup = new Label();
		folderName.setId("labelText");
		backupTaken.setId("labelText");
		nextBackup.setId("labelText");
		folderName.setText(String.valueOf(HboxIndex)+Name);
		backupTaken.setText("Backup Taken at :"+LastBackup);
		nextBackup.setText("Next Backup at: "+NextBackup);
		
		vboxOne.setPrefHeight(70);
		vboxOne.setPrefWidth(700);
		vboxOne.setSpacing(2);
		vboxOne.setAlignment(Pos.CENTER_LEFT);
		vboxOne.getChildren().addAll(folderName ,backupTaken ,nextBackup);
		
		VBox vboxTwo = new VBox();
		vboxTwo.setPrefHeight(70);
		vboxTwo.setPrefWidth(650);
		vboxTwo.setId("progressStage");	
		vboxTwo.setAlignment(Pos.CENTER_LEFT);
		
		Label stageProgress = new Label();
		Label progressName = new Label();
		stageProgress.setId("labelText");
		progressName.setId("labelText");
		ProgressBar progressBar = new ProgressBar();
		stageProgress.setPrefHeight(18);
		stageProgress.setPrefWidth(205);
		stageProgress.setText("stage 1 of 2");
		progressName.setPrefHeight(18);
		progressName.setPrefWidth(205);
		progressName.setText("<Value here>");
		
		progressBar.setPrefHeight(15);
		progressBar.setPrefWidth(250);
		progressBar.setMaxWidth(Double.MAX_VALUE);
		progressBar.setProgress(0.0);
		
		vboxTwo.getChildren().addAll(stageProgress ,progressName ,progressBar);
		
		
		VBox vboxThree = new VBox();
		vboxThree.setPrefWidth(610);
		vboxThree.setPrefHeight(70);
		vboxThree.setStyle("-fx-padding: 0 0 0 15;");
		vboxThree.setAlignment(Pos.CENTER_LEFT);
		
		HBox innerHbox =  new HBox();
		innerHbox.setAlignment(Pos.CENTER);
		innerHbox.setSpacing(10);
		Button pauseButton = new Button();
		Image pauseImage = new Image(getClass().getResourceAsStream("pause-button.png"));
		ImageView pauseView = new ImageView(pauseImage);
		pauseView.setFitHeight(35);
		pauseView.setFitWidth(35);
		pauseButton.setPrefHeight(35);
		pauseButton.setPrefWidth(35);
		pauseButton.setGraphic(pauseView);
		pauseButton.setStyle("-fx-font-size: 1px;");
		pauseButton.setStyle("-fx-background-color:none;");
		DropShadow pausePlayShadow = new DropShadow();
		pausePlayShadow.setColor(Color.web("#df7c7c"));
		pauseButton.setEffect(pausePlayShadow);
		Button cancelButton = new Button();
		cancelButton.setId("cancelButton");
		DropShadow buttonShadow = new DropShadow();
		buttonShadow.setColor(Color.web("#7a678a"));
		cancelButton.setEffect(buttonShadow);
		cancelButton.setPrefWidth(130);
		cancelButton.setPrefHeight(30);
		cancelButton.setText("Cancel Upload");
		innerHbox.getChildren().addAll(cancelButton ,pauseButton);
		vboxThree.getChildren().add(innerHbox);
		hbox.getChildren().addAll(vboxOne , vboxTwo ,vboxThree);
//		HboxContents.add(hbox);
		hashmap.put(hboxIndex ,hbox);
		pauseButton.setOnAction(e->{
			System.out.println("Pause the backup clicked");
			UploadBackup uploadObject = UploadBackup.getInstance();
//			uploadObject.runningBackupList.remove(hboxIndex);
			uploadObject.pauseBackUpObject(hboxIndex);
		});
		cancelButton.setOnAction(e->{
			System.out.println("Index:  "+newIndex);
//			HBox removeHbox = HboxContents.get(newIndex);
//			HboxContents.remove(newIndex);
			UploadBackup uploadObject = UploadBackup.getInstance();
			uploadObject.runningBackupList.remove(hboxIndex);
			hashmap.remove(hboxIndex);
			MainScreen.getChildren().remove(hbox);
		});
		MainScreen.getChildren().add(hbox);
		indexCount++;
		
	}
}
