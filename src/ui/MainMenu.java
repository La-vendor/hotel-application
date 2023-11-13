package ui;

import java.util.Scanner;

public class MainMenu {

    public static void runMainMenu() {

        boolean runMainMenuLoop = true;
        Scanner scanner = new Scanner(System.in);

        while (runMainMenuLoop) {
            System.out.println("""
                                        
                    Choose an option
                    1. Find and reserve a room
                    2. See my reservations
                    3. Create an account
                    4. Admin\s
                    5. Exit""");

            String userInput = scanner.next();

            switch (userInput) {
                case "1" -> HotelApplication.findAndReserveARoom();

                case "2" -> HotelApplication.checkReservationsByEmail();

                case "3" -> HotelApplication.setupAnAccount();

                case "4" -> AdminMenu.runAdminMenu();

                case "5" -> {
                    System.out.println("Exiting application...");
                    runMainMenuLoop = false;
                }
                default -> System.out.println("Enter number from 1 to 6");
            }
        }
    }




}
