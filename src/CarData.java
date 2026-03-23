public class CarData {

    private String make;
    private String model;
    private String fuelType;
    private double engineSize;
    private int year;
    private String transmission;

    // Constructor
    public CarData(String make, String model, String fuelType, double engineSize, int year, String transmission) {
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.engineSize = engineSize;
        this.year = year;
        this.transmission = transmission;
    }

    // Getters
    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public int getYear() {
        return year;
    }

    public String getTransmission() {
        return transmission;
    }

    // Setters
    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    // toString method
    @Override
    public String toString() {
        return make + " " + model + " (" + year + ", " + engineSize + "L, " + fuelType + ", " + transmission + ")";
    }
}