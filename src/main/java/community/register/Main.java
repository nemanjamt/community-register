package community.register;

import community.register.config.Config;
import community.register.dao.implementations.FamilyDAO;
import community.register.view.main.MainJFrame;

public class Main {

    public static void main(String[] args) {
        Config.loadConfig();
        FamilyDAO.getInstance().loadData();
        MainJFrame.getInstance();
    }
}
