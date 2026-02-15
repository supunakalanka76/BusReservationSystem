import java.util.*;

class Customer {
    String name;
    String mobileNumber;
    String email;
    String city;
    int age;

    public Customer(String name, String mobileNumber, String email, String city, int age) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.city = city;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{name='" + name + "', mobileNumber='" + mobileNumber + "', email='"
                + email + "', city='" + city + "', age=" + age + "}";
    }
}

class Bus {
    String busNumber;
    int totalSeats;
    String startingPoint;
    String endingPoint;
    String startingTime;
    double fare;
    boolean[] seats;
    Map<Integer, Customer> seatReservations = new HashMap<>();

    public Bus(String busNumber, int totalSeats, String startingPoint, String endingPoint, String startingTime, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.startingTime = startingTime;
        this.fare = fare;
        this.seats = new boolean[totalSeats];
    }

    public boolean reserveSeat(int seatNumber, Customer customer, Queue<Customer> waitingQueue) {
        // Check if the seat number is valid
        if (seatNumber >= 1 && seatNumber <= totalSeats) {
            // Check if the seat is available
            if (!seats[seatNumber - 1]) {
                seats[seatNumber - 1] = true;
                seatReservations.put(seatNumber, customer);
                System.out.println("Notification: Reservation confirmed for " + customer.name + " at seat " + seatNumber);
                return true;
            } else {
                System.out.println("Seat " + seatNumber + " is already reserved.");
            }
        } else {
            System.out.println("Invalid seat number.");
            return false; // Invalid seat number should not add to the waiting queue
        }

        // Check if the customer is not already in the waiting queue
        if (!waitingQueue.contains(customer)) {
            waitingQueue.add(customer);
            System.out.println("Notification: Bus is full. " + customer.name + " added to the waiting queue.");
        }
        return false;
    }


    public boolean cancelReservation(int seatNumber, Queue<Customer> waitingQueue) {
        if (seatNumber >= 1 && seatNumber <= totalSeats && seats[seatNumber - 1]) {
            Customer canceledCustomer = seatReservations.remove(seatNumber);
            seats[seatNumber - 1] = false;
            System.out.println("Notification: Reservation canceled for " + canceledCustomer.name + " at seat " + seatNumber);

            if (!waitingQueue.isEmpty()) {
                Customer nextCustomer = waitingQueue.poll();
                reserveSeat(seatNumber, nextCustomer, waitingQueue);
                System.out.println("Notification: Seat " + seatNumber + " is now available for " + nextCustomer.name);
            }
            return true;
        }
        return false;
    }

    public void displayReservations() {
        System.out.println("Reservations for Bus " + busNumber + ":");
        for (Map.Entry<Integer, Customer> entry : seatReservations.entrySet()) {
            System.out.println("Seat " + entry.getKey() + ": " + entry.getValue());
        }
    }

    @Override
    public String toString() {
        return "Bus{busNumber='" + busNumber + "', totalSeats=" + totalSeats +
                ", startingPoint='" + startingPoint + "', endingPoint='" + endingPoint + "', startingTime='" +
                startingTime + "', fare=" + fare + "}";
    }
}

public class BusReservationSystem {

    static List<Customer> customers = new ArrayList<>();
    static List<Bus> buses = new ArrayList<>();
    static Queue<Customer> waitingQueue = new LinkedList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Bus Reservation System ---");
            System.out.println("1. Register Customer");
            System.out.println("2. Register Bus");
            System.out.println("3. Search for Buses");
            System.out.println("4. Reserve Seat");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. View All Reservations");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Mobile Number: ");
                    String mobileNumber = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter City: ");
                    String city = scanner.nextLine();
                    System.out.print("Enter Age: ");
                    int age = scanner.nextInt();
                    customers.add(new Customer(name, mobileNumber, email, city, age));
                    System.out.println("Customer registered successfully!");
                    break;
                case 2:
                    System.out.print("Enter Bus Number: ");
                    String busNumber = scanner.nextLine();
                    System.out.print("Enter Total Seats: ");
                    int totalSeats = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Starting Point: ");
                    String startingPoint = scanner.nextLine();
                    System.out.print("Enter Ending Point: ");
                    String endingPoint = scanner.nextLine();
                    System.out.print("Enter Starting Time: ");
                    String startingTime = scanner.nextLine();
                    System.out.print("Enter Fare: ");
                    double fare = scanner.nextDouble();
                    buses.add(new Bus(busNumber, totalSeats, startingPoint, endingPoint, startingTime, fare));
                    System.out.println("Bus registered successfully!");
                    break;
                case 3:
                    System.out.print("Enter Starting Point: ");
                    String searchStart = scanner.nextLine();
                    System.out.print("Enter Ending Point: ");
                    String searchEnd = scanner.nextLine();
                    System.out.println("Available Buses:");

                    boolean busFound = false; // Flag to check if any bus is found

                    for (Bus bus : buses) {
                        if (bus.startingPoint.equalsIgnoreCase(searchStart) && bus.endingPoint.equalsIgnoreCase(searchEnd)) {
                            busFound = true; // Set flag to true when a bus is found
                            int availableSeats = 0;
                            for (boolean seat : bus.seats) {
                                if (!seat) {
                                    availableSeats++;
                                }
                            }
                            System.out.println(bus + ", Available Seats: " + availableSeats);
                        }
                    }

                    if (!busFound) {
                        System.out.println("Currently, no buses are available for the selected route.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Customer Name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter Bus Number: ");
                    busNumber = scanner.nextLine();
                    System.out.print("Enter Seat Number to Reserve: ");
                    int seatNumber = scanner.nextInt();
                    for (Bus bus : buses) {
                        if (bus.busNumber.equals(busNumber)) {
                            for (Customer customer : customers) {
                                if (customer.name.equalsIgnoreCase(customerName)) {
                                    if (!bus.reserveSeat(seatNumber, customer, waitingQueue)) {
                                        System.out.println("Reservation failed.");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 5:
                    System.out.print("Enter Bus Number: ");
                    busNumber = scanner.nextLine();
                    System.out.print("Enter Seat Number to Cancel: ");
                    seatNumber = scanner.nextInt();
                    for (Bus bus : buses) {
                        if (bus.busNumber.equals(busNumber)) {
                            if (!bus.cancelReservation(seatNumber, waitingQueue)) {
                                System.out.println("Cancellation failed.");
                            }
                            break;
                        }
                    }
                    break;
                case 6:
                    for (Bus bus : buses) {
                        bus.displayReservations();
                    }
                    break;
                case 7:
                    System.out.println("Exiting... Thank you for using the Bus Reservation System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
