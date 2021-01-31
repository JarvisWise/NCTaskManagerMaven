package ua.edu.sumdu.j2se.kolisnyk.tasks.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.TaskManagerModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedMap;

/**
 * Class Controller is responsible for
 * storing and using model part of application
 */

public class Controller {

    public static TaskManagerModel model;
    public static boolean isInitialize = false;

    static {
        try {
            model = new TaskManagerModel();
            notificationStart();
            isInitialize = true;
        } catch (Exception e) {
            TaskManagerModel.log.fatal("Fatal exception for application", e);

            Controller.showWarningAlert("Fatal Error",
                    "System failed",
                    "Sorry program initialization failed");
            Platform.exit();
        }
    }

    /**
     * This method change current scene on new scene
     *
     * @param path  path to scene that will be opened
     * @param event event of current stage
     */

    public static void changeScene(String path, Event event) {
        try {
            Parent TaskManagerMenuParent = FXMLLoader.load(Controller.class.getResource(path));
            Scene TaskManagerMenuScene = new Scene(TaskManagerMenuParent);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(TaskManagerMenuScene);
        } catch (IOException e) {
            TaskManagerModel.log.error("Scene cannot be changed", e);
        }
    }

    /**
     * show new warning alert for some situation
     *
     * @param title   title of alert
     * @param header  header of alert
     * @param content content of alert
     */

    public static void showWarningAlert(String title, String header, String content) {
        showAlert(title, header, content, Alert.AlertType.ERROR);
    }

    /**
     * show new alert for some situation with some
     *
     * @param title   title of alert
     * @param header  header of alert
     * @param content content of alert
     * @param type    type of alert
     */

    private static void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This method using for show notification about tasks
     */

    public static void notificationStart() {

        Thread notificationThread = new Thread(() -> {
            SortedMap<LocalDateTime, Set<Task>> calendar;
            StringBuilder str;
            while (true) {
                calendar = model.getCalendar(LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));
                str = new StringBuilder();

                if (!calendar.isEmpty()) {
                    boolean isFirst = true;
                    for (SortedMap.Entry<LocalDateTime, Set<Task>> entry : calendar.entrySet()) {
                        for (Task task : entry.getValue()) {
                            if (isFirst) {
                                str.append(task.getTitle());
                                isFirst = false;
                            } else {
                                str.append(", ").append(task.getTitle());
                            }
                        }
                    }

                    StringBuilder finalStr = str;
                    Platform.runLater(() ->
                            Controller.showAlert("Incoming tasks",
                                    "Within a minute you need to complete the following tasks.",
                                    "Tasks: " + finalStr,
                                    Alert.AlertType.INFORMATION)
                    );
                }

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    TaskManagerModel.log.warn("Problem with notification system", e);
                }
            }
        });
        notificationThread.setDaemon(true);
        notificationThread.start();
    }
}

