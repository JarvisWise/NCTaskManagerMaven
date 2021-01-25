package ua.edu.sumdu.j2se.kolisnyk.tasks.model;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import com.google.gson.Gson;

public class TaskIO {
    private static ZoneId zone = ZoneId.systemDefault();


    public static void write(AbstractTaskList tasks, OutputStream out) throws IOException {
        try (DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(out))){
            outStream.writeInt(tasks.size());
            for (Task task : tasks) {
                outStream.writeInt(task.getTitle().length());
                outStream.writeUTF(task.getTitle());
                outStream.writeBoolean(task.isActive());
                outStream.writeInt(task.getRepeatInterval());
                outStream.writeLong(task.getStartTime().atZone(zone).toInstant().toEpochMilli());
                if (task.isRepeated()) {
                    outStream.writeLong(task.getEndTime().atZone(zone).toInstant().toEpochMilli());
                }
            }
        }
    }

    public static void read(AbstractTaskList tasks, InputStream in) throws IOException {
        try (DataInputStream inputStream = new DataInputStream(new BufferedInputStream(in))) {
            int countTask = inputStream.readInt();
            for (int i = 0; i != countTask; i++) {
                int lengthTitle = inputStream.readInt();
                String title = inputStream.readUTF();
                boolean active = inputStream.readBoolean();
                int interval = inputStream.readInt();
                LocalDateTime start = Instant.ofEpochMilli(inputStream.readLong()).atZone(zone).toLocalDateTime();
                Task inTask;
                if (interval == 0) {
                    inTask = new Task(title, start);
                } else {
                    LocalDateTime end = Instant.ofEpochMilli(inputStream.readLong()).atZone(zone).toLocalDateTime();
                    inTask = new Task(title, start, end, interval);
                }
                inTask.setActive(active);
                tasks.add(inTask);
            }
        }
    }

    public static void writeBinary(AbstractTaskList tasks, File file) throws IOException {
        try (FileOutputStream outStream = new FileOutputStream(file)) {
            write(tasks, outStream);
        }
    }

    public static void readBinary(AbstractTaskList tasks, File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            read(tasks, inputStream);
        }
    }

    public static void write(AbstractTaskList tasks, Writer out) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(out)) {
            Gson gson = new Gson();
            String line = gson.toJson(tasks);
            writer.write(line);
        }
    }

    public static void read(AbstractTaskList tasks, Reader in) throws IOException {

        try (BufferedReader reader = new BufferedReader(in)) {
            String text = reader.readLine();
            if(text != null) {
                AbstractTaskList taskList = new Gson().fromJson(text, tasks.getClass());
                for (Task task : taskList) {
                    tasks.add(task);
                }
            }
        }
    }

    public static void writeText(AbstractTaskList tasks, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            write(tasks, fileWriter);
        }
    }

    public static void readText(AbstractTaskList tasks, File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            read(tasks, fileReader);
        }
    }
}

