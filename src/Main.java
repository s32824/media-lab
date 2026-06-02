import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DiscountPolicy policy = new LoyaltyDiscountPolicy();
        ReservationService service = new ReservationService(policy);

        //students
        service.addStudent(new Student("5001", "Anna Kowalska", "12c", 120));
        service.addStudent(new Student("5002", "Marek Nowak", "12b", 40));
        service.addStudent(new Student("5003", "Julia Zielinska", "13a", 0));

        //equipment
        service.addEquipment(new LaptopSet("E001", "Lenovo ThinkPad Lab", 89.0, 32, true));
        service.addEquipment(new LaptopSet("E002", "Dell XPS Demo", 100.0, 16, false));
        service.addEquipment(new CameraKit("E003", "Sony Content Kit", 90.0, 3, true));
        service.addEquipment(new CameraKit("E004", "Canon Interview Kit", 70.0, 1, true));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== MediaLab Equipment Reservation System ===");

            System.out.println("1. Display the list of students.");
            System.out.println("2. Display the list of equipment.");
            System.out.println("3. Create a new reservation.");
            System.out.println("4. Return equipment.");
            System.out.println("5. Display active reservations.");
            System.out.println("6. Display report and revenue.");
            System.out.println("7. Exit the program.");

            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.println("\n--- Registered Students ---");

                        for (Student s : service.getStudents()) {
                            System.out.printf("ID: %s | %s | Group: %s | Loyalty Points: %d%n",
                                    s.getId(), s.getFullName(), s.getGroupName(), s.getLoyaltyPoints());
                        }

                        System.out.println();
                        break;

                    case "2":
                        System.out.println("\n--- MediaLab Equipment Catalog ---");

                        for (Equipment e : service.getEquipmentList()) {
                            System.out.println(e.getDisplayText());
                        }

                        System.out.println();
                        break;

                    case "3":
                        System.out.println("\n--- Create Reservation ---");

                        System.out.print("Enter student id: ");
                        String sId = scanner.nextLine();

                        System.out.print("Enter equipment id: ");
                        String eId = scanner.nextLine();

                        System.out.print("Enter number of days: ");
                        int days = Integer.parseInt(scanner.nextLine());

                        Reservation res = service.createReservation(sId, eId, days);
                        System.out.println("\nReservation successfully created!");
                        System.out.println(res.getDisplayText() + "\n");
                        break;

                    case "4":
                        System.out.println("\n--- Process Equipment Return ---");

                        System.out.print("Enter reservation id: ");
                        String rId = scanner.nextLine();

                        service.returnEquipment(rId);
                        System.out.println();
                        break;

                    case "5":
                        System.out.println("\n--- Currently Active Reservations ---");

                        long activeCount = service.getReservations().stream().filter(r -> r.getStatus() == ReservationStatus.ACTIVE).peek(r -> System.out.println(r.getDisplayText())).count();

                        if (activeCount == 0) {
                            System.out.println("  No active reservations found.");
                        }

                        System.out.println();
                        break;

                    case "6":
                        service.printReport();
                        break;

                    case "7":
                        System.out.println("Thank you for using MediaLab System. Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid selection. Please choose an option from 1 to 7.\n");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Input processing error: Please enter a numeric value where requested.\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
    }
}