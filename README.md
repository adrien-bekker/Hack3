# Guest Manager

*The all-in-one tool for public and private gatherings*

# Inspirations and Purpose

We were inspired to create this application because of the restrictions placed on businesses and public places during COVID-19 as they can only have a certain amount of people in their building at a time. Our project addresses this issue by keeping track of how many people are currently present as well as how many reservations there are. In addition, you can keep track of specific guests through a search filter, and set a time limit for which they can stay. A walkthrough that goes through all of our features is given here https://www.youtube.com/watch?v=S36e-3FR3V0&feature=emb_title

# Learning and Building

Throughout the project, we learned how to work with the current time in Java as well as styling with CSS in Gluon Scene Builder. Our project has two main classes, being the Person class and the GuestController class. The Person class holds information such as their name, phone number, how time they can stay, and the time of their reservation (if they have one). The GuestController creates ObservableLists of Person objects in order to properly display them in the table with all of their information. It also controls the rest of the display for the user.

# Challenges

One of the challenges that we faced was figuring out whether the reservation time was AM or PM. In the end, we decided to have the user input AM or PM in the textField so the program could read the last two characters and set up the reminder accordingly. A similar issue to this is verifying that the user entered a valid time. We realized that a user could enter 13:61AM as a time, which makes absolutely no sense. To fix this issue, we had to edit the way that we read the time from the user. Another issue that we faced was with the guests or reservations that were expired. Since both had similar implementations, we got confused quickly when we tried to create methods that applied to both circumstances. Ultimately, we decided to make a set of methods for both guests and reservations. Another challenge we faced was when manipulating the guest data for guests that expired. We figured out that our problem was that when getting the selected table cells, we are given a readable list, which limited what we can do with it, and caused errors as we tried to manipulate it. Once we converted it to a writeable list, we were able to then apply the changes correctly as necessary.
 
