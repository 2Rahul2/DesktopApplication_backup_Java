package org.openjfx.hellofx;

import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Menu;


import javafx.application.Platform;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;




/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Stage mainStage;
    private static App instance;
    Screen screen = Screen.getPrimary();
    public static Stage allSatge;
    public Rectangle2D bounds = screen.getVisualBounds();
    public static void setInstance(App app) {
        instance = app;
    }
    public static App getInstance() {
        return instance;
    }
    @Override
    public void start(Stage primaryStage) throws IOException { 
    	setInstance(this);
    	primaryStage.setTitle("Full Screen Example");
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
        	jsonCredentials jsonCredential = new jsonCredentials();
        	if(jsonCredential.checkFolderFile()) {
        		JsonNode jsonNode = objectMapper.readTree(new File(jsonCredential.jsonPath));
        		String name =jsonNode.get("name").asText();
        		String pass = jsonNode.get("password").asText();
        		Login loginObject = Login.getInstance();
        		loginObject.checkCredentials(name, pass);
        		if(!loginObject.canLogin) {
        			System.out.println("Login Menu");
        			loginObject.loadFromLogin=true;
        			scene = new Scene(loadFXML("login"), bounds.getWidth(), bounds.getHeight());
        			primaryStage.setScene(scene);
        			this.mainStage = primaryStage;
        	        this.mainStage.show();
//        			showMainPage(bounds);
        		}else {
        			System.out.println("main application");
//        			this.mainStage = primaryStage;
        			this.mainStage = primaryStage;
        			showMainPage(bounds ,mainStage);
        			
        		}
        		
        		
        	}
        	
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        
        
//        this.allSatge = primaryStage;
        // this.mainStage = primaryStage;
//        Platform.setImplicitExit(false);
//        mainStage.setOnCloseRequest(this::handleCloseRequest);
        
//        SystemTray systemTray = SystemTray.get();
//        systemTray.setTooltip("System Tray Example");

        // Create a menu with items
//        getOngoingFXML ongoingObject = getOngoingFXML.getInstance();
//		FXMLLoader loader = ongoingObject.getFXMLfile();
//		Pane newPane = loader.load();
//		OngoingBackupController ongoingControllerObject = loader.getController();
//		ongoingControllerObject.addContents(1, "name", "back", "next");
		
		
       
//        FXMLLoader backupLoader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
//        Parent rootBackup = backupLoader.load();
//        FXMLLoader homePageLoader = new FXMLLoader(getClass().getResource("test.fxml"));
//        Parent homeLoader = homePageLoader.load();
//        homepage homeObject = homePageLoader.getController();
//        homepage backpath = backupLoader.getController();
//        System.out.println("Before assignment: backpath.allTask = " + backpath.allTask);
//        Parent ongoingRoot = backpath.OBloader.load();
//        OngoingBackupController onObj = backupLoader.getController();
//        onObj.fun(Uobject.taskList);
//        homeObject.OBloader = backupLoader;
//        onObj.allTask = Uobject.taskList;
//        System.out.println(onObj.allTask);
//        System.out.println("After assignment: backpath.allTask = " + backpath.allTask);
//        backpath.fun();
//        Uobject.getThisToFXMLFile();
        
//        if(backpath!= null) {
//        	backpath.setMainStage(mainStage);  
//        }else {
//        	System.out.println("back up path class is null");
//        }
        
        // primaryStage.show();
    }
    public static void showMainPage(Rectangle2D bounds ,Stage stage) {
    	FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("test.fxml"));
    	try {
    		scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
    		 Menu menu = new Menu();
    	        MenuItem item1 = new MenuItem("Exit");
    	        MenuItem item2 = new MenuItem("Open");
    	        item1.setCallback(e->{
    	        	System.out.println("exiting");
    	        	System.exit(0);
    	        });
    	        item2.setCallback(e ->{
    	        	Platform.runLater(()->{
    	        	      
    	        	        System.out.println("opening");
    	        	        mainStage.setFullScreen(false); 
    	        	        mainStage.toFront();
    	        	        mainStage.show();
    	        	        System.out.println("is opened");
    	        	      
    	        	    });
    	        	    
    	        });
    	        menu.add(item1);
    	        menu.add(item2);
//    	        systemTray.setImage(App.class.getResource("backup.png"));
//    	        systemTray.getMenu().add(menu);
    	        UploadBackup Uobject = UploadBackup.getInstance();
    	        Uobject.getDataFromJson();
    	        Uobject.runListOfBackup();
                // Stage stage = MainStageAcess();
    	        stage.setScene(scene);
    	        stage.setTitle("Do_BackUp");
                stage.show();
    	}catch(Exception e) {
    		
    	}
    }
    private void handleCloseRequest(WindowEvent event) {
        // Hide the stage instead of exiting the application
//        mainStage.hide();
        System.exit(0);
//        event.consume();
    }
    public static Stage MainStageAcess() {
    	return mainStage;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}