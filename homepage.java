package org.openjfx.hellofx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import org.openjfx.hellofx.JsonToObject;
import org.apache.http.client.ClientProtocolException;
import org.openjfx.hellofx.FileExplorer;
import org.openjfx.hellofx.ViewYourBackup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class homepage implements Initializable {
	@FXML
	Button addBackUp;
	@FXML
	Button yourBackUp;
	@FXML
	Button EditBackUp;
	@FXML
	Button OnGoingBackup;
	@FXML
	BorderPane mainPane;
	@FXML
	Button testButton;
	
	FXMLLoader loader = new FXMLLoader();
	
	private Pane currentContent = new Pane();
	public FXMLLoader OBloader = null;
	public void initialize(URL location, ResourceBundle resources) {
//		OBloader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
		testButton.setOnAction(e->{
			DirectoryChooser directoryChooser = new DirectoryChooser();
			Stage FileStage = new Stage();
//			FileStage.initModality(Modality.APPLICATION_MODAL);
//			FileStage.initOwner(mainStage);
			
			File selectDir = directoryChooser.showDialog(FileStage);
		});
		addBackUp.setOnAction(e->
			{
				try {
					loadBackUpPath("BackUpPath.fxml");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		);
		yourBackUp.setOnAction(e->{
			loadNewContent("YourBackUpFiles.fxml");
			System.out.println("view your backup");
//			try {
//				viewYourBackUp();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		});
		EditBackUp.setOnAction(e->
			loadEditBackUp("EditBackUp.fxml")
		);
		OnGoingBackup.setOnAction(e->{
			loadOngoingPage("OnGoingBackUp.fxml");
		});
		currentContent.setStyle("-fx-border-color: #151530;");
		mainPane.setCenter(currentContent);
		// TODO Auto-generated method stub
	}
	public void loadOngoingPage(String fxmlName) {
		try {
			getOngoingFXML ongoingObject = getOngoingFXML.getInstance();
//			OBloader = ongoingObject.getFXMLfile();
//			Pane newPane = OBloader.load();
//			OBloader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
			OngoingBackupController obj = ongoingObject.ongoingObject;
			UploadBackup Uobject = UploadBackup.getInstance();
			obj.allTask = Uobject.taskList;
			VBox childNode = ongoingObject.getRootNode();
			mainPane.setCenter(childNode);
//			obj.fun(Uobject.taskList);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void viewYourBackUp() throws ClientProtocolException, IOException {
		JsonToObject object = new JsonToObject();
		List<FileExplorer> fileExplorer = object.listOfFileExplorer;
		loader = new FXMLLoader(getClass().getResource("/org/openjfx/hellofx/YourBackUpFiles.fxml"));
//		loaderR.setControllerFactory(c -> new ViewYourBackup());
		ViewYourBackup backUpObject = loader.getController();
		if(backUpObject!=null) {
			for(int i =0;i<fileExplorer.size();i++) {
				System.out.println(fileExplorer.get(i).FileName);
//				backUpObject.addF();
			backUpObject.addFolder("sample", "25252", "1" ,fileExplorer.get(i));
			}			
		}else {
			System.out.println("loader is null");
		}
	}
	public void loadBackUpPath(String FXMLFileName) throws IOException {
		loader = new FXMLLoader(getClass().getResource(FXMLFileName));
		Pane newPane = loader.load();
		mainPane.setCenter(newPane);
		
	}
	public void loadEditBackUp(String FXMLFileName) {
		try {
			getEditbackupFXML editObject = getEditbackupFXML.getinstance();
			EditBackupController editController = editObject.editbackupObject;
			VBox editRootNode = editController.mainScreen;
//			FXMLLoader EditBackuploader = new FXMLLoader(getClass().getResource(FXMLFileName));
//			Pane loadScreen = EditBackuploader.load();
			String jsonFilePath = System.getProperty("user.home")+"/appFolder/user_data.json";
			File jsonFile = new File(jsonFilePath);
			if(jsonFile.exists()) {
				if(editController.loadOnce) {
					System.out.println("it exists");
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode rootNode = objectMapper.readTree(jsonFile);
//				EditBackupController editBackupObject = EditBackuploader.getController();
					StorePathInJson jsonDataObject = new StorePathInJson();
					UploadBackup backupObject = UploadBackup.getInstance();
					int count=0;
					for(BackUpTask task : backupObject.ScheduledBackupTaskList) {
						editController.addContents(jsonDataObject.getJsonList(jsonDataObject.jsonPath) ,task.totalMinutes ,task.Name ,count);
						count++;
					}
//				editBackupObject.addContents(rootNode ,jsonDataObject.getJsonList(jsonDataObject.jsonPath) ,backupObject.taskList);
					for(JsonNode node : rootNode) {
						String name = node.get("name").asText();
						System.out.println("Name: " +name);
					}
					editController.loadOnce=false;
				}
				
				
			}
			mainPane.setCenter(editRootNode);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void loadEditBackUpContent() {
		
	}
	
	public void loadNewContent(String FXMLFileName) {
		try {
            // Load the content from the NewContent.fxml file
            loader = new FXMLLoader(getClass().getResource("/org/openjfx/hellofx/"+FXMLFileName));
            Pane newContent = loader.load();
            JsonToObject object = new JsonToObject();
    		List<FileExplorer> fileExplorer = object.listOfFileExplorer;

    		ViewYourBackup backUpObject = loader.getController();
    		if(backUpObject!=null) {
    			int count = 1;
    			for(int i =0;i<fileExplorer.size();i++) {
    				System.out.println(fileExplorer.get(i).FileName);

    			backUpObject.addFolder(fileExplorer.get(i).FileName, "25252", String.valueOf(count) , fileExplorer.get(i));
    			count++;
    			}			
    		}else {
    			System.out.println("loader is null");
    		}

            mainPane.setCenter(newContent);

            // Update the currentContent to keep track of the loaded content
            currentContent = newContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
