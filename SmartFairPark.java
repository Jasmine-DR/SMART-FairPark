import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

class Main {
    // Vehicle Type
    static String vehicleType(int numberofWheels) {
        switch(numberofWheels) {
            case 2:
                return "Motorcycle";
            case 3:
                return "Tricycle";
            case 4:
                return "Car";
            default:
                return "Invalid vehicle type";
        }
    }

    // Calculate Time Duration
    static Duration timeDuration(LocalTime in, LocalTime out) {
        Duration gap = Duration.between(in, out);

        if (gap.isNegative()) {
            gap = gap.plusHours(24);
        }

        return gap;
    }

    // Calculate Parking Fee
    static int parkingFee(Duration gap) {
        long hours = gap.toHours();
        long minutes = gap.toMinutes() % 60;

        int fee = 50;
        int additionalHours = 0;

        if (hours > 2) {
            additionalHours = (int)(hours - 2);
        }if (hours >= 2 && minutes > 0) {
            additionalHours++;
        }

        fee += additionalHours * 20;

        return fee;
    }

    // PWD/Senior Discount
    static double discount(int fee, boolean hasDiscount) {
        if (hasDiscount) {
            return fee - (fee * 0.10);
        } else {
            return fee;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Parking Slot Records
        String[] plate = new String[5];
        int[] wheels = new int[5];
        LocalTime[] timeIn = new LocalTime[5];
        boolean[] occupied = new boolean[5];

        int availableSlots = 5;
        int totalVehicles = 0;
        double totalPay = 0;

        int choice;

        do {

            // eto yung pinaka main, always return here (DASHBOARD)
            System.out.println("\n==============================");
            System.out.println("     SMART FAIRPARKING");
            System.out.println("==============================");
            System.out.println("Available parking slots: " + availableSlots);
            System.out.println("==============================");

            System.out.println("Choose act:");
            System.out.println("1. Time in car");
            System.out.println("2. Time out car");
            System.out.println("3. Overall day report");
            System.out.println("4. Exit");

            System.out.print("\nEnter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                // TIME IN
                case 1:

                    if (availableSlots > 0) {

                        int freeSlot = -1;

                        // Find available slot
                        for (int i = 0; i < 10; i++) {
                            if (!occupied[i]) {
                                freeSlot = i;
                                break;
                            }
                        }

                        System.out.print("\nEnter number of wheels: ");
                        int numberofWheels = sc.nextInt();
                        sc.nextLine();

                        String type = vehicleType(numberofWheels);

                        if (type.equals("Invalid vehicle type")) {
                            System.out.println("INVALID VEHICLE TYPE");
                            break;
                        }

                        System.out.print("Enter license plate: ");
                        String licensePlate = sc.nextLine();

                        System.out.print("Enter time in (HH:mm): ");
                        String in = sc.nextLine();

                        // Save vehicle details
                        plate[freeSlot] = licensePlate;
                        wheels[freeSlot] = numberofWheels;
                        timeIn[freeSlot] = LocalTime.parse(in);
                        occupied[freeSlot] = true;

                        availableSlots--;
                        totalVehicles++;

                        System.out.println("\nVEHICLE ADMITTED!");
                        System.out.println("Vehicle type: " + type);
                        System.out.println("License plate: " + plate[freeSlot]);
                        System.out.println("Time in: " + timeIn[freeSlot]);
                        System.out.println("Parking slot: " + (freeSlot + 1));

                    } else {
                        System.out.println("\nPARKING FULL!");
                    }

                    break;

                // TIME OUT
                case 2:

                    System.out.print("\nEnter license plate: ");
                    String searchPlate = sc.nextLine();

                    int found = -1;

                    // Search vehicle
                    for (int i = 0; i < 5; i++) {
                        if (occupied[i] && plate[i].equalsIgnoreCase(searchPlate)) {
                            found = i;
                            break;
                        }
                    }

                    if (found != -1) {

                        System.out.print("Enter time out (HH:mm): ");
                        String out = sc.nextLine();

                        System.out.print("Has PWD/Senior discount card? (true/false): ");
                        boolean hasDiscount = sc.nextBoolean();

                        LocalTime timeOut = LocalTime.parse(out);

                        // Calculate duration
                        Duration gap = timeDuration(timeIn[found], timeOut);

                        long hours = gap.toHours();
                        long minutes = gap.toMinutes() % 60;

                        // Calculate fee
                        int fee = parkingFee(gap);

                        // Apply discount
                        double finalFee = discount(fee, hasDiscount);

                        System.out.println("\n==============================");
                        System.out.println("       PARKING RECEIPT");
                        System.out.println("==============================");

                        System.out.println("License plate: " + plate[found]);
                        System.out.println("Vehicle type: " + vehicleType(wheels[found]));
                        System.out.println("Time in: " + timeIn[found]);
                        System.out.println("Time out: " + timeOut);

                        System.out.println("Duration: " + hours + " hours and " + minutes + " minutes");

                        System.out.println("Original fee: ₱" + fee);
                        System.out.println("Discount status: " + (hasDiscount ? "Discount applied" : "No discount"));

                        System.out.printf("Total payment: ₱%.2f%n", finalFee);

                        System.out.println("==============================");

                        // Update revenue
                        totalPay += finalFee;

                        // Free parking slot
                        occupied[found] = false;
                        availableSlots++;

                        System.out.println("PARKING SLOT RELEASED!");

                        sc.nextLine();

                    } else {
                        System.out.println("\nVEHICLE NOT FOUND");
                    }

                    break;

                // OVERALL DAY REPORT
                case 3:

                    System.out.println("==============================");
                    System.out.println("       OVERALL DAY REPORT");
                    System.out.println("==============================");

                    System.out.println("Total vehicles admitted: " + totalVehicles);

                    System.out.printf("Total money collected: ₱%.2f%n", totalPay);

                    System.out.println("Available parking slots: " + availableSlots);

                    System.out.println("==============================");

                    break;

                // EXIT
                case 4:

                    System.out.println("\nClosing Parking Management System...");
                    System.out.println("Thank you!");

                    break;

                default:
                    System.out.println("\nINVALID CHOICE!");
            }

        } while (choice != 4);

        sc.close();
    }
}