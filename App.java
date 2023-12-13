package org.openjfx.hellofx;

//import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Menu;
import javafx.scene.Group;


import javafx.application.Platform;

//import java.awt.Toolkit;
//import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dorkbox.systemTray.SystemTray;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;




/**
 * JavaFX App
 */
public class App extends Application {

    private  Scene scene;
    public Stage menuStage = new Stage();
    public  Stage mainStage;
    private static App instance;
    Screen screen = Screen.getPrimary();
    public static Stage allSatge;
    public boolean deadCameBacktoalive = false , internetConnection , alternateisPaused=false;
    public Rectangle2D bounds = screen.getVisualBounds();
    public static void setInstance(App app) {
        instance = app;
    }
    public static App getInstance() {
        return instance;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
    	primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("upload.png")));
    	setInstance(this);
    	checkforInternet();
//    	checkforInternet();
    	ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(()->{
        	checkforInternet();
        }, 0, 5, TimeUnit.SECONDS);
    	primaryStage.setTitle("Updata");
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
        			loadLoginPage(primaryStage);
//        			showMainPage(bounds);
        		}else {
        			System.out.println("main application");
//        			this.mainStage = primaryStage;
        			mainStage = primaryStage;
//        			mainStage = showMainPage(bounds ,mainStage ,internetConnection);      
        			AfterLoginProcess(mainStage);
        		}
        	}
        	
        }catch(Exception e) {
        	System.out.println("Error");
        	e.printStackTrace();
        }
        
	        
	        
    }
    public void AfterLoginProcess(Stage stage) {
    	try {
    		Login login = Login.getInstance();
        	
    		Rectangle2D bounds = screen.getVisualBounds();
    		stage.setX(bounds.getMinX());
    		stage.setY(bounds.getMinY());
    		stage.setWidth(bounds.getWidth());
    		stage.setHeight(bounds.getHeight());
//    		scene = new Scene(mainPane, bounds.getWidth(), bounds.getHeight());
//    		scene = new Scene(mainPane,bounds.getWidth(),bounds.getHeight()); // Initialize the scene with a size

    		getHomePageFXML gethomeFXML = getHomePageFXML.getInstance();
    		homepage homeloader = gethomeFXML.getController();
    		BorderPane mainPane = homeloader.mainPane;
    		mainPane.setPrefSize(bounds.getWidth(), bounds.getHeight());

    		Group root = new Group(mainPane);

    		// Create the Scene using the Group
    		Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
//            Group root = (Group) scene.getRoot();
//            root.getChildren().add(mainPane);

    	        stage.setScene(scene);
    	        stage.setMaximized(true);
    	        
    	        stage.getIcons().add(new Image(getClass().getResourceAsStream("upload.png")));
    	    	stage.setTitle("Updata");
    	    	if(internetConnection) {
    	        	jsonSettings jsonSetting = new jsonSettings();
    	        	if(jsonSetting.getbackupatlaunch()) {
    	        		UploadBackup Uobject = UploadBackup.getInstance();
    	        		Uobject.getDataFromJson();
    	        		Uobject.runListOfBackup();    	        		        		
    	        	}
    	        }
    	    	System.out.println("SHOWING MY STAGEEEEEEEEEEEEEE~~~~~~~~~~~~~~");
    	    	stage.show();
    	    	
    	    Platform.setImplicitExit(false);
    	        SystemTray systemTray = SystemTray.get();
    	    	systemTray.setImage(App.class.getResource("upload.png"));
    	    	
    			 Menu menu = new Menu();
    		        MenuItem exitMenu = new MenuItem("Exit");
    		        MenuItem openMenu = new MenuItem("Open");
    		        MenuItem stopAllBackup = new MenuItem("Stop Backup");
    		        if(alternateisPaused) {
    		        	stopAllBackup.setText("Resume Backup");
    		        }else {
    		        	stopAllBackup.setText("Stop Backup");
    		        }
    		        stopAllBackup.setCallback(e->{
    		        	alternateisPaused = !alternateisPaused;
    		        	getHomePageFXML homeFxml = getHomePageFXML.getInstance();
    		        	homepage homeObject = homeFxml.homepageController;
    		        	if(alternateisPaused) {
    		        		homeObject.StopAllBackupFunction();
    		        		stopAllBackup.setText("Resume Backup");
    		        	}else {
    		        		homeObject.ResumeAllBackupFunction();
    		        		stopAllBackup.setText("Stop Backup");
    		        		
    		        	}
    		        });
    		        exitMenu.setCallback(e->{
    		        	System.out.println("exiting");
    		        	Platform.exit();
    		        	System.exit(0);
    		        });
    		        openMenu.setCallback(e ->{
    		        	Platform.runLater(()->{
    		        	        System.out.println("opening");
    		        	        stage.show();
    		        	        System.out.println("is opened");
    		        	        stage.setMaximized(true); 
    		        	        stage.toFront();    
    		        	    });
    		        });
    		        menu.add(openMenu);
    		        menu.add(exitMenu);
    		        menu.add(stopAllBackup);
    		        
    		        systemTray.getMenu().add(menu);
    		        stage.setOnCloseRequest(this::handleCloseRequest);
    		        login.currentStage=stage;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    public void loadLoginPage(Stage Stage) {
    	try {
    		Login loginObject = Login.getInstance();
    		loginObject.loadFromLogin=true;
			Scene scene = new Scene(loadFXML("login"), bounds.getWidth(), bounds.getHeight());
			Stage.setScene(scene);
			Stage loginStage = new Stage();
			loginStage = Stage;
			loginStage.setWidth(650);
			loginStage.setHeight(430);
			loginStage.setAlwaysOnTop(true);
			loginStage.setX(bounds.getMinX());
			loginStage.setY(bounds.getMinY());
			loginStage.show();
			Login login = Login.getInstance();
			login.currentStage=loginStage;
			login.currentStage.setOnCloseRequest(e->{
				closeAlways(e);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void checkforInternet() {
    	try {
        	CloseableHttpClient netCheckClient = HttpClients.createDefault();
        	HttpGet getClient = new HttpGet("https://www.google.com");
        	HttpResponse response = netCheckClient.execute(getClient);
        	getHomePageFXML homeFxml = getHomePageFXML.getInstance();
        	homepage homePage = homeFxml.homepageController;
        	if(response == null) {
        		System.out.println("no internet !!");
        		homePage.internet.setVisible(true);
        		homePage.StopAllBackupFunction();
        		deadCameBacktoalive=true;
        		internetConnection=false;
        	}else {
        		System.out.println("yesss  internet !!" +deadCameBacktoalive);
        		if(deadCameBacktoalive) {
        			Platform.runLater(()->{
        				homePage.internet.setVisible(false);
        			});
        			homePage.ResumeAllBackupFunction();
        			deadCameBacktoalive=false;
        		}
        		internetConnection=true;

        	}
        }catch(Exception e) {
        	System.out.println("no internet !!");
        	getHomePageFXML homeFxml = getHomePageFXML.getInstance();
        	homepage homePage = homeFxml.homepageController;
        	Platform.runLater(()->{
        		homePage.internet.setVisible(true);
        		homePage.StopAllBackupFunction();            		
        	});
    		deadCameBacktoalive=true;
    		internetConnection=false;
//        	internetConnection=false;
//        	e.printStackTrace();
        }
    }
    public Stage showMainPage(Rectangle2D bounds ,Stage stage ,boolean internetConnection) {
    	
    	getHomePageFXML gethomeFXML = getHomePageFXML.getInstance();
    	homepage homeloader = gethomeFXML.homepageController;
    	BorderPane mainPane = homeloader.mainPane;
    	
    	
//    	FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("test.fxml"));
//    	gethomeFXML.homeLoader = fxmlLoader.getController();
    	try {
    		scene = new Scene(mainPane, bounds.getWidth(), bounds.getHeight());	  
    	        stage.setScene(scene);
    	        stage.setMaximized(true);
//    	        stage.setTitle("Do_BackUp");
//                stage.show();
    	        return stage;
    	}catch(Exception e) {
    		return stage;
    	}
    }
    private void closeAlways(WindowEvent event) {
    	Platform.setImplicitExit(true);
		Platform.exit();
		System.exit(0);
		event.consume();
    }
    private void handleCloseRequest(WindowEvent event) {
    	jsonSettings jsonSetting = new jsonSettings();
    	if(!jsonSetting.getruninbackgroundvalue()) {
    		Platform.setImplicitExit(true);
    		Platform.exit();
    		System.exit(0);
    	}else {
    		Login getStage = Login.getInstance();
    		
    		getStage.currentStage.hide();    		
    	}
    	event.consume();
        // Hide the stage instead of exiting the application
//        System.exit(0);
    }
    public void MainStageDisable() {
//    	return mainStage;
    	Login login = Login.getInstance();
    	Node sceneRoot = login.currentStage.getScene().getRoot();
    	sceneRoot.setDisable(true);
    }
    public void MainStageEnable() {
    	Login login = Login.getInstance();
    	Node sceneRoot = login.currentStage.getScene().getRoot();
    	sceneRoot.setDisable(false);
    }

//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}