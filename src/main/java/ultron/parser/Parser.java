package ultron.parser;

import ultron.Ultron;
import ultron.exceptions.emptyCommandParameterException;
/**
 * Helps interpret string for user command by breaking it down into parameters or inputs
 */
public class Parser {
    /**
     * Separates the command word from user input, such as "find" or "list".
     */
    public String userCommand(String line) {
        return line.split(" ",2)[0];
    }
    /**
     * Seperates the event parameters of event description and from and to dates from user input.
     * @throws emptyCommandParameterException if any of these parameters are left blank.
     * @return both parameters of event description and from and to dates.
     */
    public Ultron.EventParameters getEventParameters(String line) throws emptyCommandParameterException {
        String eventDescription = line.split("/from ")[0].split("event", 2)[1].trim();
        String eventFrom = line.split("/from ")[1].split("/to ")[0];
        String eventTo = line.split("/from ")[1].split("/to ")[1];
        if(eventDescription.trim().isEmpty()||eventTo.trim().isEmpty()||eventFrom.trim().isEmpty()){
            throw new emptyCommandParameterException();
        }
        return new Ultron.EventParameters(eventDescription, eventFrom, eventTo);
    }

    public record EventParameters(String eventDescription, String eventFrom, String eventTo) {
    }
    /**
     * Seperates the deadline parameters of deadline description and due by dates from user input.
     * @throws emptyCommandParameterException if any of these parameters are left blank.
     * @return both parameters of deadline description and due by date.
     */
    public Ultron.DeadlineParameters getDeadlineParameters(String line) throws emptyCommandParameterException {
        String deadlineDescription = line.split("/by ")[0].split("deadline", 2)[1].trim();
        String deadlineBy = line.split("/by ")[1];
        if(deadlineDescription.trim().isEmpty()||deadlineBy.trim().isEmpty()){
            throw new emptyCommandParameterException();
        }
        return new Ultron.DeadlineParameters(deadlineDescription, deadlineBy);
    }

    public record DeadlineParameters(String deadlineDescription, String deadlineBy) {
    }
    /**
     * Seperates the deadline parameters of to-do description from user input.
     * @throws emptyCommandParameterException if any of these parameters are left blank.
     * @return  parameters of to-do description.
     */
    public String getTodoParameters(String line) throws emptyCommandParameterException {
        String todoDescription = line.split(" ",2)[1].trim();
        if(todoDescription.trim().isEmpty()){
            throw new emptyCommandParameterException();
        }
        return todoDescription;
    }
    /**
     * Gets task number index from a line of user input for delete, mark and unmark commands.
     * @throws emptyCommandParameterException if any of these parameters are left blank
     * @return integer of index at which task is stored in the list.
     */
    public int getTaskNumber(String line) throws emptyCommandParameterException {
        String stringTaskNumber = line.split(" ")[1];
        if(stringTaskNumber.trim().isEmpty()){
            throw new emptyCommandParameterException();
        }
        int taskNumber = Integer.parseInt(stringTaskNumber)-1;
        return taskNumber;
    }

}
