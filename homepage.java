package org.openjfx.hellofx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

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
	Button logoutBtn;
	@FXML
	Button FuturepauseBtn;
	@FXML
	Button pauseAllBtn;
	@FXML
	Label warning; 
	public String buttonStyle;
	@FXML
	public Button settingButton;
	@FXML
	ImageView settingIcon;
	@FXML
	public Label internet;
	@SuppressWarnings("exports")
	@FXML
	public BorderPane mainPane;

	@FXML
	VBox middlePage;
	
	FXMLLoader loader = new FXMLLoader();
	Stage menuStage = new Stage();
	private Pane currentContent = new Pane();
	public FXMLLoader OBloader = null;
	
	@FXML
	public VBox downloadPage;
	
	public boolean backup_in_progress=true;
	public boolean all_backup_in_progress=true;
	public void StopAllBackupFunction() {
		UploadBackup getbackup = UploadBackup.getInstance();
		getbackup.PauseAllBackup();
		warning.setText("All Backup have been Stopped");
		warning.setVisible(true);
		addBackUp.setDisable(true);
		yourBackUp.setDisable(true);
		EditBackUp.setDisable(true);
		OnGoingBackup.setDisable(true);
		
		FuturepauseBtn.setDisable(true);
		pauseAllBtn.setDisable(true);
//		if(all_backup_in_progress) {
//			pauseAllBtn.setText("Start All backup");
//			all_backup_in_progress=false;
////			Warning Sign in home page that backup has been paused!!
//		}else {
//			pauseAllBtn.setText("Stop All backup");
//			getbackup.ResumeBackup();
//			warning.setVisible(false);
//		}
	}
	public void ResumeAllBackupFunction() {
		UploadBackup getbackup = UploadBackup.getInstance();
		if(getbackup.taskList.size()==0) {
			System.out.println("addding list in task list!!");
			getbackup.getDataFromJson();
//			getbackup.runListOfBackup();
		}
		getbackup.ResumeBackup();
		warning.setText("All Backup have been Stopped");
		warning.setVisible(false);
		addBackUp.setDisable(false);
		yourBackUp.setDisable(false);
		EditBackUp.setDisable(false);
		OnGoingBackup.setDisable(false);
		FuturepauseBtn.setDisable(!backup_in_progress);
		pauseAllBtn.setDisable(!all_backup_in_progress);
	}
	/**
	 *
	 */
	public void initialize(URL location, ResourceBundle resources) {
		
		RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), settingIcon);
		settingButton.setOnAction(e->{
			getSettingsFXML settingFXML = getSettingsFXML.getInstance();
			try {
				FXMLLoader settingLoader = settingFXML.getSettingLoader();
				settingsController controller = settingFXML.getSettingController();
				VBox vb = new VBox();
//				Parent root = settingLoader.load();
				vb.getChildren().add(controller.mainPage);
				Scene menuScene = new Scene(vb, 380,500);
				menuStage.setScene(menuScene);
				controller.getTimeSettings();
				menuStage.setTitle("Settings");
				menuStage.getIcons().add(new Image(getClass().getResourceAsStream("upload.png")));
				menuStage.setResizable(false);
				menuStage.show();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		settingButton.setOnMouseEntered(e->{
			rotateTransition.setToAngle(90);
			rotateTransition.play();
		});
		settingButton.setOnMouseExited(e->{
			System.out.println("Exiting setting");
//			rotateTransition.setRate(-1);
			rotateTransition.setToAngle(0);
			rotateTransition.play();
		});
		logoutBtn.setOnAction(e->{
			Logout logout = new Logout();
			logout.logoutUser();
		});
		logoutBtn.setOnMouseEntered(e->{
			logoutBtn.setStyle("-fx-background-color:#a57abf;-fx-font-size:14px;");
		});
		logoutBtn.setOnMouseExited(e->{
			logoutBtn.setStyle("-fx-background-color: #dcb5f5;-fx-font-size:12px;");
		});
		
		FuturepauseBtn.setOnMouseEntered(e->{
			FuturepauseBtn.setStyle("-fx-background-color:#a57abf;-fx-font-size:14px;");
		});
		FuturepauseBtn.setOnMouseExited(e->{
			FuturepauseBtn.setStyle("-fx-background-color: #dcb5f5;-fx-font-size:12px;");
		});
		
		pauseAllBtn.setOnMouseEntered(e->{
			pauseAllBtn.setStyle("-fx-background-color:#a57abf;-fx-font-size:14px;");
		});
		pauseAllBtn.setOnMouseExited(e->{
			pauseAllBtn.setStyle("-fx-background-color: #dcb5f5;-fx-font-size:12px;");
		});
		pauseAllBtn.setOnAction(e->{
			UploadBackup getbackup = UploadBackup.getInstance();
			if(all_backup_in_progress) {
				pauseAllBtn.setText("Start All backup");
				getbackup.PauseAllBackup();
				warning.setText("All Backup have been Stopped");
				warning.setVisible(true);
				all_backup_in_progress=false;
				FuturepauseBtn.setDisable(true);
//				Warning Sign in home page that backup has been paused!!
			}else {
				pauseAllBtn.setText("Stop All backup");
				getbackup.ResumeBackup();
				warning.setVisible(false);
				FuturepauseBtn.setDisable(false);
				all_backup_in_progress=true;
			}
		});
		FuturepauseBtn.setOnAction(e->{
			UploadBackup getbackup = UploadBackup.getInstance();
			if(backup_in_progress) {
				FuturepauseBtn.setText("Start Future backup");
				getbackup.PauseFutureBackup();
				backup_in_progress=false;
				pauseAllBtn.setDisable(true);
				warning.setText("Future Backup have been Stopped");
				warning.setVisible(true);
//				Warning sign in home page that backup has been paused!!
//				with image too hheheeh
			}else {
				FuturepauseBtn.setText("Stop Future backup");
				getbackup.ResumeBackup();
				warning.setVisible(false);
				pauseAllBtn.setDisable(false);
				backup_in_progress=true;
			}
		});
		
//		OBloader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
//		testButton.setOnAction(e->{
//			DirectoryChooser directoryChooser = new DirectoryChooser();
//			Stage FileStage = new Stage();
////			FileStage.initModality(Modality.APPLICATION_MODAL);
////			FileStage.initOwner(mainStage);
//			
//			File selectDir = directoryChooser.showDialog(FileStage);
//		});
		addBackUp.setOnMouseEntered(event->{			
			addBackUp.setStyle("-fx-border-color: #e6a627;-fx-background-color: #5c427d;-fx-text-fill:white;");			
		});
		addBackUp.setOnMouseExited(event->{
			addBackUp.setStyle("-fx-border-color: none;-fx-background-color: #5c427d;-fx-text-fill:white;");
		});
		yourBackUp.setOnMouseEntered(event->{			
			yourBackUp.setStyle("-fx-border-color: #e6a627;-fx-background-color: #5c427d;-fx-text-fill:white;");			
		});
		yourBackUp.setOnMouseExited(event->{
			yourBackUp.setStyle("-fx-border-color: none;-fx-background-color: #5c427d;-fx-text-fill:white;");
		});
		EditBackUp.setOnMouseEntered(event->{			
			EditBackUp.setStyle("-fx-border-color: #e6a627;-fx-background-color: #5c427d;-fx-text-fill:white;");			
		});
		EditBackUp.setOnMouseExited(event->{
			EditBackUp.setStyle("-fx-border-color: none;-fx-background-color: #5c427d;-fx-text-fill:white;");
		});
		OnGoingBackup.setOnMouseEntered(event->{			
			OnGoingBackup.setStyle("-fx-border-color: #e6a627;-fx-background-color: #5c427d;-fx-text-fill:white;");			
		});
		OnGoingBackup.setOnMouseExited(event->{
			OnGoingBackup.setStyle("-fx-border-color: none;-fx-background-color: #5c427d;-fx-text-fill:white;");
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
//		mainPane.setCenter(currentContent);
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
//			Descending Order ~~~~ Descending Order of ViewBackup List from server according to Date Wise
			for(int i =0;i<fileExplorer.size();i++) {
				System.out.println(fileExplorer.get(i).FileName);
//				backUpObject.addF();
			backUpObject.addFolder("sample", "25252", "1" ,fileExplorer.get(i) ,"");
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
					backupObject.getDataFromJson();
//					backupObject.runListOfBackup();;
					int count=0;
					for(BackUpTask task : backupObject.taskList) {
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
    			String date;
    			jsonSettings getSettings = new jsonSettings();
    			if(getSettings.getISTvalue()) {
    				System.out.println("Indain Standard Time");
    				for(int i =0;i<fileExplorer.size();i++) {
//        				System.out.println(fileExplorer.get(i).FileName);
        				
        				date = fileExplorer.get(i).FileDate;
        				backUpObject.addFolder(fileExplorer.get(i).FileName, fileExplorer.get(i).FileSize, String.valueOf(count) , fileExplorer.get(i) ,date);
        				count++;
        			}
    			}else {
    				System.out.println("UTC Standard Time");
    				for(int i =0;i<fileExplorer.size();i++) {
//    				System.out.println(fileExplorer.get(i).FileName);
    					
    					date = convertDate(fileExplorer.get(i).FileDate);
    					backUpObject.addFolder(fileExplorer.get(i).FileName, fileExplorer.get(i).FileSize, String.valueOf(count) , fileExplorer.get(i) ,date);
    					count++;
    				}			    				
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
	public String convertDate(String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime istDateTime = LocalDateTime.parse(date ,formatter);
			
//		Zoned time = IST
			ZoneId istZone = ZoneId.of("Asia/Kolkata");
			ZonedDateTime istzoneDatetime = ZonedDateTime.of(istDateTime, istZone);
//		Convert to Utc
			ZoneId utcZone = ZoneId.of("UTC");
			ZonedDateTime utczonedDateTime = istzoneDatetime.withZoneSameInstant(utcZone);
//		Format the result
			DateTimeFormatter result = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String utcDate = utczonedDateTime.format(result);
			System.out.println(date+"----->"+utcDate);
			return utcDate;			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
