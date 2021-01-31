package ua.edu.sumdu.j2se.kolisnyk.tasks.model;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedMap;

import static ua.edu.sumdu.j2se.kolisnyk.tasks.model.Tasks.calendar;

/**
 * Class TaskManagerModel is responsible for
 * storing the main fields and methods
 * of the model of this application
 */

public class TaskManagerModel {

    public static final Logger log = Logger.getLogger(TaskManagerModel.class);
    private AbstractTaskList taskList;
    private File dataFile;

    /**
     * This Constructor initialize model for application
     *
     * @throws IOException problem with file system
     */

    public TaskManagerModel() throws IOException {
        dataFile = new File(System.getProperty("user.dir") + "\\data.json");

        taskList = new ArrayTaskList();
        try {
            if (!dataFile.exists()) {
                if (dataFile.createNewFile()) {
                    log.info("data.json file created");
                } else {
                    log.error("data.json file was not created");
                    throw new IOException("File to save data was not created!");
                }
            } else {
                try {
                    readTaskListFromRes();
                } catch (IOException e) {
                    log.fatal("Task list did not read correct, an unexpected situation", e);
                    throw new IOException("Task list did not read correct", e);
                }
            }
        } catch (IOException e) {
            log.fatal("Problem with source data", e);
            throw new IOException("Problem with source data", e);
        }
    }

    /**
     * Method for take taskList
     *
     * @return taskList
     */

    public AbstractTaskList getTaskList() {
        return taskList;
    }

    /**
     * Method for form calendar for taskList
     *
     * @param start start time for calendar
     * @param end   end time for calendar
     * @return formed calendar
     */

    public SortedMap<LocalDateTime, Set<Task>> getCalendar(LocalDateTime start, LocalDateTime end) {
        return calendar(taskList, start, end);
    }

    /**
     * Method for read taskList from dataFile
     *
     * @throws IOException problems with reading from file
     */

    public void readTaskListFromRes() throws IOException {
        try {
            TaskIO.readText(taskList, dataFile);
        } catch (IOException e) {
            log.warn("Reading from file failed", e);
            throw new IOException("Reading from file failed", e);
        }
    }

    /**
     * Method for write taskList to dataFile
     *
     * @throws IOException problems with writing to file
     */

    public void writeTaskListFromRes() throws IOException {
        try {
            TaskIO.writeText(taskList, dataFile);
        } catch (IOException e) {
            log.warn("Writing to file failed", e);
            throw new IOException("Writing to file failed", e);
        }
    }

    /**
     * Method for add task to taskList
     *
     * @param task task for add
     * @throws IOException problems with writing to file
     */

    public void addToTaskList(Task task) throws IOException {
        if (task == null) {
            return;
        }
        taskList.add(task);
        writeTaskListFromRes();
    }

    /**
     * Method for change value in taskList
     *
     * @param oldValue old value of task
     * @param newValue new value of task
     * @throws IOException problems with writing to file
     */

    public void changeTask(Task oldValue, Task newValue) throws IOException {
        taskList.setTask(oldValue, newValue);
        writeTaskListFromRes();
    }

    /**
     * Method for delete task from taskList
     *
     * @param task task for remove
     * @throws IOException problems with writing to file
     */

    public void removeTask(Task task) throws IOException {
        taskList.remove(task);
        writeTaskListFromRes();
    }

}

