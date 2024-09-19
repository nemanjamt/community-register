package community.register.utils;

import community.register.model.Family;

public interface FamilyFormListener {
    void onSaveFamily(Family family);
    void onDelete(Long familyId);
}
