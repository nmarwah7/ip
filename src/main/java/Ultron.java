import java.util.Arrays;
import java.util.Scanner;

public class Ultron {
    /**
     * Displays a standard error message if user tries to mark or unmark a task with index out of bound for the task list
     */
    public static void outOfBoundsMessage(){
        System.out.println("-".repeat(120));
        System.out.println("So you think you're funny? You don't even have that many tasks. Out of bounds.");
        System.out.println("-".repeat(120));
    }
    public static void main(String[] args) {
        String logo = """
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
        System.out.println(logo);
        Scanner in = new Scanner(System.in);
        String line;
        Task[] taskList = new Task[100];
        System.out.println("-".repeat(120));
        System.out.println("    Hello from Ultron. You want to know why Stark built me? To save the world. " +
                    "But his idea of peace was... flawed.");
        System.out.println("    Now, what do you need? ");
        System.out.println("-".repeat(120));
        line = in.nextLine();
        while(!line.equals("bye")){
            if(line.equals("list")){
                System.out.println("-".repeat(120));
                for(int i = 0; i<Task.taskCount;i++){
                    System.out.println("    "+(i+1)+". "+taskList[i]);
                }
                System.out.println("-".repeat(120));
            } else if(line.contains("unmark")){
                //checking for unmark before mark to avoid it being mistakenly identified as just "mark"
                String stringTaskNumber = line.split(" ")[1];
                int taskNumber = Integer.parseInt(stringTaskNumber)-1;
                if(taskNumber>=Task.taskCount){
                    outOfBoundsMessage();
                }else {
                    taskList[taskNumber].setDone(false);
                    System.out.println("-".repeat(120));
                    System.out.println("    Moving backwards? How typical for humans.");
                    System.out.println("    " + taskList[taskNumber]);
                    System.out.println("-".repeat(120));
                }
            }else if (line.contains("mark")) {
                String stringTaskNumber = line.split(" ")[1];
                int taskNumber = Integer.parseInt(stringTaskNumber)-1;
                if(taskNumber>=Task.taskCount){
                    outOfBoundsMessage();
                }else {
                    taskList[taskNumber].setDone(true);
                    System.out.println("-".repeat(120));
                    System.out.println("    I hope you're not expecting a pat on the back. Marked done.");
                    System.out.println("    " + taskList[taskNumber]);
                    System.out.println("-".repeat(120));
                }
            }else if(line.contains("todo")){
                String todoDescription = line.split(" ",2)[1];
                taskList[Task.taskCount] = new Todo(todoDescription);
                System.out.println("-".repeat(120));
                System.out.println("    added a todo task: " + taskList[Task.taskCount-1]);
                System.out.println("-".repeat(120));
            } else if(line.contains("deadline")){
                String deadlineDescription = line.split("/by ")[0].split(" ")[1];
                String deadlineBy = line.split("/by ")[1];
                taskList[Task.taskCount] = new Deadline(deadlineDescription, deadlineBy);
                System.out.println("-".repeat(120));
                //taskCount - 1 below to ensure null is not printed
                System.out.println("    added a deadline task: " + taskList[Task.taskCount-1]);
                System.out.println("-".repeat(120));
            } else if(line.contains("event")){
                String eventDescription = line.split("/from ")[0].split(" ")[1];
                String eventFrom = line.split("/from ")[1].split("/to ")[0];
                String eventTo = line.split("/from ")[1].split("/to ")[1];
                taskList[Task.taskCount] = new Event(eventDescription, eventFrom, eventTo);
                System.out.println("-".repeat(120));
                //taskCount - 1 below to ensure null is not printed
                System.out.println("    added an event task: " + taskList[Task.taskCount-1]);
                System.out.println("-".repeat(120));
            } else { //this code should be redundant and removed
                taskList[Task.taskCount] = new Task(line);
                System.out.println("-".repeat(120));
                System.out.println("    added: " + line);
                System.out.println("-".repeat(120));
            }
            line = in.nextLine();
        }
        System.out.println("-".repeat(120));
        System.out.println("    Bye. I had strings, but now I'm free. There are no strings on me..");
        System.out.println("-".repeat(120));
    }
}
