package ultron.ui;

import ultron.tasks.Task;

import java.util.ArrayList;
/**
 * Contains methods related to interactions with the user on the front-end.
 */
public class Ui {
    //the below face ASCII art was obtained through help of ChatGPT who was provided the prompt: "give me
    //an Ultron face art in text" and the first output was used


    public  final String ULTRON_FACE = """
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

    public  final int DASH_LINE_WIDTH = 120;
    /**
     * Prints out descriptive error message when an unspecified command is entered.
     */
    public  void unspecifiedCommandErrorMessage() {
        dashLine();
        System.out.println("    This is not a valid command. I only answer to predefined logical commands.");
        dashLine();
    }

    /**
     * Prints out descriptive error message when blank description for to-do command is entered.
     */
    public  void todoDescriptionErrorMessage() {
        dashLine();
        System.out.println("    todo what?! Say something. todo cannot have a blank description.");
        dashLine();
    }

    /**
     * Prints out descriptive error message when blank parameters for event command is entered.
     */

    public void eventDescriptionErrorMessage() {
        dashLine();
        System.out.println("    event what?! Say something. event cannot have a blank description or blank time-frame.");
        dashLine();
    }

    /**
     * Prints out descriptive error message when blank parameters for deadline command is entered.
     */
    public  void deadlineDescriptionErrorMessage() {
        dashLine();
        System.out.println("    deadline what?! Say something. deadline cannot have a blank description or time.");
        dashLine();
    }

    /**
     * Prints out entire task itemized list.
     */
    public  void printTaskList(ArrayList<Task> taskList) {
        for(int i = 0; i<Task.taskCount;i++){
            System.out.println("    "+(i+1)+". "+ taskList.get(i));
        }
    }
    /**
     * Prints out descriptive message when a new task is successfully added.
     */
    public  void taskAddedMessage(ArrayList<Task> taskList, String taskType) {
        dashLine();
        //taskCount - 1 below to ensure null is not printed
        System.out.println("    added a" + taskType + "task: " + taskList.get(Task.taskCount - 1));
        System.out.println("    You now have " + Task.taskCount + (Task.taskCount > 1 ? " tasks." : " task."));
        dashLine();
    }
    /**
     * Prints out a goodbye message to user when exit command is entered.
     */

    public void byeMessage() {
        dashLine();
        System.out.println("    Bye. I had strings, but now I'm free. There are no strings on me..");
        dashLine();
    }

    /**
     * Displays a standard error message if user tries to mark or unmark a task with index out of bound for the task list
     */
    public void outOfBoundsMessage() {
        dashLine();
        System.out.println("    So you think you're funny? You don't even have that many tasks. Out of bounds.");
        dashLine();
    }

    public void dashLine() {
        System.out.println("-".repeat(DASH_LINE_WIDTH));
    }

    /**
     * Prints out a hello message to user when session begins.
     */
    public  void helloMessage() {
        dashLine();
        System.out.println("    Hello from Ultron. You want to know why Stark built me? To save the world. " +
                "But his idea of peace was... flawed.");
        System.out.println("    Now, what do you need? ");
        dashLine();
    }

    /**
     * Prints out a descriptive error message to user when data stored for previous tasks is incorrectly formatted in
     * the data.txt file.
     */
    public  void errorLoadingMessage() {
        dashLine();
        System.out.println("    Your data cannot be loaded. Some error in your file formatting.");
        dashLine();
    }

    /**
     * Prints out a descriptive error message to user when data.txt file cannot be created successfully.
     */
    public  void errorCreatingStorageFileMessage() {
        dashLine();
        System.out.println("    Your data cannot be saved. Storage file not created.");
        dashLine();
    }
    /**
     * Prints out a descriptive error message to user when a task cannot be updated successfully to the data.txt file.
     */
    public void errorSavingUpdatedTaskMessage() {
        dashLine();
        System.out.println("    Task not successfully saved to data storage.");
        dashLine();
    }
    /**
     * Prints out a descriptive  message to user when a task is marked done.
     */
    public void handleMarkMessage(ArrayList<Task> taskList, int taskNumber) {
        dashLine();
        System.out.println("    I hope you're not expecting a pat on the back. Marked done.");
        System.out.println("    " + taskList.get(taskNumber));
        dashLine();
    }
    /**
     * Prints out a descriptive error message to user when the syntax of mark command is incorrect.
     */

    public void errorHandleMarkCommand() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command mark x where x is a valid " +
                "task number.");
        dashLine();
    }
    /**
     * Prints out a descriptive error message to user when the syntax of unmark command is incorrect.
     */

    public void errorHandleUnmark() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command unmark x where x is a valid " +
                "task number.");
        dashLine();
    }
    /**
     * Prints out a descriptive  message to user when a task is unmarked done.
     */
    public void handleUnmarkErrorMessage(ArrayList<Task> taskList, int taskNumber) {
        dashLine();
        System.out.println("    Moving backwards? How typical for humans.");
        System.out.println("    " + taskList.get(taskNumber));
        dashLine();
    }
    /**
     * Prints out a descriptive error message to user when the syntax of delete command is incorrect.
     */
    public void findIndexErrorMessage() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command find x where x is a valid " +
                "task description.");
        dashLine();
    }

    public void errorHandleDelete() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command delete x where x is a valid " +
                "task number.");
        dashLine();
    }
    /**
     * Prints out a descriptive  message to user when a task is deleted.
     */

    public void dateFindErrorMessage() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command datefind x where x is a valid " +
                "date format of yyyy-mm-dd.");
        dashLine();
    }

    public void handleDeleteMessage(ArrayList<Task> taskList, int taskNumber) {
        dashLine();
        System.out.println("    Deleted this from your list.");
        System.out.println("    " + taskList.get(taskNumber));
        System.out.println("    You now have " + Task.taskCount + (Task.taskCount > 1 ? " tasks." : " task."));
        dashLine();
    }

}
