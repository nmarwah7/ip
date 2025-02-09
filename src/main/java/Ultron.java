import java.util.Arrays;
import java.util.Scanner;

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

    public static void main(String[] args) {
        Task[] taskList = new Task[100];
        System.out.println(ULTRON_FACE);
        helloMessage();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String command = line.split(" ",2)[0];
        //the below switch-case refactoring is inspired by @James17042002
        while(!command.equals("bye")){
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
                handleTodo(line, taskList);
                taskAddedMessage(taskList, " todo ");
                break;
            case "deadline":
                handleDeadline(line, taskList);
                taskAddedMessage(taskList, " deadline ");
                break;
            case "event":
                handleEvent(line, taskList);
                taskAddedMessage(taskList, " event ");
                break;
            default:
                taskList[Task.taskCount] = new Task(line);
                taskAddedMessage(taskList, " ");
                break;
            }
            line = in.nextLine();
            command = line.split(" ",2)[0];
        }
        byeMessage();
    }

    private static void handleEvent(String line, Task[] taskList) {
        String eventDescription = line.split("/from ")[0].split("event", 2)[1].trim();
        String eventFrom = line.split("/from ")[1].split("/to ")[0];
        String eventTo = line.split("/from ")[1].split("/to ")[1];
        taskList[Task.taskCount] = new Event(eventDescription, eventFrom, eventTo);
    }

    private static void handleDeadline(String line, Task[] taskList) {
        String deadlineDescription = line.split("/by ")[0].split("deadline", 2)[1].trim();
        String deadlineBy = line.split("/by ")[1];
        taskList[Task.taskCount] = new Deadline(deadlineDescription, deadlineBy);
    }

    private static void handleTodo(String line, Task[] taskList) {
        String todoDescription = line.split(" ",2)[1];
        taskList[Task.taskCount] = new Todo(todoDescription);
    }

    private static void handleUnmark(String line, Task[] taskList) {
        String stringTaskNumber = line.split(" ")[1];
        int taskNumber = Integer.parseInt(stringTaskNumber)-1;
        if(taskNumber>=Task.taskCount||taskNumber<0){
            outOfBoundsMessage();
        }else {
            taskList[taskNumber].setDone(false);
            dashLine();
            System.out.println("    Moving backwards? How typical for humans.");
            System.out.println("    " + taskList[taskNumber]);
            dashLine();
        }
    }

    private static void handleMark(String line, Task[] taskList) {
        String stringTaskNumber = line.split(" ")[1];
        int taskNumber = Integer.parseInt(stringTaskNumber)-1;
        if(taskNumber>=Task.taskCount||taskNumber<0){
            outOfBoundsMessage();
        }else {
            taskList[taskNumber].setDone(true);
            dashLine();
            System.out.println("    I hope you're not expecting a pat on the back. Marked done.");
            System.out.println("    " + taskList[taskNumber]);
            dashLine();
        }
    }

    private static void printTaskList(Task[] taskList) {
        for(int i = 0; i<Task.taskCount;i++){
            System.out.println("    "+(i+1)+". "+ taskList[i]);
        }
    }

    private static void taskAddedMessage(Task[] taskList, String taskType) {
        dashLine();
        //taskCount - 1 below to ensure null is not printed
        System.out.println("    added a" +taskType+ "task: " + taskList[Task.taskCount-1]);
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
        System.out.println("So you think you're funny? You don't even have that many tasks. Out of bounds.");
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
