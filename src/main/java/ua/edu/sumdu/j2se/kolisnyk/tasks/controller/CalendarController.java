package ua.edu.sumdu.j2se.kolisnyk.tasks.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertHeader;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.AlertTitle;
import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.ViewFilePath;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedMap;

import static ua.edu.sumdu.j2se.kolisnyk.tasks.controller.Controller.extractLocalDateTime;

/**
 * Class CalendarController is responsible for
 * formation and display task calendar
 */

public class CalendarController {

    public DatePicker calendarStartDateFiled;
    public TextField calendarStartTimeFiled;
    public DatePicker calendarEndDateFiled;
    public TextField calendarEndTimeFiled;
    public ListView<String> calendarList;

    /**
     * This method check all fields and show calendar
     */

    public void onClickShowCalendarButton() {

        LocalDateTime startDateTime = extractLocalDateTime(calendarStartDateFiled, calendarStartTimeFiled,
                "Please enter the start time correct");

        if (startDateTime == null) {
            return;
        }

        if (startDateTime.compareTo(LocalDateTime.now()) < 0) {
            Controller.showWarningAlert(AlertTitle.WRONG_INPUT.getTitle(),
                    AlertHeader.START_IN_PAST.getHeader(),
                    "Start time cannot be before current time");
            return;
        }

        LocalDateTime endDateTime = extractLocalDateTime(calendarEndDateFiled, calendarEndTimeFiled,
                "Please enter the end time correct");

        if (endDateTime == null) {
            return;
        }

        if (endDateTime.compareTo(startDateTime) < 0) {
            Controller.showWarningAlert(AlertTitle.WRONG_INPUT.getTitle(),
                    AlertHeader.START_AND_END.getHeader(),
                    "Start time cannot be after end time");
            return;
        }

        SortedMap<LocalDateTime, Set<Task>> calendar = Controller.model.getCalendar(startDateTime, endDateTime);
        ObservableList<String> obsCalendarList = FXCollections.observableArrayList();

        for (SortedMap.Entry<LocalDateTime, Set<Task>> entry : calendar.entrySet()) {
            for (Task task : entry.getValue()) {
                obsCalendarList.add("Time(" + entry.getKey() + ") Title(" + task.getTitle() + ")");
            }
        }
        calendarList.setItems(obsCalendarList);
    }

    /**
     * This method change this scene on TaskManagerMenu
     *
     * @param actionEvent current event
     */

    public void onClickCancelButton(ActionEvent actionEvent) {
        Controller.changeScene(ViewFilePath.TASK_MENU_MANAGER_VIEW_PATH.getPath(), actionEvent);
    }
}

