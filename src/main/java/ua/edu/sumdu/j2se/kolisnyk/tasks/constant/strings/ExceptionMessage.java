package ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings;

/**
 * Enum ExceptionMessage is responsible
 * for storing exception messages
 */

public enum ExceptionMessage {
    TIME_NOT_NULL("Time cannot be null!"),
    FROM_LESS_THEN_TO("From time cannot be less then to time!"),
    LIST_NOT_NULL("Argument list cannot be null!"),
    INCORRECT_INDEX("Incorrect index of list"),
    INTERVAL_OUT("Out of array interval!"),
    TITLE_NOT_NULL("Parameter title cannot be null!"),
    START_AND_END_NOT_NULL("Parameters start and end cannot be null!"),
    START_AFTER_END("Start time cannot be after end time"),
    LESS_THEN_ZERO("Period cannot be less then zero!"),
    CURRENT_NOT_NULL("Parameter current cannot be null!"),
    FILE_NOT_CREATED("File to save data was not created!"),
    INCORRECT_READ("Task list did not read correct"),
    PROBLEM_WITH_SOURCE_DATA("Problem with source data"),
    READ_FAILED("Reading from file failed"),
    WRITE_FAILED("Writing to file failed"),
    TASKS_NOT_NULL("Tasks cannot be null!");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
