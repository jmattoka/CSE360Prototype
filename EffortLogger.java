package CSE360Project;
	
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.filechooser.FileSystemView;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class EffortLogger extends Application {
	
	// singleton instance
	private static EffortLogger instance;
	
	private Login loginSession;
	private EffortDataHandler effortDataHandler;
	private PokerDataHandler pokerDataHandler;
	private String documentsPath;
	private String effortLoggerRootDirectory;
	private String effortDataPath;
	
	@Override
	public void start(Stage primaryStage) {
		if(System.getProperty("os.name").equals("Mac OS X")) {
			documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Documents/";
			effortLoggerRootDirectory = documentsPath + "EffortLogger/";
			effortDataPath = effortLoggerRootDirectory + "data/";
		}
		else {
			documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
			effortLoggerRootDirectory = documentsPath + "\\EffortLogger\\";
			effortDataPath = effortLoggerRootDirectory + "data\\";
		}
		
		if (instance == null)
			instance = this;
		else
			return;
		
		// these should run after the user logs in successfully
		
		loginSession = new Login();
		effortDataHandler = new EffortDataHandler();
		pokerDataHandler = new PokerDataHandler();
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if (loginSession.getLoginSession() != null) {
						// update/create the updated/created efforts in the file system
						effortDataHandler.storeEfforts();
						effortDataHandler.storeDefects();
					}
	          	}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static EffortLogger getInstance() {
		return instance;
	}
	
	public Login getLogin() {
		return loginSession;
	}
	
	public PokerDataHandler getPokerDataHandler() {
		return pokerDataHandler;
	}
	
	public EffortDataHandler getEffortDataHandler() {
		return effortDataHandler;
	}
	
	public Path getRootDirectory() {
		return Paths.get(effortLoggerRootDirectory);
	}
	
	public Path getDataPathDirectory() {
		return Paths.get(effortDataPath);
	}
}