package project4_3fsd2.inventory_laboratorium.transaction.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, String> {

    List<InventoryTransaction> findByEquipmentId(String equipmentId);

    List<InventoryTransaction> findByTransactionType(InventoryTransaction.TransactionType type);

    List<InventoryTransaction> findByHandledBy(String handledBy);

    List<InventoryTransaction> findByUsedBy(String usedBy);

    List<InventoryTransaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM InventoryTransaction t WHERE t.equipmentId = :equipmentId " +
           "AND t.transactionType = :type ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByEquipmentAndType(
        @Param("equipmentId") String equipmentId,
        @Param("type") InventoryTransaction.TransactionType type
    );
    
    long countByIdIn(List<String> ids);
}