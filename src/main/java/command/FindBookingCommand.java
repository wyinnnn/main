
package command;

import booking.ApprovedList;
import inventory.Inventory;

import booking.Booking;
import booking.BookingList;
import exception.DukeException;
import room.RoomList;
import storage.Constants;
import storage.Storage;
import storage.StorageManager;
import ui.Ui;
import user.UserList;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FindBookingCommand extends Command {
    private String textToFind;

    //@@author Alex-Teo
    /**
     * Find the booking request matching the room, date, time and user.
     * @param input from user
     * @param splitStr tokenized input
     * @throws DukeException format error
     * @throws IOException entry error
     */
    public FindBookingCommand(String input, String[] splitStr) throws DukeException, IOException {
        if (splitStr.length <= 1) {
            throw new DukeException("☹ OOPS!!! Please input some keywords to search for!");
        }
        textToFind = input.substring(5);
    }

    @Override
    public void execute(UserList userList, Inventory inventory, RoomList roomList,
                        BookingList bookingList, ApprovedList approvedList, Ui ui,
                        StorageManager allStorage)
            throws DukeException, IOException, ParseException {
        boolean found = false;
        ui.addToOutput("Here are the request(s):");
        for (int i = 0; i < bookingList.size(); i++) {
            if (bookingList.get(i).getDescription().toUpperCase().contains(textToFind.toUpperCase())) {
                found = true;
                ui.addToOutput(i + 1 + ". " + bookingList.get(i).toString());
            }
        }
        if (!found) {
            ui.addToOutput("No items match your search!");
        }
    }
}