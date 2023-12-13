package org.openjfx.hellofx;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
public class UploadBackup {
	public String Name;
	public HashMap<ScheduledFuture<?> , BackUpTask> taskMap = new HashMap<>();
//	public ScheduledExecutorService schedule =  Executors.newScheduledThreadPool(1);
	ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();

	public List<ScheduledFuture<?>> scheduledTask = new ArrayList<>();
	public List<BackUpTask> taskList = new ArrayList<>();
	public List<BackUpTask> ScheduledBackupTaskList = new ArrayList<>();
	public List<BackUpTask> runningBackupList = new ArrayList<>();
	private int HboxIndex = 0;
	private UploadBackup() {
    }
	private static UploadBackup instance = new UploadBackup();
	public static UploadBackup getInstance() {
        return instance;
    }
	public static void main(String[] args) {	
		
	}
	public void PauseAllBackup() {
		for(int i = 0;i<scheduledTask.size();i++) {
			cancelTask(i);
//			make sure the internet is detected
		}
	}
	public void PauseFutureBackup() {
		for(ScheduledFuture<?> previousSchedule: scheduledTask) {
			previousSchedule.cancel(true);
		}
		System.out.println("Paused");
	}
	public void ResumeBackup() {
		scheduledTask.clear();
		runListOfBackup();
	}
//	public void getThisToFXMLFile() {
//		FXMLLoader backupLoader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
//		try {
//			Parent rootBackup = backupLoader.load();
//			OngoingBackupController backupObject = backupLoader.getController();
//			backupObject.uploadObject = this;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	public void getDataFromJson() {
		StorePathInJson object = new StorePathInJson();
		JsonNode rootNode = object.getJsonData();
		if(rootNode != null) {
			for(JsonNode node: rootNode) {
				BackUpTask backupTask = new BackUpTask(node.get("name").asText() ,node.get("path").asText() ,node.get("totalMinutes").asInt());
				taskList.add(backupTask);
			}			
		}
	}
//	Method to run at start of application;
	public void runListOfBackup() {
//		for(BackUpTask task: taskList) {
//			try {
//				ScheduledFuture<?> scheduleFuture = schedule.scheduleAtFixedRate(task::runBackUp, 0, task.getInterval() , TimeUnit.MINUTES);
//				scheduledTask.add(scheduleFuture);
////				scheduleFuture.
//				taskMap.put(scheduleFuture, task);
//				ScheduledBackupTaskList.add(task);
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
		if(taskList.size()!=0) {
			ExecutorService executorService = Executors.newFixedThreadPool(taskList.size());
			
			for (BackUpTask task : taskList) {
				ScheduledFuture<?> future = schedule.scheduleAtFixedRate(() -> executorService.submit(task::runBackUp),
						0, task.getInterval(), TimeUnit.MINUTES);
				scheduledTask.add(future);
				taskMap.put(future, task);
				ScheduledBackupTaskList.add(task);
			}			
		}else {
			System.out.println("size is not empty");
		}
	}
	public void pauseBackUpObject(int backupObjectIndex) {
		BackUpTask pauseBackup = runningBackupList.get(backupObjectIndex);
		if(pauseBackup.sendFileObject.progressfileBody.progressOutputStream.pause) {
			pauseBackup.sendFileObject.progressfileBody.progressOutputStream.pause();
		}else {
			pauseBackup.sendFileObject.progressfileBody.progressOutputStream.resume();			
		}
		System.out.println("correct object ??:   "+pauseBackup.sendFileObject.progressfileBody.progressOutputStream.pause);
		System.out.println("Pause the backup clicked hehehehehe");
	}
	
	public void cancelTask(int indexPosition) {
		System.out.println(indexPosition+"-----"+scheduledTask.size());
		if(indexPosition<scheduledTask.size()) {
			
			scheduledTask.get(indexPosition).cancel(true);
			BackUpTask getAllSimilarBackup = taskMap.get(scheduledTask.get(indexPosition));
			int finalIndex = -1;
			for(int i = 0;i<runningBackupList.size();i++) {
				if (runningBackupList.get(i) == getAllSimilarBackup) {
					finalIndex = i;
				}
			}
			getAllSimilarBackup.sendFileObject.cancelRequest();
			getOngoingFXML fxmlObject = getOngoingFXML.getInstance();
			try {
				HBox firstHbox = (HBox) fxmlObject.ongoingObject.hashmap.get(finalIndex);
				VBox firstVbox = (VBox) firstHbox.getChildren().get(0);
				Label name = (Label) firstVbox.getChildren().get(2);
				name.setText("Canceled");			
			}catch(Exception e){
//			Display error messaage in error box
			}			
		}else {
			System.out.println("size exceeds");
//			Display error message in error box
		}
	}
	public void addExtraTaskSchedule(BackUpTask task) {
		StorePathInJson jsonDataObject = new StorePathInJson();
		ScheduledFuture<?> newScheduledFuture = schedule.scheduleAtFixedRate(task::runBackUp, 0, task.getInterval() , TimeUnit.MINUTES);
		scheduledTask.add(newScheduledFuture);
		taskMap.put(newScheduledFuture ,task);
		taskList.add(task);
		getEditbackupFXML getEditObject = getEditbackupFXML.getinstance();
		EditBackupController controllerObject = getEditObject.editbackupObject;
		controllerObject.addContents(jsonDataObject.getJsonList(jsonDataObject.jsonPath), task.totalMinutes , task.Name ,ScheduledBackupTaskList.size());
		
	}
}

