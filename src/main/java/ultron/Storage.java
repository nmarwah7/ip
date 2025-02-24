package ultron;

import ultron.tasks.Deadline;
import ultron.tasks.Event;
import ultron.tasks.Task;
import ultron.tasks.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private static Ui ui = null; // Store Ui instance

    public Storage(Ui ui) {
        Storage.ui = ui;
    }
    public File getTaskStorageFile() {
        String directoryPath = "data";
        String filePath = directoryPath + "/ultron.txt";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File taskStorageFile = new File(filePath);

        try {
            taskStorageFile.createNewFile();

        } catch (IOException e) {
            ui.errorCreatingStorageFileMessage();
        }
        return taskStorageFile;
    }
    public void loadPreviousTaskData(File taskStorageFile, ArrayList<Task> taskList) {
        try {
            Scanner s = new Scanner(taskStorageFile);
            while(s.hasNext()){
                String line = s.nextLine();
                if(line.trim().isEmpty()){
                    continue;
                }
                String taskType = line.split("|",2)[0];
                String taskDescription = line.split("/description",2)[1];
                Boolean isDoneTask = (!line.split("/done")[1]
                        .split("/description", 2)[0].trim().equals("0")) ;
                switch (taskType) {
                case "T":
                    Ultron.handleTodo(taskDescription, taskList, true);
                    taskList.get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                case "D":
                    Ultron.handleDeadline("deadline"+ taskDescription, taskList, true);
                    taskList.get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                case "E":
                    Ultron.handleEvent("event"+taskDescription, taskList, true);
                    taskList.get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                default:
                    break;
                }
            }
            s.close();
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            ui.errorLoadingMessage();
        }
    }
    public void updateStoredTasks(File taskStorageFile, ArrayList<Task> taskList){
        try {
            FileWriter writer = new FileWriter(taskStorageFile);
            for(int i = 0; i<Task.taskCount;i++) {
                if (taskList.get(i) instanceof Deadline) {
                    writer.write("\n" + "D | /done " + (taskList.get(i).isDone()?"1":"0") + " /description " + taskList.get(i).getDescription() + " /by "
                            + ((Deadline) taskList.get(i)).getBy().trim());
                }
                else if (taskList.get(i) instanceof Todo) {
                    writer.write("\n" + "T | /done " + (taskList.get(i).isDone()?"1":"0") + " /description " + taskList.get(i).getDescription());
                }else if (taskList.get(i) instanceof Event) {
                    writer.write("\n" + "E | /done " + (taskList.get(i).isDone()?"1":"0") + " /description " + taskList.get(i).getDescription()
                            +" /from "+((Event) taskList.get(i)).getFrom().trim()+ " /to "+((Event) taskList.get(i)).getTo().trim());
                }
            }
            writer.close();
        } catch (IOException e) {
            ui.errorSavingUpdatedTaskMessage();
        }
    }
}
