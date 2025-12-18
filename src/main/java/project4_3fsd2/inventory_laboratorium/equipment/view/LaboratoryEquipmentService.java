package project4_3fsd2.inventory_laboratorium.equipment.view;

import project4_3fsd2.inventory_laboratorium.equipment.model.LaboratoryEquipment;
import project4_3fsd2.inventory_laboratorium.equipment.model.LaboratoryEquipmentRepository;
import project4_3fsd2.inventory_laboratorium.DataAlreadyExistsException;
import project4_3fsd2.inventory_laboratorium.DataNotFoundException;
import project4_3fsd2.inventory_laboratorium.InvalidDataException;
import project4_3fsd2.inventory_laboratorium.EquipmentDamageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LaboratoryEquipmentService {

    private final LaboratoryEquipmentRepository repository;

    public LaboratoryEquipmentService(LaboratoryEquipmentRepository repository) {
        this.repository = repository;
    }

    public List<LaboratoryEquipment> getAll() {
        return repository.findAll();
    }

    public List<LaboratoryEquipment> getAllWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }

    public LaboratoryEquipment getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Equipment", id));
    }

    public List<LaboratoryEquipment> searchByName(String keyword) {
        return repository.findByEquipmentNameContainingIgnoreCase(keyword);
    }

    public List<LaboratoryEquipment> searchByCategory(String categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public List<LaboratoryEquipment> searchByCondition(LaboratoryEquipment.ConditionStatus status) {
        return repository.findByConditionStatus(status);
    }

    public List<LaboratoryEquipment> searchByLocation(String location) {
        return repository.findByLocationContainingIgnoreCase(location);
    }

    // Validasi kondisi equipment - digunakan oleh Transaction
    public void validateEquipmentCondition(String equipmentId) {
        LaboratoryEquipment equipment = getById(equipmentId);
        
        if (equipment.getConditionStatus() == LaboratoryEquipment.ConditionStatus.RUSAK_BERAT) {
            throw new EquipmentDamageException(
                equipmentId, 
                "RUSAK_BERAT", 
                "Alat rusak berat tidak dapat dipinjam"
            );
        }
        
        if (equipment.getConditionStatus() == LaboratoryEquipment.ConditionStatus.DALAM_PERBAIKAN) {
            throw new EquipmentDamageException(
                equipmentId, 
                "DALAM_PERBAIKAN", 
                "Alat sedang dalam perbaikan"
            );
        }
    }

    public LaboratoryEquipment save(LaboratoryEquipment equipment) {
        if (equipment.getId() == null || equipment.getId().isBlank()) {
            throw new InvalidDataException("Equipment", "id", "wajib diisi");
        }

        if (repository.existsById(equipment.getId())) {
            throw new DataAlreadyExistsException("Equipment", equipment.getId());
        }

        if (equipment.getEquipmentName() == null || equipment.getEquipmentName().isBlank()) {
            throw new InvalidDataException("Equipment", "equipmentName", "wajib diisi");
        }

        if (equipment.getCategoryId() == null || equipment.getCategoryId().isBlank()) {
            throw new InvalidDataException("Equipment", "categoryId", "wajib diisi");
        }

        if (equipment.getTotalQuantity() == null || equipment.getTotalQuantity() < 0) {
            throw new InvalidDataException("Equipment", "totalQuantity", "harus >= 0");
        }

        if (equipment.getAvailableQuantity() == null || equipment.getAvailableQuantity() < 0) {
            throw new InvalidDataException("Equipment", "availableQuantity", "harus >= 0");
        }

        if (equipment.getAvailableQuantity() > equipment.getTotalQuantity()) {
            throw new InvalidDataException("Equipment", "availableQuantity", 
                "tidak boleh lebih besar dari totalQuantity");
        }

        return repository.save(equipment);
    }

    @Transactional
    public List<LaboratoryEquipment> saveBulk(List<LaboratoryEquipment> equipments) {
        if (equipments == null || equipments.isEmpty()) {
            throw new InvalidDataException("Equipment", "list tidak boleh kosong");
        }

        if (equipments.size() > 100) {
            throw new InvalidDataException("Equipment", "maksimal 100 data per bulk insert");
        }

        for (LaboratoryEquipment equipment : equipments) {
            if (equipment.getId() == null || equipment.getId().isBlank()) {
                throw new InvalidDataException("Equipment", "id", "wajib diisi untuk setiap data");
            }

            if (repository.existsById(equipment.getId())) {
                throw new DataAlreadyExistsException("Equipment", equipment.getId());
            }
        }

        return repository.saveAll(equipments);
    }

    public LaboratoryEquipment update(String id, LaboratoryEquipment updated) {
        LaboratoryEquipment existing = getById(id);

        existing.setEquipmentName(updated.getEquipmentName());
        existing.setCategoryId(updated.getCategoryId());
        existing.setTotalQuantity(updated.getTotalQuantity());
        existing.setAvailableQuantity(updated.getAvailableQuantity());
        existing.setConditionStatus(updated.getConditionStatus());
        existing.setLocation(updated.getLocation());
        existing.setPurchaseDate(updated.getPurchaseDate());
        existing.setPurchasePrice(updated.getPurchasePrice());

        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new DataNotFoundException("Equipment", id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("Equipment", "list ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("Equipment", "maksimal 100 data per bulk delete");
        }

        long existingCount = repository.countByIdIn(ids);
        if (existingCount != ids.size()) {
            throw new InvalidDataException("Equipment", "sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        repository.deleteAllById(ids);
    }
}