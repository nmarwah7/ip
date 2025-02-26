package ultron.tasks;
/**
 * Subclass of Task type with added parameters: from and to.
 */
public class Event extends Task{
    protected String from;
    protected String to;

    public Event(String description, String from, String to){
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String toString() {
        return "[E]" + super.toString() + " (from: " + from +" to: "+to+ ")";
    }
}
