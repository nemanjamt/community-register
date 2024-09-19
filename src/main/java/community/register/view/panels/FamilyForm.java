package community.register.view.panels;

import community.register.model.Family;
import community.register.utils.FamilyFormListener;

import javax.swing.*;
import java.awt.*;


public class FamilyForm extends JPanel {

    protected JLabel orderNumberLabel;
    protected JLabel noteLabel;
    protected JLabel placeLabel;
    protected JLabel streetLabel;
    protected JLabel patronSaintLabel;
    protected JLabel secondPatronSaintLabel;
    protected JLabel blessedHomeLabel;
    protected JLabel phoneNumberLabel;

    protected JTextField placeField;
    protected JTextField streetField;
    protected JTextField patronSaintField;
    protected JTextField secondPatronSaintField;
    protected JTextField phoneNumberField;
    protected JTextField orderNumberField;

    protected JTextArea noteField;

    protected JCheckBox blessedHomeField;

    protected JButton saveChangesButton;
    protected JButton deleteButton;

    protected GridBagLayout layout;
    protected GridBagConstraints gridBagConstraints;

    protected FamilyFormListener familyFormListener;

    protected Family family;

    protected JScrollPane scrollPane;

    public FamilyForm() {
        family = null;
        initComponents();
    }

    public FamilyForm(FamilyFormListener familyFormListener) {
        this();
        this.familyFormListener = familyFormListener;
    }
    
    public void initComponents() {
        this.layout = new GridBagLayout();
        this.gridBagConstraints = new GridBagConstraints();
        this.setLayout(this.layout);

        this.gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 0;

        placeLabel = new JLabel("Мјесто:");
        this.add(placeLabel, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 1;
        placeField = new JTextField(15);
        this.add(placeField, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 1;

        streetLabel = new JLabel("Улица:");
        this.add(streetLabel, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 1;
        streetField = new JTextField(15);
        this.add(streetField, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 2;

        patronSaintLabel = new JLabel("Крсна слава:");
        this.add(patronSaintLabel, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 1;
        patronSaintField = new JTextField(15);
        this.add(patronSaintField, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 3;

        secondPatronSaintLabel = new JLabel("Преслава:");
        this.add(secondPatronSaintLabel, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 1;
        secondPatronSaintField = new JTextField(15);
        this.add(secondPatronSaintField, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 4;

        blessedHomeLabel = new JLabel("Дом освештан:");
        this.add(blessedHomeLabel, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 1;
        blessedHomeField = new JCheckBox();
        this.add(blessedHomeField, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 5;

        phoneNumberLabel = new JLabel("Број телефона:");
        this.add(phoneNumberLabel, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 1;
        phoneNumberField = new JTextField(15);
        this.add(phoneNumberField, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 6;
        orderNumberLabel = new JLabel("Редни број:");
        this.add(orderNumberLabel, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 1;
        orderNumberField = new JTextField();
        this.add(orderNumberField, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 7;
        noteLabel = new JLabel("Напомена:");
        this.add(noteLabel, this.gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        noteField = new JTextArea();
        noteField.setLineWrap(true);
        noteField.setWrapStyleWord(true);

        scrollPane = new JScrollPane(noteField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(180, 80));
        this.add(scrollPane, this.gridBagConstraints);

        saveChangesButton = new JButton("Сачувај измјене");

        this.gridBagConstraints.gridy = 8;
        this.add(saveChangesButton, this.gridBagConstraints);
        this.gridBagConstraints.gridx = 0;
        deleteButton = new JButton("Избриши");
        this.add(deleteButton, this.gridBagConstraints);
  
        deleteButton.addActionListener(e -> {
            this.familyFormListener.onDelete(this.family.getId());
        });

        saveChangesButton.addActionListener(e -> {
            int orderNumber ;

            if (orderNumberField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Редни број је обавезно поље!", "Грешка", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                try {
                    orderNumber = Integer.parseInt(orderNumberField.getText());
                    if (orderNumber <= 0) {
                        JOptionPane.showMessageDialog(this, "Редни број треба да је искључиво број већи или једнак од 1!", "Грешка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Редни број треба да је искључиво број!", "Грешка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Long id = (this.family != null) ? this.family.getId() : null;
            Long hostId = (this.family != null) ? this.family.getHostId() : null;

            Family family = new Family(id, placeField.getText(), streetField.getText(), patronSaintField.getText(),
                    secondPatronSaintField.getText(), blessedHomeField.isSelected(), phoneNumberField.getText(),
                    orderNumber, noteField.getText(), hostId);

            this.familyFormListener.onSaveFamily(family);
        });
    }

    protected void resetFamilyFields() {
        placeField.setText("");
        patronSaintField.setText("");
        streetField.setText("");
        secondPatronSaintField.setText("");
        blessedHomeField.setSelected(false);
        phoneNumberField.setText("");
        orderNumberField.setText("");
        noteField.setText("");
    }

    protected void setFamily(Family family) {
        this.family = family;
        placeField.setText(family.getPlace());
        patronSaintField.setText(family.getPatronSaint());
        streetField.setText(family.getStreet());
        secondPatronSaintField.setText(family.getSecondPatronSaint());
        blessedHomeField.setSelected(family.isBlessedHome());
        phoneNumberField.setText(family.getPhoneNumber());
        orderNumberField.setText(family.getOrderNumber().toString());
        noteField.setText(family.getNote());
    }

}
