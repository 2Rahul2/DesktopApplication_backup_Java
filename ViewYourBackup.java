package org.openjfx.hellofx;

import org.openjfx.hellofx.JsonToObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
		public  CustomButton(String Name ,String Size , String indexNumber ,String fileId ,String fileDate) {
			final String idNumber =fileId;
			final String fileType = "folder";
			final String branchType = "mainBranch";
			final String Fname = Name;
			HBox hbox = new HBox();
			hbox.setId("folderData");
			
			Label Sno = new Label();
			Sno.setStyle("-fx-text-fill: white;");
			Sno.setText(indexNumber);
			Sno.setId("folderLabel");
			Sno.setPrefWidth(95);
		    Sno.setPrefHeight(41);
			
			Label name = new Label();
			name.setId("NamefolderLabel");
			name.setStyle("-fx-text-fill: white;");
			name.setText(Name);
			
			Image image = new Image(getClass().getResourceAsStream("folder.png"));
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			name.setGraphic(imageView);
			name.setPrefWidth(350);
			name.setPrefHeight(41);
			
			Label size = new Label();
			size.setStyle("-fx-text-fill: white;");
			size.setId("folderLabel");
			size.setText(Size+" bytes");

			size.setPrefWidth(230);
			size.setPrefHeight(41);
			
			Label date = new Label();
			date.setStyle("-fx-text-fill: white;");
			date.setId("folderLabel");

			date.setText(fileDate);
			date.setPrefWidth(230);
			date.setPrefHeight(41);
			
			Button backupButton = new Button();
			backupButton.setId("backupBtn");
			Image Dimage = new Image(getClass().getResourceAsStream("inbox.png"));
			ImageView DimageView = new ImageView(Dimage);
			DimageView.setFitHeight(20);
			DimageView.setFitWidth(20);
			
			backupButton.setId("folderButton");
			backupButton.setText("backup");
			backupButton.setPrefWidth(200);
			backupButton.setPrefHeight(47);
			backupButton.setGraphic(DimageView);
			backupButton.setOnAction(e->{
				FileDownloader filedownload = new FileDownloader(fileType ,branchType ,fileId ,Fname);
				filedownload.StartDownload();
				System.out.println("download clicked : file id: "+idNumber+" branch:"+branchType+" File Type: "+fileType);
				System.out.println("sub button");
				e.consume();
			});
			backupButton.setStyle("-fx-background-color: none;");
			backupButton.setStyle("-fx-cursor: hand;");
			backupButton.setOnMouseEntered(e->{
				backupButton.setStyle("-fx-background-color: #d9d1f0;-fx-text-fill: black;");
			});
			backupButton.setOnMouseExited(e->{
				backupButton.setStyle("-fx-background-color: none;-fx-text-fill: white;");
			});
			hbox.getChildren().addAll(Sno ,name ,date,size ,backupButton);
			hbox.setStyle("-fx-background-color: none;");
			hbox.setPrefWidth(830);
			hbox.setPrefHeight(41);
			setGraphic(hbox);
//			backupContainer.getChildren().add(hbox);
		}
	}
	public void addFolder(String Name , String Size ,String indexNumber ,FileExplorer fileExplorer ,String fileDate) {
		CustomButton customButton = new CustomButton(Name ,Size ,indexNumber , fileExplorer.FileId ,fileDate);
		customButton.getStyleClass().add("CustomButton");
		customButton.setPrefHeight(41);
		customButton.setPrefWidth(Region.USE_COMPUTED_SIZE);
		customButton.setMaxWidth(Double.MAX_VALUE);
		customButton.setStyle("-fx-cursor: hand;");
		customButton.setStyle("-fx-background-color:  #464a5c;");
		
		customButton.setOnMouseEntered(e->{
			customButton.setStyle("-fx-background-color: #3c3e4d;");
			});
		customButton.setOnMouseExited(e->{
			customButton.setStyle("-fx-background-color:  #464a5c;");
			});
		
		 
		backupContainer.setStyle("-fx-background-color: #aca0bd;");
		backupContainer.getChildren().add(customButton);
		Insets margin = new Insets(5, 0, 0, 0); // Insets(top, right, bottom, left)
		backupContainer.setMargin(customButton,margin);
		customButton.setOnAction(e->{
			System.out.println("mainButton");
			Stage folderStage = new Stage();
			try {
				
				App app = App.getInstance();
				app.MainStageDisable();
				
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
				
//				Stage parentStage = App.MainStageAcess();
//				Node sceneRoot = parentStage.getScene().getRoot();
//				sceneRoot.setDisable(true);
				
				folderStage.setOnCloseRequest(event->{
//					sceneRoot.setDisable(false);
					app.MainStageEnable();
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
