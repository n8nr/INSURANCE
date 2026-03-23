import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository {

    private final List<CarData> cars = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Quote> quotes = new ArrayList<>();

    private int nextCustomerId = 1;
    private int nextQuoteId = 1;

    // =========================
    // LOAD CAR DATA FROM CSV
    // =========================
    public void loadCars() {
        try (BufferedReader br = new BufferedReader(new FileReader("resources/cars.csv"))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String make = values[0].trim();
                String model = values[1].trim();
                String fuel = values[2].trim();
                double engine = Double.parseDouble(values[3].trim());
                int year = Integer.parseInt(values[4].trim());
                String transmission = values[5].trim();

                cars.add(new CarData(make, model, fuel, engine, year, transmission));
            }

        } catch (Exception e) {
            System.out.println("Error loading cars.csv");
            e.printStackTrace();
        }
    }

    // =========================
    // GET UNIQUE MAKES
    // =========================
    public List<String> getMakes() {
        List<String> makes = new ArrayList<>();

        for (CarData car : cars) {
            if (!makes.contains(car.getMake())) {
                makes.add(car.getMake());
            }
        }

        return makes;
    }

    // =========================
    // GET MODELS BY MAKE
    // =========================
    public List<String> getModelsByMake(String make) {
        List<String> models = new ArrayList<>();

        if (make == null) return models;

        for (CarData car : cars) {
            if (car.getMake().equalsIgnoreCase(make) && !models.contains(car.getModel())) {
                models.add(car.getModel());
            }
        }

        return models;
    }

    // =========================
    // GET FULL CAR OBJECT
    // =========================
    public CarData getCar(String make, String model) {
        for (CarData car : cars) {
            if (car.getMake().equalsIgnoreCase(make) &&
                    car.getModel().equalsIgnoreCase(model)) {
                return car;
            }
        }
        return null;
    }

    // =========================
    // CUSTOMER METHODS
    // =========================
    public Customer createCustomer(String name, String surname, String address, String phone, String email, String password) {
        Customer c = new Customer(nextCustomerId++, name, surname, address, phone, email, password);
        customers.add(c);
        return c;
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getEmail() != null && c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    // =========================
    // QUOTE METHODS
    // =========================
    public Quote createQuote(Customer customer, String vehicleText, int driverAge, int ncbYears, String policyType, double premium) {
        Quote q = new Quote(nextQuoteId++, customer, vehicleText, driverAge, ncbYears, policyType, premium);
        quotes.add(q);
        return q;
    }

    public List<Quote> getAllQuotes() {
        return new ArrayList<>(quotes);
    }

    public void approveQuote(int quoteId) {
        quotes.stream()
                .filter(q -> q.getId() == quoteId)
                .findFirst()
                .ifPresent(q -> q.setStatus(Quote.Status.APPROVED));
    }

    public void rejectQuote(int quoteId) {
        quotes.stream()
                .filter(q -> q.getId() == quoteId)
                .findFirst()
                .ifPresent(q -> q.setStatus(Quote.Status.REJECTED));
    }
}