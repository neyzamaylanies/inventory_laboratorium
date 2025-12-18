package project4_3fsd2.inventory_laboratorium;

public class DataAlreadyExistsException extends RuntimeException {

    private final String resourceName;
    private final String idValue;

    public DataAlreadyExistsException(String resourceName, String idValue) {
        super(resourceName + " dengan ID " + idValue + " sudah ada");
        this.resourceName = resourceName;
        this.idValue = idValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getIdValue() {
        return idValue;
    }
}