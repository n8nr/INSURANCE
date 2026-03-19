import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository {
    private final List<Customer> customers = new ArrayList<>();
    private final List<Quote> quotes = new ArrayList<>();

    private int nextCustomerId = 1;
    private int nextQuoteId = 1;

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

    public Quote createQuote(Customer customer, String vehicleText, int driverAge, int ncbYears, String policyType, double premium) {
        Quote q = new Quote(nextQuoteId++, customer, vehicleText, driverAge, ncbYears, policyType, premium);
        quotes.add(q);
        return q;
    }

    public List<Quote> getAllQuotes() {
        return new ArrayList<>(quotes);
    }

    public void approveQuote(int quoteId) {
        quotes.stream().filter(q -> q.getId() == quoteId).findFirst()
                .ifPresent(q -> q.setStatus(Quote.Status.APPROVED));
    }

    public void rejectQuote(int quoteId) {
        quotes.stream().filter(q -> q.getId() == quoteId).findFirst()
                .ifPresent(q -> q.setStatus(Quote.Status.REJECTED));
    }
}