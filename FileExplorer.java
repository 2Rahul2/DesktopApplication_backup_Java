package org.openjfx.hellofx;

import java.util.ArrayList;
import java.util.List;


public class FileExplorer {
//	private List<FileExplorer> node = new ArrayList<>();

	String FileName;
	String FileId;
	String FileDate;
	String FileSize;
	public List<FileExplorer> fileexplorer = new ArrayList<>();
	public List<FileInsides> subFile = new ArrayList<>();
	public FileExplorer(String FileName , String FileId ,String FileDate ,String FileSize) {
		this.FileName = FileName;
		this.FileId = FileId;
		this.FileDate=FileDate;
		this.FileSize=FileSize;
		
	}
	
	public void addFile(String FileName , String FileId ,FileExplorer FE ,String FileDate ,String FileSize) {
		FileInsides fileinside = new FileInsides(FileName ,FileId , FileDate ,FileSize);
//		fileinside.FileName = FileName;
//		fileinside.FileId = FileId;
		FE.subFile.add(fileinside);
//		System.out.println(FileName);
	}
	public void addFolder(String FolderName ,String FolderId ,String FileDate ,String FileSize) {
		FileExplorer fileExplorer = new FileExplorer(FolderName ,FolderId ,FileDate ,FileSize);
//		this.fileexplorer.add(fileExplorer);
//		System.out.println(FolderName);
	}
	
	
}
class FileInsides{
	String FileName;
	String FileId;
	String FileDate;
	String FileSize;
	public FileInsides(String FileName ,String FileId ,String FileDate ,String FileSize) {
		this.FileName = FileName;
		this.FileId = FileId;
		this.FileDate=FileDate;
		this.FileSize=FileSize;
	}
}

