package community.register.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Family {

    private Long id;
    private String place;
    private String street;
    private String patronSaint;
    private String secondPatronSaint;
    private boolean blessedHome;
    private String phoneNumber;
    private Integer orderNumber;
    private String note;
    private Long hostId;
    private FamilyMember host;
    private List<FamilyMember> familyMembers;
    private boolean deleted;

    public Family() {
        this.familyMembers = new ArrayList<>();
    }

    public Family(Long id, String place, String street, String patronSaint, String secondPatronSaint, boolean blessedHome, String phoneNumber, Integer orderNumber, String note, Long hostId) {
        this();
        this.id = id;
        this.place = place;
        this.street = street;
        this.patronSaint = patronSaint;
        this.secondPatronSaint = secondPatronSaint;
        this.blessedHome = blessedHome;
        this.phoneNumber = phoneNumber;
        this.orderNumber = orderNumber;
        this.note = note;
        this.hostId = hostId;
    }

    public List<FamilyMember> getFamilyMembersWithoutHost() {
        return this.familyMembers.stream()
                .filter(familyMember -> !familyMember.isHost())
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isBlessedHome() {
        return blessedHome;
    }

    public void setBlessedHome(boolean blessedHome) {
        this.blessedHome = blessedHome;
    }

    public String getSecondPatronSaint() {
        return secondPatronSaint;
    }

    public void setSecondPatronSaint(String secondPatronSaint) {
        this.secondPatronSaint = secondPatronSaint;
    }

    public String getPatronSaint() {
        return patronSaint;
    }

    public void setPatronSaint(String patronSaint) {
        this.patronSaint = patronSaint;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }



    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public FamilyMember getHost() {
        return host;
    }

    public void setHost(FamilyMember host) {
        this.host = host;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Family deepCopy() {
            List<FamilyMember> familyMemberList = new ArrayList<>();

            for(FamilyMember familyMember: this.getFamilyMembers()) {
                familyMemberList.add(familyMember.deepCopy());
            }

            Family h = new Family(this.getId(), this.getPlace(), this.getStreet(), this.getPatronSaint(), this.getSecondPatronSaint(), this.isBlessedHome(), this.getPhoneNumber(), this.getOrderNumber(), this.getNote(), this.getHostId());

            h.setFamilyMembers(familyMemberList);
            h.setHost(this.getHost());

            return h;
    }

    @Override
    public String toString() {
        return "Family{" +
                "id=" + id +
                ", street='" + place + '\'' +
                ", familyNumber='" + street + '\'' +
                ", patronSaint='" + patronSaint + '\'' +
                ", secondPatronSaint='" + secondPatronSaint + '\'' +
                ", blessedHome=" + blessedHome +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", orderNumber=" + orderNumber +
                ", note='" + note + '\'' +
                ", hostId=" + hostId +
                ", familyMembers=" + familyMembers +
                ", deleted=" + deleted +
                '}';
    }
}
