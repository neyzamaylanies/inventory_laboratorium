package project4_3fsd2.inventory_laboratorium.conditionlog.view;

import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLog;
import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLog.ConditionStatus;
import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLogRepository;
import project4_3fsd2.inventory_laboratorium.DataAlreadyExistsException;
import project4_3fsd2.inventory_laboratorium.DataNotFoundException;
import project4_3fsd2.inventory_laboratorium.InvalidDataException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EquipmentConditionLogService {

    private final EquipmentConditionLogRepository repository;

    public EquipmentConditionLogService(EquipmentConditionLogRepository repository) {
        this.repository = repository;
    }

    public List<EquipmentConditionLog> getAll() {
        return repository.findAll();
    }

    public List<EquipmentConditionLog> getAllWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }

    public EquipmentConditionLog getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("ConditionLog", id));
    }

    public List<EquipmentConditionLog> getByEquipmentId(String equipmentId) {
        return repository.findByEquipmentIdOrderByCheckDateDesc(equipmentId);
    }

    public List<EquipmentConditionLog> getByCheckedBy(String checkedBy) {
        return repository.findByCheckedBy(checkedBy);
    }

    public List<EquipmentConditionLog> getByConditionAfter(ConditionStatus conditionAfter) {
        return repository.findByConditionAfter(conditionAfter);
    }

    public List<EquipmentConditionLog> getByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByCheckDateBetween(startDate, endDate);
    }

    public EquipmentConditionLog save(EquipmentConditionLog log) {
        if (log.getId() == null || log.getId().isBlank()) {
            throw new InvalidDataException("ConditionLog", "id", "wajib diisi");
        }

        if (!log.getId().matches("^LOG\\d{8}\\d{3}$")) {
            throw new InvalidDataException("ConditionLog", "id", "harus format LOG20250101001, LOG20250101002, dst");
        }

        if (repository.existsById(log.getId())) {
            throw new DataAlreadyExistsException("ConditionLog", log.getId());
        }

        if (log.getEquipmentId() == null || log.getEquipmentId().isBlank()) {
            throw new InvalidDataException("ConditionLog", "equipmentId", "wajib diisi");
        }

        if (log.getCheckedBy() == null || log.getCheckedBy().isBlank()) {
            throw new InvalidDataException("ConditionLog", "checkedBy", "wajib diisi");
        }

        if (log.getConditionBefore() == null) {
            throw new InvalidDataException("ConditionLog", "conditionBefore", "wajib diisi");
        }

        if (log.getConditionAfter() == null) {
            throw new InvalidDataException("ConditionLog", "conditionAfter", "wajib diisi");
        }

        return repository.save(log);
    }

    @Transactional
    public List<EquipmentConditionLog> saveBulk(List<EquipmentConditionLog> logs) {
        if (logs == null || logs.isEmpty()) {
            throw new InvalidDataException("ConditionLog", "list tidak boleh kosong");
        }

        if (logs.size() > 100) {
            throw new InvalidDataException("ConditionLog", "maksimal 100 data per bulk insert");
        }

        for (EquipmentConditionLog log : logs) {
            if (log.getId() == null || log.getId().isBlank()) {
                throw new InvalidDataException("ConditionLog", "id", "wajib diisi untuk setiap data");
            }

            if (repository.existsById(log.getId())) {
                throw new DataAlreadyExistsException("ConditionLog", log.getId());
            }
        }

        return repository.saveAll(logs);
    }

    public EquipmentConditionLog update(String id, EquipmentConditionLog updated) {
        EquipmentConditionLog existing = getById(id);

        existing.setEquipmentId(updated.getEquipmentId());
        existing.setConditionBefore(updated.getConditionBefore());
        existing.setConditionAfter(updated.getConditionAfter());
        existing.setCheckDate(updated.getCheckDate());
        existing.setCheckedBy(updated.getCheckedBy());
        existing.setNote(updated.getNote());

        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new DataNotFoundException("ConditionLog", id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("ConditionLog", "list ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("ConditionLog", "maksimal 100 data per bulk delete");
        }

        long existingCount = repository.countByIdIn(ids);
        if (existingCount != ids.size()) {
            throw new InvalidDataException("ConditionLog", "sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        repository.deleteAllById(ids);
    }

    public List<EquipmentConditionLog> getConditionDegraded() {
        return repository.findAll().stream()
            .filter(log -> isConditionWorsened(log.getConditionBefore(), log.getConditionAfter()))
            .toList();
    }

    private boolean isConditionWorsened(ConditionStatus before, ConditionStatus after) {
        int beforeLevel = getConditionLevel(before);
        int afterLevel = getConditionLevel(after);
        return afterLevel > beforeLevel;
    }

    private int getConditionLevel(ConditionStatus status) {
        return switch (status) {
            case BAIK -> 0;
            case RUSAK_RINGAN -> 1;
            case DALAM_PERBAIKAN -> 2;
            case RUSAK_BERAT -> 3;
        };
    }
}