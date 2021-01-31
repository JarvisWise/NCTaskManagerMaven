package ua.edu.sumdu.j2se.kolisnyk.tasks.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertHeader;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertTitle;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.ViewFilePath;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.TaskManagerModel;

import java.io.IOException;

import static ua.edu.sumdu.j2se.kolisnyk.tasks.controller.Controller.createTask;

/**
 * Class AddTaskController is responsible for
 * adding a new task to the task list
 */

public class AddTaskController {

    public TextField titleField;
    public DatePicker startDateField;
    public TextField startTimeField;
    public CheckBox checkActive;
    public CheckBox checkRepeated;
    public DatePicker endDateField;
    public TextField endTimeField;
    public TextField intervalField;
    public VBox forRepeatedTask;

    /**
     * method react on Add button clicked, check all fields and add new task to task list
     *
     * @param actionEvent current event
     */

    public void onClickAddButton(ActionEvent actionEvent) {

        Task newTask = createTask(titleField, startDateField,
                startTimeField, checkActive, checkRepeated,
                endDateField, endTimeField, intervalField);

        if (newTask == null) {
            return;
        }

        try {
            Controller.model.addToTaskList(newTask);
        } catch (IOException e) {
            Controller.showWarningAlert(AlertTitle.FILE_PROBLEM.getTitle(),
                    AlertHeader.ADD_TASK.getHeader(),
                    "Problems with file system, task was not added");
            TaskManagerModel.log.warn("Task was not added", e);
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
        Controller.changeScene(ViewFilePath.TASK_MENU_MANAGER_VIEW_PATH.getPath(), actionEvent);
    }

    /**
     * This method using for show and hide fields,
     * that can be used for create repeated task
     */

    public void onChangedRepeated() {
        forRepeatedTask.setVisible(checkRepeated.isSelected());
    }
}

