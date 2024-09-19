package community.register.dao.implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import community.register.config.Config;
import community.register.dao.CrudDAO;
import community.register.model.Family;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FamilyDAO implements CrudDAO<Family> {
    private List<Family> familyList;
    private String filePath;
    private static FamilyDAO familyDAO;
    public static FamilyDAO getInstance() {
        if ( familyDAO == null) {
            familyDAO = new FamilyDAO();
            return familyDAO;
        }
        return familyDAO;
    }

    private FamilyDAO() {
        this.familyList = new ArrayList<>();
        this.filePath= Config.resourcePath + "/data/families-"+ LocalDateTime.now().toString().replace(":",".")+".csv";
    }

    public Family findFamilyById(Long familyId) {
        return familyList.stream()
                .filter(family -> family.getId().equals(familyId))
                .findFirst()
                .map(Family::deepCopy)
                .orElse(null);
    }

    private Family getFamilyById(Long familyId) {
        return familyList.stream()
                .filter(family -> family.getId().equals(familyId))
                .findFirst()
                .orElse(null);
    }

    public void addFamily(Family h) {

        h.setId(System.currentTimeMillis());

        boolean exists = familyList.stream().anyMatch(family -> family.getOrderNumber().equals(h.getOrderNumber()));
        if (exists) {
            familyList.sort(Comparator.comparingInt(Family::getOrderNumber));

            int curr = h.getOrderNumber();

            for (Family currentFamily : familyList) {
                if (currentFamily.getOrderNumber() < curr) {
                    continue;
                }
                if (curr != currentFamily.getOrderNumber()) {
                    break;
                }
                curr++;
                currentFamily.setOrderNumber(curr);
            }
        }

        familyList.sort(Comparator.comparingInt(Family::getOrderNumber));
        familyList.add(h);
    }



    public List<Family> search(String lastName, String street, Integer orderNumber) {
        List<Family> results = new ArrayList<>();

        for(Family h : familyList) {
            if ((lastName == null || h.getHost().getLastName().equalsIgnoreCase(lastName)) &&
                    (street == null || h.getStreet().equalsIgnoreCase(street)) &&
                    (orderNumber == null ||  h.getOrderNumber().equals(orderNumber))) {
                results.add(h.deepCopy());
            }
        }

        return results;
    }


    @Override
    public Family create(Family family) {
        return null;
    }

    @Override
    public Family read(Long id) {
        return null;
    }

    @Override
    public Family update(Family family) {
        Family familyToUpdate = getFamilyById(family.getId());
        familyList.remove(familyToUpdate);
        familyList.add(family);
        familyList.sort(Comparator.comparingInt(Family::getOrderNumber));
        return family;
    }

    @Override
    public boolean delete(Long id) {
        return familyList.removeIf(family -> family.getId().equals(id));
    }

    public List<Family> getFamilyList() {
        return familyList;
    }

    public List<Family> getSortedFamilyListByOrderNumber() {
        familyList.sort(Comparator.comparingInt(Family::getOrderNumber));
        return familyList;
    }

    public void saveData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); ;
        String json = gson.toJson(this.familyList);
        this.filePath= Config.resourcePath + "/data/families-"+ LocalDateTime.now().toString().replace(":",".")+".json";
        File f = new File(this.filePath);

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(this.filePath)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String familyPath = Config.resourcePath + "/data/family-path.csv";
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(familyPath), "utf-8"))) {
            out.println(this.filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {

        String familyPath = Config.resourcePath + "/data/family-path.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(familyPath))) {
            this.filePath = br.readLine();
            if (this.filePath == null) {
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(this.filePath)) {
            Type listType = new TypeToken<List<Family>>() {}.getType();
            this.familyList = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
