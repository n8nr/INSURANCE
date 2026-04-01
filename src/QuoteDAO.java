import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuoteDAO {

    public int createQuote(int customerId, String vehicleText, int age, int ncbYears, String policyType, double premium) throws Exception {
        String now = LocalDateTime.now().toString();

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO quotes(customer_id,vehicle_text,driver_age,ncb_years,policy_type,premium,status,created_at) " +
                             "VALUES(?,?,?,?,?,?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {

            ps.setInt(1, customerId);
            ps.setString(2, vehicleText);
            ps.setInt(3, age);
            ps.setInt(4, ncbYears);
            ps.setString(5, policyType);
            ps.setDouble(6, premium);
            ps.setString(7, "GENERATED");
            ps.setString(8, now);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }

        return -1;
    }

    public List<AdminQuoteRow> getAllQuotesForAdmin() throws Exception {
        List<AdminQuoteRow> rows = new ArrayList<>();

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT q.id, c.email, q.vehicle_text, q.policy_type, q.premium, q.status, q.created_at " +
                             "FROM quotes q JOIN customers c ON q.customer_id = c.id " +
                             "ORDER BY q.id DESC"
             );
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new AdminQuoteRow(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("vehicle_text"),
                        rs.getString("policy_type"),
                        rs.getDouble("premium"),
                        rs.getString("status"),
                        rs.getString("created_at")
                ));
            }
        }

        return rows;
    }

    public void approve(int quoteId) throws Exception {
        setStatus(quoteId, "APPROVED");
    }

    public void reject(int quoteId) throws Exception {
        setStatus(quoteId, "REJECTED");
    }

    private void setStatus(int quoteId, String status) throws Exception {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE quotes SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, quoteId);
            ps.executeUpdate();
        }
    }

    public static class AdminQuoteRow {
        public final int id;
        public final String customerEmail;
        public final String vehicleText;
        public final String policyType;
        public final double premium;
        public final String status;
        public final String createdAt;

        public AdminQuoteRow(int id, String customerEmail, String vehicleText, String policyType, double premium, String status, String createdAt) {
            this.id = id;
            this.customerEmail = customerEmail;
            this.vehicleText = vehicleText;
            this.policyType = policyType;
            this.premium = premium;
            this.status = status;
            this.createdAt = createdAt;
        }
    }

    public java.util.List<CustomerQuoteRow> getQuotesForCustomer(int customerId) throws Exception {
        java.util.List<CustomerQuoteRow> rows = new java.util.ArrayList<>();

        try (java.sql.Connection c = DB.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(
                     "SELECT id, vehicle_text, policy_type, premium, status, created_at " +
                             "FROM quotes WHERE customer_id=? ORDER BY id DESC"
             )) {

            ps.setInt(1, customerId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new CustomerQuoteRow(
                            rs.getInt("id"),
                            rs.getString("vehicle_text"),
                            rs.getString("policy_type"),
                            rs.getDouble("premium"),
                            rs.getString("status"),
                            rs.getString("created_at")
                    ));
                }
            }
        }

        return rows;
    }

    public static class CustomerQuoteRow {
        public final int id;
        public final String vehicleText;
        public final String policyType;
        public final double premium;
        public final String status;
        public final String createdAt;

        public CustomerQuoteRow(int id, String vehicleText, String policyType, double premium, String status, String createdAt) {
            this.id = id;
            this.vehicleText = vehicleText;
            this.policyType = policyType;
            this.premium = premium;
            this.status = status;
            this.createdAt = createdAt;
        }
    }
}