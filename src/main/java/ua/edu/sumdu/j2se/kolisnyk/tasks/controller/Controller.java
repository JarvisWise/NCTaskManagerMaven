package ua.edu.sumdu.j2se.kolisnyk.tasks.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertHeader;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertTitle;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.TaskManagerModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.SortedMap;

/**
 * Class Controller is responsible for
 * storing and using model part of application
 */

public class Controller {

    public static TaskManagerModel model;
    public static boolean isInitialize = false;
    public static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter formatterInterval = DateTimeFormatter.ofPattern("HH:mm:ss");

    static {
        try {
            model = new TaskManagerModel();
            notificationStart();
            isInitialize = true;
        } catch (Exception e) {
            TaskManagerModel.log.fatal("Fatal exception for application", e);

            Controller.showWarningAlert(AlertTitle.FATAL_ERROR.getTitle(),
                    AlertHeader.SYS_FAIL.getHeader(),
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
     * method extract LocalDateTime from two field
     *
     * @param dateFiled    date filed
     * @param timeFiled    time field
     * @param alertContent alert content
     * @return return extracted value or null
     */

    public static LocalDateTime extractLocalDateTime(DatePicker dateFiled, TextField timeFiled, String alertContent) {

        LocalDateTime localDateTime;

        try {
            LocalDate localDate = dateFiled.getValue();
            LocalTime localTime = LocalTime.parse(timeFiled.getText(), formatterTime);
            localDateTime = localDate.atTime(localTime);
            return localDateTime;
        } catch (Exception e) {
            Controller.showWarningAlert(AlertTitle.WRONG_INPUT.getTitle(),
                    AlertHeader.TIME_FIELD.getHeader(),
                    alertContent);
            return null;
        }
    }

    /**
     * create new task with values from fields
     *
     * @param titleField     title field
     * @param startDateField startDate field
     * @param startTimeField startTime field
     * @param checkActive    active checkbox
     * @param checkRepeated  repeated checkbox
     * @param endDateField   endDate field
     * @param endTimeField   endTime field
     * @param intervalField  interval field
     * @return created new task or null
     */

    public static Task createTask(TextField titleField, DatePicker startDateField,
                                  TextField startTimeField, CheckBox checkActive, CheckBox checkRepeated,
                                  DatePicker endDateField, TextField endTimeField, TextField intervalField) {

        Task newTask;

        String title = titleField.getText();
        if (titleField.getText().equals("")) {
            showWarningAlert(AlertTitle.WRONG_INPUT.getTitle(),
                    AlertHeader.TITLE_FIELD.getHeader(),
                    "Please enter the title");
            return null;
        }

        LocalDateTime startDateTime = extractLocalDateTime(startDateField, startTimeField,
                "Please enter the start time correct");

        if (startDateTime == null) {
            return null;
        }

        if (startDateTime.compareTo(LocalDateTime.now()) < 0) {
            showWarningAlert(AlertTitle.WRONG_INPUT.getTitle(),
                    AlertHeader.START_IN_PAST.getHeader(),
                    "Start time cannot be before current time");
            return null;
        }

        boolean isActive = checkActive.isSelected();
        if (!checkRepeated.isSelected()) {
            newTask = new Task(title, startDateTime);

        } else {
            LocalDateTime endDateTime = extractLocalDateTime(endDateField, endTimeField,
                    "Please enter the end time correct");

            if (endDateTime == null) {
                return null;
            }

            int interval;
            try {
                interval = (LocalTime.parse(intervalField.getText(), formatterInterval)).toSecondOfDay();
            } catch (Exception e) {
                showWarningAlert(AlertTitle.WRONG_INPUT.getTitle(),
                        AlertHeader.TIME_FIELD.getHeader(),
                        "Please enter interval correct");
                return null;
            }

            if (endDateTime.compareTo(startDateTime) < 0) {
                showWarningAlert(AlertTitle.WRONG_INPUT.getTitle(),
                        AlertHeader.START_AND_END.getHeader(),
                        "Start time cannot be after end time");
                return null;
            }

            newTask = new Task(title, startDateTime, endDateTime, interval);
        }
        newTask.setActive(isActive);
        return newTask;
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
                            Controller.showAlert(AlertTitle.INCOMING_TASKS.getTitle(),
                                    AlertHeader.COMING_TASKS.getHeader(),
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

