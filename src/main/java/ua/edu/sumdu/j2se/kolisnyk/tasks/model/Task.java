package ua.edu.sumdu.j2se.kolisnyk.tasks.model;

import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.ExceptionMessage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class Task is responsible for
 * for storing information about task
 */

public class Task implements Cloneable, Serializable {

    private static final long serialVersionUID = 1;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private int interval;
    private boolean active;

    public Task(String title, LocalDateTime time) {

        if (title == null) {
            throw new NullPointerException(ExceptionMessage.TITLE_NOT_NULL.getMessage());
        }

        if (time == null) {
            throw new IllegalArgumentException(ExceptionMessage.TIME_NOT_NULL.getMessage());
        }

        start = time;
        end = time;
        interval = 0;
        active = false;
        this.title = title;
    }

    public Task(String title, LocalDateTime start, LocalDateTime end, int interval) {

        if (title == null) {
            throw new NullPointerException(ExceptionMessage.TITLE_NOT_NULL.getMessage());
        }

        if (start == null || end == null) {
            throw new NullPointerException(ExceptionMessage.START_AND_END_NOT_NULL.getMessage());
        }

        if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException(ExceptionMessage.START_AFTER_END.getMessage());
        }

        if (interval < 0) {
            throw new IllegalArgumentException(ExceptionMessage.LESS_THEN_ZERO.getMessage());
        }

        if (interval == 0) {
            this.end = start;
        } else {
            this.end = end;
        }
        this.start = start;
        this.interval = interval;
        this.title = title;
        this.active = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new NullPointerException(ExceptionMessage.TITLE_NOT_NULL.getMessage());
        }

        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getTime() {
        return start;
    }

    public void setTime(LocalDateTime time) {

        if (time == null) {
            throw new NullPointerException(ExceptionMessage.TIME_NOT_NULL.getMessage());
        }

        start = time;
        end = time;
        interval = 0;
    }

    public LocalDateTime getStartTime() {
        return start;
    }

    public LocalDateTime getEndTime() {
        return end;
    }

    public int getRepeatInterval() {
        return interval;
    }

    public void setTime(LocalDateTime start, LocalDateTime end, int interval) {

        if (start == null || end == null) {
            throw new NullPointerException(ExceptionMessage.TIME_NOT_NULL.getMessage());
        }

        if (interval < 0) {
            throw new IllegalArgumentException(ExceptionMessage.LESS_THEN_ZERO.getMessage());
        }

        if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException(ExceptionMessage.START_AFTER_END.getMessage());
        }

        if (interval == 0) {
            this.end = start;
        } else {
            this.end = end;
        }

        this.start = start;
        this.interval = interval;
    }

    public boolean isRepeated() {
        return interval != 0;
    }

    public LocalDateTime nextTimeAfter(LocalDateTime current) {

        if (current == null) {
            throw new NullPointerException(ExceptionMessage.CURRENT_NOT_NULL.getMessage());
        }

        if (!active) {
            return null;
        }

        if (interval == 0) {
            if (end.compareTo(current) > 0) {
                return end;
            } else {
                return null;
            }
        } else {
            for (LocalDateTime i = start; i.compareTo(end) <= 0; i = i.plusSeconds(interval)) {
                if (i.compareTo(current) > 0) {
                    return i;
                }
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (!(otherObject instanceof Task)) return false;
        Task task = (Task) otherObject;
        return start.equals(task.start)
                && end.equals(task.end)
                && interval == task.interval
                && active == task.active
                && title.equals(task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, start, end, interval, active);
    }

    @Override
    public String toString() {
        return "Task{"
                + "title='" + title + '\''
                + ", start=" + start
                + ", end=" + end
                + ", interval=" + interval
                + ", active=" + active
                + '}';
    }

    public String showTask() {
        String strTask;
        if (interval == 0) {
            strTask = "Title: " + title +
                    " Time: " + start +
                    " Active: " + (active ? "yes" : "no") + "";
        } else {
            strTask = "Title: " + title
                    + " Start: " + start
                    + " End: " + end
                    + " Interval (sec): " + interval
                    + " Active: " + (active ? "yes" : "no");
        }
        return strTask;
    }

    public Object clone() throws CloneNotSupportedException {
        Task clone = (Task) super.clone();
        clone.title = title;
        clone.start = start;
        clone.end = end;
        clone.interval = interval;
        clone.active = active;
        return clone;
    }
}


