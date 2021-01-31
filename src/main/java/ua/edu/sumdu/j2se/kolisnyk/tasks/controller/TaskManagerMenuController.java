package ua.edu.sumdu.j2se.kolisnyk.tasks.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;

import java.util.stream.Stream;

/**
 * Class TaskManagerMenuController is responsible for
 * displaying task list and switching to other tabs
 * of application
 */

public class TaskManagerMenuController {

    public ListView taskList;

    /**
     * Initialize this scene
     */

    public void initialize() {
        initListView();
    }

    /**
     * This method change this scene on AddTaskView, where we can add new task
     *
     * @param actionEvent current event
     */

    public void onClickedAddTask(ActionEvent actionEvent) {
        Controller.changeScene("/view/AddTaskView.fxml", actionEvent);
    }

    /**
     * This method change this scene on CalendarView, where we can form calendar
     *
     * @param actionEvent current event
     */

    public void onClickedCalendar(ActionEvent actionEvent) {
        Controller.changeScene("/view/CalendarView.fxml", actionEvent);
    }

    /**
     * initialize task list of this scene
     */

    private void initListView() {
        ObservableList<String> obsTaskList = FXCollections.observableArrayList();
        Stream<Task> stream = Controller.model.getTaskList().getStream();
        stream.forEach((t) -> obsTaskList.add(t.showTask()));

        if (obsTaskList.size() != 0) {
            taskList.setItems(obsTaskList);
        } else {
            taskList.getItems().add("No tasks yet");
        }

    }

    /**
     * This method change this scene on EditTaskView, where we can edit choosed task
     *
     * @param mouseEvent current event
     */

    public void onClickEditTask(MouseEvent mouseEvent) {
        if (Controller.model.getTaskList().size() == 0) {
            Controller.showWarningAlert("Wrong action",
                    "Empty task list",
                    "Task list is empty, you cannot edit any task");
            return;
        }

        EditTaskController.setSelectedTask(Controller.model.getTaskList().getTask(taskList.getSelectionModel().getSelectedIndex()));
        Controller.changeScene("/view/EditTaskView.fxml", mouseEvent);
    }
}

