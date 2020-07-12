import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Person 
{
    private SimpleStringProperty first, last, phoneNumber, timeRemaining, timeReserved;

    public Person(String fName, String lName, String pNumber, String tLeft, String tReserved)
    {
        first = new SimpleStringProperty(fName);
        last = new SimpleStringProperty(lName);
        phoneNumber = new SimpleStringProperty(pNumber);
        timeRemaining = new SimpleStringProperty(tLeft);
        timeReserved = new SimpleStringProperty(tReserved);
    }

    //sets fName as the value of first
    public void setFirstName(String fName)
    {
        first.set(fName);
    }

    //sets lName as the value of last 
    public void setLastName(String lName)
    {
        last.set(lName);
    }

    //sets newNumber as the value of phoneNumber
    public void setPhoneNumber(String newNumber)
    {
        phoneNumber.set(newNumber);
    }

    //sets newTime as the value of timeRemaining
    public void setTimeRemaining(String newTime)
    {
        timeRemaining.set(newTime);
    }

    //sets newReservation as the value of timeRemaining
    public void setTimeReserved(String newReservation)
    {
        timeReserved.set(newReservation);
    }

    //returns a String representing the First Name
    public String getFirstName()
    {
        return first.get();
    }

    //returns a String representing the Last Name
    public String getLastName()
    {
        return last.get();
    }

    //returns a String representing the phone number
    public String getPhoneNumber()
    {
        return phoneNumber.get();
    }

    //returns a String representing how much Time is Remaining
    public String getTimeRemaining()
    {
        return timeRemaining.get();
    }

    //returns a String representing what time the reservation is scheduled
    public String getTimeReserved()
    {
        return timeReserved.get();
    }

    public String getAMPM()
    {
        return timeReserved.get().substring(timeReserved.get().length() - 2);
    }

    public String getTimeReservedAlone()
    {
        return timeReserved.get().substring(0, 5);
    }

}