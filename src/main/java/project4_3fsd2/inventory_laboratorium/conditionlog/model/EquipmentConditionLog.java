package project4_3fsd2.inventory_laboratorium.conditionlog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_condition_log")
public class EquipmentConditionLog {

    @Id
    @Column(name = "id", length = 15)
    private String id;

    @Column(name = "equipment_id", length = 10, nullable = false)
    private String equipmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_before", nullable = false)
    private ConditionStatus conditionBefore;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_after", nullable = false)
    private ConditionStatus conditionAfter;

    @Column(name = "check_date", nullable = false)
    private LocalDateTime checkDate;

    @Column(name = "checked_by", length = 10, nullable = false)
    private String checkedBy;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Enum untuk status kondisi
    public enum ConditionStatus {
        BAIK, 
        RUSAK_RINGAN, 
        RUSAK_BERAT, 
        DALAM_PERBAIKAN
    }

    protected EquipmentConditionLog() {
    }

    public EquipmentConditionLog(String id, String equipmentId, ConditionStatus conditionBefore, 
                                 ConditionStatus conditionAfter, LocalDateTime checkDate, 
                                 String checkedBy, String note) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.conditionBefore = conditionBefore;
        this.conditionAfter = conditionAfter;
        this.checkDate = checkDate;
        this.checkedBy = checkedBy;
        this.note = note;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.checkDate == null) {
            this.checkDate = LocalDateTime.now();
        }
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public ConditionStatus getConditionBefore() {
        return conditionBefore;
    }

    public void setConditionBefore(ConditionStatus conditionBefore) {
        this.conditionBefore = conditionBefore;
    }

    public ConditionStatus getConditionAfter() {
        return conditionAfter;
    }

    public void setConditionAfter(ConditionStatus conditionAfter) {
        this.conditionAfter = conditionAfter;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}