package org.openjfx.hellofx;
import org.openjfx.hellofx.JsonToObject;
import org.openjfx.hellofx.FileExplorer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.LinkedList;
import java.util.List;
public class windowFile implements Initializable  {
	@FXML
	private VBox leftMenuTree;
	@FXML
	private VBox mainFolderPage;
	@FXML 
	private Button forwardButton;
	@FXML
	private Button backButton;
	@FXML
	private ImageView forwardimage;
	@FXML
	private ImageView backimage;
	
	
	public LinkedList<FileExplorer> previousFolder = new LinkedList<>();
	public LinkedList<FileExplorer> forwardFolder = new LinkedList<>();
	public FileExplorer CurrentFile;
	public int listIndex;
	public String rootFolderName;
	
	public void forwardPage() {
		if(!forwardFolder.isEmpty()) {
			previousFolder.add(CurrentFile);
			createFolderPannel(forwardFolder.getFirst());
			forwardFolder.remove(forwardFolder.getFirst());			
		}else {
			System.out.println("Cant go forward!");
		}
		checkForwardPage();
		checkPreviousPage();
	}
	
	void checkForwardPage() {
		if(forwardFolder.isEmpty()) {
			forwardimage.setOpacity(0.25);
		}else {
			forwardimage.setOpacity(1);
		}
	}
	
	void checkPreviousPage() {
		if(previousFolder.isEmpty()) {
			backimage.setOpacity(0.25);
		}else {
			backimage.setOpacity(1);
		}
	}
	
	public void previousPage() {
		if(!previousFolder.isEmpty()) {
			forwardFolder.addFirst(CurrentFile);
			createFolderPannel(previousFolder.getLast());
			previousFolder.remove(previousFolder.getLast());			
		}else {
			System.out.println("cant go back!");
		}
		checkPreviousPage();
		checkForwardPage();
	}
	
	
//	public static void main(String[] args) {
//        launch(args);
//    }

//        for(int i=0;i<2;i++) {
//        	TreeItem<String> treeViewItem = new TreeItem<>("Secondary Root");
//        	TreeView<String> treeViewTwo = new TreeView<>(treeViewItem);
//        	treeViewItem.getChildren().addAll(
//        			new TreeItem<>("second Item 1")
//        			);
//        	String name = "random";
//        	rootItem.getChildren().addAll(
//                    new TreeItem<>(name),
//                    new TreeItem<>("Item 2"),
//                    treeViewItem
//                    
//                );
//        }
        

//        TreeItem<String> treeViewItem2 = new TreeItem<>("third root");
//        TreeView<String> treeViewThree = new TreeView<>(treeViewItem2);
//        treeViewItem2.getChildren().addAll(
//        		new TreeItem<>("third sub item"),
//        		new TreeItem<>("third fourth item")
//        		);
        
//        treeView.setStyle("-fx-indent: -10;");
//        treeViewTwo.setStyle("-fx-indent: 0;");
//        treeViewThree.setStyle("-fx-indent: 0;");

        
        
//        Scene scene = new Scene(root, 300, 250);
//        primaryStage.setScene(scene);
//        scene.getStylesheets().add(getClass().getResource("treeview.css").toExternalForm());
//        primaryStage.show();
	public class customFolderButton extends Button{
		public customFolderButton(String sno , String folderName ,String size ,String id ,String isFolder ,String Branch) {
			final String fileId = id;
			final String branchType = Branch;
			final String fileType = isFolder;
			final String Fname = folderName; 
			
			final String foldername = folderName;
			HBox rootHbox = new HBox();
			rootHbox.setPrefHeight(35);
			rootHbox.setPrefWidth(540);
			
			Label Sno = new Label();
			Sno.setPrefHeight(130);
			Sno.setPrefWidth(30);
			Sno.setText(sno);
			
			Label name = new Label();
			name.setPrefHeight(130);
			name.setPrefWidth(195);
			name.setText(folderName);
			
			Label Size = new Label();
			Size.setPrefHeight(130);
			Size.setPrefWidth(167);
			Size.setText(size);
			
			Button downloadBtn = new Button();
			downloadBtn.setPrefHeight(95);
			downloadBtn.setPrefWidth(154);
			downloadBtn.setText("download");
			downloadBtn.setOnAction(e->{
				FileDownloader filedownload = new FileDownloader(fileType ,branchType ,fileId , Fname);
				filedownload.StartDownload();
				System.out.println("download clicked : file id: "+fileId+" branch:"+Branch+" File Type: "+fileType);
//				System.out.println("downlad Button:  "+foldername+"Id:  "+id);
				e.consume();
			});
			
			rootHbox.getChildren().addAll(Sno ,name ,Size ,downloadBtn);
			setGraphic(rootHbox);
			
		}
	}
	public void createFolderPannel(FileExplorer FE) {
		mainFolderPage.getChildren().clear();
		CurrentFile = FE;
		int count = 0;
		for(int i = 0;i<FE.fileexplorer.size();i++) {
			final int indexPosition = i;
			count++;
			customFolderButton folderBtn = new customFolderButton(String.valueOf(count) ,FE.fileexplorer.get(i).FileName ,"2525" ,FE.fileexplorer.get(i).FileId ,"folder", "subBranch");
			folderBtn.setStyle("-fx-background-color:  #968fa8;");
//			folderBtn.setStyle("-fx-border-width: 2px 2px 2px 0px");
			folderBtn.setMaxWidth(Double.MAX_VALUE);
			folderBtn.setPrefHeight(Region.USE_COMPUTED_SIZE);
			folderBtn.setOnAction(e->{
				listIndex++;
				previousFolder.add(CurrentFile);
				createFolderPannel(FE.fileexplorer.get(indexPosition));
			});
			mainFolderPage.getChildren().add(folderBtn);			
		}
		for(int j =0;j<FE.subFile.size();j++) {
			count++;
			customFolderButton fileBtn = new customFolderButton(String.valueOf(count) ,FE.subFile.get(j).FileName ,"2323",FE.subFile.get(j).FileId ,"file" ,"file");
			fileBtn.setStyle("-fx-background-color:  #968fa8;");
			fileBtn.setMaxWidth(Double.MAX_VALUE);
			fileBtn.setPrefHeight(Region.USE_COMPUTED_SIZE);
			mainFolderPage.getChildren().add(fileBtn);
		}
		
		
	}
	public void createTreeView(FileExplorer FE) {
		JsonToObject object;
        try {
			object = new JsonToObject();
//			List<FileExplorer> listOfExplorer = object.listOfFileExplorer;
//			System.out.println(listOfExplorer.get(1).FileName);
//			System.out.println(listOfExplorer);
			TreeItem<String> rootItem = new TreeItem<>(FE.FileName);
			
			TreeView<String> treeView = new TreeView<>(rootItem);
			
			treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
				@Override
				public TreeCell<String> call(TreeView<String> param) {
					return new CustomTreeCell();
				}
			});
			
