package ua.edu.sumdu.j2se.kolisnyk.tasks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.ViewFilePath;
import ua.edu.sumdu.j2se.kolisnyk.tasks.controller.Controller;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.TaskManagerModel;

/**
 * Class Main has methods that initialize
 * primaryStage and launch application
 */

public class Main extends Application {

    /**
     * method initialize primaryStage of application
     *
     * @param primaryStage main stage of application
     * @throws Exception unexpected exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (Controller.isInitialize) {
            Parent root = FXMLLoader.load(getClass().getResource(
                    ViewFilePath.TASK_MENU_MANAGER_VIEW_PATH.getPath()));
            primaryStage.setTitle("TaskManager");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            TaskManagerModel.log.info("TaskManager started");
        } else {
            TaskManagerModel.log.info("TaskManager did not start");
        }
    }

    /**
     * method is responsible for the
     * correct stop of the application
     *
     * @throws Exception unexpected exception
     */

    @Override
    public void stop() throws Exception {
        TaskManagerModel.log.info("TaskManager stopped");
        super.stop();
    }

    /**
     * method launch application, main method
     * of application
     *
     * @param args unnecessary arguments
     */

    public static void main(String[] args) {
        launch(args);
    }
}
