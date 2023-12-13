package org.openjfx.hellofx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.control.Label;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileDownloader {
	private String fileType;
	private String branchType;
	private String fileId;
	private String name;
	ProgressIndicator progressIndicator = new ProgressIndicator();
	Label progressText;
	public FileDownloader(String fileType , String branchType ,String fileId ,String name) {
		this.fileType=fileType;
		this.branchType=branchType;
		this.fileId=fileId;
		this.name =name;
		
	}
	public String getToken(HttpClient tempClient) {
//		HttpClient tempClient = HttpClients.createDefault();
		getServerUrl rootUrl = new getServerUrl();
		HttpPost tempPost = new HttpPost(rootUrl.getRootUrl()+"loginsite/");
		
		tempPost.setHeader("Content-Type" ,"application/json");
		JSONObject json = new JSONObject();
		jsonCredentials credObject = new jsonCredentials();
		String username = credObject.getUserName();
		String userpassword = credObject.getUserPassword();
		json.put("username", username);
		json.put("password", userpassword);
		
		StringEntity tempEntity;
		try {
			tempEntity = new StringEntity(json.toString());
			tempPost.setEntity(tempEntity);
			try {
				HttpResponse httpResponse = tempClient.execute(tempPost);
				HttpEntity entity = httpResponse.getEntity();
				String responseString = EntityUtils.toString(entity);
				JSONObject jObject = new JSONObject(responseString);
				
				String token = jObject.getString("token");
//				String user = jObject.getString("user");
				
				System.out.println("Token : "+token);
				return token;
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public void StartDownload() {
		
		Platform.runLater(()->{
			System.out.println("adding ui");
			HBox hbox = new HBox();
			
			hbox.setPrefWidth(300);
			hbox.setPrefHeight(40);
			hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.setStyle("-fx-border-color: black;");
			hbox.setStyle("-fx-border-width: 0px 0px 1px 0px;");
			
			VBox vbox = new VBox();
			Insets margin = new Insets(0, 0, 0, 20); // Insets(top, right, bottom, left)
		    hbox.setMargin(vbox ,margin);
			vbox.setAlignment(Pos.CENTER_LEFT);
			vbox.setPrefWidth(100);
			vbox.setPrefHeight(200);
			
			Label fileNameLabel = new Label();
			fileNameLabel.setStyle("-fx-text-fill: white;");
			fileNameLabel.setText(name);				
			progressText = new Label();
			progressText.setStyle("-fx-text-fill: white;");
			progressText.setText("downloading...");
			vbox.getChildren().addAll(fileNameLabel ,progressText);
			hbox.getChildren().addAll(progressIndicator ,vbox);
			getHomePageFXML homeInstance = getHomePageFXML.getInstance();
			homepage homePageObject = homeInstance.homepageController;
			homePageObject.downloadPage.getChildren().add(hbox);	
			System.out.println("added ui");
		});
		getServerUrl rootUrl = new getServerUrl();
		String serverUrl = rootUrl.getRootUrl()+"getData/";
		String downloadPath = System.getProperty("user.home")+"/Downloads/"+name+".zip";
//		String downloadPath = "C:\\Users\\Rahul\\Downloads\\downloaded_file.zip";
		
		HttpClient httpClient = HttpClients.createDefault();
		String UserToken = getToken(httpClient);
		if(!UserToken.equals("")) {
			HttpPost httpPost = new HttpPost(serverUrl);
			httpPost.setHeader("Authorization", "token "+UserToken);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			jsonCredentials getUserName = new jsonCredentials();
			String nameuser = getUserName.getUserName();
			builder.addTextBody("username", nameuser);
			builder.addPart("file" , new StringBody(fileType , ContentType.TEXT_PLAIN));
			builder.addPart("id" , new StringBody(fileId , ContentType.TEXT_PLAIN));
			builder.addPart("branchType" , new StringBody(branchType , ContentType.TEXT_PLAIN));
			httpPost.setEntity(builder.build());
			try {
				HttpResponse response = httpClient.execute(httpPost);
				String status = response.getFirstHeader("Status").getValue();
				if(status.equals("Yes")) {
					long totalSize = response.getFirstHeader("Content-Length") != null
							? Long.parseLong(response.getFirstHeader("Content-Length").getValue())
									: 0;
					Path finalDownloadPath = getPath(downloadPath ,name);
					DownloadProgressHandler progress = new DownloadProgressHandler(finalDownloadPath ,totalSize);
					ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
					
					
					scheduler.scheduleAtFixedRate(()->{
						progressPercentage = (int) (progress.progress);
//						System.out.println("progress:  "+progressPercentage);
//						Platform.runLater(()->{
							progressIndicator.setProgress(progressPercentage/100);							
//						});
						if(progressPercentage>=100) {
							Platform.runLater(()->{
								progressText.setText("Download Complete");						
							});
							System.out.println("Downloadeddddddd/....");
							scheduler.shutdown();
						}
					}, 0, 1 , TimeUnit.MICROSECONDS);
					HttpEntity entity = response.getEntity();
					progress.onNext(entity.getContent());					
				}else {
					System.out.println("File cannot be downloaded");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}		
		}else {
			System.out.println("Unable to retrieve user token");
		}
	}
	private Path getPath(String dPath, String fileName) {
		Path path = Paths.get(dPath);
		int counter = 1;
        while (Files.exists(path)) {
            String timeStamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
            String filename = fileName + "_" + timeStamp + "_" + counter+".zip";
            path = Paths.get(path.getParent().toString(), filename);
            counter++;
        }
        return path;
	}
	private static int progressPercentage;
}

class DownloadProgressHandler {
    private Path downloadPath;
    private long bytesDownloaded;
    private long totalSize; // You may need to obtain the total size from the server
    public double progress;

    public DownloadProgressHandler(Path downloadPath ,long totalSize) {
        this.downloadPath = downloadPath;
        this.totalSize=totalSize;
    }

    public void onNext(InputStream inputStream) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(downloadPath.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // Update progress
                bytesDownloaded += bytesRead;
                progress = ((double) bytesDownloaded / totalSize) * 100;
                System.out.printf("Download Progress: %.2f%%\n", progress);

                // Write to the output stream
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}