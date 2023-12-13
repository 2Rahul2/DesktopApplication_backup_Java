package org.openjfx.hellofx;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Login {
	public boolean loadFromLogin = false;
	private static Login instance = new Login();
	public boolean canLogin = false;
	public Stage currentStage;
	private Login() {
	}
	public static Login getInstance() {
		return instance;
	}
	public void checkCredentials(String name ,String password) {
		HttpClient httpClient = HttpClients.createDefault();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart("username",new StringBody(name ,ContentType.TEXT_PLAIN));
		builder.addPart("password" ,new StringBody(password ,ContentType.TEXT_PLAIN));
		getServerUrl rootUrl = new getServerUrl();
		HttpPost httpPost = new HttpPost(rootUrl.getRootUrl()+"loginsite/");
		httpPost.setEntity(builder.build());
		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity(); 
			String content = EntityUtils.toString(entity).trim();
			System.out.println("Content:    "+content);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode node = objectMapper.readTree(content);
			String result = node.get("detail").asText();
			System.out.println(content);
//			int statusCode = response.getStatusLine().getStatusCode();
//			HttpEntity entity = response.getEntity();
			if (result.equals("yes")) {
				jsonCredentials credentialObject = new jsonCredentials(name ,password);
				credentialObject.StoreCredentials();
				System.out.println("Logged in Successfull");
				canLogin=true;
				
				if(loadFromLogin) {
					System.out.println("CREATING A NEW STAGEE INSIDE HERE~~~~~~~~~~~~~~~~~~~");
					App app = App.getInstance();
//					app.mainStage.hide();
					try {
						currentStage.close();						
					}catch(Exception e){
						
					}
					Stage stage = new Stage();
//					stage = app.showMainPage(app.bounds ,stage ,app.internetConnection);
					app.AfterLoginProcess(stage);
//					Screen screen = Screen.getPrimary();
//					Rectangle2D bounds = screen.getVisualBounds();
//					Parent root = FXMLLoader.load(Login.class.getResource("test.fxml"));
//					Stage secondStage = new Stage();
					
//					SystemTray systemTray = SystemTray.get();
//			        systemTray.setTooltip("System Tray Example");
//			        Platform.setImplicitExit(false);
			        
//					secondStage.setTitle("Second Stage");
//					secondStage.setX(bounds.getMinX());
//					secondStage.setY(bounds.getMinY());
//					secondStage.setWidth(bounds.getWidth());
//					secondStage.setHeight(bounds.getHeight());
					
					
//					Menu menu = new Menu();
//					MenuItem item1 = new MenuItem("Exit");
//					MenuItem item2 = new MenuItem("Open");
//					item1.setCallback(e->{
//						System.out.println("exiting");
//						System.exit(0);
//					});
//					item2.setCallback(e ->{
//						Platform.runLater(()->{
//							
//							System.out.println("opening");
//							secondStage.setFullScreen(false); 
//							secondStage.toFront();
//							secondStage.show();
//							System.out.println("is opened");
//							
//						});
//						
//					});
//					menu.add(item1);
//					menu.add(item2);
//    	        systemTray.setImage(App.class.getResource("backup.png"));
//    	        systemTray.getMenu().add(menu);
//					UploadBackup Uobject = UploadBackup.getInstance();
//					Uobject.getDataFromJson();
//					Uobject.runListOfBackup();
//					secondStage.setOnCloseRequest();
					
					
//					secondStage.getIcons().add(new Image(getClass().getResourceAsStream("upload.png")));
//					secondStage.setScene(new Scene(root, bounds.getWidth(), bounds.getHeight()));
//					secondStage.setMaximized(true);
//					secondStage.show();
//					App.getInstance().mainStage.hide();
					
				}
//				App.getInstance().showMainPage(App.getInstance().bounds ,App.getInstance().allSatge);
			}else {
				canLogin=false;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			canLogin = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			canLogin = false;
		}
		
		
	}
	
}
