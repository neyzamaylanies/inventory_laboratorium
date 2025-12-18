package project4_3fsd2.inventory_laboratorium;

public class InvalidDataException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final String reason;

    // Constructor lengkap (dengan field name)
    public InvalidDataException(String resourceName, String fieldName, String reason) {
        super(resourceName + " tidak valid: " + fieldName + " " + reason);
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.reason = reason;
    }

    // Constructor tanpa fieldName (untuk validasi umum)
    public InvalidDataException(String resourceName, String reason) {
        super(resourceName + " tidak valid: " + reason);
        this.resourceName = resourceName;
        this.fieldName = null;
        this.reason = reason;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getReason() {
        return reason;
    }
}