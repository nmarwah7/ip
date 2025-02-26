package ultron;

import ultron.exceptions.unspecifiedCommandException;
import ultron.parser.Parser;
import ultron.storage.Storage;
import ultron.tasks.Task;
import ultron.tasks.Tasklist;
import ultron.ui.Ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
;

public class Ultron {

    private static Ui ui;
    private static Storage storage;
    private static Parser parser;
    private static Tasklist tasklist = null;
    public Ultron(){
        ui = new Ui();
        parser = new Parser();
        tasklist = new Tasklist(ui,parser);
        storage = new Storage(ui,tasklist);


    }
    public void startChat(){
        Tasks populatedTasks = getTasks();
        System.out.println(ui.ULTRON_FACE);
        ui.helloMessage();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        runCommandLoopUntilExit(populatedTasks.taskList(), line, in);
        exit(populatedTasks.taskStorageFile(), populatedTasks.taskList());
    }

    private static Tasks getTasks() {
        ArrayList<Task> taskList= new ArrayList<>();
        File taskStorageFile = storage.getTaskStorageFile();
        storage.loadPreviousTaskData(taskStorageFile, taskList);
        return new Tasks(taskList, taskStorageFile);
    }

    private record Tasks(ArrayList<Task> taskList, File taskStorageFile) {
    }


    private static void exit(File taskStorageFile, ArrayList<Task> taskList) {
        storage.updateStoredTasks(taskStorageFile, taskList);
        ui.byeMessage();
    }

    private static void runCommandLoopUntilExit( ArrayList<Task> taskList, String line, Scanner in) {
        String command = parser.userCommand(line);
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
                    tasklist.handleMark(line, taskList);
                    break;
                case "unmark":
                    tasklist.handleUnmark(line, taskList);
                    break;
                case "todo":
                    //error-handling within the handle function
                    tasklist.handleTodo(line, taskList, false);
                    break;
                case "deadline":
                    tasklist.handleDeadline(line, taskList, false);
                    break;
                case "event":
                    tasklist.handleEvent(line, taskList, false);
                    break;
                case "delete":
                    tasklist.handleDelete(line, taskList);
                    break;
                case "find":
                    tasklist.handleFind(line, taskList);
                    break;
                default:
                    throw new unspecifiedCommandException();
                }
            } catch (unspecifiedCommandException e) {
                ui.unspecifiedCommandErrorMessage();
            }
            line = in.nextLine();
            command = parser.userCommand(line);
        }
    }

    public static void main(String[] args){
        Ultron ultron = new Ultron();
        ultron.startChat();
    }

    public record EventParameters(String eventDescription, String eventFrom, String eventTo) {
    }

    public record DeadlineParameters(String deadlineDescription, String deadlineBy) {
    }

}
