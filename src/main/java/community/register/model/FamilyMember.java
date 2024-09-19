package community.register.model;

public class FamilyMember {

    private Long id;
    private String lastName;
    private String name;
    private String birthday;
    private boolean baptized;
    private boolean married;
    private String relationshipWithHost;
    private boolean deleted;
    private boolean isHost;

    public FamilyMember() {

    }

    public FamilyMember(Long id, String relationshipWithHost, boolean married, boolean baptized, String birthYear, String name, String lastName, boolean isHost) {
        this.id = id;
        this.deleted = false;
        this.relationshipWithHost = relationshipWithHost;
        this.married = married;
        this.baptized = baptized;
        this.birthday = birthYear;
        this.name = name;
        this.lastName = lastName;
        this.isHost = isHost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isBaptized() {
        return baptized;
    }

    public void setBaptized(boolean baptized) {
        this.baptized = baptized;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public String getRelationshipWithHost() {
        return relationshipWithHost;
    }

    public void setRelationshipWithHost(String relationshipWithHost) {
        this.relationshipWithHost = relationshipWithHost;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public FamilyMember deepCopy() {
        return new FamilyMember(this.getId(), this.getRelationshipWithHost(), this.isMarried(), this.isBaptized(), this.getBirthday(), this.getName(), this.getLastName(), this.isHost);
    }

    @Override
    public String toString() {
        return "FamilyMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthYear=" + birthday +
                ", baptized=" + baptized +
                ", married=" + married +
                ", relationshipWithHost='" + relationshipWithHost + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
