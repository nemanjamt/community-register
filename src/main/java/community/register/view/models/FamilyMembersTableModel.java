package community.register.view.models;

import community.register.model.FamilyMember;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FamilyMembersTableModel extends AbstractTableModel {

    private List<FamilyMember> familyMemberList;
    private String[] columnNames = {"Id", "Име", "Презиме", "Датум рођења", "Крштен", "Вјенчан", "Сродство са домаћином", "Домаћин"};

    public FamilyMembersTableModel() {
        this.familyMemberList = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return this.familyMemberList.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FamilyMember familyMember = familyMemberList.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> familyMember.getId();
            case 1 -> familyMember.getName();
            case 2 -> familyMember.getLastName();
            case 3 -> familyMember.getBirthday();
            case 4 -> familyMember.isBaptized() ? "Да" : "Не";
            case 5 -> familyMember.isMarried() ? "Да" : "Не";
            case 6 -> familyMember.getRelationshipWithHost();
            case 7 -> familyMember.isHost() ? "Да" : "Не";
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public FamilyMember getFamilyMemberAt(int rowIndex) {
        return familyMemberList.get(rowIndex);
    }

    public void addRow(FamilyMember familyMember) {
        this.familyMemberList.add(familyMember);
        fireTableStructureChanged();
    }

    public void removeRow(int row) {
        this.familyMemberList.remove(row);
        fireTableStructureChanged();
    }

    public List<FamilyMember> getFamilyMemberList() {
        return this.familyMemberList;
    }

    public void setFamilyMemberList(List<FamilyMember> familyMembers) {
        this.familyMemberList = familyMembers;
    }

    public void reset() {
        this.familyMemberList = new ArrayList<>();
        fireTableStructureChanged();
    }
}
