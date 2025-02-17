package ultron;
import ultron.exceptions.emptyCommandParameterException;
import ultron.exceptions.unspecifiedCommandException;
import ultron.tasks.Deadline;
import ultron.tasks.Event;
import ultron.tasks.Task;
import ultron.tasks.Todo;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Ultron {
    //the below face ASCII art was obtained through help of ChatGPT who was provided the prompt: "give me
    //an Ultron face art in text" and the first output was used
    public static final String ULTRON_FACE = """
                   ______
                .-'      `-.
               /            \\
              |,  .-.  .-.  ,|
              | )(_o/  \\o_)( |
              |/     /\\     \\|
              (_     ^^     _)
               \\__|IIIIII|__/
                | \\IIIIII/ |
                \\          /
                 `--------`
            """;
    public static final int DASH_LINE_WIDTH = 120;

    public static void main(String[] args){

        ArrayList<Task> taskList= new ArrayList<>();
        File taskStorageFile = getTaskStorageFile();
        loadPreviousTaskData(taskStorageFile, taskList);
        System.out.println(ULTRON_FACE);
        helloMessage();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String command = line.split(" ",2)[0];
        //the below switch-case refactoring is inspired by @James17042002
        while(!command.equals("bye")){
            try {
                switch (command){
                case "list":
                    dashLine();
                    printTaskList(taskList);
                    dashLine();
                    break;
                case "mark":
                    handleMark(line, taskList);
                    break;
                case "unmark":
                    handleUnmark(line, taskList);
                    break;
                case "todo":
                    //error-handling within the handle function
                    handleTodo(line, taskList, false);
                    break;
                case "deadline":
                    handleDeadline(line, taskList, false);
                    break;
                case "event":
                    handleEvent(line, taskList, false);
                    break;
                case "delete":
                    handleDelete(line, taskList);
                    break;
                default:
                    throw new unspecifiedCommandException();
                }
            } catch (unspecifiedCommandException e) {
                unspecifiedCommandErrorMessage();
            }
            line = in.nextLine();
            command = line.split(" ",2)[0];
        }
        updateStoredTasks(taskStorageFile,taskList);
        byeMessage();
    }

    private static void loadPreviousTaskData(File taskStorageFile, ArrayList<Task> taskList) {
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
                    handleTodo(taskDescription, taskList, true);
                    taskList.get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                case "D":
                    handleDeadline("deadline"+ taskDescription, taskList, true);
                    taskList.get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                case "E":
                    handleEvent("event"+taskDescription, taskList, true);
                    taskList.get(Task.taskCount-1).setDone(isDoneTask);
                    break;
                default:
                    break;
                }
            }
            s.close();
        } catch (FileNotFoundException|ArrayIndexOutOfBoundsException e) {
            dashLine();
            System.out.println("    Your data cannot be loaded. Some error in your file formatting.");
            dashLine();
        }
    }

    private static File getTaskStorageFile() {
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
            dashLine();
            System.out.println("    Your data cannot be saved. Storage file not created.");
            dashLine();
        }
        return taskStorageFile;
    }

    private static void unspecifiedCommandErrorMessage() {
        dashLine();
        System.out.println("    This is not a valid command. I only answer to predefined logical commands.");
        dashLine();
    }

    private static void todoDescriptionErrorMessage() {
        dashLine();
        System.out.println("    todo what?! Say something. todo cannot have a blank description.");
        dashLine();
    }
    private static void eventDescriptionErrorMessage() {
        dashLine();
        System.out.println("    event what?! Say something. event cannot have a blank description or blank time-frame.");
        dashLine();
    }
    private static void deadlineDescriptionErrorMessage() {
        dashLine();
        System.out.println("    deadline what?! Say something. deadline cannot have a blank description or time.");
        dashLine();
    }


    private static void handleEvent(String line, ArrayList<Task> taskList, boolean inStoredTask) {
        try {
            String eventDescription = line.split("/from ")[0].split("event", 2)[1].trim();
            String eventFrom = line.split("/from ")[1].split("/to ")[0];
            String eventTo = line.split("/from ")[1].split("/to ")[1];
            if(eventDescription.trim().isEmpty()||eventTo.trim().isEmpty()||eventFrom.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }

            taskList.add(Task.taskCount, new Event(eventDescription, eventFrom, eventTo));
            if (!inStoredTask) {
                taskAddedMessage(taskList, " event ");
            }
        } catch (emptyCommandParameterException|ArrayIndexOutOfBoundsException e) {
            eventDescriptionErrorMessage();
        }
    }


    private static void handleDeadline(String line, ArrayList<Task> taskList, boolean inStoredTask) {
        try {
            String deadlineDescription = line.split("/by ")[0].split("deadline", 2)[1].trim();
            String deadlineBy = line.split("/by ")[1];
            if(deadlineDescription.trim().isEmpty()||deadlineBy.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }

            taskList.add(Task.taskCount, new Deadline(deadlineDescription, deadlineBy));
            if (!inStoredTask) {
                taskAddedMessage(taskList, " deadline ");
            }        } catch (emptyCommandParameterException|ArrayIndexOutOfBoundsException e) {
            deadlineDescriptionErrorMessage();
        }
    }


    private static void handleTodo(String line, ArrayList<Task> taskList, boolean inStoredTask) {
        try {
            String todoDescription = line.split(" ",2)[1];
            if(todoDescription.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }

            taskList.add(Task.taskCount, new Todo(todoDescription));
            if (!inStoredTask) {
                taskAddedMessage(taskList, " todo ");
            }
        } catch (emptyCommandParameterException|ArrayIndexOutOfBoundsException e) {
            todoDescriptionErrorMessage();
        }
    }


    private static void updateStoredTasks(File taskStorageFile, ArrayList<Task> taskList){
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
                dashLine();
                System.out.println("    Task not successfully saved to data storage.");
                dashLine();                }
        }



    private static void handleUnmark(String line, ArrayList<Task> taskList) {
        try {
            String stringTaskNumber = line.split(" ")[1];
            if(stringTaskNumber.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }
            int taskNumber = Integer.parseInt(stringTaskNumber)-1;
            if(taskNumber>=Task.taskCount||taskNumber<0){
                outOfBoundsMessage();
            }else {
                taskList.get(taskNumber).setDone(false);
                dashLine();
                System.out.println("    Moving backwards? How typical for humans.");
                System.out.println("    " + taskList.get(taskNumber));
                dashLine();
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            dashLine();
            System.out.println("    This is not a valid command. Type in command unmark x where x is a valid " +
                                "task number.");
            dashLine();
        }
    }

    private static void handleMark(String line, ArrayList<Task> taskList) {
        try {
            String stringTaskNumber = line.split(" ")[1];
            int taskNumber = Integer.parseInt(stringTaskNumber)-1;
            if(stringTaskNumber.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }
            if(taskNumber>=Task.taskCount||taskNumber<0){
                outOfBoundsMessage();
            }else {
                taskList.get(taskNumber).setDone(true);
                dashLine();
                System.out.println("    I hope you're not expecting a pat on the back. Marked done.");
                System.out.println("    " + taskList.get(taskNumber));
                dashLine();
            }
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|emptyCommandParameterException e) {
            dashLine();
            System.out.println("    This is not a valid command. Type in command mark x where x is a valid " +
                    "task number.");
            dashLine();
        }
    }

    private static void handleDelete(String line, ArrayList<Task> taskList) {
        try {
            String stringTaskNumber = line.split(" ")[1];
            if(stringTaskNumber.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }
            int taskNumber = Integer.parseInt(stringTaskNumber)-1;
            if(taskNumber>=Task.taskCount||taskNumber<0){
                outOfBoundsMessage();
            }else {
                Task.taskCount--;
                dashLine();
                System.out.println("    Deleted this from your list.");
                System.out.println("    " + taskList.get(taskNumber));
                System.out.println("    You now have "+Task.taskCount+(Task.taskCount>1?" tasks.":" task."));
                dashLine();
                taskList.remove(taskNumber);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            dashLine();
            System.out.println("    This is not a valid command. Type in command unmark x where x is a valid " +
                    "task number.");
            dashLine();
        }
    }

    private static void printTaskList(ArrayList<Task> taskList) {
        for(int i = 0; i<Task.taskCount;i++){
            System.out.println("    "+(i+1)+". "+ taskList.get(i));
        }
    }

    private static void taskAddedMessage(ArrayList<Task> taskList, String taskType) {
        dashLine();
        //taskCount - 1 below to ensure null is not printed
        System.out.println("    added a" +taskType+ "task: " + taskList.get(Task.taskCount - 1));
        System.out.println("    You now have "+Task.taskCount+(Task.taskCount>1?" tasks.":" task."));
        dashLine();
    }

    private static void byeMessage() {
        dashLine();
        System.out.println("    Bye. I had strings, but now I'm free. There are no strings on me..");
        dashLine();
    }

    /**
     * Displays a standard error message if user tries to mark or unmark a task with index out of bound for the task list
     */
    public static void outOfBoundsMessage(){
        dashLine();
        System.out.println("    So you think you're funny? You don't even have that many tasks. Out of bounds.");
        dashLine();
    }

    private static void dashLine() {
        System.out.println("-".repeat(DASH_LINE_WIDTH));
    }

    private static void helloMessage() {
        dashLine();
        System.out.println("    Hello from Ultron. You want to know why Stark built me? To save the world. " +
                    "But his idea of peace was... flawed.");
        System.out.println("    Now, what do you need? ");
        dashLine();
    }

}
