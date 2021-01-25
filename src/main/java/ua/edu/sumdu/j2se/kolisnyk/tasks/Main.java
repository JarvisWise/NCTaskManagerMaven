package ua.edu.sumdu.j2se.kolisnyk.tasks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.edu.sumdu.j2se.kolisnyk.tasks.controller.Controller;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.TaskManagerModel;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		if(Controller.isInitialize) {
			Parent root = FXMLLoader.load(getClass().getResource("/view/TaskManagerMenuView.fxml")); //view/TaskManagerMenuView.fxml
			primaryStage.setTitle("TaskManager");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			TaskManagerModel.log.info("TaskManager started");
		} else {
			TaskManagerModel.log.info("TaskManager did not start");
		}
	}

	@Override
	public void stop() throws Exception {
		TaskManagerModel.log.info("TaskManager stopped");
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
