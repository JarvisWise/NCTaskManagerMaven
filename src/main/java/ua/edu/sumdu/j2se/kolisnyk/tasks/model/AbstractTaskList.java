package ua.edu.sumdu.j2se.kolisnyk.tasks.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Class AbstractTaskList is template for
 * list with objects of type Task
 */

public abstract class AbstractTaskList implements Iterable<Task>, Cloneable, Serializable {

    private static final long serialVersionUID = 1;
    protected int size;
    protected ListTypes.types type;


    public abstract void add(Task task);

    public abstract boolean remove(Task task);

    public int size() {
        return size;
    }

    public abstract Task getTask(int index);

    public abstract void setTask(Task oldValue, Task newValue);

    public Stream<Task> getStream() {
        Stream.Builder<Task> streamBuilder = Stream.builder();
        for (Task task : this) {
            streamBuilder.add(task);
        }
        return streamBuilder.build();
    }

    public final AbstractTaskList incoming(LocalDateTime from, LocalDateTime to) {

        if (from == null || to == null) {
            throw new NullPointerException("Time  cannot be null!");
        }

        if (to.compareTo(from) < 0) {
            throw new IllegalArgumentException("From time  cannot be less then to time!");
        }

        AbstractTaskList list = TaskListFactory.createTaskList(type);
        getStream().filter((t) -> t.nextTimeAfter(from) != null && t.nextTimeAfter(from).compareTo(to) <= 0)
                .forEach(list::add);

        return list;
    }
}

