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
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertHeader;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertTitle;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.ViewFilePath;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.TaskManagerModel;

import java.io.IOException;
import java.time.LocalTime;

import static ua.edu.sumdu.j2se.kolisnyk.tasks.controller.Controller.*;

/**
 * Class EditTaskController is responsible for
 * edit a exist task
 */

public class EditTaskController {

    private static Task selectedTask;
    public TextField titleField;
    public TextField startTimeField;
    public DatePicker startDateField;
    public CheckBox checkActive;
    public CheckBox checkRepeated;
    public DatePicker endDateField;
    public TextField endTimeField;
    public TextField intervalField;
    public VBox forRepeatedTask;

    /**
     * set value of task that will be edited or deleted
     *
     * @param st selected task in TaskManager Scene
     */

    public static void setSelectedTask(Task st) {
        selectedTask = st;
    }

    /**
     * Initialize this scene
     */

    public void initialize() {
        initTaskView();
    }

    /**
     * initialize all fields of this scene
     */

    private void initTaskView() {
        if (selectedTask != null) {
            titleField.setText(selectedTask.getTitle());

            startDateField.setValue(selectedTask.getStartTime().toLocalDate());
            startTimeField.setText(formatterTimeWrite.format(selectedTask.getStartTime().toLocalTime()));
            checkActive.setSelected(selectedTask.isActive());
            checkRepeated.setSelected(selectedTask.isRepeated());

            if (selectedTask.isRepeated()) {
                endDateField.setValue(selectedTask.getEndTime().toLocalDate());
                endTimeField.setText(formatterTimeWrite.format(selectedTask.getEndTime().toLocalTime()));
                intervalField.setText(formatterIntervalWrite.format(LocalTime.ofSecondOfDay(selectedTask.getRepeatInterval())));
            } else {
                forRepeatedTask.setVisible(false);
            }
        } else {
            TaskManagerModel.log.warn("Unexpected open EditTaskController, selectedTask must be not null");
            try {
                Parent TaskManagerMenuParent = FXMLLoader.load(Controller.class.getResource(
                        ".." + ViewFilePath.TASK_MENU_MANAGER_VIEW_PATH.getPath()));
                Scene TaskManagerMenuScene = new Scene(TaskManagerMenuParent);
                Stage currentStage = (Stage) intervalField.getScene().getWindow();
                currentStage.setScene(TaskManagerMenuScene);
            } catch (IOException e) {
                TaskManagerModel.log.error("Scene cannot be changed", e);
            }
        }
    }

    /**
     * method react on edit button clicked, check all fields and change current task
     *
     * @param actionEvent current event
     */

    public void onClickEditButton(ActionEvent actionEvent) {

        Task newTask = createTask(titleField, startDateField,
                startTimeField, checkActive, checkRepeated,
                endDateField, endTimeField, intervalField);

        if (newTask == null) {
            return;
        }

        try {
            Controller.model.changeTask(selectedTask, newTask);
        } catch (IOException e) {
            Controller.showWarningAlert(AlertTitle.FILE_PROBLEM.getTitle(),
                    AlertHeader.EDIT_TASK.getHeader(),
                    "Problems with file system, task was not changed");
            TaskManagerModel.log.warn("The task has not been changed", e);
            return;
        }
        onClickCancelButton(actionEvent);
    }


    /**
     * method react on delete button clicked and delete current task
     *
     * @param actionEvent current event
     */

    public void onClickDeleteButton(ActionEvent actionEvent) {
        try {
            Controller.model.removeTask(selectedTask);
        } catch (IOException e) {
            Controller.showWarningAlert(AlertTitle.FILE_PROBLEM.getTitle(),
                    AlertHeader.DELETE_TASK.getHeader(),
                    "Problems with file system, task was not deleted");
            TaskManagerModel.log.warn("The task has not been deleted", e);
            return;
        }
        onClickCancelButton(actionEvent);
    }

    /**
     * This method change this scene on TaskManagerMenu
     *
     * @param actionEvent current event
     */

    public void onClickCancelButton(ActionEvent actionEvent) {
        selectedTask = null;
        Controller.changeScene(ViewFilePath.TASK_MENU_MANAGER_VIEW_PATH.getPath(), actionEvent);
    }

    /**
     * This method using for show and hide fields,
     * that can be used for edit task
     */

    public void onChangedRepeated() {
        forRepeatedTask.setVisible(checkRepeated.isSelected());
    }
}

