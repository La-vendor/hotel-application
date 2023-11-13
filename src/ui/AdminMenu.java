package ui;

import java.util.Scanner;

public class AdminMenu {

    public static void runAdminMenu() {

        Scanner scanner = new Scanner(System.in);
        boolean repeatAdminLoop = true;
        String userInput;

        while (repeatAdminLoop) {
            System.out.println("""
                                                            
                                Choose an option
                                1. See all Customers
                                2. See all Rooms
                                3. See all Reservations
                                4. Add a Room\s
                                5. Populate system with test data
                                6. Back to Main Menu""");


            userInput = scanner.next();


            switch (userInput) {

                case "1" -> HotelApplication.seeAllCustomers();

                case "2" -> HotelApplication.seeAllRooms();

                case "3" -> HotelApplication.seeAllReservations();

                case "4" -> HotelApplication.addARoom();

                case "5" -> HotelApplication.populateSystem();

                case "6" -> repeatAdminLoop = false;

                default -> System.out.println("Enter number from 1 to 6");
            }
        }

    }

}
