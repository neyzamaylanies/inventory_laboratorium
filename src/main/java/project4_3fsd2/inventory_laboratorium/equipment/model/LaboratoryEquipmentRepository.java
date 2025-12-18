package project4_3fsd2.inventory_laboratorium.equipment.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LaboratoryEquipmentRepository extends JpaRepository<LaboratoryEquipment, String> {

    List<LaboratoryEquipment> findByEquipmentNameContainingIgnoreCase(String keyword);

    List<LaboratoryEquipment> findByCategoryId(String categoryId);

    List<LaboratoryEquipment> findByConditionStatus(LaboratoryEquipment.ConditionStatus status);

    List<LaboratoryEquipment> findByLocationContainingIgnoreCase(String location);
    
    long countByIdIn(List<String> ids);
}