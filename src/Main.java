import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {

    private static final String[] CARDS = {"LOGIN", "VEHICLE", "FACTORS", "POLICY", "PERSONAL", "QUOTE"};

    private static final int FRAME_W = 900;
    private static final int FRAME_H = 560;

    private static final int FIELD_W = 260;
    private static final int FIELD_H = 30;

    private static final int COMBO_W = 220;
    private static final int COMBO_H = 32;

    private static final int BTN_W = 120;
    private static final int BTN_H = 36;

    private final JFrame frame = new JFrame("Insurance Quote App");
    private final JPanel cards = new JPanel(new CardLayout());

    private final Repository repo = new Repository();
    private final QuoteCalculator calculator = new QuoteCalculator();

    private JComboBox<String> makeCb, modelCb, yearCb, engineCb, colourCb;
    private JComboBox<String> ageCb, ncbCb;
    private JToggleButton policyThirdParty, policyTpft, policyComprehensive;

    private JTextField nameTf, surnameTf, addressTf, phoneTf, emailTf;
    private JPasswordField passwordPf;

    private JLabel qVehicle, qPolicy, qAge, qNcb, qPrice, qStatus;

    private Customer currentCustomer;
    private Quote currentQuote;

    public static void main(String[] ignored) {
        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private void start() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(FRAME_W, FRAME_H);
        frame.setLocationRelativeTo(null);

        cards.add(loginScreen(), CARDS[0]);
        cards.add(vehicleScreen(), CARDS[1]);
        cards.add(factorsScreen(), CARDS[2]);
        cards.add(policyScreen(), CARDS[3]);
        cards.add(personalScreen(), CARDS[4]);
        cards.add(quoteScreen(), CARDS[5]);

        frame.setContentPane(cards);
        showCard(CARDS[0]);
        frame.setVisible(true);
    }

    private void showCard(String name) {
        ((CardLayout) cards.getLayout()).show(cards, name);
    }

    private JPanel basePanel() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(18, 18, 18, 18));
        return p;
    }

    private JLabel labelLeft(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.LEFT);
        l.setForeground(Color.BLACK);
        l.setFont(new Font("SansSerif", Font.PLAIN, size));
        return l;
    }

    private JLabel labelCenter(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(Color.BLACK);
        l.setFont(new Font("SansSerif", Font.PLAIN, size));
        return l;
    }

    private JLabel progressLabel(int step) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append(i == step ? "●" : "○");
            if (i < 5) sb.append(" ");
        }
        JLabel l = new JLabel(sb.toString(), SwingConstants.RIGHT);
        l.setForeground(Color.BLACK);
        l.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return l;
    }

    private JButton button(String text, int w, int h) {
        JButton b = new JButton(text);
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        b.setPreferredSize(new Dimension(w, h));
        return b;
    }

    private JButton button(String text) {
        return button(text, BTN_W, BTN_H);
    }

    private void onClick(JButton b, Runnable action) {
        b.addActionListener(evt -> action.run());
    }

    private JTextField textField() {
        JTextField f = new JTextField();
        f.setBackground(Color.WHITE);
        f.setForeground(Color.BLACK);
        f.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        Dimension d = new Dimension(FIELD_W, FIELD_H);
        f.setPreferredSize(d);
        f.setMaximumSize(d);
        return f;
    }

    private JPasswordField passField() {
        JPasswordField f = new JPasswordField();
        f.setBackground(Color.WHITE);
        f.setForeground(Color.BLACK);
        f.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        Dimension d = new Dimension(FIELD_W, FIELD_H);
        f.setPreferredSize(d);
        f.setMaximumSize(d);
        return f;
    }

    private JComboBox<String> comboBox(int w) {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBackground(Color.WHITE);
        cb.setForeground(Color.BLACK);
        cb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        Dimension d = new Dimension(w, COMBO_H);
        cb.setPreferredSize(d);
        cb.setMaximumSize(d);
        return cb;
    }

    private JPanel topBar(String title, int step) {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.add(labelLeft(title, 18), BorderLayout.WEST);
        top.add(progressLabel(step), BorderLayout.EAST);
        return top;
    }

    // Screen 1
    private JPanel loginScreen() {
        JPanel root = basePanel();
        root.add(labelCenter("Insurance Quote App", 24), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JButton newQuote = button("START NEW QUOTE", 320, 48);
        newQuote.setMaximumSize(new Dimension(320, 48));
        newQuote.setAlignmentX(Component.CENTER_ALIGNMENT);
        onClick(newQuote, () -> showCard(CARDS[1]));

        JLabel resume = labelCenter("Resume a saved quote", 12);
        resume.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(Color.BLACK);

        JLabel loginHint = labelCenter("Login to view saved quotes", 12);
        loginHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField username = textField();
        JPasswordField password = passField();

        JButton login = button("Login", 120, 34);
        login.setMaximumSize(new Dimension(120, 34));
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        onClick(login, () -> showCard(CARDS[1]));

        center.add(Box.createVerticalStrut(24));
        center.add(newQuote);
        center.add(Box.createVerticalStrut(10));
        center.add(resume);
        center.add(Box.createVerticalStrut(22));
        center.add(sep);
        center.add(Box.createVerticalStrut(22));
        center.add(loginHint);
        center.add(Box.createVerticalStrut(10));
        center.add(username);
        center.add(Box.createVerticalStrut(10));
        center.add(password);
        center.add(Box.createVerticalStrut(12));
        center.add(login);
        center.add(Box.createVerticalGlue());

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // Screen 2
    private JPanel vehicleScreen() {
        JPanel root = basePanel();
        root.add(topBar("Vehicle Details", 1), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(780, 260));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel row1Labels = new JPanel(new GridLayout(1, 3, 12, 0));
        row1Labels.setBackground(Color.WHITE);
        row1Labels.add(labelLeft("Make", 12));
        row1Labels.add(labelLeft("Model", 12));
        row1Labels.add(labelLeft("Year", 12));

        JPanel row1Fields = new JPanel(new GridLayout(1, 3, 12, 0));
        row1Fields.setBackground(Color.WHITE);

        makeCb = comboBox(COMBO_W);
        modelCb = comboBox(COMBO_W);
        yearCb = comboBox(COMBO_W);

        makeCb.addItem("");
        for (String make : CarData.getMakes()) {
            makeCb.addItem(make);
        }

        modelCb.addItem("");

        makeCb.addActionListener(evt -> {
            String selectedMake = safe(makeCb.getSelectedItem());

            modelCb.removeAllItems();
            modelCb.addItem("");

            for (String m : CarData.getModelsForMake(selectedMake)) {
                modelCb.addItem(m);
            }
        });

        yearCb.addItem("");
        for (int y = 2010; y <= 2026; y++) yearCb.addItem(String.valueOf(y));

        row1Fields.add(makeCb);
        row1Fields.add(modelCb);
        row1Fields.add(yearCb);

        JPanel row2Labels = new JPanel(new GridLayout(1, 2, 12, 0));
        row2Labels.setBackground(Color.WHITE);
        row2Labels.add(labelLeft("Engine Size", 12));
        row2Labels.add(labelLeft("Colour", 12));

        JPanel row2Fields = new JPanel(new GridLayout(1, 2, 12, 0));
        row2Fields.setBackground(Color.WHITE);

        engineCb = comboBox(330);
        colourCb = comboBox(330);

        engineCb.addItem("");
        engineCb.addItem("1.0");
        engineCb.addItem("1.2");
        engineCb.addItem("1.4");
        engineCb.addItem("1.6");
        engineCb.addItem("2.0+");

        colourCb.addItem("");
        colourCb.addItem("Black");
        colourCb.addItem("White");
        colourCb.addItem("Silver");
        colourCb.addItem("Blue");

        row2Fields.add(engineCb);
        row2Fields.add(colourCb);

        form.add(row1Labels);
        form.add(Box.createVerticalStrut(6));
        form.add(row1Fields);
        form.add(Box.createVerticalStrut(18));
        form.add(row2Labels);
        form.add(Box.createVerticalStrut(6));
        form.add(row2Fields);

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);

        JButton back = button("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[0]));
        onClick(next, () -> showCard(CARDS[2]));

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        wrapper.add(Box.createVerticalStrut(18));
        wrapper.add(form);
        wrapper.add(Box.createVerticalGlue());

        root.add(wrapper, BorderLayout.CENTER);
        root.add(nav, BorderLayout.SOUTH);
        return root;
    }

    // Screen 3
    private JPanel factorsScreen() {
        JPanel root = basePanel();
        root.add(topBar("Insurance Factors", 2), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(700, 140));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel labels = new JPanel(new GridLayout(1, 2, 12, 0));
        labels.setBackground(Color.WHITE);
        labels.add(labelLeft("Driver Age", 12));
        labels.add(labelLeft("No Claim Bonus (years)", 12));

        JPanel fields = new JPanel(new GridLayout(1, 2, 12, 0));
        fields.setBackground(Color.WHITE);

        ageCb = comboBox(240);
        ncbCb = comboBox(240);

        ageCb.addItem("");
        for (int i = 17; i <= 80; i++) ageCb.addItem(String.valueOf(i));

        ncbCb.addItem("");
        for (int i = 0; i <= 15; i++) ncbCb.addItem(String.valueOf(i));

        fields.add(ageCb);
        fields.add(ncbCb);

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);

        JButton back = button("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[1]));
        onClick(next, () -> showCard(CARDS[3]));

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        form.add(labels);
        form.add(Box.createVerticalStrut(6));
        form.add(fields);

        wrapper.add(Box.createVerticalStrut(40));
        wrapper.add(form);
        wrapper.add(Box.createVerticalGlue());

        root.add(wrapper, BorderLayout.CENTER);
        root.add(nav, BorderLayout.SOUTH);
        return root;
    }

    // Screen 4
    private JPanel policyScreen() {
        JPanel root = basePanel();
        root.add(topBar("Select Policy Type", 3), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 18, 0));
        cardsRow.setBackground(Color.WHITE);
        cardsRow.setMaximumSize(new Dimension(780, 210));
        cardsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();

        policyThirdParty = policyCard("Third Party", new String[]{"Injury to others", "Damage to others", "No cover for own car"});
        policyTpft = policyCard("Third Party, Fire & Theft", new String[]{"Third Party cover", "Fire damage", "Theft cover"});
        policyComprehensive = policyCard("Comprehensive", new String[]{"Own car damage", "Third Party cover", "Fire & theft included"});

        group.add(policyThirdParty);
        group.add(policyTpft);
        group.add(policyComprehensive);

        cardsRow.add(policyThirdParty);
        cardsRow.add(policyTpft);
        cardsRow.add(policyComprehensive);

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);

        JButton back = button("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[2]));
        onClick(next, () -> showCard(CARDS[4]));

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        wrapper.add(Box.createVerticalStrut(50));
        wrapper.add(cardsRow);
        wrapper.add(Box.createVerticalGlue());

        root.add(wrapper, BorderLayout.CENTER);
        root.add(nav, BorderLayout.SOUTH);
        return root;
    }

    private JToggleButton policyCard(String title, String[] lines) {
        JToggleButton b = new JToggleButton();
        b.setLayout(new BorderLayout());
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setBorderPainted(true);
        b.setContentAreaFilled(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        b.setPreferredSize(new Dimension(240, 200));

        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBorder(new EmptyBorder(12, 8, 8, 8));
        header.setOpaque(true);
        header.setBackground(Color.WHITE);

        JPanel list = new JPanel();
        list.setBackground(Color.WHITE);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(0, 18, 12, 18));

        for (String line : lines) {
            JLabel item = new JLabel("• " + line);
            item.setForeground(Color.BLACK);
            item.setFont(new Font("SansSerif", Font.PLAIN, 12));
            list.add(item);
            list.add(Box.createVerticalStrut(6));
        }

        b.add(header, BorderLayout.NORTH);
        b.add(list, BorderLayout.CENTER);

        b.addItemListener(evt -> {
            b.setBackground(Color.WHITE);
            header.setBackground(Color.WHITE);
            list.setBackground(Color.WHITE);
            if (b.isSelected()) {
                b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            } else {
                b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
        });

        return b;
    }

    // Screen 5
    private JPanel personalScreen() {
        JPanel root = basePanel();
        root.add(topBar("Personal Details", 4), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setMaximumSize(new Dimension(780, 260));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        nameTf = textField();
        surnameTf = textField();
        addressTf = textField();
        phoneTf = textField();
        emailTf = textField();
        passwordPf = passField();

        addRow(form, gbc, 0, "Name", nameTf);
        addRow(form, gbc, 1, "Surname", surnameTf);
        addRow(form, gbc, 2, "Address", addressTf);
        addRow(form, gbc, 3, "Phone", phoneTf);
        addRow(form, gbc, 4, "Email", emailTf);
        addRow(form, gbc, 5, "Password", passwordPf);

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);

        JButton back = button("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[3]));
        onClick(next, this::createCustomerAndQuote);

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        wrapper.add(Box.createVerticalStrut(18));
        wrapper.add(form);
        wrapper.add(Box.createVerticalGlue());

        root.add(wrapper, BorderLayout.CENTER);
        root.add(nav, BorderLayout.SOUTH);
        return root;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(labelLeft(label, 12), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(field, gbc);
    }

    private void createCustomerAndQuote() {
        String name = safe(nameTf.getText());
        String surname = safe(surnameTf.getText());
        String address = safe(addressTf.getText());
        String phone = safe(phoneTf.getText());
        String email = safe(emailTf.getText());
        String password = new String(passwordPf.getPassword());

        currentCustomer = repo.createCustomer(name, surname, address, phone, email, password);

        String make = safe(makeCb.getSelectedItem());
        String model = safe(modelCb.getSelectedItem());
        String year = safe(yearCb.getSelectedItem());

        String vehicleText = (make + " " + model + " " + year).trim();

        int age = parseIntSafe(ageCb.getSelectedItem(), 0);
        int ncb = parseIntSafe(ncbCb.getSelectedItem(), 0);

        String engine = safe(engineCb.getSelectedItem());
        String policy = selectedPolicyText();

        double basePrice = basePriceByMake(make);
        double premium = calculator.calculate(basePrice, age, ncb, policy);
        premium = premium * engineFactor(engine);
        premium = round2(premium);

        currentQuote = repo.createQuote(currentCustomer, vehicleText, age, ncb, policy, premium);

        updateQuoteLabels();
        showCard(CARDS[5]);
    }

    private String safe(Object v) {
        return v == null ? "" : String.valueOf(v).trim();
    }

    private int parseIntSafe(Object v, int fallback) {
        try {
            String s = safe(v);
            if (s.isEmpty()) return fallback;
            return Integer.parseInt(s);
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String selectedPolicyText() {
        if (policyTpft != null && policyTpft.isSelected()) return "Third Party, Fire & Theft";
        if (policyComprehensive != null && policyComprehensive.isSelected()) return "Comprehensive";
        return "Third Party";
    }

    private double basePriceByMake(String make) {
        String m = make == null ? "" : make.toLowerCase();
        if (m.contains("ford")) return 450;
        if (m.contains("vauxhall")) return 440;
        if (m.contains("volkswagen")) return 500;
        if (m.contains("toyota")) return 470;
        return 480;
    }

    private double engineFactor(String engine) {
        String e = engine == null ? "" : engine.trim();
        if (e.equals("1.0")) return 1.00;
        if (e.equals("1.2")) return 1.05;
        if (e.equals("1.4")) return 1.10;
        if (e.equals("1.6")) return 1.15;
        if (e.equals("2.0+")) return 1.25;
        return 1.00;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // Screen 6
    private JPanel quoteScreen() {
        JPanel root = basePanel();
        root.add(topBar("Your Quote", 5), BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(6, 2, 12, 10));
        info.setBackground(Color.WHITE);

        qVehicle = labelLeft("", 12);
        qPolicy = labelLeft("", 12);
        qAge = labelLeft("", 12);
        qNcb = labelLeft("", 12);
        qPrice = labelLeft("", 12);
        qStatus = labelLeft("", 12);

        info.add(labelLeft("Vehicle:", 12));
        info.add(qVehicle);
        info.add(labelLeft("Policy Type:", 12));
        info.add(qPolicy);
        info.add(labelLeft("Driver Age:", 12));
        info.add(qAge);
        info.add(labelLeft("No Claim Bonus:", 12));
        info.add(qNcb);
        info.add(labelLeft("Quote Price:", 12));
        info.add(qPrice);
        info.add(labelLeft("Status:", 12));
        info.add(qStatus);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);

        JButton back = button("Back");
        onClick(back, () -> showCard(CARDS[4]));

        JButton save = button("Save");
        JButton confirm = button("Confirm");

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(Color.WHITE);
        right.add(save);
        right.add(confirm);

        bottom.add(back, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);

        root.add(info, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private void updateQuoteLabels() {
        if (currentQuote == null) return;
        qVehicle.setText(currentQuote.getVehicleText());
        qPolicy.setText(currentQuote.getPolicyType());
        qAge.setText(String.valueOf(currentQuote.getDriverAge()));
        qNcb.setText(String.valueOf(currentQuote.getNcbYears()));
        qPrice.setText("£" + currentQuote.getPremium());
        qStatus.setText(String.valueOf(currentQuote.getStatus()));
    }
}