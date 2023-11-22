package org.openjfx.hellofx;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;



import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;



import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//import org.apache.http.entity.mime.content.InputStreamBody;

import javax.swing.SwingUtilities;


//import org.apache.http.entity.AbstractHttpEntity;
public class SendFilesToServer {
	private String filePath;
	private String fileName;
	public long bytesWritten;
	long currentTotalSize;
	long currentBytesWritten;
	private int currentStage;
	
//	int totalCompressPercentage;
	private int HboxIndex;
	getOngoingFXML getOngoingObject = getOngoingFXML.getInstance();
	OngoingBackupController ongoingBackupObject = getOngoingObject.ongoingObject;
	public ProgressFileBody progressfileBody;
	public SendFilesToServer(String filePath ,String fileName , int HboxIndex) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.HboxIndex = HboxIndex;
//		this.compressObject = compressObject;
	}
	
	public void StartProcdeure() {
		class MyRunable implements Runnable{
			private Compress_Files compressObject;
//			private CountDownLatch latch = new CountDownLatch(1);
			public MyRunable(Compress_Files compresObject) {
				this.compressObject = compresObject;
			}

			@Override
			public void run() {		
				
//		    	PBar.setProgress(progressPercentage);
				ScheduledExecutorService zipSchedule = Executors.newSingleThreadScheduledExecutor();
				zipSchedule.scheduleAtFixedRate(()->{
//					System.out.println("bytes Written  "+compressObject.bytesWritten);
//					System.out.println("total Size  "+compressObject.totalSize);
					long byW =compressObject.bytesWritten;
					long totalS =compressObject.totalSize;
					if(totalS!=0) {
						float totalCompressPercentage = ((float) byW / totalS) * 100;
						System.out.println("Extraction progress : "+totalCompressPercentage);
						Platform.runLater(()->{
							HBox thisHbox = ongoingBackupObject.hashmap.get(HboxIndex);
							VBox secondVbox = (VBox) thisHbox.getChildren().get(1);
							ProgressBar PBar = (ProgressBar) secondVbox.getChildren().get(2);
							Label stageStatus = (Label) secondVbox.getChildren().get(0);
							Label progressName = (Label) secondVbox.getChildren().get(1);
							stageStatus.setText("Stage 1 of 2");
							progressName.setText("Extracting..");					
							PBar.setProgress((float) totalCompressPercentage/100);
						});
						if (totalCompressPercentage>=100) {
							System.out.println("Extraction progress : "+totalCompressPercentage);
							zipSchedule.shutdown();
							
						}
						
					}
				} ,0 , 1 , TimeUnit.MILLISECONDS);				
			}			
		}
		if(getConnectionStatus()) {
			//Compress File
			
			Compress_Files compressObject = new Compress_Files(fileName ,filePath);
			try {
//				Runnable zipRunable = ()->{
//					ScheduledExecutorService zipSchedule = Executors.newSingleThreadScheduledExecutor();
//					zipSchedule.scheduleAtFixedRate(()->{
//						System.out.println("in schedule  "+compressObject.totalSize);
//						long byW =compressObject.bytesWritten;
//						long totalS =compressObject.totalSize;
//						if(totalS!=0) {
//							int totalCompressPercentage = (int)((double)(byW/totalS)*100);
//							System.out.println("Extraction progress : "+totalCompressPercentage);
//							if (totalCompressPercentage>=100) {
//								System.out.println("Extraction progress : "+totalCompressPercentage);
//								zipSchedule.shutdown();
//								
//							}
//							
//						}
//					} ,0 , 5 , TimeUnit.MILLISECONDS);							
//				};
//				
				MyRunable myrunable = new MyRunable(compressObject);
				Thread zipThread = new Thread(myrunable);
				
				zipThread.start();
//				Thread zipThread = new Thread(zipRunable , compressObject);				
//				Thread zipThread = new Thread(zipRunable);
				String compressFilePath = compressObject.createZipFile();
				System.out.println(compressObject.bytesWritten);
//				myrunable.latch.countDown();
				
				if(compressFilePath != "") {
					if(FileExists(compressFilePath)) {
						System.out.println("Path:  "+compressFilePath);		
						startSending(compressFilePath ,fileName);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//SendFiles
		}else {
			System.out.println("Server not Found");
		}		
	}
	public static void main(String[] args) {
//		SendFilesToServer hb =new SendFilesToServer("" ,"" ,1);
//		hb.StartProcdeure();
	}
	void startSending(String ZipFilePath ,String FileName) {
		if(FileExists(ZipFilePath)) {
			if(getConnectionStatus()) {
				//			Start Sending
				
				File ZipFile_ToSent = new File(ZipFilePath);
				
//				 ProgressTracker progressTracker = new ProgressHandler();
//				 ProgressFileEntity progressEntity = new ProgressFileEntity(file, ContentType.DEFAULT_BINARY, progressTracker);
				progressfileBody = new ProgressFileBody(ZipFile_ToSent ,ContentType.DEFAULT_BINARY ,this);
				
//				ProgressTracker progressTracker = percentage -> {
//		            System.out.println("percentage:  " + percentage);
//		        };
//				ProgressInputStreamEntity.ProgressTracker progressTracker = percentage -> {
//				    System.out.println("percentage:  " + percentage);
//				};

//				ProgressInputStreamEntity progressEntity = new ProgressInputStreamEntity(ZipFile_ToSent, ContentType.DEFAULT_BINARY, progressTracker);
//				ProgressFileEntity progressEntity = new ProgressFileEntity(
//						ZipFile_ToSent ,
//						ContentType.DEFAULT_BINARY ,
//						(uploaded ,total)->{
//							System.out.println(total);
//							 double progress = (double) uploaded / total * 100;
//			                    System.out.printf("Progress: %.2f%%\n", progress);
//						});
//				ContentType contentType = ContentType.create("application/octet-stream");
//				ProgressFileEntity progressFileBody = new ProgressFileEntity(
//				        Paths.get("your_file_path"),
//				        contentType,
//				        (uploaded, total) -> {
//				            double progress = (double) uploaded / total * 100;
//				            System.out.printf("Progress: %.2f%%\n", progress);
//				        });
//				FileBody fileBody = new FileBody(ZipFile_ToSent, ContentType.DEFAULT_BINARY) {
//	                @Override
//	                public void writeTo(OutputStream out) throws IOException {
//	                    super.writeTo(new ProgressOutputStream(out, ZipFile_ToSent.length()));
//	                }
//	            };
//				ProgressFileEntity fileBody = new ProgressFileEntity(ZipFile_ToSent, ContentType.DEFAULT_BINARY);
				
				
				ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
				Platform.runLater(()->{
					HBox thisHbox = ongoingBackupObject.hashmap.get(HboxIndex);
					VBox secondVbox = (VBox) thisHbox.getChildren().get(1);
					Label stageStatus = (Label) secondVbox.getChildren().get(0);
					Label progressName = (Label) secondVbox.getChildren().get(1);
					stageStatus.setText("Stage 2 of 2");
					progressName.setText("Uploading..");					
				});
				scheduler.scheduleAtFixedRate(() -> {
//					Platform.runLater(()->{
//						
//					});
					
				    // Calculate percentage
				    int percentage = (int) (((double) currentBytesWritten / currentTotalSize) * 100);
				    float progressPercentage = percentage/100;
//				    Platform.runLater(()->{}
				    try {
				    	Platform.runLater(()->{
				    		HBox ThisHbox = ongoingBackupObject.hashmap.get(HboxIndex);
							VBox SecondVbox = (VBox) ThisHbox.getChildren().get(1);
							ProgressBar PBar = (ProgressBar) SecondVbox.getChildren().get(2);
						    currentBytesWritten = progressfileBody.getBytes();
						    currentTotalSize = progressfileBody.getTotal();
						    PBar.setProgress(progressPercentage);
				    	});
				    	
				    }catch(Exception e) {
				    	System.out.println("unable to get box components");
				    }
//				    System.out.println("percentage: " + percentage);

				    if (percentage == 100) {
				        // Stop the scheduler after the upload is complete
				        scheduler.shutdown();
				    }

				}, 0, 4, TimeUnit.MILLISECONDS);
				HttpClient httpClient =  HttpClients.createDefault();
				HttpPost httpPost = new HttpPost("http://127.0.0.1:8000/sendData/");
				
				
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.addPart("file", progressfileBody);
//				builder.addBinaryBody("file", ZipFile_ToSent, ContentType.DEFAULT_BINARY, ZipFile_ToSent.getName(), progressEntity::writeTo);
//			builder.addPart("file",  new ProgressFileBody(progressEntity));
//	        builder.addBinaryBody("file", ZipFile_ToSent, ContentType.DEFAULT_BINARY, ZipFile_ToSent.getName());
				builder.addPart("name" ,new StringBody(FileName ,ContentType.TEXT_PLAIN));
				httpPost.setEntity(builder.build());
				try {
//					progressfileBody.resetProgress();
					HttpResponse response = httpClient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						Platform.runLater(()->{
							HBox thisHbox = ongoingBackupObject.hashmap.get(HboxIndex);
							VBox secondVbox = (VBox) thisHbox.getChildren().get(1);
							Label stageStatus = (Label) secondVbox.getChildren().get(0);
							Label progressName = (Label) secondVbox.getChildren().get(1);
							stageStatus.setText("Stage 2 of 2");
							progressName.setText("Uploaded..");					
							ProgressBar PBar = (ProgressBar) secondVbox.getChildren().get(2);
							secondVbox.getChildren().remove(PBar);
						});
//						thisHbox = ongoingBackupObject.hashmap.get(HboxIndex);
//				    	secondVbox = (VBox) thisHbox.getChildren().get(1);
//				    	stageStatus = (Label) secondVbox.getChildren().get(0);
//				    	progressName = (Label) secondVbox.getChildren().get(1);
//				    	stageStatus.setText("Stage 2 of 2");
//				    	progressName.setText("Uploaded..");
						System.out.println("File Uploaded");
					}
					scheduler.shutdown();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					scheduler.shutdownNow();
//					scheduler.shutdown();
				}
				
//				httpPost.setEntity(builder.build());
//				HttpResponse response;
//				try {
//					response = httpClient.execute(httpPost);
//					HttpEntity entity = response.getEntity();
//					if(entity != null) {
//						timeline.stop();
//						System.out.println("File Uploaded");
//					}
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
//				try (FileInputStream fileInputStream = new FileInputStream(ZipFile_ToSent)) {
//		            // Use InputStreamBody for the file part
//		            ContentBody filePart = new InputStreamBody(
//		                    fileInputStream,
//		                    ContentType.DEFAULT_BINARY,
//		                    ZipFile_ToSent.getName());
//		            
//		            // Continue building your request...
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
//				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//				
//				builder.addPart("file" ,progressEntity);
				
				
//				try {
//					
//				} catch (ClientProtocolException e) {
//					System.out.println("Couldnt upload");
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					System.out.println("Couldnt upload");
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}else {
				System.out.println("Server not Found");
			}
				
		}else {
			System.out.println("File not Found");
		}
	}
	
	public void uploadProgress(double totalPercent) {
		System.out.println((int)totalPercent);
	}
	
	boolean FileExists(String ZipfilePath) {
		File folder = new File(ZipfilePath);
		if(folder.exists()) {
			return true;
		}
		return false;
	}
	boolean getConnectionStatus() {
		String serverUrl = "http://127.0.0.1:8000/sendData/";
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpGet request = new HttpGet(serverUrl);
			HttpResponse httpResponse = httpClient.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode == 200) {
				System.out.println("connection can be made");
				return true;
			}else {
				System.out.println("cant connect to server");
				return false;
			}
			
		}catch(Exception e) {
			return false;
		}		
	}
}

