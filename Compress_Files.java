
package org.openjfx.hellofx;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
//import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class Compress_Files {
//	static String userHome = System.getProperty("user.home");
	static String userHome = "C:\\Users\\Rahul";
	static String desktopPath = Paths.get(userHome, "Desktop").toString()+"\\sendFile";
	public String tempDirPath = System.getProperty("java.io.tmpdir");
	public String SourceFolderPath;
	public String zipFileName;
	private String zipFilePath;
	public volatile long bytesWritten;
	public volatile long totalSize;
	
	public Compress_Files(String folderName , String folderPath) {
		this.zipFileName = folderName+".zip";
		this.SourceFolderPath = folderPath;
		this.zipFilePath = tempDirPath+zipFileName;
		File fileSize = new File(SourceFolderPath);
		if(fileSize.exists()) {
			totalSize =  fileSize.length();
		}
	}
	public static void main(String[] args) throws FileNotFoundException , IOException {
//		String tempDirPath = System.getProperty("java.io.tmpdir");
//        String SourceFolder = "C:\\Users\\Rahul\\Desktop\\sendFiles";
        
//        String zipFileName = new File(SourceFolder).getName() ;
//        String zipFilePath = tempDirPath + zipFileName+".zip";

//        System.out.println("Storage path + "+zipFilePath);
//        createZipFile(zipFilePath , SourceFolder);
//        System.out.println("done creating zipping!");
//		compressFile_getPath(userHome+"\\sendFiles" ,desktopPath);
	}
	public String createZipFile() throws FileNotFoundException, IOException {
//		File fileSize = new File(SourceFolderPath);
		try (Stream<Path> walk = Files.walk(Paths.get(SourceFolderPath))) {
	        totalSize = walk
	            .filter(Files::isRegularFile)
	            .mapToLong(path -> {
	                try {
	                    return Files.size(path);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    return 0;
	                }
	            })
	            .sum();
	    }
		System.out.println("Initial SIZE: " + totalSize);
		try(ZipArchiveOutputStream zipOutPutStream = new ZipArchiveOutputStream(new FileOutputStream(zipFilePath))){
			try(Stream<Path> walk = Files.walk(Paths.get(SourceFolderPath))){
				walk.forEach(path -> {
					if(Files.isRegularFile(path)) {
						try {
							addFileToZip(zipOutPutStream ,SourceFolderPath ,path.toString());
						}catch(IOException e){
							
						}
					}
				});
			}
		}catch(Exception e) {
			return "";
		}
		return zipFilePath;
	}
	
	private void addFileToZip(ZipArchiveOutputStream zipOutputStream ,String SourceFolderPath ,String filePath) throws IOException {
		String relativePath = Paths.get(SourceFolderPath).relativize(Paths.get(filePath)).toString();
		ZipArchiveEntry entry = new ZipArchiveEntry(new File(filePath) ,relativePath);
		zipOutputStream.putArchiveEntry(entry);
		
		try(InputStream inputStream = new FileInputStream(filePath)) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while((bytesRead = inputStream.read(buffer)) != -1) {
				zipOutputStream.write(buffer ,0 ,bytesRead);
				
					bytesWritten += bytesRead;
					
				
			}		
		}
		
//		totalSize = new File(zipFilePath).length();
		zipOutputStream.closeArchiveEntry();
	}
}
