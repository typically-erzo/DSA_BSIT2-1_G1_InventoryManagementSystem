package inventory.model;

public class Supplier {
    private String id;
    private String name;
    private String contactInfo;
    private String address;

    public Supplier(String id, String name, String contactInfo, String address) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}