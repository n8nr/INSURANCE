import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Main {

    private static final String[] CARDS = {"LOGIN", "VEHICLE", "FACTORS", "POLICY", "PERSONAL", "QUOTE", "ADMIN", "MY_QUOTES"};

    private static final int FRAME_W = 980;
    private static final int FRAME_H = 620;

    private static final int FIELD_W = 280;
    private static final int FIELD_H = 34;

    private static final int COMBO_W = 240;
    private static final int COMBO_H = 34;

    private static final int BTN_W = 130;
    private static final int BTN_H = 40;

    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT = new Color(35, 35, 35);
    private static final Color MUTED = new Color(110, 110, 110);
    private static final Color PRIMARY = new Color(32, 43, 67);
    private static final Color PRIMARY_HOVER = new Color(46, 61, 92);
    private static final Color BORDER = new Color(225, 229, 236);
    private static final Color INPUT_BG = new Color(250, 251, 253);
    private static final Color SELECTED_CARD = new Color(233, 240, 255);

    private final JFrame frame = new JFrame("Insurance Quote App");
    private final JPanel cards = new JPanel(new CardLayout());

    private final Repository repo = new Repository();
    private final QuoteCalculator calculator = new QuoteCalculator();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final QuoteDAO quoteDAO = new QuoteDAO();

    private JComboBox<String> makeCb, modelCb, yearCb, engineCb, colourCb;
    private JComboBox<String> ageCb, ncbCb;
    private JToggleButton policyThirdParty, policyTpft, policyComprehensive;

    private JTextField loginEmailTf;
    private JPasswordField loginPasswordPf;

    private JTextField nameTf, surnameTf, addressTf, phoneTf, emailTf;
    private JPasswordField passwordPf;

    private JLabel qVehicle, qPolicy, qAge, qNcb, qPrice, qStatus;

    private Customer currentCustomer;
    private Quote currentQuote;

    private boolean quoteOpenedFromMyQuotes = false;

    public static void main(String[] ignored) {
        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private void start() {
        repo.loadCars();
        DB.init();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(FRAME_W, FRAME_H);
        frame.setLocationRelativeTo(null);

        cards.setBackground(BG);

        cards.add(loginScreen(), CARDS[0]);
        cards.add(vehicleScreen(), CARDS[1]);
        cards.add(factorsScreen(), CARDS[2]);
        cards.add(policyScreen(), CARDS[3]);
        cards.add(personalScreen(), CARDS[4]);
        cards.add(quoteScreen(), CARDS[5]);
        cards.add(adminScreen(), CARDS[6]);
        cards.add(myQuotesScreen(), CARDS[7]);
        frame.setContentPane(cards);
        showCard(CARDS[0]);
        frame.setVisible(true);
    }

    private void showCard(String name) {
        ((CardLayout) cards.getLayout()).show(cards, name);
    }

    private JPanel basePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        return p;
    }

    private JPanel cardPanel() {
        JPanel p = new JPanel(new BorderLayout(16, 16));
        p.setBackground(CARD_BG);
        p.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(22, 22, 22, 22)
        ));
        p.setPreferredSize(new Dimension(860, 520));
        return p;
    }

    private JLabel labelLeft(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.LEFT);
        l.setForeground(TEXT);
        l.setFont(new Font("SansSerif", Font.PLAIN, size));
        return l;
    }

    private JLabel labelLeftBold(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.LEFT);
        l.setForeground(TEXT);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private JLabel labelCenter(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(TEXT);
        l.setFont(new Font("SansSerif", Font.PLAIN, size));
        return l;
    }

    private JLabel mutedLabel(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(MUTED);
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
        l.setForeground(PRIMARY);
        l.setFont(new Font("SansSerif", Font.BOLD, 18));
        return l;
    }

    private JButton button(String text, int w, int h) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(PRIMARY);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setPreferredSize(new Dimension(w, h));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(PRIMARY_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(PRIMARY);
            }
        });

        return b;
    }

    private JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setForeground(TEXT);
        b.setBackground(Color.WHITE);
        b.setOpaque(true);
        b.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 18, 10, 18)
        ));
        b.setPreferredSize(new Dimension(BTN_W, BTN_H));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(248, 249, 252));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(Color.WHITE);
            }
        });

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
        f.setBackground(INPUT_BG);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        Dimension d = new Dimension(FIELD_W, FIELD_H);
        f.setPreferredSize(d);
        f.setMaximumSize(d);
        return f;
    }

    private JPasswordField passField() {
        JPasswordField f = new JPasswordField();
        f.setBackground(INPUT_BG);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        Dimension d = new Dimension(FIELD_W, FIELD_H);
        f.setPreferredSize(d);
        f.setMaximumSize(d);
        return f;
    }

    private JComboBox<String> comboBox(int w) {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBackground(INPUT_BG);
        cb.setForeground(TEXT);
        cb.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(2, 6, 2, 6)
        ));
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        Dimension d = new Dimension(w, COMBO_H);
        cb.setPreferredSize(d);
        cb.setMaximumSize(d);
        return cb;
    }

    private JPanel topBar(String title, int step) {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(CARD_BG);
        top.add(labelLeftBold(title, 22), BorderLayout.WEST);
        top.add(progressLabel(step), BorderLayout.EAST);
        return top;
    }

    // Screen 1
    private JPanel loginScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();

        JLabel title = labelCenter("Insurance Quote App", 28);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = mutedLabel("Create a new quote or resume an existing one", 14);

        JPanel top = new JPanel();
        top.setBackground(CARD_BG);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(Box.createVerticalStrut(10));
        top.add(title);
        top.add(Box.createVerticalStrut(8));
        top.add(subtitle);

        JPanel center = new JPanel();
        center.setBackground(CARD_BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JButton newQuote = button("START NEW QUOTE", 320, 46);
        newQuote.setAlignmentX(Component.CENTER_ALIGNMENT);
        onClick(newQuote, () -> showCard(CARDS[1]));

        JLabel resume = mutedLabel("Resume a saved quote", 13);
        resume.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);

        JLabel loginHint = mutedLabel("Login to view saved quotes", 13);
        loginHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginEmailTf = textField();
        loginPasswordPf = passField();

        loginEmailTf.setMaximumSize(new Dimension(320, FIELD_H));
        loginPasswordPf.setMaximumSize(new Dimension(320, FIELD_H));

        JButton login = button("Login", 130, 38);
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        onClick(login, this::handleLogin);

        center.add(Box.createVerticalStrut(22));
        center.add(newQuote);
        center.add(Box.createVerticalStrut(12));
        center.add(resume);
        center.add(Box.createVerticalStrut(28));
        center.add(sep);
        center.add(Box.createVerticalStrut(28));
        center.add(loginHint);
        center.add(Box.createVerticalStrut(12));
        center.add(loginEmailTf);
        center.add(Box.createVerticalStrut(12));
        center.add(loginPasswordPf);
        center.add(Box.createVerticalStrut(16));
        center.add(login);
        center.add(Box.createVerticalGlue());

        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);

        root.add(card);
        return root;
    }

    // Screen 2
    private JPanel vehicleScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();

        card.add(topBar("Vehicle Details", 1), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(CARD_BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel helper = new JLabel("Select a make and model. The engine size and year will update automatically.");
        helper.setForeground(MUTED);
        helper.setFont(new Font("SansSerif", Font.PLAIN, 13));
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel form = new JPanel();
        form.setBackground(CARD_BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(780, 260));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel row1Labels = new JPanel(new GridLayout(1, 3, 14, 0));
        row1Labels.setBackground(CARD_BG);
        row1Labels.add(labelLeft("Make", 12));
        row1Labels.add(labelLeft("Model", 12));
        row1Labels.add(labelLeft("Year", 12));

        JPanel row1Fields = new JPanel(new GridLayout(1, 3, 14, 0));
        row1Fields.setBackground(CARD_BG);

        makeCb = comboBox(COMBO_W);
        modelCb = comboBox(COMBO_W);
        yearCb = comboBox(COMBO_W);

        makeCb.addItem("");
        for (String make : repo.getMakes()) {
            makeCb.addItem(make);
        }

        modelCb.addItem("");

        yearCb.addItem("");
        for (int y = 2010; y <= 2026; y++) {
            yearCb.addItem(String.valueOf(y));
        }

        makeCb.addActionListener(evt -> {
            String selectedMake = safe(makeCb.getSelectedItem());

            modelCb.removeAllItems();
            modelCb.addItem("");

            engineCb.removeAllItems();
            engineCb.addItem("");

            yearCb.setSelectedIndex(0);

            if (!selectedMake.isEmpty()) {
                for (String m : repo.getModelsByMake(selectedMake)) {
                    modelCb.addItem(m);
                }
            }
        });

        row1Fields.add(makeCb);
        row1Fields.add(modelCb);
        row1Fields.add(yearCb);

        JPanel row2Labels = new JPanel(new GridLayout(1, 2, 14, 0));
        row2Labels.setBackground(CARD_BG);
        row2Labels.add(labelLeft("Engine Size", 12));
        row2Labels.add(labelLeft("Colour", 12));

        JPanel row2Fields = new JPanel(new GridLayout(1, 2, 14, 0));
        row2Fields.setBackground(CARD_BG);

        engineCb = comboBox(330);
        colourCb = comboBox(330);

        engineCb.addItem("");

        colourCb.addItem("");
        colourCb.addItem("Black");
        colourCb.addItem("White");
        colourCb.addItem("Silver");
        colourCb.addItem("Blue");
        colourCb.addItem("Red");
        colourCb.addItem("Grey");

        modelCb.addActionListener(evt -> {
            String make = safe(makeCb.getSelectedItem());
            String model = safe(modelCb.getSelectedItem());

            engineCb.removeAllItems();
            engineCb.addItem("");

            if (!make.isEmpty() && !model.isEmpty()) {
                CarData car = repo.getCar(make, model);

                if (car != null) {
                    String engineValue = String.valueOf(car.getEngineSize());
                    engineCb.addItem(engineValue);
                    engineCb.setSelectedItem(engineValue);
                    yearCb.setSelectedItem(String.valueOf(car.getYear()));
                }
            }
        });

        row2Fields.add(engineCb);
        row2Fields.add(colourCb);

        form.add(row1Labels);
        form.add(Box.createVerticalStrut(8));
        form.add(row1Fields);
        form.add(Box.createVerticalStrut(24));
        form.add(row2Labels);
        form.add(Box.createVerticalStrut(8));
        form.add(row2Fields);

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(CARD_BG);

        JButton back = secondaryButton("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[0]));
        onClick(next, () -> showCard(CARDS[2]));

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        wrapper.add(helper);
        wrapper.add(Box.createVerticalStrut(22));
        wrapper.add(form);
        wrapper.add(Box.createVerticalGlue());

        card.add(wrapper, BorderLayout.CENTER);
        card.add(nav, BorderLayout.SOUTH);

        root.add(card);
        return root;
    }

    // Screen 3
    private JPanel factorsScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();

        card.add(topBar("Insurance Factors", 2), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(CARD_BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel helper = new JLabel("These factors influence the premium shown in your final quote.");
        helper.setForeground(MUTED);
        helper.setFont(new Font("SansSerif", Font.PLAIN, 13));
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel form = new JPanel();
        form.setBackground(CARD_BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(700, 140));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel labels = new JPanel(new GridLayout(1, 2, 14, 0));
        labels.setBackground(CARD_BG);
        labels.add(labelLeft("Driver Age", 12));
        labels.add(labelLeft("No Claim Bonus (years)", 12));

        JPanel fields = new JPanel(new GridLayout(1, 2, 14, 0));
        fields.setBackground(CARD_BG);

        ageCb = comboBox(240);
        ncbCb = comboBox(240);

        ageCb.addItem("");
        for (int i = 17; i <= 80; i++) ageCb.addItem(String.valueOf(i));

        ncbCb.addItem("");
        for (int i = 0; i <= 15; i++) ncbCb.addItem(String.valueOf(i));

        fields.add(ageCb);
        fields.add(ncbCb);

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(CARD_BG);

        JButton back = secondaryButton("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[1]));
        onClick(next, () -> showCard(CARDS[3]));

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        form.add(labels);
        form.add(Box.createVerticalStrut(8));
        form.add(fields);

        wrapper.add(helper);
        wrapper.add(Box.createVerticalStrut(40));
        wrapper.add(form);
        wrapper.add(Box.createVerticalGlue());

        card.add(wrapper, BorderLayout.CENTER);
        card.add(nav, BorderLayout.SOUTH);

        root.add(card);
        return root;
    }

    // Screen 4
    private JPanel policyScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();

        card.add(topBar("Select Policy Type", 3), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(CARD_BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel helper = new JLabel("Choose the level of cover you want for your vehicle.");
        helper.setForeground(MUTED);
        helper.setFont(new Font("SansSerif", Font.PLAIN, 13));
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 18, 0));
        cardsRow.setBackground(CARD_BG);
        cardsRow.setMaximumSize(new Dimension(780, 230));
        cardsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();

        policyThirdParty = policyCard("Third Party", new String[]{
                "Injury to others",
                "Damage to others",
                "No cover for own car"
        });
        policyTpft = policyCard("Third Party, Fire & Theft", new String[]{
                "Third Party cover",
                "Fire damage",
                "Theft cover"
        });
        policyComprehensive = policyCard("Comprehensive", new String[]{
                "Own car damage",
                "Third Party cover",
                "Fire & theft included"
        });

        group.add(policyThirdParty);
        group.add(policyTpft);
        group.add(policyComprehensive);

        cardsRow.add(policyThirdParty);
        cardsRow.add(policyTpft);
        cardsRow.add(policyComprehensive);

        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(CARD_BG);

        JButton back = secondaryButton("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[2]));
        onClick(next, () -> showCard(CARDS[4]));

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        wrapper.add(helper);
        wrapper.add(Box.createVerticalStrut(42));
        wrapper.add(cardsRow);
        wrapper.add(Box.createVerticalGlue());

        card.add(wrapper, BorderLayout.CENTER);
        card.add(nav, BorderLayout.SOUTH);

        root.add(card);
        return root;
    }

    private JToggleButton policyCard(String title, String[] lines) {
        JToggleButton b = new JToggleButton();
        b.setLayout(new BorderLayout());
        b.setBackground(CARD_BG);
        b.setForeground(TEXT);
        b.setFocusPainted(false);
        b.setBorderPainted(true);
        b.setContentAreaFilled(false);
        b.setOpaque(true);
        b.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(6, 6, 6, 6)
        ));
        b.setPreferredSize(new Dimension(240, 210));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setForeground(TEXT);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setBorder(new EmptyBorder(14, 10, 10, 10));
        header.setOpaque(true);
        header.setBackground(CARD_BG);

        JPanel list = new JPanel();
        list.setBackground(CARD_BG);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(4, 18, 14, 18));

        for (String line : lines) {
            JLabel item = new JLabel("• " + line);
            item.setForeground(MUTED);
            item.setFont(new Font("SansSerif", Font.PLAIN, 12));
            list.add(item);
            list.add(Box.createVerticalStrut(8));
        }

        b.add(header, BorderLayout.NORTH);
        b.add(list, BorderLayout.CENTER);

        b.addItemListener(evt -> {
            if (b.isSelected()) {
                b.setBackground(SELECTED_CARD);
                header.setBackground(SELECTED_CARD);
                list.setBackground(SELECTED_CARD);
                b.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY, 2, true),
                        new EmptyBorder(5, 5, 5, 5)
                ));
            } else {
                b.setBackground(CARD_BG);
                header.setBackground(CARD_BG);
                list.setBackground(CARD_BG);
                b.setBorder(new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(6, 6, 6, 6)
                ));
            }
        });

        return b;
    }

    // Screen 5
    private JPanel personalScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();

        card.add(topBar("Personal Details", 4), BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(CARD_BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel helper = new JLabel("Enter the customer details required to generate and save the quote.");
        helper.setForeground(MUTED);
        helper.setFont(new Font("SansSerif", Font.PLAIN, 13));
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD_BG);
        form.setMaximumSize(new Dimension(780, 290));
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
        nav.setBackground(CARD_BG);

        JButton back = secondaryButton("Back");
        JButton next = button("Next");

        onClick(back, () -> showCard(CARDS[3]));
        onClick(next, this::createCustomerAndQuote);

        nav.add(back, BorderLayout.WEST);
        nav.add(next, BorderLayout.EAST);

        wrapper.add(helper);
        wrapper.add(Box.createVerticalStrut(20));
        wrapper.add(form);
        wrapper.add(Box.createVerticalGlue());

        card.add(wrapper, BorderLayout.CENTER);
        card.add(nav, BorderLayout.SOUTH);

        root.add(card);
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

        try {
            currentCustomer = customerDAO.register(name, surname, address, phone, email, password);
        } catch (Exception ex) {
            if ("Email already registered".equalsIgnoreCase(ex.getMessage())) {
                try {
                    currentCustomer = customerDAO.login(email, password);
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(frame, ex2.getMessage(), "Login failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Registration failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

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

        try {
            int quoteId = quoteDAO.createQuote(currentCustomer.getId(), vehicleText, age, ncb, policy, premium);
            currentQuote = new Quote(quoteId, currentCustomer, vehicleText, age, ncb, policy, premium);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Quote save failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        quoteOpenedFromMyQuotes = true;
        updateQuoteLabels();
        showCard(CARDS[5]);
    }

    private void handleLogin() {
        String email = safe(loginEmailTf.getText());
        String pass = new String(loginPasswordPf.getPassword());

        if (email.equalsIgnoreCase("admin@email.com") && pass.equals("admin123")) {
            showCard(CARDS[6]);
            return;
        }

        try {
            currentCustomer = customerDAO.login(email, pass);
            showCard(CARDS[7]);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Login failed", JOptionPane.ERROR_MESSAGE);
        }
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
        if (m.contains("peugeot")) return 460;
        if (m.contains("renault")) return 445;
        if (m.contains("kia")) return 435;
        if (m.contains("hyundai")) return 440;
        if (m.contains("seat")) return 445;
        if (m.contains("skoda")) return 455;
        return 480;
    }

    private double engineFactor(String engine) {
        String e = engine == null ? "" : engine.trim();
        if (e.equals("1.0")) return 1.00;
        if (e.equals("1.2")) return 1.05;
        if (e.equals("1.3")) return 1.08;
        if (e.equals("1.4")) return 1.10;
        if (e.equals("1.5")) return 1.12;
        if (e.equals("1.6")) return 1.15;
        if (e.equals("1.8")) return 1.20;
        if (e.equals("2.0")) return 1.25;
        if (e.equals("2.0+")) return 1.30;
        return 1.00;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // Screen 6
    private JPanel quoteScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();

        card.add(topBar("Your Quote", 5), BorderLayout.NORTH);

        JPanel centerWrap = new JPanel();
        centerWrap.setBackground(CARD_BG);
        centerWrap.setLayout(new BoxLayout(centerWrap, BoxLayout.Y_AXIS));

        JLabel bigPriceTitle = labelCenter("Estimated Premium", 14);
        bigPriceTitle.setForeground(MUTED);
        bigPriceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        qPrice = labelCenter("£0.00", 34);
        qPrice.setFont(new Font("SansSerif", Font.BOLD, 34));
        qPrice.setForeground(PRIMARY);
        qPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel infoCard = new JPanel(new GridLayout(5, 2, 12, 12));
        infoCard.setBackground(new Color(249, 250, 252));
        infoCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));

        qVehicle = labelLeft("", 12);
        qPolicy = labelLeft("", 12);
        qAge = labelLeft("", 12);
        qNcb = labelLeft("", 12);
        qStatus = labelLeft("", 12);

        infoCard.add(labelLeftBold("Vehicle", 12));
        infoCard.add(qVehicle);
        infoCard.add(labelLeftBold("Policy Type", 12));
        infoCard.add(qPolicy);
        infoCard.add(labelLeftBold("Driver Age", 12));
        infoCard.add(qAge);
        infoCard.add(labelLeftBold("No Claim Bonus", 12));
        infoCard.add(qNcb);
        infoCard.add(labelLeftBold("Status", 12));
        infoCard.add(qStatus);

        centerWrap.add(Box.createVerticalStrut(10));
        centerWrap.add(bigPriceTitle);
        centerWrap.add(Box.createVerticalStrut(6));
        centerWrap.add(qPrice);
        centerWrap.add(Box.createVerticalStrut(26));
        centerWrap.add(infoCard);
        centerWrap.add(Box.createVerticalGlue());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(CARD_BG);

        JButton back = secondaryButton("Back");
        onClick(back, () -> {
            if (quoteOpenedFromMyQuotes) {
                quoteOpenedFromMyQuotes = false;
                showCard(CARDS[7]); // MY_QUOTES
            } else {
                showCard(CARDS[4]); // PERSONAL
            }
        });
        JButton save = secondaryButton("Save");
        save.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "Quote saved.", "Saved", JOptionPane.INFORMATION_MESSAGE)
        );

        JButton confirm = button("Confirm");
        confirm.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Quote submitted successfully.\nAwaiting admin review.",
                    "Submitted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearAllFields();
            showCard(CARDS[0]);
        });

        JButton logout = secondaryButton("Logout");
        logout.addActionListener(e -> logoutCustomer());


        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(CARD_BG);
        right.add(logout);
        right.add(save);
        right.add(confirm);

        bottom.add(back, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);

        card.add(centerWrap, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        root.add(card);
        return root;
    }

    // Screen 7
    private JPanel adminScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();
        JButton viewDetails = secondaryButton("View Details");

        card.add(topBar("Admin Panel", 5), BorderLayout.NORTH);

        String[] cols = {"Quote ID", "Customer Email", "Vehicle", "Policy", "Premium", "Status", "Created"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int quoteId = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
                    showQuoteDetailsDialog(quoteId);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER, 1, true));

        JButton refresh = secondaryButton("Refresh");
        JButton approve = button("Approve");
        JButton reject = secondaryButton("Reject");
        JButton logout = secondaryButton("Logout");

        Runnable load = () -> {
            try {
                model.setRowCount(0);
                List<QuoteDAO.AdminQuoteRow> rows = quoteDAO.getAllQuotesForAdmin();
                for (QuoteDAO.AdminQuoteRow r : rows) {
                    model.addRow(new Object[]{
                            r.id,
                            r.customerEmail,
                            r.vehicleText,
                            r.policyType,
                            "£" + round2(r.premium),
                            r.status,
                            r.createdAt
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Admin error", JOptionPane.ERROR_MESSAGE);
            }
        };

        refresh.addActionListener(e -> load.run());

        approve.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Please select a quote first.");
                return;
            }

            int quoteId = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
            try {
                quoteDAO.approve(quoteId);
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Admin error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reject.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Please select a quote first.");
                return;
            }

            int quoteId = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
            try {
                quoteDAO.reject(quoteId);
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Admin error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewDetails.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a quote first.");
                return;
            }

            int quoteId = Integer.parseInt(String.valueOf(table.getValueAt(selectedRow, 0)));
            showQuoteDetailsDialog(quoteId);
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int quoteId = Integer.parseInt(String.valueOf(table.getValueAt(selectedRow, 0)));
                        showQuoteDetailsDialog(quoteId);
                    }
                }
            }
        });

        logout.addActionListener(e -> showCard(CARDS[0]));

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(CARD_BG);

        JLabel hint = new JLabel("Select a quote and use View Details, Approve or Reject to update its status.");
        hint.setForeground(MUTED);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 13));

        center.add(hint, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(CARD_BG);
        bottom.add(refresh);
        bottom.add(viewDetails);
        bottom.add(approve);
        bottom.add(reject);
        bottom.add(logout);

        card.add(center, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        root.add(card);

        load.run();
        return root;
    }

    private void logoutCustomer() {
        int choice = JOptionPane.showConfirmDialog(frame, "Logout now?", "Confirm logout", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        currentCustomer = null;
        currentQuote = null;

        if (loginEmailTf != null) loginEmailTf.setText("");
        if (loginPasswordPf != null) loginPasswordPf.setText("");

        showCard(CARDS[0]);
    }

    // Screen 8
    private JPanel myQuotesScreen() {
        JPanel root = basePanel();
        JPanel card = cardPanel();

        card.add(topBar("My Quotes", 5), BorderLayout.NORTH);

        String[] cols = {"Quote ID", "Vehicle", "Policy", "Premium", "Status", "Created"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER, 1, true));

        JButton refresh = secondaryButton("Refresh");
        JButton view = button("View");
        JButton newQuoteBtn = secondaryButton("New Quote");
        JButton logout = secondaryButton("Logout");

        Runnable load = () -> {
            try {
                model.setRowCount(0);
                if (currentCustomer == null) return;

                java.util.List<QuoteDAO.CustomerQuoteRow> rows = quoteDAO.getQuotesForCustomer(currentCustomer.getId());
                for (QuoteDAO.CustomerQuoteRow r : rows) {
                    model.addRow(new Object[]{
                            r.id,
                            r.vehicleText,
                            r.policyType,
                            "£" + round2(r.premium),
                            r.status,
                            r.createdAt
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        refresh.addActionListener(e -> load.run());

        newQuoteBtn.addActionListener(e -> {
            currentQuote = null;
            showCard(CARDS[1]);
        });

        logout.addActionListener(e -> logoutCustomer());

        view.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) return;

            int quoteId = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
            String vehicle = String.valueOf(model.getValueAt(row, 1));
            String policy = String.valueOf(model.getValueAt(row, 2));
            String premiumText = String.valueOf(model.getValueAt(row, 3)).replace("£", "");
            double premium = Double.parseDouble(premiumText);
            String status = String.valueOf(model.getValueAt(row, 4));

            currentQuote = new Quote(quoteId, currentCustomer, vehicle, 0, 0, policy, premium);
            currentQuote.setStatus(Quote.Status.valueOf(status));

            quoteOpenedFromMyQuotes = true;
            updateQuoteLabels();
            showCard(CARDS[5]);
        });

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(CARD_BG);

        JLabel hint = new JLabel("Select a quote and click View, or start a new quote.");
        hint.setForeground(MUTED);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 13));

        center.add(hint, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(CARD_BG);
        bottom.add(refresh);
        bottom.add(newQuoteBtn);
        bottom.add(view);
        bottom.add(logout);

        card.add(center, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        root.add(card);

        load.run();
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

    // 👇 PASTE HERE
    private void clearAllFields() {
        if (makeCb != null) makeCb.setSelectedIndex(0);

        if (modelCb != null) {
            modelCb.removeAllItems();
            modelCb.addItem("");
        }

        if (yearCb != null) yearCb.setSelectedIndex(0);
        if (engineCb != null) engineCb.setSelectedIndex(0);
        if (colourCb != null) colourCb.setSelectedIndex(0);

        if (ageCb != null) ageCb.setSelectedIndex(0);
        if (ncbCb != null) ncbCb.setSelectedIndex(0);

        if (nameTf != null) nameTf.setText("");
        if (surnameTf != null) surnameTf.setText("");
        if (addressTf != null) addressTf.setText("");
        if (phoneTf != null) phoneTf.setText("");
        if (emailTf != null) emailTf.setText("");
        if (passwordPf != null) passwordPf.setText("");
    }

    private void showQuoteDetailsDialog(int quoteId) {
        try {
            QuoteDAO.QuoteDetailsRow q = quoteDAO.findQuoteDetailsById(quoteId);

            if (q == null) {
                JOptionPane.showMessageDialog(frame, "Quote not found.");
                return;
            }

            JDialog dialog = new JDialog(frame, "Quote Details", true);
            dialog.setSize(520, 420);
            dialog.setLocationRelativeTo(frame);

            JPanel panel = new JPanel(new BorderLayout(12, 12));
            panel.setBorder(new EmptyBorder(16, 16, 16, 16));
            panel.setBackground(Color.WHITE);

            JPanel details = new JPanel(new GridLayout(0, 2, 10, 10));
            details.setBackground(Color.WHITE);

            details.add(new JLabel("Quote ID:"));
            details.add(new JLabel(String.valueOf(q.id)));

            details.add(new JLabel("Customer:"));
            details.add(new JLabel(q.customerName + " " + q.customerSurname));

            details.add(new JLabel("Email:"));
            details.add(new JLabel(q.customerEmail));

            details.add(new JLabel("Phone:"));
            details.add(new JLabel(q.customerPhone));

            details.add(new JLabel("Address:"));
            details.add(new JLabel(q.customerAddress));

            details.add(new JLabel("Vehicle:"));
            details.add(new JLabel(q.vehicleText));

            details.add(new JLabel("Driver Age:"));
            details.add(new JLabel(String.valueOf(q.driverAge)));

            details.add(new JLabel("No Claim Bonus:"));
            details.add(new JLabel(String.valueOf(q.ncbYears)));

            details.add(new JLabel("Policy Type:"));
            details.add(new JLabel(q.policyType));

            details.add(new JLabel("Premium:"));
            details.add(new JLabel("£" + round2(q.premium)));

            details.add(new JLabel("Status:"));
            details.add(new JLabel(q.status));

            details.add(new JLabel("Created:"));
            details.add(new JLabel(q.createdAt));

            JButton close = new JButton("Close");
            close.addActionListener(e -> dialog.dispose());

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottom.setBackground(Color.WHITE);
            bottom.add(close);

            panel.add(details, BorderLayout.CENTER);
            panel.add(bottom, BorderLayout.SOUTH);

            dialog.setContentPane(panel);
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}