public class QuoteCalculator {

    public double calculate(double basePrice, int age, int ncbYears, String policyType) {
        double ageFactor = ageFactor(age);
        double policyFactor = policyFactor(policyType);
        double ncbFactor = ncbDiscountFactor(ncbYears);

        double premium = basePrice * ageFactor * policyFactor * ncbFactor;

        if (premium < 200) premium = 200;

        return round2(premium);
    }

    private double ageFactor(int age) {
        if (age < 21) return 1.8;
        if (age < 25) return 1.5;
        if (age < 30) return 1.2;
        return 1.0;
    }

    private double policyFactor(String policyType) {
        if (policyType == null) return 1.0;
        String p = policyType.trim().toLowerCase();

        if (p.contains("fire") || p.contains("theft")) return 1.15;
        if (p.contains("comprehensive")) return 1.30;
        return 1.0;
    }

    private double ncbDiscountFactor(int ncbYears) {
        int capped = Math.max(0, Math.min(ncbYears, 15));
        double discount = 0.03 * capped;   // 3% per year
        if (discount > 0.45) discount = 0.45;
        return 1.0 - discount;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}