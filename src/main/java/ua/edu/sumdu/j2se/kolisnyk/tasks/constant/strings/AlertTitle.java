package ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings;

/**
 * Enum AlertTitle is responsible
 * for storing alert titles
 */

public enum AlertTitle {
    WRONG_INPUT("Wrong input"),
    FILE_PROBLEM("File problem"),
    FATAL_ERROR("Fatal Error"),
    WRONG_ACTION("Wrong action"),
    INCOMING_TASKS("Incoming tasks");


    private final String title;

    AlertTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
