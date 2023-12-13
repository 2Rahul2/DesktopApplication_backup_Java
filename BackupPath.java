package org.openjfx.hellofx;

//import java.awt.event.KeyEvent;
import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.nio.file.Path;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class BackupPath implements Initializable{
	@FXML
	Button selectFolder;
	@FXML
	Button selectFile;
	@FXML
	TextField TimeText1;
	@FXML
	TextField TimeText2;
	@FXML
	TextField TimeText3;
	@FXML
	TextField TimeText4;
	@FXML
	Button fileUpload;
	@FXML
	Button folderUpload;
	@FXML
	Label folderLabelName;
	@FXML
	Label fileLabelName;
	
	
	@FXML
	Button folderHourInc;
	@FXML
	Button folderHourDec;
	@FXML
	Button folderMinInc;
	@FXML 
	Button folderMinDec;
	
	
	@FXML
	Button fileHourInc;
	@FXML 
	Button fileHourDec;
	@FXML
	Button fileMinInc;
	@FXML
	Button fileMinDec;
	
	
	
	
	private File FolderPath;
	private File FilePath;
	private Stage mainStage;
	private String folder_name_str;
	private String folder_path_str;
	public static UploadBackup obj;
	private long sizeLimit=10485760;
	private int setDurationLimit = 1;
	public void setMainStage(Stage stage) {
		this.mainStage = stage;
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
	public void showAlert(String headerName , String contentText ,int totalMinutes) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(headerName);
		alert.getDialogPane().setGraphic(null);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		Image customIcon = new Image(getClass().getResourceAsStream("backup_2.png"));
		alertStage.getIcons().add(customIcon);
		alert.setContentText(contentText);
		ButtonType okButton = alert.getButtonTypes().stream()
                .filter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData())
                .findFirst()
                .orElse(null);

        // Set an event handler for the OK button
        if (okButton != null) {
        	Button okButtonNode = (Button) alert.getDialogPane().lookupButton(okButton);
        	okButtonNode.setOnAction(event -> {
            	try{
            		
					StorePathInJson jsonObject = new StorePathInJson(folder_name_str , folder_path_str ,totalMinutes);
					jsonObject.storeJsonData();
					UploadBackup uploadObject =  UploadBackup.getInstance();
					BackUpTask newtask = new BackUpTask(folder_name_str ,folder_path_str ,totalMinutes);
					uploadObject.addExtraTaskSchedule(newtask);
					
				}catch(Exception e1) {
					e1.printStackTrace();
				}
                System.out.println("OK button clicked! Performing a task...");
            });
        }
		alert.showAndWait();
	}
	public long getFolderSize(File file) {
		long size = 0;
		if(file.isDirectory()) {
			for(File files:file.listFiles()) {
				size += getFolderSize(files);
			}
		}else {
			size += file.length();
		}
		
		return size;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		checkNumbers(TimeText1);
		checkNumbers(TimeText2);
		checkNumbers(TimeText3);
		checkNumbers(TimeText4);
		
		
		folderHourInc.setOnAction(e->{
			int currentInt = STI(TimeText1.getText())+1;
			if(trueNumber(currentInt)) {
				TimeText1.setText(String.valueOf(currentInt));				
			}
		});
		folderHourDec.setOnAction(e->{
			int currentInt = STI(TimeText1.getText())-1;
			if(trueNumber(currentInt)) {
				TimeText1.setText(String.valueOf(currentInt));				
			}
		});
		
		fileHourInc.setOnAction(e->{
			int currentInt = STI(TimeText3.getText())+1;
			TimeText3.setText(String.valueOf(currentInt));
		});
		
		fileHourDec.setOnAction(e->{
			int currentInt = STI(TimeText3.getText())-1;
			TimeText3.setText(String.valueOf(currentInt));
		});
		
		
		folderMinInc.setOnAction(e->{
			int currentInt = STI(TimeText2.getText())+1;
			TimeText2.setText(String.valueOf(currentInt));
		});
		
		folderMinDec.setOnAction(e->{
			int currentInt = STI(TimeText2.getText())-1;
			TimeText2.setText(String.valueOf(currentInt));
		});
		
		fileMinInc.setOnAction(e->{
			int currentInt = STI(TimeText4.getText())+1;
			TimeText4.setText(String.valueOf(currentInt));
		});
		
		fileMinDec.setOnAction(e->{
			int currentInt = STI(TimeText4.getText())-1;
			TimeText4.setText(String.valueOf(currentInt));
		});
		
		
		folderUpload.setOnAction(e->{
			
			if(FolderPath != null) {
				if(TimeText1.getText() == "") {
					TimeText1.setText("0");
				}
				if(TimeText2.getText()=="") {
					TimeText2.setText("0");
				}
				if(TimeText1.getText()!="" || TimeText2.getText()!="") {
					getHomePageFXML homePageFxml = getHomePageFXML.getInstance();
					homepage homePage = homePageFxml.homepageController;
					if(homePage.all_backup_in_progress && homePage.backup_in_progress) {
						int hour = Integer.parseInt(TimeText1.getText());
						int minutes = Integer.parseInt(TimeText2.getText());
						int totalMinutes =(hour*60)+minutes; 
						if(miniumSetTime(totalMinutes)) {
							System.out.println("Able to Upload");
							System.out.println(hour+"---"+minutes);	
							File file = new File(folder_path_str);
		            		if(getFolderSize(file) < sizeLimit) {
		            			showAlert(folder_name_str ," backup will be made every "+hour+" hour and "+minutes+" minutes" ,totalMinutes);		            			
		            		}else {
		            			Alert alert = new Alert(AlertType.INFORMATION);
		            			alert.setHeaderText("Size Exceeds");
		            			alert.getDialogPane().setGraphic(null);
		            			Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		            			Image customIcon = new Image(getClass().getResourceAsStream("attention.png"));
		            			alertStage.getIcons().add(customIcon);
		            			alert.setContentText("Upload File less than 10 MB");
		            			alert.showAndWait();
		            		}
							
						}else {
							Alert alert = new Alert(AlertType.INFORMATION);
	            			alert.setHeaderText("Set Duration");
	            			alert.getDialogPane().setGraphic(null);
	            			Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
	            			Image customIcon = new Image(getClass().getResourceAsStream("time.png"));
	            			alertStage.getIcons().add(customIcon);
	            			alert.setContentText("Duration should be Atleast "+setDurationLimit+" Mins");
	            			alert.showAndWait();
						}						
					}
				}
				
			}
		});
		
		fileUpload.setOnAction(e->{
			if(FilePath!=null) {
				if(TimeText3.getText()!="" && TimeText4.getText()!="") {
					int hour = Integer.parseInt(TimeText3.getText());
					int min = Integer.parseInt(TimeText4.getText());
					
					System.out.println(hour+"---"+min);
				}
			}
		});
		selectFolder.setOnAction(e->{
			DirectoryChooser directoryChooser = new DirectoryChooser();
			Stage FileStage = new Stage();
			FolderPath = directoryChooser.showDialog(FileStage);
			if(FolderPath.exists()) {
				Path folder_name = FolderPath.toPath();
				folder_path_str = folder_name.toString();
				folder_name_str = folder_name.getFileName().toString();
				folderLabelName.setText(folder_name_str);
				
			}
//			System.out.println(FolderPath.getPath());
//			System.out.println("absoulte path : " + FolderPath.getAbsolutePath());
		});
		
		
		
		selectFile.setOnAction(e->{
			FileChooser fileChooser = new FileChooser();
			Stage FileStage = new Stage();
			FilePath = fileChooser.showOpenDialog(FileStage);
			
			Path file_name = FilePath.toPath();
			String file_name_str = file_name.getFileName().toString();
			fileLabelName.setText(file_name_str);
		});
	}
	
}
