import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDAO {

    public Customer register(String fullName, String address, String phone, String email, String password) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email required");
        }

        String cleanedFullName = fullName == null ? "" : fullName.trim();
        String[] parts = cleanedFullName.split("\\s+", 2);
        String name = parts.length > 0 ? parts[0] : "";
        String surname = parts.length > 1 ? parts[1] : "";

        try (Connection c = DB.getConnection()) {
            try (PreparedStatement check = c.prepareStatement(
                    "SELECT id FROM customers WHERE lower(email)=lower(?)"
            )) {
                check.setString(1, email.trim());
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        throw new IllegalArgumentException("Email already registered");
                    }
                }
            }

            try (PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO customers(name,surname,address,phone,email,password) VALUES(?,?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {
                ins.setString(1, name);
                ins.setString(2, surname);
                ins.setString(3, address);
                ins.setString(4, phone);
                ins.setString(5, email.trim());
                ins.setString(6, password);

                ins.executeUpdate();

                try (ResultSet keys = ins.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        return new Customer(id, name, surname, address, phone, email.trim(), password);
                    }
                }
            }
        }

        throw new RuntimeException("Register failed");
    }

    public Customer login(String email, String password) throws Exception {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id,name,surname,address,phone,email,password FROM customers WHERE lower(email)=lower(?)"
             )) {

            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalArgumentException("Invalid login");

                String dbPass = rs.getString("password");
                if (!dbPass.equals(password)) throw new IllegalArgumentException("Invalid login");

                return new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        }
    }
}