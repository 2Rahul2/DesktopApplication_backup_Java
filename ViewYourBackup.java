package org.openjfx.hellofx;

import org.openjfx.hellofx.JsonToObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import org.openjfx.hellofx.FileExplorer;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.client.ClientProtocolException;
public class ViewYourBackup implements Initializable {
	@FXML
	public VBox backupContainer;
	public static void main(String[] args) {
		
	}
	public void addF() {
		System.out.println("testing");
	}
	public class CustomButton extends Button{
		public  CustomButton(String Name ,String Size , String indexNumber ,String fileId) {
			final String idNumber =fileId;
			final String fileType = "folder";
			final String branchType = "mainBranch";
			final String Fname = Name;
			HBox hbox = new HBox();
			hbox.setId("folderData");
			
			Label Sno = new Label();
			Sno.setText(indexNumber);
			Sno.setId("folderLabel");
			Sno.setPrefWidth(94);
		    Sno.setPrefHeight(41);
			
			Label name = new Label();
			name.setId("NamefolderLabel");
			name.setText(Name+fileId);
			
			Image image = new Image(getClass().getResourceAsStream("folder.png"));
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			name.setGraphic(imageView);
			name.setPrefWidth(407);
			name.setPrefHeight(41);
			
			Label size = new Label();
			size.setId("folderLabel");
			size.setText(Size);
			size.setPrefWidth(230);
			size.setPrefHeight(41);
			
			Button backupButton = new Button();
			backupButton.setId("backupBtn");

			backupButton.setId("folderButton");
			backupButton.setText("backup");
			backupButton.setPrefWidth(251);
			backupButton.setPrefHeight(47);
			
			backupButton.setOnAction(e->{
				FileDownloader filedownload = new FileDownloader(fileType ,branchType ,fileId ,Fname);
				filedownload.StartDownload();
				System.out.println("download clicked : file id: "+idNumber+" branch:"+branchType+" File Type: "+fileType);
				System.out.println("sub button");
				e.consume();
			});
			hbox.getChildren().addAll(Sno ,name ,size ,backupButton);

			hbox.setPrefWidth(830);
			hbox.setPrefHeight(41);
			setGraphic(hbox);
//			backupContainer.getChildren().add(hbox);
		}
	}
	public void addFolder(String Name , String Size ,String indexNumber ,FileExplorer fileExplorer) {
		CustomButton customButton = new CustomButton(Name ,Size ,indexNumber , fileExplorer.FileId);
		customButton.getStyleClass().add("CustomButton");
		customButton.setPrefHeight(41);
		customButton.setPrefWidth(Region.USE_COMPUTED_SIZE);
		customButton.setMaxWidth(Double.MAX_VALUE);
		backupContainer.getChildren().add(customButton);
		customButton.setOnAction(e->{
			System.out.println("mainButton");
			Stage folderStage = new Stage();
			try {
				FXMLLoader windowloader = loadFXML("WindowFile");
				Scene newScene = new Scene(windowloader.load() ,750 ,500);
				newScene.getStylesheets().add(getClass().getResource("treeview.css").toExternalForm());
				folderStage.setScene(newScene);
				folderStage.setTitle("File Explorer");
				folderStage.getIcons().add(new Image(getClass().getResourceAsStream("folder.png")));
				folderStage.show();
				
				windowFile windowFileObject = windowloader.getController();
				windowFileObject.createTreeView(fileExplorer);
				windowFileObject.createFolderPannel(fileExplorer);
				windowFileObject.checkForwardPage();
				windowFileObject.checkPreviousPage();
				
				Stage parentStage = App.MainStageAcess();
				Node sceneRoot = parentStage.getScene().getRoot();
				sceneRoot.setDisable(true);
				folderStage.setOnCloseRequest(event->{
					sceneRoot.setDisable(false);
				});
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		
	}
	private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

	
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
}
