package community.register.view.panels;

import community.register.model.FamilyMember;
import community.register.utils.FamilyMemberFormListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FamilyMemberForm extends JPanel {

    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField birthDateField;
    private JTextField relationshipField;

    private JCheckBox baptizedCheckbox;
    private JCheckBox marriedCheckbox;
    private JCheckBox hostCheckbox;

    private JButton saveButton;

    private GridBagConstraints gbc;

    private final FamilyMemberFormListener listener;

    public FamilyMemberForm(FamilyMemberFormListener listener) {
        this.listener = listener;
        initComponents();
    }

    /* =========================
       Initialization
       ========================= */

    private void initComponents() {
        initLayout();
        initFields();
        initSaveButton();
        initNavigation();
    }

    private void initLayout() {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    }

    private void initFields() {
        addRow(0, "Име:", nameField = new JTextField(15));
        addRow(1, "Презиме:", lastNameField = new JTextField(15));
        addRow(2, "Датум рођења:", birthDateField = new JTextField(15));
        addRow(3, "Крштен:", baptizedCheckbox = new JCheckBox());
        addRow(4, "Вјенчан:", marriedCheckbox = new JCheckBox());
        addRow(5, "Сродство са домаћином:", relationshipField = new JTextField(15));
        addRow(6, "Домаћин:", hostCheckbox = new JCheckBox());
        highlightCheckboxFocus(baptizedCheckbox);
        highlightCheckboxFocus(marriedCheckbox);
        highlightCheckboxFocus(hostCheckbox);
    }

    private void initSaveButton() {
        gbc.gridx = 0;
        gbc.gridy = 7;
        saveButton = new JButton("Сачувај");
        add(saveButton, gbc);

        saveButton.addActionListener(e -> onSave());
        simulateArrowFocus(saveButton);
    }

    /* =========================
       Navigation / Key bindings
       ========================= */

    private void initNavigation() {
        configureNavigation(nameField, false,true);
        configureNavigation(lastNameField, true,true);
        configureNavigation(birthDateField, true,true);
        configureNavigation(relationshipField, true,true);
        configureNavigation(baptizedCheckbox, true,true);
        configureNavigation(marriedCheckbox, true,true);
        configureNavigation(hostCheckbox, true,true);
        configureNavigation(saveButton, true,false); // button: ENTER = save, arrows = focus
    }

    /**
     * Configures keyboard navigation for a component:
     *  - ENTER: moves focus to the next component or clicks the button
     *  - UP: moves focus to the previous component
     *  - DOWN: (if allowed) moves focus to the next component
     */
    private void configureNavigation(JComponent component, boolean allowUp, boolean allowDown) {
        InputMap im = component.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = component.getActionMap();

        im.put(KeyStroke.getKeyStroke("ENTER"), "enter");

        if(allowUp){
            im.put(KeyStroke.getKeyStroke("UP"), "up");
            am.put("up", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    component.transferFocusBackward();
                }
            });
        }

        if (allowDown) {
            im.put(KeyStroke.getKeyStroke("DOWN"), "down");
            am.put("down", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    component.transferFocus();
                }
            });

        }

        am.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (component instanceof JButton button) {
                    button.doClick();
                    nameField.requestFocus();
                } else {
                    component.transferFocus();
                }
            }
        });
    }

    /* =========================
       Actions
       ========================= */

    private void onSave() {
        FamilyMember member = readForm();
        listener.onSaveFamilyMember(member);
        resetForm();
    }

    private FamilyMember readForm() {
        Long id = System.currentTimeMillis();
        return new FamilyMember(
                id,
                relationshipField.getText(),
                marriedCheckbox.isSelected(),
                baptizedCheckbox.isSelected(),
                birthDateField.getText(),
                nameField.getText(),
                lastNameField.getText(),
                hostCheckbox.isSelected()
        );
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

    public void setFamilyMember(FamilyMember member) {
        nameField.setText(member.getName());
        lastNameField.setText(member.getLastName());
        birthDateField.setText(member.getBirthday());
        baptizedCheckbox.setSelected(member.isBaptized());
        marriedCheckbox.setSelected(member.isMarried());
        relationshipField.setText(member.getRelationshipWithHost());
        hostCheckbox.setSelected(member.isHost());
    }

    public void resetForm() {
        nameField.setText("");
        lastNameField.setText("");
        birthDateField.setText("");
        relationshipField.setText("");
        baptizedCheckbox.setSelected(false);
        marriedCheckbox.setSelected(false);
        hostCheckbox.setSelected(false);
    }
    private void simulateArrowFocus(JButton button) {
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setFocusPainted(false);
                button.getModel().setRollover(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.getModel().setRollover(false);
            }
        });
    }
}

