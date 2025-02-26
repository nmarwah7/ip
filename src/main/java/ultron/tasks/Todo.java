package ultron.tasks;
/**
 * Subclass of Task type with no added parameters
 */
public class Todo extends Task{
    public Todo(String description){
        super(description);
    }

    @Override
    public String toString() {
        return "[T]"+super.toString();
    }
}
