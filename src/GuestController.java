import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.lang.model.util.ElementScanner14;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;

public class GuestController implements Initializable
{
    @FXML private TableView<Person> table;

    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;
    @FXML private TableColumn<Person, String> phoneNumberColumn;
    @FXML private TableColumn<Person, String> timeRemainingColumn;
    @FXML private TableColumn<Person, String> timeReservedColumn;

    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button swapButton;
    @FXML private Button jumpButton;
    @FXML private Button addReservationButton;
    @FXML private Button exitButton;
    @FXML private Button deleteExpiredButton;
    
    @FXML private TextField searchText;
    @FXML private TextField firstNameText;
    @FXML private TextField lastNameText;
    @FXML private TextField phoneNumberText;
    @FXML private TextField timeRemainingText;
    @FXML private TextField timeReservedText;
    
    @FXML private Label timeLabel;
    @FXML private Label countLabel;
    @FXML private Label currentScreenLabel;
    @FXML private Label reservationLabel;
    @FXML private Label guestLabel;

    @FXML private ToggleButton timeRemainingToggle;

    private String currentTime;
    private int reminder, hours, minutes, counter;

    private ObservableList<Person> guestData = FXCollections.observableArrayList();
    private ObservableList<Person> reservationData = FXCollections.observableArrayList();
    private ObservableList<Person> expiredGuests = FXCollections.observableArrayList();
    private ObservableList<Person> expiredReservations = FXCollections.observableArrayList();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //Sets up columns
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));
        timeRemainingColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("timeRemaining"));
        timeReservedColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("timeReserved"));
        
        //Allows column editing
        table.setEditable(true);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        timeRemainingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        timeReservedColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //Allows us to select multiple rows
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Set data in column
        table.setItems(guestData);

        //Implements search and listeners
        search();
        setUpListener();

        //Sets up real time clock
        Timeline timer = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
            timeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();

        currentScreenLabel.setText("Guests:");
        timeReservedColumn.setVisible(false);
        timeReservedText.setVisible(false);
        timeReservedText.setDisable(true);
        swapButton.setText("Reservations");

        disableExpiredButtons();
    }

    //addPerson() is called when the add button is clicked. adds a person with the parameters the user entered in the textFields
    public void addPerson()
    {
        if(currentScreenLabel.getText().equals("Guests:")) //checks which "mode" the user is in
        {
            if(!(firstNameText.getText().equals("")) && !(lastNameText.getText().equals("")) && !(phoneNumberText.getText().equals("")))
            {    
                if(timeRemainingToggle.isSelected() == true)
                {
                    if(!(timeRemainingText.getText().equals("")))
                    {
                        guestData.add(new Person(firstNameText.getText(), lastNameText.getText(), phoneNumberText.getText(), timeRemainingText.getText(), ""));
                        setUpTimer();
                        firstNameText.setText("");
                        lastNameText.setText("");
                        phoneNumberText.setText("");
                        timeRemainingText.setText("");
                        countLabel.setText(guestData.size() + "");
                    }
                    else
                    {
                        guestLabel.setText("You must fill in all required fields");
                    }
                }
                else
                {
                    guestData.add(new Person(firstNameText.getText(), lastNameText.getText(), phoneNumberText.getText(), timeRemainingText.getText(), ""));
                    setUpTimer();
                    firstNameText.setText("");
                    lastNameText.setText("");
                    phoneNumberText.setText("");
                    timeRemainingText.setText("");
                    countLabel.setText(guestData.size() + "");
                }
            }
            else
            {
                guestLabel.setText("You must fill in all required fields");
            }
        }
        else if(currentScreenLabel.getText().equals("Reservations:"))
        {
            if(!(firstNameText.getText().equals("")) && !(lastNameText.getText().equals("")) && !(phoneNumberText.getText().equals("")) && !(timeReservedText.getText().equals("")))
            {    
                if(timeReservedText.getText().substring(timeReservedText.getText().length() - 2).toUpperCase().equals("AM") || timeReservedText.getText().substring(timeReservedText.getText().length() - 2).toUpperCase().equals("PM"))
                {
                    if(timeRemainingToggle.isSelected() == true)
                    {
                        if(!(timeRemainingText.getText().equals("")))
                        {
                            String tReserved = timeReservedText.getText().toUpperCase();
                            int index = tReserved.indexOf(":");
                            if(index == 1)
                            {
                                tReserved = "0" + tReserved; 
                            }
                            reservationData.add(new Person(firstNameText.getText(), lastNameText.getText(), phoneNumberText.getText(), timeRemainingText.getText(), tReserved));
                            setUpTimer();
                            firstNameText.setText("");
                            lastNameText.setText("");
                            phoneNumberText.setText("");
                            timeRemainingText.setText("");
                            timeReservedText.setText("");
                            countLabel.setText(reservationData.size() + "");
                        }
                        else
                        {
                            reservationLabel.setText("You must fill in all required fields");
                        }
                    }
                    else
                    {
                        String tReserved = timeReservedText.getText().toUpperCase();
                        int index = tReserved.indexOf(":");
                        if(index == 1)
                        {
                            tReserved = "0" + tReserved; 
                        }
                        reservationData.add(new Person(firstNameText.getText(), lastNameText.getText(), phoneNumberText.getText(), timeRemainingText.getText(), tReserved));
                        setUpTimer();
                        firstNameText.setText("");
                        lastNameText.setText("");
                        phoneNumberText.setText("");
                        timeRemainingText.setText("");
                        timeReservedText.setText("");
                        countLabel.setText(reservationData.size() + "");
                    }
                }
                else
                {
                    reservationLabel.setText("You must specify whether the reservation is at AM or PM");
                }
            }
            else
            {
               reservationLabel.setText("You must fill in all required fields");
            }
        }
    }
    
    //happens when the delete button is clicked. deletes all selected entries
    public void deletePerson()
    {
        if(currentScreenLabel.getText().equals("Guests:"))
        {
            guestData.removeAll(table.getSelectionModel().getSelectedItems());
            countLabel.setText("" + guestData.size());
        }
        else 
        {
            reservationData.removeAll(table.getSelectionModel().getSelectedItems());
            countLabel.setText("" + reservationData.size());
        }
            
    }

    //happens autonomously when the TextField searchText is updated. filters the list to any attributes an object shares with the TextField
    public void search()
    {
        FilteredList<Person> tempData = new FilteredList<Person>(table.getItems());
        searchText.textProperty().addListener((observable, oldVal, newVal) -> {
            tempData.setPredicate(Person -> {
                String lowerNewVal = newVal.toLowerCase();
                if(newVal.isEmpty() || Person.getFirstName().toLowerCase().contains(lowerNewVal) || Person.getLastName().toLowerCase().contains(lowerNewVal) || Person.getPhoneNumber().toLowerCase().contains(lowerNewVal))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            });
        });

        SortedList<Person> sorted = new SortedList<>(tempData);

        sorted.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sorted);
    }

    //sets up the timer when a new object is added to the table
    public void setUpTimer()
    {
        if(currentScreenLabel.getText().equals("Guests:"))
        {
            if(!(guestData.get(guestData.size() - 1).getTimeRemaining().equals("")))
            {
                counter = 0;
                reminder = Integer.parseInt(guestData.get(guestData.size() - 1).getTimeRemaining());  //gets the most recent entry
                hours = reminder / 60;
                minutes = reminder % 60;
                if(hours > 9) //hours is 2 digits
                {
                    if(minutes < 10) //minutes is 1 digit
                        guestData.get(guestData.size() - 1).setTimeRemaining(hours + ":" + "0" + minutes);
                    else //minutes is 2 digits
                        guestData.get(guestData.size() - 1).setTimeRemaining(hours + ":" + minutes);
                }
                else //hours is 1 digit
                {
                    if(minutes < 10) //minutes is 1 digit
                        guestData.get(guestData.size() - 1).setTimeRemaining("0" + hours + ":" + "0" + minutes);
                    else //minutes is 2 digits
                        guestData.get(guestData.size() - 1).setTimeRemaining("0" + hours + ":" + minutes);
                }
            }
        }

        else if(currentScreenLabel.getText().equals("Reservations:"))
        {
            if(!(reservationData.get(reservationData.size() - 1).getTimeRemaining().equals("")))
            {
                counter = 0;
                reminder = Integer.parseInt(reservationData.get(reservationData.size() - 1).getTimeRemaining());  //gets the most recent entry
                hours = reminder / 60;
                minutes = reminder % 60;
                if(hours > 9) //hours is 2 digits
                {
                    if(minutes < 10) //minutes is 1 digit
                        reservationData.get(reservationData.size() - 1).setTimeRemaining(hours + ":" + "0" + minutes);
                    else //minutes is 2 digits
                        reservationData.get(reservationData.size() - 1).setTimeRemaining(hours + ":" + minutes);
                }
                else //hours is 1 digit
                {
                    if(minutes < 10) //minutes is 1 digit
                        reservationData.get(reservationData.size() - 1).setTimeRemaining("0" + hours + ":" + "0" + minutes);
                    else //minutes is 2 digits
                        reservationData.get(reservationData.size() - 1).setTimeRemaining("0" + hours + ":" + minutes);
                }
            }
        }
    }

    //sets up the listener for the Label timeLabel when a new second hapens
    public void setUpListener()
    {
        timeLabel.textProperty().addListener((String, oldVal, newVal) -> { //sets up a listener which activates every second
            if(0 == Integer.parseInt(newVal.substring(6))) //checks if it is a new minute
            {
                for(int i = 0; i < guestData.size(); i++) //traverses guestData, an array of all person objects
                {
                    counter = i;
                    if(guestData.get(counter).getTimeRemaining().equals(""))
                    {
                        System.out.println("THERE IS NO VALUE ~");
                        continue;
                    }
                    
                    currentTime = guestData.get(counter).getTimeRemaining();
                    System.out.println("the time remaining is: " + currentTime);
                    String[] times = currentTime.split(":");
                        
                    hours = Integer.parseInt(times[0]);
                    minutes = Integer.parseInt(times[1]);

                    minutes--;
                    if(hours == 0)
                        if(minutes == 0)
                            expiredGuests.add(guestData.get(counter)); //fix this
                           
                    if(minutes < 0) //resets the hour and minute value upon a new hour
                    {
                        hours--;
                        minutes = 59;
                    }

                    if(hours > 9) //hours is 2 digits
                    {
                        if(minutes < 10) //minutes is 1 digit
                            guestData.get(counter).setTimeRemaining(hours + ":" + "0" + minutes);
                        else //minutes is 2 digits
                            guestData.get(counter).setTimeRemaining(hours + ":" + minutes);
                    }
                    else //hours is 1 digit
                    {
                        if(minutes < 10) //minutes is 1 digit
                            guestData.get(counter).setTimeRemaining("0" + hours + ":" + "0" + minutes);
                        else //minutes is 2 digits
                            guestData.get(counter).setTimeRemaining("0" + hours + ":" + minutes);
                    }
                } //end of for loop
                searchText.setText(" ");
                searchText.setText("");
                table.refresh();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a");
                String currentMeridiem = LocalDateTime.now().format(formatter);
                
                for(int j = 0; j < reservationData.size(); j++) //traverses reservationData, an array of all person objects, to check if there is a reservation where a person should arrive
                {
                    System.out.println("Time reserved alone: " + reservationData.get(counter).getTimeReservedAlone());
                    System.out.println("Time on the clock: " + timeLabel.getText().substring(0, 5));
                    counter = j;
                    
                    if(reservationData.get(counter).getTimeReservedAlone().equals(timeLabel.getText().substring(0, 5)) && reservationData.get(counter).getAMPM().equals(currentMeridiem))
                    {
                        expiredReservations.add(reservationData.get(counter));
                    }
                }

                if(expiredReservations.size() > 0)
                {
                    addReservationButton.setVisible(true);
                    addReservationButton.setDisable(false);
                    jumpButton.setVisible(true);
                    jumpButton.setDisable(false);
                    reservationLabel.setVisible(true);
                    reservationLabel.setText("There are " + expiredReservations.size() + " person(s) who have reservations right now.");
                }
                 //end of for loop
            } //end of if statements
        }); //end of listene
    }

    //creates functionality for jumpTo() method. sets the expired items depending on which mode was originally there
    public void jumpTo()
    {
        if(currentScreenLabel.getText().equals("Guests:"))
            table.setItems(expiredGuests);
        else
            table.setItems(expiredReservations);
        searchText.setText(" ");
        searchText.setText("");
        table.refresh();
    }

    //adds any selected expired reservations to the guest list as they should be here
    public void addToGuest()
    {
        guestData.addAll(table.getSelectionModel().getSelectedItems());
        expiredReservations.removeAll(table.getSelectionModel().getSelectedItems());
        if(expiredReservations.size() == 0)
            table.setItems(reservationData);
    }

    //deletes any selected reservations that are expired
    public void deleteExpired()
    {
        expiredReservations.removeAll(table.getSelectionModel().getSelectedItems());
        if(expiredReservations.size() == 0)
            if(currentScreenLabel.getText().equals("Reservations:"))
                table.setItems(guestData);
            else
                table.setItems(reservationData);  
    }

    //switches the mode of the program from guests to reservations and vice versa
    public void switchMode()
    {
        if(currentScreenLabel.getText().equals("Reservations:"))
        {
            currentScreenLabel.setText("Guests:");
            timeReservedColumn.setVisible(false);
            timeReservedText.setVisible(false);
            timeReservedText.setDisable(true);
            swapButton.setText("Reservations");
            countLabel.setText("" + guestData.size());
            table.setItems(guestData);
        }
        else
        {
            currentScreenLabel.setText("Reservations:");
            timeReservedColumn.setVisible(true);
            timeReservedText.setVisible(true);
            timeReservedText.setDisable(false);
            swapButton.setText("Guests");
            countLabel.setText("" + reservationData.size());
            table.setItems(reservationData);
        }
    }

    public void changeFirstName(CellEditEvent cell) {
        Person personSelected = table.getSelectionModel().getSelectedItem();
        System.out.println(personSelected);
        personSelected.setFirstName(cell.getNewValue().toString());
    }

    public void changeLastName(CellEditEvent cell) {
        Person personSelected = table.getSelectionModel().getSelectedItem();
        personSelected.setLastName(cell.getNewValue().toString());
    }

    public void changePhoneNumber(CellEditEvent cell) {
        Person personSelected = table.getSelectionModel().getSelectedItem();
        personSelected.setPhoneNumber(cell.getNewValue().toString());
    }

    public void changeTimeRemaining(CellEditEvent cell) {
        Person personSelected = table.getSelectionModel().getSelectedItem();
        personSelected.setTimeRemaining(cell.getNewValue().toString());
    }

    public void changeTimeReserved(CellEditEvent cell) {
        Person personSelected = table.getSelectionModel().getSelectedItem();
        personSelected.setTimeRemaining(cell.getNewValue().toString());
    }

    public void disableExpiredButtons()
    {
        addReservationButton.setVisible(false);
        addReservationButton.setDisable(true);
        jumpButton.setVisible(false);
        jumpButton.setDisable(true);
        deleteExpiredButton.setVisible(false);
        deleteExpiredButton.setDisable(true);
    }
    public void exit() 
    {
        System.exit(0);
    }

    
}