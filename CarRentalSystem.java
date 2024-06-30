import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CarRentalSystem {

    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();
        Scanner scanner = new Scanner(System.in);

        // cars
        agency.addCar(new Car(1, "Toyota", "Camry", 2023, 50.0));
        agency.addCar(new Car(2, "Honda", "Civic", 2022, 40.0));
        agency.addCar(new HyperCar(3, "Bugatti", "Chiron", 2022, 1500.0));
        agency.addCar(new HyperCar(4, "Koenigsegg", "Agera RS", 2023, 2000.0));

        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewAvailableCars(agency);
                    break;
                case 2:
                    enterCustomerDetails(agency, scanner);
                    break;
                case 3:
                    rentCar(agency, scanner);
                    break;
                case 4:
                    returnCar(agency, scanner);
                    break;
                case 5:
                    System.out.println("Thank you for using the Car Rental System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\nWelcome to the Car Rental System");
        System.out.println("1. View available cars");
        System.out.println("2. Enter customer details");
        System.out.println("3. Rent a car");
        System.out.println("4. Return a car");
        System.out.println("5. Exit");
        System.out.print("Please enter your choice: ");
    }

    private static void viewAvailableCars(RentalAgency agency) {
        List<Car> availableCars = agency.findAvailableCars();
        if (availableCars.isEmpty()) {
            System.out.println("No cars available for rent.");
        } else {
            System.out.println("Available cars:");
            for (Car car : availableCars) {
                System.out.println(car);
            }
        }
    }

    private static void enterCustomerDetails(RentalAgency agency, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();  

        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter driver license number: ");
        String driverLicense = scanner.nextLine();

        agency.addCustomer(new Customer(customerId, name, driverLicense));
        System.out.println("Customer details added successfully!");
    }

    private static void rentCar(RentalAgency agency, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();

        System.out.print("Enter car ID: ");
        int carId = scanner.nextInt();

        try {
            agency.rentCar(customerId, carId);
            System.out.println("Car rented successfully!");
        } catch (CarRentalException e) {
            System.out.println("Error renting car: " + e.getMessage());
        }
    }

    private static void returnCar(RentalAgency agency, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();

        System.out.print("Enter car ID: ");
        int carId = scanner.nextInt();

        try {
            agency.returnCar(customerId, carId);
            System.out.println("Car returned successfully!");
        } catch (CarRentalException e) {
            System.out.println("Error returning car: " + e.getMessage());
        }
    }
}

class CarRentalException extends Exception {
    public CarRentalException(String message) {
        super(message);
    }
}

class Rental {
    private Customer customer;
    private Car car;
    private Date rentalStartDate;
    private Date rentalEndDate;
    private double rentalCost;

    public Rental(Customer customer, Car car, Date rentalStartDate, Date rentalEndDate, double rentalCost) {
        this.customer = customer;
        this.car = car;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.rentalCost = rentalCost;
    }

}

class Car {
    protected int carId;
    protected String make;
    protected String model;
    protected int year;
    protected double dailyRate;
    protected boolean available;

    public Car(int carId, String make, String model, int year, double dailyRate) {
        this.carId = carId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.available = true;
    }

    public int getCarId() {
        return carId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void rent() {
        this.available = false;
    }

    public void returnCar() {
        this.available = true;
    }

    public double calculateRentalCost(Date startDate, Date endDate) {
        long diffInDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return diffInDays * dailyRate;
    }

    @Override
    public String toString() {
        return "Car ID: " + carId + ", Make: " + make + ", Model: " + model + ", Year: " + year + ", Daily Rate: $" + dailyRate + (available ? " (Available)" : " (Rented)");
    }
}

class HyperCar extends Car {
    public HyperCar(int carId, String make, String model, int year, double dailyRate) {
        super(carId, make, model, year, dailyRate);
    }

    @Override
    public String toString() {
        return "HyperCar ID: " + getCarId() + ", Make: " + make + ", Model: " + model + ", Year: " + year + ", Daily Rate: $" + dailyRate + (isAvailable() ? " (Available)" : " (Rented)");
    }
}

class Customer {
    private int customerId;
    private String name;
    private String driverLicense;

    public Customer(int customerId, String name, String driverLicense) {
        this.customerId = customerId;
        this.name = name;
        this.driverLicense = driverLicense;
    }

    public int getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name;
    }
}

class RentalAgency {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public RentalAgency() {
        this.cars = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.rentals = new ArrayList<>();
    }
    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(int customerId, int carId) throws CarRentalException {
        Car car = findCarById(carId);
        if (car == null || !car.isAvailable()) {
            throw new CarRentalException("Car not available");
        }
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new CarRentalException("Customer not found");
        }
        car.rent();
        rentals.add(new Rental(customer, car, new Date(), null, 0)); 
    }

    public void returnCar(int customerId, int carId) throws CarRentalException {
        Car car = findCarById(carId);
        if (car == null || car.isAvailable()) {
            throw new CarRentalException("Car not rented");
        }
        car.returnCar();
        
    }

    public List<Car> findAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    private Car findCarById(int carId) {
        for (Car car : cars) {
            if (car.getCarId() == carId) {
                return car;
            }
        }
        return null;
    }

    private Customer findCustomerById(int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }
}