			traverseFolder(FE ,rootItem);
			leftMenuTree.getChildren().add(treeView);
//			for(int i=0;i< listOfExplorer.size()-1 ;i++) {
				
//		        root.getChildren().add(treeView);
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
	
	public void traverseFolder(FileExplorer FE ,TreeItem<String> RI) {
		System.out.println(FE.FileName);
		if(FE.fileexplorer.size()!=0) {
			for(int j=0;j<FE.fileexplorer.size();j++) {
				String name = FE.fileexplorer.get(j).FileName;
				TreeItem<String> TVItem = new TreeItem<>(name);
//				TreeView<String> TV = new TreeView<>(TVItem);
				RI.getChildren().add(TVItem);
				traverseFolder(FE.fileexplorer.get(j) ,TVItem);
			}
		}
		if(FE.subFile.size()!=0) {
			for(int j =0;j<FE.subFile.size();j++) {
				String name = FE.subFile.get(j).FileName;
				TreeItem<String> TVItem = new TreeItem<>(name);
//				TreeView<String> TV = new TreeView<>(TVItem);
				RI.getChildren().add(TVItem);
				System.out.println(FE.subFile.get(j).FileName);
			}
		}
	}
	
	public class CustomTreeCell extends TreeCell<String> {
        private final Button button = new Button("Click Me");

        public CustomTreeCell() {
            button.setOnMouseClicked(event -> {
                // Handle button click
            	if (event.getClickCount() == 2) {
            		System.out.println("Button clicked for: " + getItem());
            	}
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
                setText(null);
            } else {
            	button.setText(item);
//                setText(item);
                setGraphic(button);
            }
            
        }
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
	


	

