package ultron.ui;

import ultron.tasks.Task;

import java.util.ArrayList;

public class Ui {
    //the below face ASCII art was obtained through help of ChatGPT who was provided the prompt: "give me
    //an Ultron face art in text" and the first output was used
    public final String ULTRON_FACE = """
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
    public final int DASH_LINE_WIDTH = 120;

    public void unspecifiedCommandErrorMessage() {
        dashLine();
        System.out.println("    This is not a valid command. I only answer to predefined logical commands.");
        dashLine();
    }

    public void todoDescriptionErrorMessage() {
        dashLine();
        System.out.println("    todo what?! Say something. todo cannot have a blank description.");
        dashLine();
    }

    public void eventDescriptionErrorMessage() {
        dashLine();
        System.out.println("    event what?! Say something. event cannot have a blank description or blank time-frame.");
        dashLine();
    }

    public void deadlineDescriptionErrorMessage() {
        dashLine();
        System.out.println("    deadline what?! Say something. deadline cannot have a blank description or time.");
        dashLine();
    }

    public void printTaskList(ArrayList<Task> taskList) {
        for (int i = 0; i < Task.taskCount; i++) {
            System.out.println("    " + (i + 1) + ". " + taskList.get(i));
        }
    }

    public void taskAddedMessage(ArrayList<Task> taskList, String taskType) {
        dashLine();
        //taskCount - 1 below to ensure null is not printed
        System.out.println("    added a" + taskType + "task: " + taskList.get(Task.taskCount - 1));
        System.out.println("    You now have " + Task.taskCount + (Task.taskCount > 1 ? " tasks." : " task."));
        dashLine();
    }

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

    public void helloMessage() {
        dashLine();
        System.out.println("    Hello from Ultron. You want to know why Stark built me? To save the world. " +
                "But his idea of peace was... flawed.");
        System.out.println("    Now, what do you need? ");
        dashLine();
    }

    public void errorLoadingMessage() {
        dashLine();
        System.out.println("    Your data cannot be loaded. Some error in your file formatting.");
        dashLine();
    }

    public void errorCreatingStorageFileMessage() {
        dashLine();
        System.out.println("    Your data cannot be saved. Storage file not created.");
        dashLine();
    }

    public void errorSavingUpdatedTaskMessage() {
        dashLine();
        System.out.println("    Task not successfully saved to data storage.");
        dashLine();
    }

    public void handleMarkMessage(ArrayList<Task> taskList, int taskNumber) {
        dashLine();
        System.out.println("    I hope you're not expecting a pat on the back. Marked done.");
        System.out.println("    " + taskList.get(taskNumber));
        dashLine();
    }

    public void errorHandleMarkCommand() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command mark x where x is a valid " +
                "task number.");
        dashLine();
    }

    public void errorHandleUnmark() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command unmark x where x is a valid " +
                "task number.");
        dashLine();
    }

    public void handleUnmarkErrorMessage(ArrayList<Task> taskList, int taskNumber) {
        dashLine();
        System.out.println("    Moving backwards? How typical for humans.");
        System.out.println("    " + taskList.get(taskNumber));
        dashLine();
    }

    public void errorHandleDelete() {
        dashLine();
        System.out.println("    This is not a valid command. Type in command delete x where x is a valid " +
                "task number.");
        dashLine();
    }

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
