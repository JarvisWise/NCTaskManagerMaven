package ua.edu.sumdu.j2se.kolisnyk.tasks.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ua.edu.sumdu.j2se.kolisnyk.tasks.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.SortedMap;

/**
 * Class CalendarController is responsible for
 * formation and display task calendar
 */

public class CalendarController {

    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    public DatePicker calendarStartDateFiled;
    public TextField calendarStartTimeFiled;
    public DatePicker calendarEndDateFiled;
    public TextField calendarEndTimeFiled;
    public ListView calendarList;

    /**
     * This method check all fields and show calendar
     */

    public void onClickShowCalendarButton() {

        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        try {
            LocalDate startLocalDate = calendarStartDateFiled.getValue();
            LocalTime startLocalTime = LocalTime.parse(calendarStartTimeFiled.getText(), formatterTime);
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

        try {
            LocalDate endLocalDate = calendarEndDateFiled.getValue();
            LocalTime endLocalTime = LocalTime.parse(calendarEndTimeFiled.getText(), formatterTime);
            endDateTime = endLocalDate.atTime(endLocalTime);
        } catch (Exception e) {
            Controller.showWarningAlert("Wrong input",
                    "Time Field",
                    "Please enter the end time correct");
            return;
        }

        if (endDateTime.compareTo(startDateTime) < 0) {
            Controller.showWarningAlert("Wrong input",
                    "Start and end time",
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
        Controller.changeScene("/view/TaskManagerMenuView.fxml", actionEvent);
    }
}

