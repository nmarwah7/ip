package ultron.storage;

import ultron.tasks.Tasklist;
import ultron.tasks.Deadline;
import ultron.tasks.Event;
import ultron.tasks.Task;
import ultron.tasks.Todo;
import ultron.ui.Ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads tasks entered by user as data stored in local memory of device, and retrieves these saved tasks when a new CLI
 * session is opened by the user.
 */
public class Storage {

    private static Ui ui = null;
    private static Tasklist tasklist;


    public Storage(Ui ui, Tasklist tasklist) {
        Storage.ui = ui;
        Storage.tasklist = tasklist;
    }

    /**
     * Returns the file data.txt where previously saved tasks are stored. A new storage file and /data directory is
     * created if they don't exist already.
     * @throws IOException if new file is not successfully created.
     * @return .txt file where task data is stored.
     */
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

    /**
     * Loads all tasks saved in data.txt file into task list initialized in every CLI session. This involves reading
     * data from the file, splitting the lines based on their predefined syntax and calling the handleTasks methods
     * to appropriately restore these tasks.
     * @param taskStorageFile .txt file location where previous task data is saved.
     * @throws ArrayIndexOutOfBoundsException if syntax or formatting error in data saved in the file.
     * @throws FileNotFoundException if data.txt file is not found in the data directory.
     */
    public void loadPreviousTaskData(File taskStorageFile) {
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
                    tasklist.handleTodo(taskDescription, true);
                    tasklist.getTaskList().get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                case "D":
                    tasklist.handleDeadline("deadline"+ taskDescription, true);
                    tasklist.getTaskList().get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                case "E":
                    tasklist.handleEvent("event"+taskDescription, true);
                    tasklist.getTaskList().get(Task.taskCount-1).setDone(isDoneTask);
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

    /**
     * Saves all added tasks and updated tasks back into data.txt at the end of CLI session with user. This involves
     * reformatting all tasks in the task list to match predefined syntax for the data.txt file.
     * @throws IOException if data cannot be saved correctly to file
     */
    public void updateStoredTasks(File taskStorageFile, ArrayList<Task> taskList){
        try {
            FileWriter writer = new FileWriter(taskStorageFile);
            for(int i = 0; i<Task.taskCount;i++) {
                if (taskList.get(i) instanceof Deadline) {
                    writer.write("\n" + "D | /done " + (taskList.get(i).isDone()?"1":"0") + " /description " +
                                taskList.get(i).getDescription() + " /by "
                                + ((Deadline) taskList.get(i)).getBy().trim());
                }
                else if (taskList.get(i) instanceof Todo) {
                    writer.write("\n" + "T | /done " + (taskList.get(i).isDone()?"1":"0") + " /description "
                                + taskList.get(i).getDescription());
                }else if (taskList.get(i) instanceof Event) {
                    writer.write("\n" + "E | /done " + (taskList.get(i).isDone()?"1":"0") + " /description "
                                + taskList.get(i).getDescription()
                                +" /from "+((Event) taskList.get(i)).getFrom().trim()+ " /to "
                                +((Event) taskList.get(i)).getTo().trim());
                }
            }
            writer.close();
        } catch (IOException e) {
            ui.errorSavingUpdatedTaskMessage();
        }
    }
}
