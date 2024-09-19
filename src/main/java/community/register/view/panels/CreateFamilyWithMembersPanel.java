package community.register.view.panels;

import community.register.dao.implementations.FamilyDAO;
import community.register.model.Family;
import community.register.model.FamilyMember;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;

public class CreateFamilyWithMembersPanel extends FamilyPanel {

    private final FamilyDAO familyDAO;

    public CreateFamilyWithMembersPanel() {
        super();
        this.setDeleteButtonVisible(false);
        this.familyDAO = FamilyDAO.getInstance();
        this.setSaveChangesContent("Креирај");
    }

    @Override
    void reset() {
        super.familyMemberForm.resetFamilyMemberFields();
        super.familyForm.resetFamilyFields();
        super.tableModel.reset();
        
        TableColumn idColumn = super.table.getColumnModel().getColumn(0);
        table.removeColumn(idColumn);
    }

    @Override
    public void onSaveFamily(Family family) {
        List<FamilyMember> familyMembers = tableModel.getFamilyMemberList();
        if (familyMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Потребно је навести барем једног члана породице(домаћина)!", "Грешка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int count = (int) familyMembers.stream()
                .filter(FamilyMember::isHost)
                .count();

        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Потребно је навести домаћина!", "Грешка", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (count > 1) {
            JOptionPane.showMessageDialog(this, "Дозвољено је да постоји само један домаћин!", "Грешка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (family.getPatronSaint().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Потребно је да се наведе крсна слава!", "Грешка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(FamilyMember familyMember: familyMembers) {
            if (familyMember.isHost()) {
                family.setHost(familyMember);
                break;
            }
        }

        family.setFamilyMembers(familyMembers);
        familyDAO.addFamily(family);
        familyDAO.saveData();
        JOptionPane.showMessageDialog(this, "Породица је успјешно додата у систем", "Успјешно", JOptionPane.INFORMATION_MESSAGE);
        this.reset();
    }
}
