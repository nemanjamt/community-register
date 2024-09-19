package community.register.view.panels;

import community.register.dao.implementations.FamilyDAO;
import community.register.model.Family;
import community.register.utils.PanelChangeListener;
import community.register.view.models.FamiliesSearchTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchFamiliesPanel extends JPanel {

    JPanel searchPanel;
    JButton searchButton;
    FamilyDAO familyDAO;
    PanelChangeListener panelChangeListener;
    FamiliesSearchTableModel tableModel;
    JTable table;
    TableColumn idColumn;
    GridBagConstraints gridBagConstraints;
    JLabel lastNameLabel;
    JTextField lastNameField;
    JLabel streetLabel;
    JTextField streetField;
    JLabel orderNumberLabel;
    JTextField orderNumberField;
    JPanel buttonPanel;
    JButton showMoreButton;
    
    public SearchFamiliesPanel() {
        this.familyDAO = FamilyDAO.getInstance();
        this.setLayout(new BorderLayout());
        this.searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        lastNameLabel = new JLabel("Презиме:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        this.searchPanel.add(lastNameLabel, gridBagConstraints);

        lastNameField = new JTextField(15);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        this.searchPanel.add(lastNameField, gridBagConstraints);

        streetLabel = new JLabel("Улица:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        this.searchPanel.add(streetLabel, gridBagConstraints);

        streetField = new JTextField(15);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        this.searchPanel.add(streetField, gridBagConstraints);

        orderNumberLabel = new JLabel("Редни број:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        this.searchPanel.add(orderNumberLabel, gridBagConstraints);

        orderNumberField = new JTextField(15);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        this.searchPanel.add(orderNumberField, gridBagConstraints);

        this.searchButton = new JButton("Претражи");
        this.searchButton.setPreferredSize(new Dimension(100, 25));  // Фиксна величина дугмета
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        this.searchPanel.add(this.searchButton, gridBagConstraints);

        this.searchButton.addActionListener(e -> {
            String lastName = lastNameField.getText().isEmpty() ? null : lastNameField.getText();
            String street = streetField.getText().isEmpty() ? null : streetField.getText();
            Integer orderNumber ;

            try {
                orderNumber = orderNumberField.getText().isEmpty() ? null : Integer.parseInt(orderNumberField.getText());
            } catch (NumberFormatException ex)  {
                JOptionPane.showMessageDialog(this, "Редни број треба да је искључиво број!", "Грешка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.setFamilies(familyDAO.search(lastName, street, orderNumber));

            idColumn = table.getColumnModel().getColumn(0);
            table.removeColumn(idColumn);
        });

        this.add(this.searchPanel, BorderLayout.NORTH);

        tableModel = new FamiliesSearchTableModel();
        table = new JTable(tableModel);
        idColumn = table.getColumnModel().getColumn(0);
        table.removeColumn(idColumn);
        JScrollPane scrollPane = new JScrollPane(table);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));  // Razmak oko dugmeta

        showMoreButton = new JButton("Прикажи више");
        showMoreButton.setPreferredSize(new Dimension(140, 25));
        showMoreButton.setMinimumSize(new Dimension(140, 25));
        showMoreButton.setMaximumSize(new Dimension(140, 25));
        showMoreButton.setEnabled(false);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(showMoreButton);
        buttonPanel.add(Box.createHorizontalGlue());
        
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                showMoreButton.setEnabled(selectedRow != -1);
            }
        });

        showMoreButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
               Family h = tableModel.getFamilyAt(selectedRow);
               tableModel.reset();

               idColumn = table.getColumnModel().getColumn(0);
               table.removeColumn(idColumn);

               panelChangeListener.onPanelChange("changeFamilyPanel", h.getId());
            }
       });

    }

    public void setPanelChangeListener(PanelChangeListener panelChangeListener) {
        this.panelChangeListener = panelChangeListener;
    }

}
