package project4_3fsd2.inventory_laboratorium.transaction.view;

import project4_3fsd2.inventory_laboratorium.transaction.model.InventoryTransaction;
import project4_3fsd2.inventory_laboratorium.transaction.model.InventoryTransactionRepository;
import project4_3fsd2.inventory_laboratorium.equipment.model.LaboratoryEquipment;
import project4_3fsd2.inventory_laboratorium.equipment.model.LaboratoryEquipmentRepository;
import project4_3fsd2.inventory_laboratorium.DataAlreadyExistsException;
import project4_3fsd2.inventory_laboratorium.DataNotFoundException;
import project4_3fsd2.inventory_laboratorium.InvalidDataException;
import project4_3fsd2.inventory_laboratorium.EquipmentDamageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventoryTransactionService {

    private final InventoryTransactionRepository repository;
    private final LaboratoryEquipmentRepository equipmentRepository;

    public InventoryTransactionService(
            InventoryTransactionRepository repository,
            LaboratoryEquipmentRepository equipmentRepository) {
        this.repository = repository;
        this.equipmentRepository = equipmentRepository;
    }

    public List<InventoryTransaction> getAll() {
        return repository.findAll();
    }

    public List<InventoryTransaction> getAllWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }

    public InventoryTransaction getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Transaction", id));
    }

    public List<InventoryTransaction> getByEquipment(String equipmentId) {
        return repository.findByEquipmentId(equipmentId);
    }

    public List<InventoryTransaction> getByType(InventoryTransaction.TransactionType type) {
        return repository.findByTransactionType(type);
    }

    public List<InventoryTransaction> getByHandler(String handledBy) {
        return repository.findByHandledBy(handledBy);
    }

    public List<InventoryTransaction> getByStudent(String usedBy) {
        return repository.findByUsedBy(usedBy);
    }

    public List<InventoryTransaction> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByTransactionDateBetween(start, end);
    }

    /**
     * Get active borrowings (yang belum dikembalikan)
     */
    public List<InventoryTransaction> getActiveBorrowings() {
        return repository.findByTransactionType(InventoryTransaction.TransactionType.OUT).stream()
            .filter(out -> out.getUsedBy() != null) // Hanya peminjaman mahasiswa
            .filter(out -> !hasBeenReturned(out))   // Belum dikembalikan
            .toList();
    }

    /**
     * Cek apakah transaksi OUT sudah dikembalikan
     */
    private boolean hasBeenReturned(InventoryTransaction outTransaction) {
        List<InventoryTransaction> returns = repository.findByEquipmentAndType(
            outTransaction.getEquipmentId(),
            InventoryTransaction.TransactionType.IN
        );
        
        return returns.stream()
            .anyMatch(returnTrx -> 
                returnTrx.getUsedBy() != null &&
                returnTrx.getUsedBy().equals(outTransaction.getUsedBy()) &&
                returnTrx.getTransactionDate().isAfter(outTransaction.getTransactionDate())
            );
    }

    /**
     * Get borrowing history per student
     */
    public List<InventoryTransaction> getStudentBorrowingHistory(String studentId) {
        return repository.findByUsedBy(studentId);
    }

    /**
     * Validasi kondisi equipment sebelum transaksi OUT
     * Exception EquipmentDamageException hanya digunakan di Transaction
     */
    private void validateEquipmentCondition(LaboratoryEquipment equipment) {
        if (equipment.getConditionStatus() == LaboratoryEquipment.ConditionStatus.RUSAK_BERAT) {
            throw new EquipmentDamageException(
                equipment.getId(),
                "RUSAK_BERAT",
                "Alat rusak berat tidak dapat dipinjam"
            );
        }

        if (equipment.getConditionStatus() == LaboratoryEquipment.ConditionStatus.DALAM_PERBAIKAN) {
            throw new EquipmentDamageException(
                equipment.getId(),
                "DALAM_PERBAIKAN",
                "Alat sedang dalam perbaikan"
            );
        }
    }

    /**
     * Validasi ketersediaan stok
     */
    private void validateStock(LaboratoryEquipment equipment, Integer quantity) {
        if (equipment.getAvailableQuantity() < quantity) {
            throw new InvalidDataException(
                "Transaction",
                "quantity",
                "stok tidak mencukupi. Tersedia: " + equipment.getAvailableQuantity()
            );
        }
    }

    /**
     * Update stok equipment berdasarkan tipe transaksi
     */
    private void updateEquipmentStock(LaboratoryEquipment equipment, 
                                     InventoryTransaction.TransactionType type, 
                                     Integer quantity) {
        if (type == InventoryTransaction.TransactionType.OUT) {
            equipment.setAvailableQuantity(equipment.getAvailableQuantity() - quantity);
        } else { // IN
            equipment.setAvailableQuantity(equipment.getAvailableQuantity() + quantity);
            
            // Pastikan tidak melebihi total quantity
            if (equipment.getAvailableQuantity() > equipment.getTotalQuantity()) {
                equipment.setAvailableQuantity(equipment.getTotalQuantity());
            }
        }
        
        equipmentRepository.save(equipment);
    }

    @Transactional
    public InventoryTransaction save(InventoryTransaction transaction) {
        // Validasi ID
        if (transaction.getId() == null || transaction.getId().isBlank()) {
            throw new InvalidDataException("Transaction", "id", "wajib diisi");
        }

        if (repository.existsById(transaction.getId())) {
            throw new DataAlreadyExistsException("Transaction", transaction.getId());
        }

        // Validasi Equipment ID
        if (transaction.getEquipmentId() == null || transaction.getEquipmentId().isBlank()) {
            throw new InvalidDataException("Transaction", "equipmentId", "wajib diisi");
        }

        // Cek equipment exists
        LaboratoryEquipment equipment = equipmentRepository.findById(transaction.getEquipmentId())
                .orElseThrow(() -> new DataNotFoundException("Equipment", transaction.getEquipmentId()));

        // Validasi quantity
        if (transaction.getQuantity() == null || transaction.getQuantity() <= 0) {
            throw new InvalidDataException("Transaction", "quantity", "harus lebih dari 0");
        }

        // Validasi transaction type
        if (transaction.getTransactionType() == null) {
            throw new InvalidDataException("Transaction", "transactionType", "wajib diisi");
        }

        // Validasi handled_by
        if (transaction.getHandledBy() == null || transaction.getHandledBy().isBlank()) {
            throw new InvalidDataException("Transaction", "handledBy", "wajib diisi");
        }

        // KHUSUS TRANSAKSI OUT: Validasi kondisi equipment dan stok
        if (transaction.getTransactionType() == InventoryTransaction.TransactionType.OUT) {
            // ✅ Validasi kondisi (throw EquipmentDamageException jika rusak/perbaikan)
            validateEquipmentCondition(equipment);
            
            // Validasi stok
            validateStock(equipment, transaction.getQuantity());
        }

        // Update stok equipment
        updateEquipmentStock(equipment, transaction.getTransactionType(), transaction.getQuantity());

        // Simpan transaksi
        return repository.save(transaction);
    }

    @Transactional
    public List<InventoryTransaction> saveBulk(List<InventoryTransaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new InvalidDataException("Transaction", "list tidak boleh kosong");
        }

        if (transactions.size() > 100) {
            throw new InvalidDataException("Transaction", "maksimal 100 data per bulk insert");
        }

        // Validasi ID duplikat
        for (InventoryTransaction transaction : transactions) {
            if (transaction.getId() == null || transaction.getId().isBlank()) {
                throw new InvalidDataException("Transaction", "id", "wajib diisi untuk setiap data");
            }

            if (repository.existsById(transaction.getId())) {
                throw new DataAlreadyExistsException("Transaction", transaction.getId());
            }
        }

        // Proses setiap transaksi (akan otomatis validasi kondisi dan update stok)
        for (InventoryTransaction transaction : transactions) {
            save(transaction);
        }

        return transactions;
    }

    public InventoryTransaction update(String id, InventoryTransaction updated) {
        InventoryTransaction existing = getById(id);

        // Note: Update transaksi biasanya terbatas, 
        // karena akan mempengaruhi stok dan history
        existing.setNote(updated.getNote());

        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new DataNotFoundException("Transaction", id);
        }
        
        // Note: Penghapusan transaksi harus hati-hati
        // karena akan mempengaruhi stok equipment
        // Bisa ditambahkan logic rollback stok jika diperlukan
        
        repository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("Transaction", "list ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("Transaction", "maksimal 100 data per bulk delete");
        }

        long existingCount = repository.countByIdIn(ids);
        if (existingCount != ids.size()) {
            throw new InvalidDataException("Transaction", "sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        repository.deleteAllById(ids);
    }
}