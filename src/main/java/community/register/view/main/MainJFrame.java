package community.register.view.main;

import community.register.config.Config;
import community.register.dao.implementations.FamilyDAO;
import community.register.documents.generators.PdfGenerator;
import community.register.utils.PanelChangeListener;
import community.register.view.panels.ChangeFamilyPanel;
import community.register.view.panels.CreateFamilyWithMembersPanel;
import community.register.view.panels.SearchFamiliesPanel;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainJFrame extends JFrame implements PanelChangeListener {

    public static final Dimension PREFERRED_SIZE = new Dimension(1200,800);
    private static MainJFrame instance = null;
    private final JPanel mainPanel;
    private CreateFamilyWithMembersPanel createFamilyWithMembersPanel;
    private SearchFamiliesPanel searchFamiliesPanel;
    private ChangeFamilyPanel changeFamilyPanel;
    private PdfGenerator pdfGenerator;

    private JMenuItem saveAsPdf;
    private JMenuItem addFamily;
    private JMenuItem searchFamily;

    private JMenuBar menu;

    private JMenu saveMenu;
    private JMenu familyMenu;

    public static MainJFrame getInstance() {
        if (instance == null) {
            instance = new MainJFrame();
            return instance;
        }
        return instance;
    }

    private MainJFrame() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(PREFERRED_SIZE);
        this.setPreferredSize(PREFERRED_SIZE);
        this.setTitle("ДОМОВНИК");

        this.pdfGenerator = new PdfGenerator();
        this.mainPanel = new JPanel(new CardLayout());

        this.createFamilyWithMembersPanel = new CreateFamilyWithMembersPanel();
        this.searchFamiliesPanel = new SearchFamiliesPanel();
        this.changeFamilyPanel = new ChangeFamilyPanel();

        this.searchFamiliesPanel.setPanelChangeListener(this);
        this.changeFamilyPanel.setPanelChangeListener(this);

        this.mainPanel.add(this.createFamilyWithMembersPanel, "createFamilyWithMembersPanel");
        this.mainPanel.add(this.searchFamiliesPanel, "searchFamiliesPanel");
        this.mainPanel.add(this.changeFamilyPanel, "changeFamilyPanel");
        this.menu = new JMenuBar();

        this.familyMenu = new JMenu("Породица");
        this.saveMenu = new JMenu("Сачувај као");

        this.searchFamily = new JMenuItem("претражи породице");
        this.addFamily = new JMenuItem("додај породицу");
        this.saveAsPdf = new JMenuItem("пдф документ");

        this.searchFamily.addActionListener(e -> changeCard("searchFamiliesPanel"));

        this.addFamily.addActionListener(e -> changeCard("createFamilyWithMembersPanel"));

        this.saveAsPdf.addActionListener(e -> {
            try {
                this.pdfGenerator.generateDocument();
                JOptionPane.showMessageDialog(this, "Пдф је креиран ", "Успјешно", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Није пронађена путања на којој би требало да се генерише пдф!", "Грешка", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Документ није креиран!", "Грешка", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        });

        this.familyMenu.add(this.searchFamily);
        this.familyMenu.addSeparator();
        this.familyMenu.add(this.addFamily);

        this.saveMenu.add(this.saveAsPdf);

        this.menu.add(this.familyMenu);
        this.menu.add(this.saveMenu);

        this.setJMenuBar(this.menu);
        this.add(this.mainPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void onPanelChange(String name, Long familyId) {
        if (name.equalsIgnoreCase("changeFamilyPanel")) {
            this.changeFamilyPanel.setFamilyId(familyId);
        }
        this.changeCard(name);
    }

    private void changeCard(String name) {
        CardLayout cl = (CardLayout)(mainPanel.getLayout());
        cl.show(mainPanel, name);
    }

}
