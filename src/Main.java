import service_impl.*;
import java.io.*;
import java.util.*;

public class Main {

    private final UserService_impl userService_impl = new UserService_impl();
    private final AdminService_impl adminService_impl = new AdminService_impl();
    Scanner scanner = new Scanner(System.in);

    static Main main =  null;
    // Clear screen
    public static void clearScreen() {
        System.out.print("===================================");
    }


    // Press any key to continue
    public void pressAnyKeyToContinue() {
        System.out.println("Press any key to continue...");
        try {
            System.in.read();
            clearScreen();
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }

    public int getChoice() {
        main = new Main();
        return Integer.parseInt(main.scanner.nextLine());
    }

    // Menu
    public void userMenu() {
        boolean active = true;
        do {
            System.out.print("1. View account information\n" +
                    "2. View transaction history\n" +
                    "3. Transfer money\n" +
                    "4. Withdraw money\n" +
                    "5. Deposit money\n" +
                    "6. Logout\n" +
                    "Enter your choice: ");

            int choice = getChoice();
            handleUserChoice(choice);
        } while (active);
    }

    public void handleUserChoice(int choice) {
        switch (choice) {
            case 1:
                clearScreen();
                System.out.println("View account information");
                userService_impl.viewInfo();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 2:
                clearScreen();
                System.out.println("View transaction history");
                userService_impl.viewTransactionHistory();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 3:
                clearScreen();
                System.out.println("Transfer money");
                userService_impl.transfer();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 4:
                System.out.println("Withdraw money");
                userService_impl.withdraw();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 5:
                System.out.println("Deposit money");
                userService_impl.deposit();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 6:
                System.out.println("Logout");
                userService_impl.logout();
                mainMenu();
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    public void adminMenu() {
        boolean active = true;
        do {
            System.out.print("1. View transaction history\n" +
                    "2. View account information\n" +
                    "3. Find account\n" +
                    "4. Find transaction\n" +
                    "5. Account statistics\n" +
                    "6. Transaction statistics\n" +
                    "7. Update user information\n" +
                    "8. Logout\n" +
                    "Enter your choice: ");

            int choice = getChoice();
            handleAdminChoice(choice);
        } while (active);
    }

    public void handleAdminChoice(int choice) {
        switch (choice) {
            case 1:
                clearScreen();
                System.out.println("View transaction history");
                adminService_impl.viewTransaction();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 2:
                clearScreen();
                System.out.println("View account information");
                adminService_impl.viewAccount();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 3:
                clearScreen();
                System.out.println("Find account");
                adminService_impl.findAccount();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 4:
                clearScreen();
                System.out.println("Find transaction");
                adminService_impl.findTransaction();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 5:
                clearScreen();
                System.out.println("Account statistics");
                adminService_impl.accountStatistics();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 6:
                clearScreen();
                System.out.println("Transaction statistics");
                adminService_impl.transactionStatistics();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 7:
                clearScreen();
                System.out.println("Update user information");
                adminService_impl.updateUserInfo();
                pressAnyKeyToContinue();
                clearScreen();
                break;
            case 8:
                System.out.println("Logout");
                adminService_impl.logout();
                mainMenu();
                break;
            default:
                System.out.println("Invalid choice!");
                break;

        }
    }

    public void mainMenu() {
        System.out.println("Welcome to ATM");
        System.out.println("1. Login\n" +
                "2. Login to Admin Session\n" +
                "3. Register\n" +
                "4. Exit");
        System.out.print("Enter your choice: ");
        int choice = getChoice();
        handleMainMenuChoice(choice);
    }

    public void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                clearScreen();
                System.out.println("Login");
                if (userService_impl.login()) {
                    pressAnyKeyToContinue();
                    clearScreen();
                    userMenu();
                } else {
                    System.out.println("Login failed!");
                    mainMenu();
                }
                break;
            case 2:
                clearScreen();
                System.out.println("Login to Admin Session");
                if (adminService_impl.login()) {
                    System.out.println("Login successfully!");
                    pressAnyKeyToContinue();
                    clearScreen();
                    adminMenu();
                } else {
                    System.out.println("Login failed!");
                    mainMenu();
                }
                break;
            case 3:
                clearScreen();
                System.out.println("Register");
                if (userService_impl.register()) {
                    System.out.println("Register successfully!");
                    pressAnyKeyToContinue();
                    clearScreen();
                    mainMenu();
                } else {
                    System.out.println("Register failed!");
                    mainMenu();
                }
                break;
            case 4:
                System.out.println("Exit");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    public static void main(String[] args) {
        main = new Main();
        main.clearScreen();
        main.mainMenu();
    }
}