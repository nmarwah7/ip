package ultron.tasks;
/**
 * General parent class for Task type with 2 parameters: description and done status
 */
public class Task {
    protected String description;
    protected boolean isDone;
    //a class-level attribute to help quantify total number of created tasks
    public static int taskCount = 0;
    public Task(){
        this.description="";
        taskCount++;
    }

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        taskCount++;
    }
    /**
     * Prints out X if task is done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
    /**
     * Prints out task description and done status.
     */
    public String toString() {
        return "["+getStatusIcon()+"] "+getDescription();
    }
}
