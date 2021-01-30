package ua.edu.sumdu.j2se.kolisnyk.tasks.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.TaskManagerModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EditTaskController {
    public TextField titleField;
    public TextField startTimeField;
    public DatePicker startDateField;
    public CheckBox checkActive;
    public CheckBox checkRepeated;
    public DatePicker endDateField;
    public TextField endTimeField;
    public TextField intervalField;
    public VBox forRepeatedTask;
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter formatterInterval = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static Task selectedTask;

    /**
     * set value of task that will be edited or deleted
     * @param st selected task in TaskManager Scene
     */

    public static void setSelectedTask(Task st) {
        selectedTask = st;
    }

    /**
     * Initialize this scene
     */

    public void initialize(){
        initTaskView();
    }

    /**
     * initialize all fields of this scene
     */

    private void initTaskView() {
        if (selectedTask != null ) {
            titleField.setText(selectedTask.getTitle());

            startDateField.setValue(selectedTask.getStartTime().toLocalDate());
            startTimeField.setText(formatterTime.format(selectedTask.getStartTime().toLocalTime()));
            checkActive.setSelected(selectedTask.isActive());
            checkRepeated.setSelected(selectedTask.isRepeated());

            if (selectedTask.isRepeated()) {
                endDateField.setValue(selectedTask.getEndTime().toLocalDate());
                endTimeField.setText(formatterTime.format(selectedTask.getEndTime().toLocalTime()));
                intervalField.setText(formatterInterval.format(LocalTime.ofSecondOfDay(selectedTask.getRepeatInterval())));
            } else {
                forRepeatedTask.setVisible(false);
            }
        } else {
            TaskManagerModel.log.warn("Unexpected open EditTaskController, selectedTask must be not null");
            try {
                Parent TaskManagerMenuParent = FXMLLoader.load(Controller.class.getResource("../view/TaskManagerMenuView.fxml"));
                Scene TaskManagerMenuScene = new Scene(TaskManagerMenuParent);
                Stage currentStage = (Stage)  intervalField.getScene().getWindow();
                currentStage.setScene(TaskManagerMenuScene);
            } catch (IOException e) {
                TaskManagerModel.log.error("Scene cannot be changed", e);
            }
        }
    }

    /**
     * method react on edit button clicked, check all fields and change current task
     * @param actionEvent current event
     */

    public void onClickEditButton(ActionEvent actionEvent) {
        Task newTask;
        LocalDateTime startDateTime;

        String title = titleField.getText();
        if(titleField.getText().equals("")) {
            Controller.showWarningAlert("Wrong input",
                    "Title field",
                    "Please enter the title");
            return;
        }

        try {
            LocalDate startLocalDate = startDateField.getValue();
            LocalTime startLocalTime = LocalTime.parse(startTimeField.getText(),formatterTime);
            startDateTime = startLocalDate.atTime(startLocalTime);
        } catch (Exception e) {
            Controller.showWarningAlert("Wrong input",
                    "Time Field",
                    "Please enter the start time correct");
            return;
        }

        if (startDateTime.compareTo(LocalDateTime.now()) < 0) {
            Controller.showWarningAlert("Wrong input",
                    "Start in past",
                    "Start time cannot be before current time");
            return;
        }

        boolean isActive = checkActive.isSelected();
        if (!checkRepeated.isSelected()) {
            newTask = new Task(title, startDateTime);
            newTask.setActive(isActive);
        } else {
            LocalDateTime endDateTime;
            int interval;

            try {
                LocalDate endLocalDate = endDateField.getValue();
                LocalTime endLocalTime = LocalTime.parse(endTimeField.getText(),formatterTime);
                endDateTime = endLocalDate.atTime(endLocalTime);
                interval = (LocalTime.parse(intervalField.getText(),formatterInterval)).toSecondOfDay();
            } catch (Exception e) {
                Controller.showWarningAlert("Wrong input",
                        "Time Field",
                        "Please enter the end time or interval correct");
                return;
            }

            if (endDateTime.compareTo(startDateTime) < 0) {
                Controller.showWarningAlert("Wrong input",
                        "Start and end time",
                        "Start time cannot be after end time");
                return;
            }

            newTask = new Task(title, startDateTime, endDateTime, interval);
            newTask.setActive(isActive);
        }

        try {
            Controller.model.changeTask(selectedTask, newTask);
        } catch (IOException e) {
            Controller.showWarningAlert("File problem",
                    "Edit task",
                    "Problems with file system, task was not changed");
            TaskManagerModel.log.warn("The task has not been changed", e);
            return;
        }
        onClickCancelButton(actionEvent);
    }



    /**
     * method react on delete button clicked and delete current task
     * @param actionEvent current event
     */

    public void onClickDeleteButton(ActionEvent actionEvent) {
        try {
            Controller.model.removeTask(selectedTask);
        } catch (IOException e) {
            Controller.showWarningAlert("File problem",
                    "Delete task",
                    "Problems with file system, task was not deleted");
            TaskManagerModel.log.warn("The task has not been deleted", e);
            return;
        }
        onClickCancelButton(actionEvent);
    }

    /**
     * This method change this scene on TaskManagerMenu
     * @param actionEvent current event
     */

    public void onClickCancelButton(ActionEvent actionEvent) {
        selectedTask = null;
        Controller.changeScene("/view/TaskManagerMenuView.fxml",actionEvent);
    }

    /**
     * This method using for show and hide fields,
     * that can be used for edit task
     */

    public void onChangedRepeated() {
        if(checkRepeated.isSelected()) {
            forRepeatedTask.setVisible(true);
        } else {
            forRepeatedTask.setVisible(false);
        }
    }
}

