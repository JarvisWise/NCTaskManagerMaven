package ua.edu.sumdu.j2se.kolisnyk.tasks.model;

import ua.edu.sumdu.j2se.kolisnyk.tasks.constant.strings.ExceptionMessage;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class ArrayTaskList extends AbstractTaskList
 * and implement task list using array
 */

public class ArrayTaskList extends AbstractTaskList {

    private static final int START_LENGTH = 10;
    private Task[] array;

    public ArrayTaskList() {
        array = new Task[START_LENGTH];
        size = 0;
        type = ListTypes.types.ARRAY;
    }

    public ArrayTaskList(ArrayTaskList list) {

        if (list == null) {
            throw new NullPointerException(ExceptionMessage.LIST_NOT_NULL.getMessage());
        }

        array = new Task[list.array.length];
        size = list.size;
        System.arraycopy(list.array, 0, array, 0, size);
        type = ListTypes.types.ARRAY;
    }

    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (size < array.length) {
            array[size++] = task;
        } else {
            Task[] newArray = new Task[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
            array[size++] = task;
        }
    }

    public boolean remove(Task task) {
        if (task == null) {
            return false;
        } else {

            for (int i = 0; i != size; i++) {
                if (task.equals(array[i])) {
                    System.arraycopy(array, i + 1, array, i, size - (i + 1));
                    array[size--] = null;
                    return true;
                }
            }
            return false;
        }
    }

    public Task getTask(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(ExceptionMessage.INCORRECT_INDEX.getMessage());
        } else {
            return array[index];
        }
    }

    public void setTask(Task oldValue, Task newValue) {
        if (oldValue != null || newValue != null) {
            int i = 0;
            for (Task task : this) {
                if (task.equals(oldValue)) {
                    array[i] = newValue;
                }
                i++;
            }
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (!(otherObject instanceof ArrayTaskList)) return false;

        ArrayTaskList tasks = (ArrayTaskList) otherObject;
        return (size == tasks.size) && Arrays.equals(array, tasks.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("ArrayTaskList ( size = " + size + " ){\n");
        int i = 0;
        if (i != size) {
            str.append(array[i++]);
        }
        while (i != size) {
            str.append(",\n").append(array[i++]);
        }
        str.append("\n};");
        return str.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        ArrayTaskList clone = (ArrayTaskList) super.clone();
        clone.array = Arrays.copyOf(array, size);
        clone.size = size;
        return clone;
    }

    @Override
    public Iterator<Task> iterator() {
        return new ArrayListTaskIterator();
    }

    private class ArrayListTaskIterator implements Iterator<Task> {

        private int cursor = 0;
        private int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public Task next() {
            if (cursor >= size) {
                throw new NoSuchElementException(ExceptionMessage.INTERVAL_OUT.getMessage());
            }
            lastRet = cursor;
            return array[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException(ExceptionMessage.INTERVAL_OUT.getMessage());
            }
            ArrayTaskList.this.remove(array[lastRet]);
            cursor = lastRet--;
        }
    }
}

