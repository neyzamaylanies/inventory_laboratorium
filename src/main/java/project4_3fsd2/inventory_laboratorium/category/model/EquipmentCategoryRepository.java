package project4_3fsd2.inventory_laboratorium.category.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, String> {

    List<EquipmentCategory> findByCategoryNameContainingIgnoreCase(String keyword);

    boolean existsByCategoryName(String categoryName);

    Optional<EquipmentCategory> findByCategoryName(String categoryName);
    
    long countByIdIn(List<String> ids);
}