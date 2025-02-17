package ui;

import booking.Booking;
import booking.BookingList;
import control.Duke;
import exception.DukeException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import room.Room;
import room.RoomList;
import storage.BookingConstants;
import user.UserList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller for ui.MainWindow. Provides the layout for the other controls.
 */
public class Ui extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ScrollPane scrollPane2;
    @FXML
    private VBox dialogContainer;
    @FXML
    private VBox listContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    @FXML
    private AnchorPane flexiblePane;
    @FXML
    private Label userLabel;

    private String output;
    private Duke duke;
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DukeCat.png"));

    /**
     * Do on application start. Prints a welcome message.
     */
    @FXML
    public void initialize() throws DukeException {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        dialogContainer.getChildren().addAll(
                DialogBox.getDukeDialog(showWelcome(), dukeImage)
        );
        userLabel.setText("Not Logged In");
    }

    public void setDuke(Duke d) {
        duke = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing control.Duke's reply and then
     * appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() throws DukeException {
        String input = userInput.getText();
        String[] splitStr = input.split(" ", 2);
        String response = duke.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
        switch (splitStr[0]) {
        case "add":
        case "edit":
        case "delete":
        case "approve":
        case "reject":
        case "list":
            listContainer.getChildren().clear();
            BookingList bookingList = duke.getBookingList();
            showList(bookingList);
            break;
        case "addroom":
        case "deleteroom":
        case "listroom":
            listContainer.getChildren().clear();
            RoomList roomList = duke.getRoomList();
            showRoomList(roomList);
            break;
        case "listday":
            listContainer.getChildren().clear();
            BookingList bookingList1 = duke.getBookingList();
            showListDay(bookingList1, splitStr[1]);
            break;
        case "listyear":
            listContainer.getChildren().clear();
            BookingList bookingList2 = duke.getBookingList();
            showListYear(bookingList2, splitStr[1]);
            break;
        case "listmonth":
            listContainer.getChildren().clear();
            BookingList bookingList3 = duke.getBookingList();
            showListMonth(bookingList3, splitStr[1]);
            break;
        case "find":
            listContainer.getChildren().clear();
            BookingList bookingList4 = duke.getBookingList();
            showFound(bookingList4, splitStr[1]);
            break;
        case "findindex":
            listContainer.getChildren().clear();
            BookingList bookingList5 = duke.getBookingList();
            showFoundIndex(bookingList5, splitStr[1]);
            break;
        case "login":
        case "logout":
            UserList userList = duke.getUserList();
            if (userList.getLoginStatus()) {
                userLabel.setText("Logged in as: " + userList.getCurrentUser());
            } else {
                userLabel.setText("Not Logged In");
            }
            break;
        default:
            break;
        }
    }

    private void showRoomList(RoomList roomList) throws DukeException {
        addToList(new RoomListBox("S/N", "Room Code", "Capacity"));
        Integer index = 1;
        for (Room i : roomList) {
            String capacity = Integer.toString(i.getCapacity());
            addToList(new RoomListBox(index.toString(), i.getRoomcode(), capacity));
            index++;
        }
    }

    private void showList(BookingList bookingList) throws DukeException {
        addToList(new ListBox("S/N", "Name", "Venue", "Date", "From",
                "To", "Status", "Purpose", "Approved/\nRejected By"));
        Integer index = 1;
        for (Booking i : bookingList) {
            addToList(customListBox(bookingList, i, index));
            index++;
        }
    }

    private void showListDay(BookingList bookingList, String date) throws DukeException {
        addToList(new ListBox("S/N", "Name", "Venue", "Date", "From",
                "To", "Status", "Purpose", "Approved/ Rejected By:"));
        Integer index = 1;
        try {
            DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateStart = LocalDate.parse(date, formatterStart);
            for (Booking i : bookingList) {
                if (i.getDateStart().equals(dateStart)) {
                    addToList(customListBox(bookingList, i, index));
                    index++;
                }
            }
        } catch (DateTimeParseException error) {
            //new DukeException (BookingConstants.DATEERROR);
        }
    }

    private void showListYear(BookingList bookingList, String date) throws DukeException {
        addToList(new ListBox("S/N", "Name", "Venue", "Date", "From",
                "To", "Status", "Purpose", "Approved/ Rejected By"));
        Integer index = 1;
        try {
            DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateStart = LocalDate.parse(date, formatterStart);
            for (Booking i : bookingList) {
                if (i.getStartYear() == dateStart.getYear()) {
                    addToList(customListBox(bookingList, i, index));
                    index++;
                }
            }
        } catch (DateTimeParseException error) {
            //new DukeException (BookingConstants.DATEERROR);
        }
    }

    private void showListMonth(BookingList bookingList, String month) throws DukeException {
        addToList(defaultListBox());
        Integer index = 1;
        int intMonth = Integer.parseInt(month);
        for (Booking i : bookingList) {
            if (i.getStartMonth() == intMonth) {
                addToList(customListBox(bookingList, i, index));
                index++;
            }
        }
    }

    private void showFound(BookingList bookingList, String keywords) throws DukeException {
        addToList(new ListBox("S/N", "Name", "Venue", "Date", "From",
                "To", "Status", "Purpose", "Approved/ Rejected By"));
        boolean found = false;
        Integer index = 1;
        for (Booking i : bookingList) {
            if (i.getDescription().toUpperCase().contains(keywords.toUpperCase()))  {
                found = true;
                addToList(customListBox(bookingList, i, index));
            }
        }
    }

    private void showFoundIndex(BookingList bookingList, String index) throws DukeException {
        addToList(new ListBox("S/N", "Name", "Venue", "Date", "From",
                "To", "Status", "Purpose", "Approved/ Rejected By"));
        int indexindex = 1;
        int intIndex = Integer.parseInt(index);
        if (intIndex >= bookingList.size()) {
            throw new DukeException("OOPS!!!  No such entry exist!");
        } else {
            Booking result = bookingList.get(intIndex);
            addToList(customListBox(bookingList, result, indexindex));;
        }
    }

    private void addToList(ListBox listBox) {
        listContainer.getChildren().addAll(listBox);
    }

    private void addToList(RoomListBox roomListBox) {
        listContainer.getChildren().addAll(roomListBox);
    }

    public void showLoadingError() {
        setOutput(":( OOPS!!! File path not found. Creating directory /data/data.txt");
    }

    public void showError(String error) {
        System.out.println(error);
    }

    public void addToOutput(String string) {
        output += string + "\n";
    }

    /**
     * Prints user input to GUI.
     * @param string string to display
     */
    public void showUserInput(String string) throws DukeException {
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(string, userImage)
        );
    }

    /**
     * Show Welcome message on programme start.
     */
    public String showWelcome() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        return "Welcome to HallBooker!\n"
                + "Type \"help\" to see the commands available.";
    }

    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    /**
     * to exit the application.
     */
    public void showBye() {
        setOutput("Bye. Hope to see you again soon!");
        Platform.exit();
        System.exit(0);
    }

    public void indent() {
        System.out.print("    ");
    }

    public void setOutput(String text) {
        output = text;
    }

    public String getOutput() {
        return output;
    }

    private ListBox defaultListBox() throws DukeException {
        return new ListBox("S/N", "Name", "Venue", "Date", "From",
                "To", "Status", "Purpose", "Approved/ Rejected By");
    }

    private ListBox customListBox(BookingList bookinglist, Booking booking, Integer index) throws DukeException {
        return new ListBox(String.valueOf(bookinglist.indexOf(booking) + 1), booking.getName(),
                booking.getVenue(), booking.getDateStart().toString(),
                booking.getTimeStart().toString(), booking.getTimeEnd().toString(), booking.getStatus(),
                booking.getDescription(), booking.getApprovedBy());
    }

    /**
     * Displays help message.
     */
    public void showHelp() {
        setOutput("Here are the available commands:\n"
                + "--------------\n"
                + "General\n"
                + "Help: help\n"
                + "Add user: adduser <username>\n"
                + "Exit HallBooker: bye\n"
                + "Remove user: rmuser <username>\n"
                + "Login: login <username>\n"
                + "Logout: logout\n"
                + "--------------\n"
                + "Booking\n"
                + "Add booking: add <name> <description> /at <room code> /from <HHMM> /to <HHMM>\n"
                + "List booking: list\n"
                + "List day: listday <date>\n"
                + "List month: listmonth <month>\n"
                + "List year: listyear <year>\n"
                + "Edit booking: edit <index> <description>\n"
                + "Delete booking: delete <index>\n"
                + "Approve booking: approve <index>\n"
                + "Reject booking: reject <index>\n"
                + "Find booking by keyword: find <keyword>\n"
                + "Find booking by index: findindex <index>\n"
                + "--------------\n"
                + "Rooms\n"
                + "Add room: addroom <room code> <capacity>\n"
                + "List room: listroom\n"
                + "Delete room: deleteroom <index>\n"
                + "--------------\n"
                + "Inventory\n"
                + "Add to inventory: addinventory <item> /qty <number> /in <room code>\n"
                + "List inventory: listinventory\n");
    }
}