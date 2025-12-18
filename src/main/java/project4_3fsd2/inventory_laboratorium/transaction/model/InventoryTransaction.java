package project4_3fsd2.inventory_laboratorium.transaction.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transaction")
public class InventoryTransaction {

    @Id
    @Column(name = "id", length = 15)
    private String id;

    @Column(name = "equipment_id", length = 10, nullable = false)
    private String equipmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "handled_by", length = 10, nullable = false)
    private String handledBy;

    @Column(name = "used_by", length = 10)
    private String usedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Enum untuk tipe transaksi
    public enum TransactionType {
        IN,   // Masuk / Pengembalian
        OUT   // Keluar / Peminjaman
    }

    protected InventoryTransaction() {
    }

    public InventoryTransaction(String id, String equipmentId, TransactionType transactionType,
                                Integer quantity, LocalDateTime transactionDate, String note,
                                String handledBy, String usedBy) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
        this.note = note;
        this.handledBy = handledBy;
        this.usedBy = usedBy;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }

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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}