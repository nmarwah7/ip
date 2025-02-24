package ultron.parser;

import ultron.Ultron;
import ultron.exceptions.emptyCommandParameterException;

public class Parser {
    public String userCommand(String line) {
        return line.split(" ",2)[0];
    }
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
    public String getTodoParameters(String line) throws emptyCommandParameterException {
        String todoDescription = line.split(" ",2)[1];
        if(todoDescription.trim().isEmpty()){
            throw new emptyCommandParameterException();
        }
        return todoDescription;
    }

    public int getTaskNumber(String line) throws emptyCommandParameterException {
        String stringTaskNumber = line.split(" ")[1];
        if(stringTaskNumber.trim().isEmpty()){
            throw new emptyCommandParameterException();
        }
        int taskNumber = Integer.parseInt(stringTaskNumber)-1;
        return taskNumber;
    }

}
