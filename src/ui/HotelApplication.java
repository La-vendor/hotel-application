package ui;

import api.AdminResource;
import api.HotelResource;
import model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class HotelApplication {

    static HotelResource hotelResource = HotelResource.getInstance();
    static AdminResource adminResource = AdminResource.getInstance();

    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    static final Pattern datePattern = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/]((2[0-9])[0-9]{2})$");


    public static void main(String[] args) {

        MainMenu.runMainMenu();

    }

    protected static void checkReservationsByEmail() {

        System.out.println("Enter Email format: name@domain.com");

        String userEmail = checkEmail();

        Collection<Reservation> userReservations = hotelResource.getCustomerReservations(userEmail);
        if (userReservations.isEmpty()) System.out.println("No reservation found for that email address.");
        else {
            for (Reservation reservation : userReservations) {
                System.out.println(reservation);
            }
        }
    }

    protected static void addARoom() {
        Scanner scanner = new Scanner(System.in);

        boolean addingRoomsFinished = false;
        List<IRoom> addRoomsList = new ArrayList<>();
        Collection<IRoom> allRooms;
        Pattern numberPattern = Pattern.compile("^(\\d+)$");
        String newRoomNumber = "";

        while (!addingRoomsFinished) {

            boolean correctRoomNumber = false;
            while (!correctRoomNumber) {
                correctRoomNumber = true;
                System.out.println("Enter room number");
                newRoomNumber = scanner.next().trim();
                if (!numberPattern.matcher(newRoomNumber).matches()) {
                    System.out.println("Invalid room number");
                    correctRoomNumber = false;
                    continue;
                }
                allRooms = adminResource.getAllRooms();

                for (IRoom room : allRooms) {
                    if (newRoomNumber.equals(room.getRoomNumber())) {
                        System.out.println("Room with this number already exists");
                        correctRoomNumber = false;
                        break;
                    }
                }
                for (IRoom room : addRoomsList) {
                    if (newRoomNumber.equals(room.getRoomNumber())) {
                        System.out.println("Room with this number already exists");
                        correctRoomNumber = false;
                        break;
                    }
                }
            }


            boolean priceCorrect = false;
            double newRoomPrice = 0.0;
            while (!priceCorrect) {
                priceCorrect = true;
                System.out.println("Enter room price per night");
                if (scanner.hasNextDouble()) {
                    newRoomPrice = scanner.nextDouble();
                } else {
                    scanner.next();
                    System.out.println("Invalid price format. Example: 100");
                    priceCorrect = false;

                }

            }

            String newRoomTypeInput;
            RoomType newRoomType = RoomType.DOUBLE;
            boolean correctType = false;

            System.out.println("Enter room type: 1 for single bed, 2 for double bed");
            while (!correctType) {
                newRoomTypeInput = scanner.next();

                if (newRoomTypeInput.equals("1")) {
                    newRoomType = RoomType.SINGLE;
                    correctType = true;
                } else if (newRoomTypeInput.equals("2")) {
                    correctType = true;
                } else {
                    System.out.println("Please enter 1 or 2 for room type");
                }
            }

            addRoomsList.add(new Room(newRoomNumber, newRoomPrice, newRoomType));

            System.out.println("Would like to add another room? (y/n)");

            String answer = checkAnswerYesOrNo();
            if (answer.equals("y")) {
                System.out.println("Adding another room");
            } else {
                addingRoomsFinished = true;
            }

        }
        adminResource.addRoom(addRoomsList);
    }

    protected static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    protected static void seeAllCustomers() {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        for (Customer customer : allCustomers) {
            System.out.println(customer);
        }
    }

    protected static void seeAllRooms() {
        Collection<IRoom> allRooms;
        allRooms = adminResource.getAllRooms();
        for (IRoom room : allRooms) {
            System.out.println(room);
        }
    }

    protected static void findAndReserveARoom() {
        Scanner scanner = new Scanner(System.in);

        boolean checkInDateOk = false;
        boolean checkOutDateOk = false;

        String checkInDateString;
        String checkOutDateString;

        Date checkInDate = new Date();
        Date checkOutDate = new Date();
        Date alternativeCheckInDate;
        Date alternativeCheckOutDate;
        Date presentDate = new Date();


        while (!checkInDateOk) {
            System.out.println("Please enter check in date as dd/MM/yyyy");

            checkInDateString = scanner.next();

            if (!datePattern.matcher(checkInDateString).matches()) {
                System.out.println("Invalid date");
                continue;
            }

            try {
                checkInDate = dateFormat.parse(checkInDateString);

            } catch (ParseException e) {
                System.out.println("Invalid date");
                continue;
            }

            if (checkInDate.compareTo(presentDate) < 0) {
                System.out.println("Check-in date has to be today or any date in the future");
                continue;
            }
            checkInDateOk = true;
        }

        while (!checkOutDateOk) {
            System.out.println("Please enter check out date as dd/MM/yyyy");

            checkOutDateString = scanner.next();

            if (!datePattern.matcher(checkOutDateString).matches()) {
                System.out.println("Invalid date");
                continue;
            }

            try {
                checkOutDate = dateFormat.parse(checkOutDateString);
            } catch (ParseException e) {
                System.out.println("Invalid date");
                continue;
            }

            if (checkInDate.compareTo(checkOutDate) < 0) {
                checkOutDateOk = true;
            } else {
                System.out.println("Checkout date has to be after check in date");
            }
        }

        Collection<IRoom> availableRooms;

        availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }

        if (availableRooms.isEmpty()) {
            System.out.println("I am sorry, there are no rooms available for the specified date");

            alternativeCheckInDate = addDays(checkInDate, 7);
            alternativeCheckOutDate = addDays(checkOutDate, 7);

            Collection<IRoom> alternativeAvailableRooms;

            alternativeAvailableRooms = hotelResource.findARoom(alternativeCheckInDate, alternativeCheckOutDate);
            if (!alternativeAvailableRooms.isEmpty()) {
                SimpleDateFormat alternativeDateFormat = new SimpleDateFormat("E dd MMMM yyyy", Locale.ENGLISH);
                System.out.println("Here is a list of rooms available between: " + alternativeDateFormat.format(alternativeCheckInDate) + " and " + alternativeDateFormat.format(alternativeCheckOutDate));
                for (IRoom room : alternativeAvailableRooms) {
                    System.out.println(room);
                }
                checkInDate = alternativeCheckInDate;
                checkOutDate = alternativeCheckOutDate;
                availableRooms = alternativeAvailableRooms;
            } else return;
        }

        System.out.println("Would you like to book a room? (y/n)");

        String answer = checkAnswerYesOrNo();
        if (answer.equals("n")) {
            System.out.println("Room reservation cancelled");
            return;
        }

        System.out.println("Do you have an account with us? (y/n)");
        answer = checkAnswerYesOrNo();
        if (answer.equals("n")) {
            System.out.println("Would you like to set up an account? (y/n)");
            String nestedAnswer = checkAnswerYesOrNo();
            if (nestedAnswer.equals("y")) {
                setupAnAccount();
            } else {
                System.out.println("Room reservation cancelled");
                return;
            }
        }

        boolean emailCorrect = false;
        String userEmail = "";
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        boolean emailIsInDatabase = false;

        while (!emailCorrect) {
            System.out.println("Enter Email format: name@domain.com");

            userEmail = checkEmail();

            for (Customer customer : allCustomers) {
                if (userEmail.equals(customer.getEmail())) {
                    emailIsInDatabase = true;
                    break;
                }
            }
            if (!emailIsInDatabase) {
                System.out.println("Entered email is not in our database, would you like to enter email again? (y/n)");

                answer = checkAnswerYesOrNo();
                if (answer.equals("n")) {
                    System.out.println("Room reservation cancelled");
                    return;
                }
            } else emailCorrect = true;
        }

        System.out.println("What room would you like to reserve?");
        String userRoomNumber = "";
        boolean roomNumberCorrect = false;
        while (!roomNumberCorrect) {
            userRoomNumber = scanner.next();

            for (IRoom room : availableRooms) {
                if (userRoomNumber.equals(room.getRoomNumber())) {
                    roomNumberCorrect = true;
                    break;
                }
            }
            if (!roomNumberCorrect)
                System.out.println("Entered room number is not available, please enter room number again ");
        }

        Reservation newReservation = hotelResource.bookARoom(userEmail, hotelResource.getRoom(userRoomNumber), checkInDate, checkOutDate);
        System.out.println(newReservation);
    }

    protected static void setupAnAccount() {

        Scanner scanner = new Scanner(System.in);
        String userEmail, userFirstName, userLastName;

        System.out.println("Enter Email format: name@domain.com");

        Collection<Customer> allCustomers = adminResource.getAllCustomers();

        userEmail = checkEmail();

        for (Customer customer : allCustomers) {
            if (userEmail.equals(customer.getEmail())) {
                System.out.println("Email is already registered for: " + customer.getFirstName() + " " + customer.getLastName());
                return;
            }
        }
        System.out.println("First name:");
        userFirstName = scanner.next();

        System.out.println("Last name:");
        userLastName = scanner.next();
        hotelResource.createACustomer(userEmail, userFirstName, userLastName);

        System.out.println("Welcome " + userFirstName + " " + userLastName + " to Hotel Reservation Application");
    }

    private static String checkAnswerYesOrNo() {

        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            userInput = scanner.next();

            if (userInput.equals("y") || userInput.equals("n")) return userInput;
            else System.out.println("Please enter y or n");
        }
    }

    private static String checkEmail() {

        Pattern pattern = Pattern.compile("^(.+)@([a-z]+).com$");
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            userInput = scanner.next();

            if (pattern.matcher(userInput).matches()) return userInput;
            else System.out.println("Wrong email pattern, example: name@domain.com");
        }
    }

    private static Date addDays(Date date, int days) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    protected static void populateSystem() {
        System.out.println("Populating system....");
        List<IRoom> roomList = new ArrayList<>();
        roomList.add(new Room("100", 100.0, RoomType.SINGLE));
        roomList.add(new Room("101", 150.0, RoomType.SINGLE));
        roomList.add(new Room("102", 200.0, RoomType.DOUBLE));
        roomList.add(new Room("103", 100.0, RoomType.SINGLE));

        hotelResource.createACustomer("m@m.com", "Mark", "June");
        hotelResource.createACustomer("a@a.com", "Judith", "May");
        hotelResource.createACustomer("g@m.com", "Greg", "April");
        hotelResource.createACustomer("o@m.com", "Tony", "Berg");
        adminResource.addRoom(roomList);

        Calendar calendarIn = Calendar.getInstance();
        Calendar calendarOut = Calendar.getInstance();
        calendarIn.set(2023, Calendar.JANUARY, 10);
        calendarOut.set(2023, Calendar.JANUARY, 15);
        hotelResource.bookARoom("m@m.com", hotelResource.getRoom("102"), calendarIn.getTime(), calendarOut.getTime());
        calendarIn.set(2023, Calendar.FEBRUARY, 10);
        calendarOut.set(2023, Calendar.FEBRUARY, 15);
        hotelResource.bookARoom("a@a.com", hotelResource.getRoom("103"), calendarIn.getTime(), calendarOut.getTime());

    }


}
