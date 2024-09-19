package community.register.view.panels;

import community.register.dao.implementations.FamilyDAO;
import community.register.model.FamilyMember;
import community.register.utils.FamilyFormListener;
import community.register.utils.FamilyMemberFormListener;
import community.register.view.models.FamilyMembersTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public abstract class FamilyPanel extends JPanel implements FamilyMemberFormListener, FamilyFormListener {

    protected FamilyDAO familyDAO;

    protected FamilyMembersTableModel tableModel;

    protected FamilyMemberForm familyMemberForm;
    protected FamilyForm familyForm;

    protected JTable table;
    private TableColumn idColumn;

    protected JScrollPane tableScrollPane;

    protected JButton changeButton;
    protected JButton deleteButton;

    protected JPanel centerPanel;
    protected JPanel buttonPanel;

    public FamilyPanel() {
        this.familyMemberForm = new FamilyMemberForm(this);
        this.familyForm = new FamilyForm(this);
        initComponents();
    }

    private void initComponents() {
        this.familyDAO = FamilyDAO.getInstance();

        this.setLayout(new BorderLayout());

        this.add(this.familyForm, BorderLayout.NORTH);

        this.tableModel = new FamilyMembersTableModel();
        table = new JTable(tableModel);

        idColumn = table.getColumnModel().getColumn(0);
        table.removeColumn(idColumn);
        tableScrollPane = new JScrollPane(table);
        this.add(tableScrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        changeButton = new JButton("Измијени");
        deleteButton = new JButton("Избриши");

        buttonPanel.add(changeButton);
        buttonPanel.add(deleteButton);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        centerPanel.add(familyMemberForm, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                idColumn = table.getColumnModel().getColumn(0);
                table.removeColumn(idColumn);
            }
        });

        changeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                FamilyMember familyMember = tableModel.getFamilyMemberAt(selectedRow);
                familyMemberForm.setFamilyMember(familyMember);

                tableModel.removeRow(selectedRow);
                idColumn = table.getColumnModel().getColumn(0);
                table.removeColumn(idColumn);
            }
        });

    }

    protected void setSaveChangesContent(String content) {
        this.familyForm.saveChangesButton.setText(content);
    }

    protected void setDeleteButtonVisible(boolean isVisible) {
        this.familyForm.deleteButton.setVisible(isVisible);
    }

    @Override
    public void onSaveFamilyMember(FamilyMember familyMember) {
        tableModel.addRow(familyMember);

        idColumn = table.getColumnModel().getColumn(0);
        table.removeColumn(idColumn);
    }


    @Override
    public void onDelete(Long familyId) {

    }

    abstract void reset();

}
