package project4_3fsd2.inventory_laboratorium;

public class EquipmentDamageException extends RuntimeException {

    private final String equipmentId;
    private final String conditionStatus;
    private final String reason;

    public EquipmentDamageException(String equipmentId, String conditionStatus, String reason) {
        super("Alat dengan ID " + equipmentId + " dalam kondisi " + conditionStatus + ": " + reason);
        this.equipmentId = equipmentId;
        this.conditionStatus = conditionStatus;
        this.reason = reason;
    }

    // Constructor sederhana
    public EquipmentDamageException(String equipmentId, String reason) {
        super("Alat dengan ID " + equipmentId + " tidak dapat digunakan: " + reason);
        this.equipmentId = equipmentId;
        this.conditionStatus = null;
        this.reason = reason;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getConditionStatus() {
        return conditionStatus;
    }

    public String getReason() {
        return reason;
    }
}