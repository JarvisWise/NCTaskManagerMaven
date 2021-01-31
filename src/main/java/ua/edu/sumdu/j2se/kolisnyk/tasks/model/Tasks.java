package ua.edu.sumdu.j2se.kolisnyk.tasks.model;

import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.ExceptionMessage;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Class Tasks is responsible for
 * storing methods for use by
 * non-AbstractTaskList containers
 */

public class Tasks {

    public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {

        if (tasks == null) {
            throw new NullPointerException(ExceptionMessage.TASKS_NOT_NULL.getMessage());
        }

        if (start == null || end == null) {
            throw new NullPointerException(ExceptionMessage.START_AND_END_NOT_NULL.getMessage());
        }

        if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException(ExceptionMessage.START_AFTER_END.getMessage());
        }

        ArrayList<Task> list = new ArrayList<>();

        for (Task task : tasks) {
            if (task.nextTimeAfter(start) != null && task.nextTimeAfter(start).compareTo(end) <= 0) {
                list.add(task);
            }
        }

        return list;
    }

    public static SortedMap<LocalDateTime, Set<Task>> calendar(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {

        if (tasks == null) {
            throw new NullPointerException(ExceptionMessage.TASKS_NOT_NULL.getMessage());
        }

        if (start == null || end == null) {
            throw new NullPointerException(ExceptionMessage.START_AND_END_NOT_NULL.getMessage());
        }

        if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException(ExceptionMessage.START_AFTER_END.getMessage());
        }

        SortedMap<LocalDateTime, Set<Task>> map = new TreeMap<>();

        for (Task task : tasks) {
            for (LocalDateTime i = task.nextTimeAfter(start); i != null && i.compareTo(end) <= 0; i = task.nextTimeAfter(i)) {
                if (map.get(i) == null) {
                    HashSet<Task> set = new HashSet<>();
                    set.add(task);
                    map.put(i, set);
                } else {
                    map.get(i).add(task);
                }
            }
        }
        return map;
    }
}

