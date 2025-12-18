package project4_3fsd2.inventory_laboratorium.equipment.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "laboratory_equipment")
public class LaboratoryEquipment {

    @Id
    @Column(name = "id", length = 10)
    private String id;

    @Column(name = "equipment_name", length = 150, nullable = false)
    private String equipmentName;

    @Column(name = "category_id", length = 10, nullable = false)
    private String categoryId;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity = 0;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_status")
    private ConditionStatus conditionStatus = ConditionStatus.BAIK;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enum untuk status kondisi
    public enum ConditionStatus {
        BAIK,
        RUSAK_RINGAN,
        RUSAK_BERAT,
        DALAM_PERBAIKAN
    }

    protected LaboratoryEquipment() {
    }

    public LaboratoryEquipment(String id, String equipmentName, String categoryId, 
                               Integer totalQuantity, Integer availableQuantity,
                               ConditionStatus conditionStatus, String location,
                               LocalDate purchaseDate, BigDecimal purchasePrice) {
        this.id = id;
        this.equipmentName = equipmentName;
        this.categoryId = categoryId;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.conditionStatus = conditionStatus;
        this.location = location;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public ConditionStatus getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(ConditionStatus conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}