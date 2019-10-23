package command;

import booking.BookingList;
import exception.DukeException;
import storage.Storage;
import room.AddRoom;
import room.RoomList;
import ui.Ui;
import user.User;
import java.io.FileReader;
import java.io.IOException;

public class AddRoomCommand extends Command {

    private String[] splitC;
    private String roomcode;
    private String[] datesplit;
    private String date;
    private String timeslot;

    //@@ zkchang97
    /**
     * Creates a new room entry in the list of rooms.
     * Format is addroom ROOMCODE /date DD/MM/YYYY /timeslot HH:MM AM/PM to HH:MM AM/PM
     * @param input from user
     * @param splitStr tokenized input
     * @throws DukeException when format is incorrect
     */
    public AddRoomCommand(String input, String[] splitStr) throws DukeException {
        if (splitStr.length == 1) {
            throw new DukeException("Please enter the following to add a room:\n"
                    + "addroom ROOMCODE /date DATE /timeslot TIMESLOT.\n"
                    + "DATE format: DD/MM/YYYY.\n"
                    + "TIMESLOT format: HH:MM AM/PM to HH:MM AM/PM.");
        }
        if (!input.contains(" /date ")) {
            throw new DukeException("Please enter correct date for the room.");
        }
        if (!input.contains(" /timeslot ")) {
            throw new DukeException("Please enter a timeslot for the room.");
        }
        // addroom ROOMCODE /date DATE /timeslot TIMESLOT
        String tempAR = input.substring(8);
        splitC = tempAR.split(" /date ", 2); // splitC[] = {ROOMCODE, DATE /timeslot TIMESLOT}
        this.roomcode = splitC[0]; // ROOMCODE
        this.datesplit = splitC[1].split(" /timeslot ", 2); // datesplit[] == {DATE, TIMESLOT}
        this.date = datesplit[0];
        this.timeslot = datesplit[1];
    }

    /**
     * Executes the command to add a room to the system.
     * @param roomList room list
     * @param bookingList bookings list
     * @param ui user interface
     * @param bookingstorage booking storage in command execution
     * @param roomstorage room storage in command execution
     * @param user current user
     * @throws IOException if input entry is incorrect
     */
    @Override
    public void execute(RoomList roomList, BookingList bookingList, Ui ui, Storage bookingstorage,
                        Storage roomstorage, User user) throws IOException {
        AddRoom addroom = new AddRoom(roomcode, date, timeslot);
        roomList.add(addroom);
        roomstorage.saveToFile(roomList);
        ui.addToOutput("Got it, I've added this room.\n"
            + addroom.toString() + "\n" + "Now you have " + roomList.size() + " room(s) in the list.");
    }
}
