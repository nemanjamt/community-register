package community.register.view.models;

import community.register.model.Family;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FamiliesSearchTableModel extends AbstractTableModel {

    private List<Family> families;
    private String[] columnNames = {"Id", "Презиме", "Мјесто", "Улица", "Број телефона"};

    public FamiliesSearchTableModel() {
        this.families = new ArrayList<>();
    }

    public Family getFamilyAt(int rowIndex) {
        return families.get(rowIndex);
    }
    @Override
    public int getRowCount() {
        return this.families.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Family family = families.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> family.getId();
            case 1 -> family.getHost().getLastName();
            case 2 -> family.getPlace();
            case 3 -> family.getStreet();
            case 4 -> family.getPhoneNumber();
            default -> null;
        };

    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setFamilies(List<Family> families) {
        this.families = families;
        fireTableStructureChanged();
    }

    public List<Family> getFamilies() {
        return this.families;
    }

    public void reset() {
        this.families = new ArrayList<>();
        fireTableStructureChanged();
    }
}
