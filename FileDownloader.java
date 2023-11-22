package org.openjfx.hellofx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	public FileDownloader(String fileType , String branchType ,String fileId ,String name) {
		this.fileType=fileType;
		this.branchType=branchType;
		this.fileId=fileId;
		this.name =name;
		
	}
	public void StartDownload() {
		String serverUrl = "http://127.0.0.1:8000/getData/";
		String downloadPath = System.getProperty("user.home")+"/Downloads/"+name+".zip";
//		String downloadPath = "C:\\Users\\Rahul\\Downloads\\downloaded_file.zip";
		
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(serverUrl);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart("file" , new StringBody(fileType , ContentType.TEXT_PLAIN));
		builder.addPart("id" , new StringBody(fileId , ContentType.TEXT_PLAIN));
		builder.addPart("branchType" , new StringBody(branchType , ContentType.TEXT_PLAIN));
		httpPost.setEntity(builder.build());
		try {
			HttpResponse response = httpClient.execute(httpPost);
			long totalSize = response.getFirstHeader("Content-Length") != null
					? Long.parseLong(response.getFirstHeader("Content-Length").getValue())
							: 0;
			Path finalDownloadPath = getPath(downloadPath ,name);
			DownloadProgressHandler progress = new DownloadProgressHandler(finalDownloadPath ,totalSize);
			ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleAtFixedRate(()->{
				progressPercentage = (int) (progress.progress);
				System.out.println("progress:  "+progressPercentage);
				if(progressPercentage>=100) {
					scheduler.shutdown();
				}
			}, 0, 5 , TimeUnit.MILLISECONDS);
			HttpEntity entity = response.getEntity();
			progress.onNext(entity.getContent());
		}catch(Exception e) {
			e.printStackTrace();
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
//                System.out.printf("Download Progress: %.2f%%\n", progress);

                // Write to the output stream
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}