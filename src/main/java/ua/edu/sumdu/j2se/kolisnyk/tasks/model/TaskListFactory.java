package ua.edu.sumdu.j2se.kolisnyk.tasks.model;

public class TaskListFactory {
    public static AbstractTaskList createTaskList(ListTypes.types type) {
        AbstractTaskList list = null;
        switch (type) {
            case ARRAY:
                list = new ArrayTaskList();
                break;
            case LINKED:
                list = new LinkedTaskList();
                break;
        }
        return list;
    }
}
