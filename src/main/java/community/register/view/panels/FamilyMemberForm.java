package community.register.view.panels;

import community.register.model.FamilyMember;
import community.register.utils.FamilyMemberFormListener;

import javax.swing.*;
import java.awt.*;

public class FamilyMemberForm extends JPanel {

    protected JTextField nameField;
    protected JTextField birthDateField;
    protected JTextField lastNameField;
    protected JTextField relationshipField;

    protected JCheckBox baptizedCheckbox;
    protected JCheckBox marriedCheckbox;
    protected JCheckBox hostCheckbox;

    protected JButton saveButton;

    protected GridBagConstraints gridBagConstraints;
    protected GridBagLayout layout;

    protected JLabel nameLabel;
    protected JLabel birthDateLabel;
    protected JLabel lastNameLabel;
    protected JLabel baptizedLabel;
    protected JLabel marriedLabel;
    protected JLabel hostLabel;
    protected JLabel relationshipLabel;

    private FamilyMemberFormListener familyMemberFormListener;

    public FamilyMemberForm() {
        initComponents();
    }
    public FamilyMemberForm(FamilyMemberFormListener familyMemberFormListener) {
        this();
        this.familyMemberFormListener = familyMemberFormListener;
    }

    private void initComponents() {
        this.layout = new GridBagLayout();
        this.gridBagConstraints = new GridBagConstraints();
        this.setLayout(this.layout);

        this.gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 0;
        this.nameLabel = new JLabel("Име:");
        this.add(this.nameLabel, gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        this.nameField = new JTextField(15);
        this.add(nameField, gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 1;
        this.lastNameLabel = new JLabel("Презиме:");
        this.add(this.lastNameLabel, gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        this.lastNameField = new JTextField(15);
        this.add(lastNameField, gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 2;
        this.birthDateLabel = new JLabel("Датум рођења:");
        this.add(this.birthDateLabel, gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        this.birthDateField = new JTextField(15);
        this.add(birthDateField, gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 3;
        this.baptizedLabel = new JLabel("Крштен:");
        this.add(this.baptizedLabel, gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        this.baptizedCheckbox = new JCheckBox();
        this.add(baptizedCheckbox, gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 4;
        this.marriedLabel = new JLabel("Вјенчан:");
        this.add(this.marriedLabel, gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        this.marriedCheckbox = new JCheckBox();
        this.add(marriedCheckbox, gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 5;
        this.relationshipLabel = new JLabel("Сродство са домаћином:");
        this.add(this.relationshipLabel, gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        this.relationshipField = new JTextField(15);
        this.add(relationshipField, gridBagConstraints);

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 6;
        this.hostLabel = new JLabel("Домаћин:");
        this.add(this.hostLabel, gridBagConstraints);

        this.gridBagConstraints.gridx = 1;
        this.hostCheckbox = new JCheckBox();
        this.add(hostCheckbox, gridBagConstraints);

        this.gridBagConstraints.gridy = 7;
        this.saveButton = new JButton("Сачувај");
        this.add(saveButton, gridBagConstraints);

        this.saveButton.addActionListener(e -> {
            Long id = System.currentTimeMillis();
            String name = nameField.getText();
            String lastName = lastNameField.getText();
            String birthDate = birthDateField.getText();
            boolean baptized = baptizedCheckbox.isSelected();
            boolean married = marriedCheckbox.isSelected();
            String relationship = relationshipField.getText();
            boolean host = hostCheckbox.isSelected();

            FamilyMember familyMember = new FamilyMember(id, relationship, married, baptized, birthDate, name, lastName, host);

            this.familyMemberFormListener.onSaveFamilyMember(familyMember);

            this.resetFamilyMemberFields();
        });
    }

    protected void setFamilyMember(FamilyMember familyMember) {
        nameField.setText(familyMember.getName());
        lastNameField.setText(familyMember.getLastName());
        birthDateField.setText(familyMember.getBirthday());
        baptizedCheckbox.setSelected(familyMember.isBaptized());
        marriedCheckbox.setSelected(familyMember.isMarried());
        relationshipField.setText(familyMember.getRelationshipWithHost());
        hostCheckbox.setSelected(familyMember.isHost());
    }

    protected void resetFamilyMemberFields() {
        nameField.setText("");
        lastNameField.setText("");
        birthDateField.setText("");
        baptizedCheckbox.setSelected(false);
        marriedCheckbox.setSelected(false);
        relationshipField.setText("");
        hostCheckbox.setSelected(false);
    }


}
