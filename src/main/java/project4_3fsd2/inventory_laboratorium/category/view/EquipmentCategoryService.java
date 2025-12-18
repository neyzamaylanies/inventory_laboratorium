package project4_3fsd2.inventory_laboratorium.category.view;

import project4_3fsd2.inventory_laboratorium.category.model.EquipmentCategory;
import project4_3fsd2.inventory_laboratorium.category.model.EquipmentCategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EquipmentCategoryService {

    private final EquipmentCategoryRepository repository;

    public EquipmentCategoryService(EquipmentCategoryRepository repository) {
        this.repository = repository;
    }

    public List<EquipmentCategory> getAll() {
        return repository.findAll();
    }

    public List<EquipmentCategory> getAllWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }

    public EquipmentCategory getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<EquipmentCategory> searchByName(String keyword) {
        return repository.findByCategoryNameContainingIgnoreCase(keyword);
    }

    public EquipmentCategory save(EquipmentCategory category) {
        if (category.getId() == null || category.getId().isBlank()) {
            throw new IllegalArgumentException("ID kategori wajib diisi");
        }

        if (repository.existsById(category.getId())) {
            throw new CategoryAlreadyExistsException(category.getId());
        }

        if (category.getCategoryName() == null || category.getCategoryName().isBlank()) {
            throw new IllegalArgumentException("Nama kategori wajib diisi");
        }

        if (repository.existsByCategoryName(category.getCategoryName())) {
            throw new IllegalArgumentException("Nama kategori '" + category.getCategoryName() + "' sudah ada");
        }

        return repository.save(category);
    }

    @Transactional
    public List<EquipmentCategory> saveBulk(List<EquipmentCategory> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("List kategori tidak boleh kosong");
        }

        if (categories.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk insert");
        }

        for (EquipmentCategory category : categories) {
            if (category.getId() == null || category.getId().isBlank()) {
                throw new IllegalArgumentException("ID kategori wajib diisi untuk setiap data");
            }

            if (repository.existsById(category.getId())) {
                throw new CategoryAlreadyExistsException(category.getId());
            }
        }

        return repository.saveAll(categories);
    }

    public EquipmentCategory update(String id, EquipmentCategory updated) {
        EquipmentCategory existing = getById(id);

        existing.setCategoryName(updated.getCategoryName());
        existing.setDescription(updated.getDescription());

        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        long existingCount = repository.countByIdIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalArgumentException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        repository.deleteAllById(ids);
    }
}