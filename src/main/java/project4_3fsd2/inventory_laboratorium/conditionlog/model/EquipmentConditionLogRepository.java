package project4_3fsd2.inventory_laboratorium.conditionlog.model;

import org.springframework.data.jpa.repository.JpaRepository;
import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLog.ConditionStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EquipmentConditionLogRepository extends JpaRepository<EquipmentConditionLog, String> {

    // Cari log berdasarkan equipment_id
    List<EquipmentConditionLog> findByEquipmentId(String equipmentId);

    // Cari log berdasarkan equipment_id, urut dari terbaru
    List<EquipmentConditionLog> findByEquipmentIdOrderByCheckDateDesc(String equipmentId);

    // Cari log berdasarkan petugas yang cek
    List<EquipmentConditionLog> findByCheckedBy(String checkedBy);

    // Cari log berdasarkan kondisi setelah
    List<EquipmentConditionLog> findByCurrentCondition(ConditionStatus currentCondition);

    // Cari log berdasarkan rentang tanggal
    List<EquipmentConditionLog> findByCheckDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Cari log yang kondisinya berubah (before != after)
    List<EquipmentConditionLog> findByPreviousCondition(ConditionStatus previousCondition);

    // Hitung log berdasarkan ID list
    long countByIdIn(List<String> ids);
}