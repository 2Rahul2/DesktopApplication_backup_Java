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

import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Login {
	public boolean loadFromLogin = false;
	private static Login instance = new Login();
	public boolean canLogin = false;
	private Login() {
	}
	public static Login getInstance() {
		return instance;
	}
	public void checkCredentials(String name ,String password) {
		HttpClient httpClient = HttpClients.createDefault();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart("name",new StringBody(name ,ContentType.TEXT_PLAIN));
		builder.addPart("password" ,new StringBody(password ,ContentType.TEXT_PLAIN));
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8000/checkCredential/");
		httpPost.setEntity(builder.build());
		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity(); 
			String content = EntityUtils.toString(entity);
			System.out.println(content);
//			int statusCode = response.getStatusLine().getStatusCode();
//			HttpEntity entity = response.getEntity();
			if (content.equals("okay")) {
				jsonCredentials credentialObject = new jsonCredentials(name ,password);
				credentialObject.StoreCredentials();
				System.out.println("Logged in Successfull");
				canLogin=true;
				
				if(loadFromLogin) {					
					Screen screen = Screen.getPrimary();
					Rectangle2D bounds = screen.getVisualBounds();
					Parent root = FXMLLoader.load(Login.class.getResource("test.fxml"));
					Stage secondStage = new Stage();
					
					SystemTray systemTray = SystemTray.get();
			        systemTray.setTooltip("System Tray Example");
			        Platform.setImplicitExit(false);
			        
					secondStage.setTitle("Second Stage");
					secondStage.setX(bounds.getMinX());
					secondStage.setY(bounds.getMinY());
					secondStage.setWidth(bounds.getWidth());
					secondStage.setHeight(bounds.getHeight());
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
							secondStage.setFullScreen(false); 
							secondStage.toFront();
							secondStage.show();
							System.out.println("is opened");
							
						});
						
					});
					menu.add(item1);
					menu.add(item2);
    	        systemTray.setImage(App.class.getResource("backup.png"));
    	        systemTray.getMenu().add(menu);
					UploadBackup Uobject = UploadBackup.getInstance();
					Uobject.getDataFromJson();
					Uobject.runListOfBackup();
//					secondStage.setOnCloseRequest();
					secondStage.setScene(new Scene(root, bounds.getWidth(), bounds.getHeight()));
					secondStage.show();
					App.getInstance().mainStage.hide();
					
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
