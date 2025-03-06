package ultron.tasks;

import ultron.exceptions.emptyCommandParameterException;
import ultron.exceptions.unspecifiedCommandException;
import ultron.parser.Parser;
import ultron.ui.Ui;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Performs methods to add/delete/perform executive functions on tasks stored in the task list
 */
public class Tasklist {
    private static Ui ui = null; // Store Ui instance
    private static Parser parser;

    public Tasklist(Ui ui, Parser parser) {
        Tasklist.ui = ui;
        Tasklist.parser = parser;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public ArrayList<Task> taskList= new ArrayList<>();
    /**
     * Handles the creation of a task of type event. This involves invoking the parser to obtain user parameters
     * for the event specified then creating a task of Event type and adding it to task list.
     * @param line - line entered by user.
     * @param inStoredTask - if the task is being uploaded from the stored file this is set to true to toggle task added
     * message.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     */
    public  void handleEvent(String line, boolean inStoredTask) {
        try {
            Parser.EventParameters parsedParams = parser.getEventParameters(line);

            taskList.add(Task.taskCount, new Event(parsedParams.eventDescription(),
                        parsedParams.eventFrom(), parsedParams.eventTo()));
            if (!inStoredTask) {
                ui.taskAddedMessage(taskList, " event ");
            }
        } catch (emptyCommandParameterException | ArrayIndexOutOfBoundsException e) {
            ui.eventDescriptionErrorMessage();
        }
    }

    /**
     * Handles the filtering/finding of a task of type deadline by the date stored in tasklist
     * @param line - line entered by user.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     */
    public void findDeadlineByDate(String line) {

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

    /**
     * Handles the creation of a task of type deadline. This involves invoking the parser to obtain user parameters
     * for the deadline specified then creating a task of Deadline type and adding it to task list.
     * @param line - line entered by user.
     * @param inStoredTask - if the task is being uploaded from the stored file this is set to true to toggle task added
     * message.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     */
    public void handleDeadline(String line, boolean inStoredTask) {
        try {
            Parser.DeadlineParameters parsedParams = parser.getDeadlineParameters(line);

            taskList.add(Task.taskCount, new Deadline(parsedParams.deadlineDescription(), parsedParams.deadlineBy()));
            if (!inStoredTask) {
                ui.taskAddedMessage(taskList, " deadline ");
            }
        } catch (emptyCommandParameterException | ArrayIndexOutOfBoundsException  e) {
            ui.deadlineDescriptionErrorMessage();
        }
    }


    /**
     * Handles the filtering/finding of a task of type deadline by the description stored in its tasklist
     * @param line - line entered by user.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     */
    public void handleFind(String line) {
        try {
            String find = line.split(" ", 2)[1];
            if (find.trim().isEmpty()){
                throw new emptyCommandParameterException();
            }
            ArrayList<Task> filteredList = new ArrayList<>();
            int filteredIndex = 0;
            for (int i = 0; i < Task.taskCount; i++) {
                if (taskList.get(i).getDescription().contains(find)) {
                    filteredList.add(taskList.get(i));
                    filteredIndex++;
                }
            }
            ui.dashLine();
            for (int k = 0; k < filteredIndex; k++) {

                System.out.println("    " + (k + 1) + ". " + filteredList.get(k));

            }
            ui.dashLine();
        } catch (ArrayIndexOutOfBoundsException| emptyCommandParameterException e) {
            ui.findIndexErrorMessage();
        }

    }

    /**
     * Handles the creation of a task of type to-do. This involves invoking the parser to obtain user parameters
     * for the deadline specified then creating a task of Todo type and adding it to task list.
     * @param line - line entered by user.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     */
    public void handleTodo(String line,  boolean inStoredTask) {
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

    /**
     * Handles the unmarking a task in the list by setting it to not done. This involves invoking the parser to
     * obtain index to be unmarked and making the change to corresponding task in task list.
     * @param line - line entered by user.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     * @throws NumberFormatException if a user does not input the numerical value of an index.
     */

    public void handleUnmark(String line) {
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
    /**
     * Handles the marking a task in the list by setting it to done. This involves invoking the parser to
     * obtain index to be marked and making the change to corresponding task in task list.
     * @param line - line entered by user.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     * @throws NumberFormatException if a user does not input the numerical value of an index.
     */
    public void handleMark(String line) {
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

    /**
     * Handles the deleting a specified task in the list. This involves invoking the parser to
     * obtain index to be deleted and removign it from the list.
     * @param line - line entered by user.
     * @throws ArrayIndexOutOfBoundsException if there is some incorrect formatting of syntax such that parser cannot
     * find the required parameters from user input.
     * @throws NumberFormatException if a user does not input the numerical value of an index.
     */

    public void handleDelete(String line) {
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

    /**
     * Starts a command loop which repeatedly parses user input to find and execute the associated command until exit
     * command "bye".
     * @throws unspecifiedCommandException if a user input is different from set list of available commands
     */
    public void runCommandLoopUntilExit(String line, Scanner in) {
        String command = parser.userCommand(line);
        //the below switch-case refactoring is inspired by @James17042002
        while (!command.equals("bye")) {
            try {
                switch (command) {
                case "list":
                    ui.dashLine();
                    ui.printTaskList(taskList);
                    ui.dashLine();
                    break;
                case "mark":
                    handleMark(line);
                    break;
                case "unmark":
                    handleUnmark(line);
                    break;
                case "todo":
                    //error-handling within the handle function
                    handleTodo(line, false);
                    break;
                case "deadline":
                    handleDeadline(line, false);
                    break;
                case "event":
                    handleEvent(line, false);
                    break;
                case "delete":
                    handleDelete(line);
                    break;
                case "datefind":
                    findDeadlineByDate(line);
                    break;
                case "find":
                    handleFind(line);
                    break;
                default:
                    throw new unspecifiedCommandException();
                }
            } catch (unspecifiedCommandException e) {
                ui.unspecifiedCommandErrorMessage();
            }
            line = in.nextLine();
            command = parser.userCommand(line);
        }
    }

}
