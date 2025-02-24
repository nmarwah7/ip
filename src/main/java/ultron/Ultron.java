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

    private static Ui ui;
    public Ultron(){
        ui = new Ui();
    }
    public void startChat(){
        ArrayList<Task> taskList= new ArrayList<>();
        File taskStorageFile = getTaskStorageFile();
        loadPreviousTaskData(taskStorageFile, taskList);
        System.out.println(ui.ULTRON_FACE);
        ui.helloMessage();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String command = line.split(" ",2)[0];
        //the below switch-case refactoring is inspired by @James17042002
        while(!command.equals("bye")){
            try {
                switch (command){
                case "list":
                    ui.dashLine();
                    ui.printTaskList(taskList);
                    ui.dashLine();
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
                ui.unspecifiedCommandErrorMessage();
            }
            line = in.nextLine();
            command = line.split(" ",2)[0];
        }
        updateStoredTasks(taskStorageFile,taskList);
        ui.byeMessage();
    }
    public static void main(String[] args){
        Ultron ultron = new Ultron();
        ultron.startChat();
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
            ui.errorLoadingMessage();
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
            ui.errorCreatingStorageFileMessage();
        }
        return taskStorageFile;
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
                ui.taskAddedMessage(taskList, " event ");
            }
        } catch (emptyCommandParameterException|ArrayIndexOutOfBoundsException e) {
            ui.eventDescriptionErrorMessage();
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
                ui.taskAddedMessage(taskList, " deadline ");
            }        } catch (emptyCommandParameterException|ArrayIndexOutOfBoundsException e) {
            ui.deadlineDescriptionErrorMessage();
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
                ui.taskAddedMessage(taskList, " todo ");
            }
        } catch (emptyCommandParameterException|ArrayIndexOutOfBoundsException e) {
            ui.todoDescriptionErrorMessage();
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
                ui.errorSavingUpdatedTaskMessage();
            }
        }


    private static void handleUnmark(String line, ArrayList<Task> taskList) {
        try {
            String stringTaskNumber = line.split(" ")[1];
            if(stringTaskNumber.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }
            int taskNumber = Integer.parseInt(stringTaskNumber)-1;
            if(taskNumber>=Task.taskCount||taskNumber<0){
                ui.outOfBoundsMessage();
            }else {
                taskList.get(taskNumber).setDone(false);
                ui.handleUnmarkErrorMessage(taskList, taskNumber);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            ui.errorHandleUnmark();
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
                ui.outOfBoundsMessage();
            }else {
                taskList.get(taskNumber).setDone(true);
                ui.handleMarkMessage(taskList, taskNumber);
            }
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|emptyCommandParameterException e) {
            ui.errorHandleMarkCommand();
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
                ui.outOfBoundsMessage();
            }else {
                Task.taskCount--;
                ui.handleDeleteMessage(taskList, taskNumber);
                taskList.remove(taskNumber);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            ui.errorHandleDelete();
        }
    }
}
