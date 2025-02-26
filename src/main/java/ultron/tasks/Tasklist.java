package ultron.tasks;

import ultron.Ultron;
import ultron.exceptions.emptyCommandParameterException;
import ultron.parser.Parser;
import ultron.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;

public class Tasklist {
    private static Ui ui = null; // Store Ui instance

    private static Parser parser;

    public Tasklist(Ui ui, Parser parser) {
        Tasklist.ui = ui;
        Tasklist.parser = parser;
    }

    public ArrayList<Task> taskList = new ArrayList<>();

    public void handleEvent(String line, ArrayList<Task> taskList, boolean inStoredTask) {
        try {
            Ultron.EventParameters parsedParams = parser.getEventParameters(line);

            taskList.add(Task.taskCount, new Event(parsedParams.eventDescription(), parsedParams.eventFrom(), parsedParams.eventTo()));
            if (!inStoredTask) {
                ui.taskAddedMessage(taskList, " event ");
            }
        } catch (emptyCommandParameterException | ArrayIndexOutOfBoundsException e) {
            ui.eventDescriptionErrorMessage();
        }
    }

    public void findDeadlineByDate(ArrayList<Task> taskList, String line) {
        try {
            String date = (line.split(" ", 2)[1]);
            if (date.trim().isEmpty()) {
                throw new emptyCommandParameterException();
            }
            ArrayList<Task> filteredList = new ArrayList<>();
            int filteredIndex = 0;
            for (int i = 0; i < Task.taskCount; i++) {
                if (taskList.get(i) instanceof Deadline) {
                    if (((Deadline) taskList.get(i)).getBy().equals(date.trim())) {
                        filteredList.add(taskList.get(i));
                        filteredIndex++;
                        ;
                    }
                }
            }
            ui.dashLine();
            for (int k = 0; k < filteredIndex; k++) {
                System.out.println("    " + (k + 1) + ". " + filteredList.get(k));
            }
            ui.dashLine();
        } catch (ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            ui.dateFindErrorMessage();
        }
    }

    public void handleDeadline(String line, ArrayList<Task> taskList, boolean inStoredTask) {
        try {
            Ultron.DeadlineParameters parsedParams = parser.getDeadlineParameters(line);

            taskList.add(Task.taskCount, new Deadline(parsedParams.deadlineDescription(), parsedParams.deadlineBy()));
            if (!inStoredTask) {
                ui.taskAddedMessage(taskList, " deadline ");
            }
        } catch (emptyCommandParameterException | ArrayIndexOutOfBoundsException e) {
            ui.deadlineDescriptionErrorMessage();
        }
    }

    public void handleTodo(String line, ArrayList<Task> taskList, boolean inStoredTask) {
        try {
            String todoDescription = parser.getTodoParameters(line);

            taskList.add(Task.taskCount, new Todo(todoDescription));
            if (!inStoredTask) {
                ui.taskAddedMessage(taskList, " todo ");
            }
        } catch (emptyCommandParameterException | ArrayIndexOutOfBoundsException e) {
            ui.todoDescriptionErrorMessage();
        }
    }

    public void handleUnmark(String line, ArrayList<Task> taskList) {
        try {
            int taskNumber = parser.getTaskNumber(line);
            if (taskNumber >= Task.taskCount || taskNumber < 0) {
                ui.outOfBoundsMessage();
            } else {
                taskList.get(taskNumber).setDone(false);
                ui.handleUnmarkErrorMessage(taskList, taskNumber);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            ui.errorHandleUnmark();
        }
    }

    public void handleMark(String line, ArrayList<Task> taskList) {
        try {
            int taskNumber = parser.getTaskNumber(line);
            if (taskNumber >= Task.taskCount || taskNumber < 0) {
                ui.outOfBoundsMessage();
            } else {
                taskList.get(taskNumber).setDone(true);
                ui.handleMarkMessage(taskList, taskNumber);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            ui.errorHandleMarkCommand();
        }
    }


    public void handleDelete(String line, ArrayList<Task> taskList) {
        try {
            int taskNumber = parser.getTaskNumber(line);
            if (taskNumber >= Task.taskCount || taskNumber < 0) {
                ui.outOfBoundsMessage();
            } else {
                Task.taskCount--;
                ui.handleDeleteMessage(taskList, taskNumber);
                taskList.remove(taskNumber);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | emptyCommandParameterException e) {
            ui.errorHandleDelete();
        }
    }

}
