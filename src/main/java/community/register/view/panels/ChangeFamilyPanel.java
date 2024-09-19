package community.register.view.panels;

import community.register.dao.implementations.FamilyDAO;
import community.register.model.Family;
import community.register.model.FamilyMember;
import community.register.utils.PanelChangeListener;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;

public class ChangeFamilyPanel extends FamilyPanel {

    private FamilyDAO familyDAO;
    private Long familyId;
    private Family family;
    private PanelChangeListener panelChangeListener;

    public ChangeFamilyPanel() {
        super();
        this.familyDAO = FamilyDAO.getInstance();
        this.familyId = -1L;
        this.setSaveChangesContent("Сачувај измјене");
        this.setDeleteButtonVisible(true);
    }

    @Override
    void reset() {
        super.familyMemberForm.resetFamilyMemberFields();
        super.familyForm.resetFamilyFields();
        super.tableModel.reset();
        
        TableColumn idColumn = super.table.getColumnModel().getColumn(0);
        table.removeColumn(idColumn);
    }

    public void setFamilyId(Long familyId) {
        this.reset();
        this.familyId = familyId;
        this.family = familyDAO.findFamilyById(this.familyId);
        
        if (this.family != null) {
            this.tableModel.setFamilyMemberList(this.family.getFamilyMembers());
            this.tableModel.fireTableStructureChanged();
            this.familyForm.setFamily(family);
            TableColumn idColumn = table.getColumnModel().getColumn(0);
            table.removeColumn(idColumn);
        }
    }

    public void setPanelChangeListener(PanelChangeListener panelChangeListener) {
        this.panelChangeListener = panelChangeListener;
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
                family.setHostId(familyMember.getId());
                break;
            }
        }

        family.setFamilyMembers(familyMembers);
        familyDAO.update(family);
        familyDAO.saveData();
        JOptionPane.showMessageDialog(this, "Промјене су сачуване", "Успјешно", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onDelete(Long familyId) {
        Object[] options = {"Да", "Не"};

        int result = JOptionPane.showOptionDialog(
                this,
                "Јесте ли сигурни да желите избрисати породицу из читавог система?",
                "Потврда",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (result == 0) {
            this.familyDAO.delete(this.familyId);
            JOptionPane.showMessageDialog(this, "Породица успјешно уклоњена из система", "Успјешно", JOptionPane.INFORMATION_MESSAGE);
            familyDAO.saveData();
            this.reset();
            if (panelChangeListener != null) {
                panelChangeListener.onPanelChange("searchFamiliesPanel", -1L);
            }
        }
    }
}
