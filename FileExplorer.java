package org.openjfx.hellofx;

import java.util.ArrayList;
import java.util.List;


public class FileExplorer {
//	private List<FileExplorer> node = new ArrayList<>();

	String FileName;
	String FileId;
	public List<FileExplorer> fileexplorer = new ArrayList<>();
	public List<FileInsides> subFile = new ArrayList<>();
	public FileExplorer(String FileName , String FileId) {
		this.FileName = FileName;
		this.FileId = FileId;
		
	}
	
	public void addFile(String FileName , String FileId ,FileExplorer FE) {
		FileInsides fileinside = new FileInsides(FileName ,FileId);
//		fileinside.FileName = FileName;
//		fileinside.FileId = FileId;
		FE.subFile.add(fileinside);
//		System.out.println(FileName);
	}
	public void addFolder(String FolderName ,String FolderId) {
		FileExplorer fileExplorer = new FileExplorer(FolderName ,FolderId);
//		this.fileexplorer.add(fileExplorer);
//		System.out.println(FolderName);
	}
	
	
}
class FileInsides{
	String FileName;
	String FileId;
	public FileInsides(String FileName ,String FileId) {
		this.FileName = FileName;
		this.FileId = FileId;
	}
}

