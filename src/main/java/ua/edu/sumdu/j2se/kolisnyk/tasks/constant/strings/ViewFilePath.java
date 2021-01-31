package ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings;

/**
 * Enum ViewFilePath is responsible
 * for storing paths for view files
 */

public enum ViewFilePath {
    ADD_TASK_VIEW_PATH("/view/AddTaskView.fxml"),
    CALENDAR_VIEW_PATH("/view/CalendarView.fxml"),
    EDIT_TASK_VIEW_PATH("/view/EditTaskView.fxml"),
    TASK_MENU_MANAGER_VIEW_PATH("/view/TaskManagerMenuView.fxml");

    private final String path;

    ViewFilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
