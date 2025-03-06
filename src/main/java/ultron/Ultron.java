package ultron;

import ultron.parser.Parser;
import ultron.storage.Storage;
import ultron.tasks.Tasklist;
import ultron.ui.Ui;

import java.util.Scanner;
import java.io.File;


public class Ultron {

    private static Ui ui;
    private static Storage storage;
    private static Parser parser;
    private static Tasklist tasklist = null;

    public Ultron() {
        ui = new Ui();
        parser = new Parser();
        tasklist = new Tasklist(ui, parser);
        storage = new Storage(ui, tasklist);

    }

    /**
     * Begins the chat with Ultron by displaying welcome message and accepting user input in a command loop until exit
     */

    public void startChat() {
        getStoredTasks();
        System.out.println(ui.ULTRON_FACE);
        ui.helloMessage();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        tasklist.runCommandLoopUntilExit( line, in);
        exit(storage.getTaskStorageFile());
    }

    /**
     * Loads previously saved task data from data directory and loading it into the tasklist used in current user session
     */

    private static void getStoredTasks() {
        File taskStorageFile = storage.getTaskStorageFile();
        storage.loadPreviousTaskData(taskStorageFile);
    }

    private static void exit(File taskStorageFile) {
        storage.updateStoredTasks(taskStorageFile,  tasklist.getTaskList());
        ui.byeMessage();
    }


    public static void main(String[] args) {
        Ultron ultron = new Ultron();
        ultron.startChat();
    }

}
