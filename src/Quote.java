import java.time.LocalDateTime;

public class Quote {
    public enum Status { GENERATED, APPROVED, REJECTED }

    private final int id;
    private final Customer customer;
    private final String vehicleText;
    private final int driverAge;
    private final int ncbYears;
    private final String policyType;
    private final double premium;
    private Status status;
    private final LocalDateTime createdAt;

    public Quote(int id, Customer customer, String vehicleText, int driverAge, int ncbYears,
                 String policyType, double premium) {
        this.id = id;
        this.customer = customer;
        this.vehicleText = vehicleText;
        this.driverAge = driverAge;
        this.ncbYears = ncbYears;
        this.policyType = policyType;
        this.premium = premium;
        this.status = Status.GENERATED;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public String getVehicleText() { return vehicleText; }
    public int getDriverAge() { return driverAge; }
    public int getNcbYears() { return ncbYears; }
    public String getPolicyType() { return policyType; }
    public double getPremium() { return premium; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(Status status) { this.status = status; }
}