class BackUpTask{
	public String Name;
	public String Path;
	public String lastBackup;
	public String nextBackup;
	int runningTaskIndex;
	public int totalMinutes;
	UploadBackup uploadbackupObject = UploadBackup.getInstance();
	public SendFilesToServer sendFileObject;
//	FXMLLoader loader = new FXMLLoader(getClass().getResource("OnGoingBackUp.fxml"));
	
	public BackUpTask(String Name , String Path ,int totalMinutes) {
		this.Name = Name;
		this.Path = Path;
		this.totalMinutes = totalMinutes;
	}
	public void runBackUp() {
		lastBackup= String.valueOf(new Date(System.currentTimeMillis()));
		System.out.println("Runing backup: "+Name+" , Started at : "+new Date(System.currentTimeMillis()));
		System.out.println(totalMinutes);
		long nextRunTime = System.currentTimeMillis() +  TimeUnit.MINUTES.toMillis(totalMinutes);
		nextBackup = String.valueOf(new Date(nextRunTime));
        System.out.println("Next task will run at: " + new Date(nextRunTime));
        uploadbackupObject.runningBackupList.add(this);
        runningTaskIndex = uploadbackupObject.runningBackupList.size()-1;
        Platform.runLater(()->{
        	try {
        		getOngoingFXML ongoingObject = getOngoingFXML.getInstance();
//        		loader = ongoingObject.getFXMLfile();
        		OngoingBackupController ongoingControllerObject =ongoingObject.ongoingObject;
//        		Pane newPane = loader.load();
//        		OngoingBackupController ongoingControllerObject = loader.getController();
//        		BackUpTask task = uploadbackupObject.runningBackupList.get(this);
//        		int runningTaskIndex = uploadbackupObject.runningBackupList.indexOf(this);
        		ongoingControllerObject.addContents(runningTaskIndex, Name, lastBackup, nextBackup);        	
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        });
        sendFileObject = new SendFilesToServer(Path ,Name ,runningTaskIndex);
        sendFileObject.StartProcdeure();
//        ongoingObject.allTask.add(this);
        System.out.println("current task index position :  "+uploadbackupObject.runningBackupList.indexOf(this));
        System.out.println("running backup list : "+uploadbackupObject.runningBackupList);
//        ongoingObject.addContents(1, Name, lastBackup, nextBackup);
        System.out.println("eeeehhhh");
	}
	public int getInterval() {
		return totalMinutes;
	}
}
