package control;

import command.*;
import exception.DukeException;
import room.AddRoom;

import java.io.IOException;

/**
 * Parse input and execute respective user command.
 */
public class Parser {

    /**
     * Converts user input into commands for control.Duke.
     * @param input from user
     * @return Command to be executed
     * @throws DukeException if user enters wrong input format
     */
    public static Command parse(String input, boolean loginStatus) throws DukeException, IOException {
        String[] splitStr = input.split(" ");
        switch (splitStr[0]) {
        case "bye":
            return new ByeCommand();
        case "login":
            return new LoginCommand(input, splitStr);
        case "logout":
            return new LogoutCommand(input, splitStr);
        case "create":
            return new CreateAccountCommand(input, splitStr);
        case "add":
            return new AddBookingCommand(input, splitStr);
        case "addroom":
            return new AddRoomCommand(input, splitStr);
        case "list":
            return new ListCommand();
        case "listroom":
            return new ListRoomCommand();
        case "edit":
            return new EditBookingCommand(input, splitStr);
        case "inventory":
            return new ;
        case "addinventory":
            return new AddInventoryCommand(input, splitStr);
        case "edit inventory":
            return new;
            return new EditBookingCommand(input, splitStr);
        case "approve":
            return new ApproveCommand(input, splitStr);
        case "reject":
            return new RejectCommand(input, splitStr);
        case "delete":
            return new DeleteCommand(input, splitStr);
        default:
            throw new DukeException("\u2639 OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }
}
