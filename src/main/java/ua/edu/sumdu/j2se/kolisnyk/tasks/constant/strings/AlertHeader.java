package ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings;

/**
 * Enum AlertHeader is responsible
 * for storing alert headers
 */

public enum AlertHeader {
    ADD_TASK("Add new task"),
    START_IN_PAST("Start in past"),
    START_AND_END("Start and end time"),
    SYS_FAIL("System failed"),
    TIME_FIELD("Time Field"),
    TITLE_FIELD("Title field"),
    COMING_TASKS("Within a minute you need to complete the following tasks."),
    EDIT_TASK("Edit task"),
    DELETE_TASK("Delete task"),
    EMPTY_LIST("Empty task list");

    private final String header;

    AlertHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
