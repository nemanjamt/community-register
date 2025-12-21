package community.register.view.panels;

import community.register.model.Family;
import community.register.utils.FamilyFormListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FamilyForm extends JPanel {

    private JTextField placeField;
    private JTextField streetField;
    private JTextField patronSaintField;
    private JTextField secondPatronSaintField;
    private JTextField phoneNumberField;
    private JTextField orderNumberField;

    private JTextArea noteField;
    private JCheckBox blessedHomeCheckbox;

    private JButton saveButton;
    private JButton deleteButton;

    private GridBagConstraints gbc;

    private FamilyFormListener listener;
    private Family family;

    public FamilyForm(FamilyFormListener listener) {
        this.listener = listener;
        initComponents();
    }

    /* =========================
       Initialization
       ========================= */

    private void initComponents() {
        initLayout();
        initFields();
        initButtons();
        initNavigation();
    }

    private void initLayout() {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    }

    private void highlightCheckboxFocus(JCheckBox checkBox) {
        checkBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                checkBox.getModel().setRollover(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                checkBox.getModel().setRollover(false);
            }
        });
    }



    private void initFields() {
        addRow(0, "Мјесто:", placeField = new JTextField(15));
        addRow(1, "Улица:", streetField = new JTextField(15));
        addRow(2, "Крсна слава:", patronSaintField = new JTextField(15));
        addRow(3, "Преслава:", secondPatronSaintField = new JTextField(15));
        addRow(4, "Дом освештан:", blessedHomeCheckbox = new JCheckBox());
        addRow(5, "Број телефона:", phoneNumberField = new JTextField(15));
        addRow(6, "Редни број:", orderNumberField = new JTextField());
        highlightCheckboxFocus(blessedHomeCheckbox);
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Напомена:"), gbc);

        noteField = new JTextArea();
        noteField.setLineWrap(true);
        noteField.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(noteField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(180, 80));

        gbc.gridx = 1;
        add(scrollPane, gbc);
    }

    private void initButtons() {
        gbc.gridy = 8;
        saveButton = new JButton("Сачувај измјене");
        add(saveButton, gbc);

        gbc.gridx = 0;
        deleteButton = new JButton("Избриши");
        add(deleteButton, gbc);

        saveButton.addActionListener(e -> onSave());
        deleteButton.addActionListener(e -> onDelete());
    }

    /* =========================
       Navigation
       ========================= */

    private void initNavigation() {
        configureNavigation(placeField, false,true);
        configureNavigation(streetField, true,true);
        configureNavigation(patronSaintField, true,true);
        configureNavigation(secondPatronSaintField, true,true);
        configureNavigation(blessedHomeCheckbox, true,true);
        configureNavigation(phoneNumberField, true,true);
        configureNavigation(orderNumberField, true,true);
    }

    /**
     * Configures keyboard navigation for a component:
     *  - ENTER: moves focus to the next component
     *  - UP: moves focus to the previous component
     *  - DOWN: (if allowed) moves focus to the next component
     */
    private void configureNavigation(JComponent component, boolean allowUp, boolean allowDown) {
        InputMap im = component.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = component.getActionMap();

        im.put(KeyStroke.getKeyStroke("ENTER"), "enter");
        im.put(KeyStroke.getKeyStroke("UP"), "up");

        if(allowUp){
            am.put("up", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    component.transferFocusBackward();
                }
            });
        }

        if (allowDown) {
            im.put(KeyStroke.getKeyStroke("DOWN"), "down");
            am.put("down", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    component.transferFocus();
                }
            });
        }

        am.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                component.transferFocus();
            }
        });
    }

    /* =========================
       Actions
       ========================= */

    private void onSave() {
        Integer orderNumber = parseOrderNumber();
        if (orderNumber == null) return;

        Long id = family != null ? family.getId() : null;
        Long hostId = family != null ? family.getHostId() : null;

        Family updatedFamily = new Family(
                id,
                placeField.getText(),
                streetField.getText(),
                patronSaintField.getText(),
                secondPatronSaintField.getText(),
                blessedHomeCheckbox.isSelected(),
                phoneNumberField.getText(),
                orderNumber,
                noteField.getText(),
                hostId
        );

        listener.onSaveFamily(updatedFamily);
    }

    private void onDelete() {
        if (family != null) {
            listener.onDelete(family.getId());
        }
    }

    private Integer parseOrderNumber() {
        String text = orderNumberField.getText();

        if (text.isEmpty()) {
            showError("Редни број је обавезно поље!");
            return null;
        }

        try {
            int value = Integer.parseInt(text);
            if (value <= 0) {
                showError("Редни број мора бити већи или једнак 1!");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            showError("Редни број мора бити број!");
            return null;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Грешка", JOptionPane.ERROR_MESSAGE);
    }

    /* =========================
       Helpers
       ========================= */

    private void addRow(int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(field, gbc);
    }

    public void setFamily(Family family) {
        this.family = family;

        placeField.setText(family.getPlace());
        streetField.setText(family.getStreet());
        patronSaintField.setText(family.getPatronSaint());
        secondPatronSaintField.setText(family.getSecondPatronSaint());
        blessedHomeCheckbox.setSelected(family.isBlessedHome());
        phoneNumberField.setText(family.getPhoneNumber());
        orderNumberField.setText(family.getOrderNumber().toString());
        noteField.setText(family.getNote());
    }

    public void resetForm() {
        placeField.setText("");
        streetField.setText("");
        patronSaintField.setText("");
        secondPatronSaintField.setText("");
        phoneNumberField.setText("");
        orderNumberField.setText("");
        noteField.setText("");
        blessedHomeCheckbox.setSelected(false);
        family = null;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}

