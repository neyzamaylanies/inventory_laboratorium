package project4_3fsd2.inventory_laboratorium.transaction.controller;

import project4_3fsd2.inventory_laboratorium.transaction.model.InventoryTransaction;
import project4_3fsd2.inventory_laboratorium.transaction.view.InventoryTransactionService;
import project4_3fsd2.inventory_laboratorium.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Inventory Transaction", description = "API untuk mengelola transaksi inventori laboratorium")
public class InventoryTransactionController {

    private final InventoryTransactionService service;

    public InventoryTransactionController(InventoryTransactionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Mengambil daftar transaksi",
        description = "Mengambil data transaksi dengan optional filtering (equipmentId, type, handledBy, usedBy, start, end, activeOnly, studentId) dan pagination"
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> list(
            @RequestParam(required = false) String equipmentId,
            @RequestParam(required = false) InventoryTransaction.TransactionType type,
            @RequestParam(required = false) String handledBy,
            @RequestParam(required = false) String usedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        List<InventoryTransaction> transactions;

        if (equipmentId != null) {
            transactions = service.getByEquipment(equipmentId);
        } else if (type != null) {
            transactions = service.getByType(type);
        } else if (handledBy != null) {
            transactions = service.getByHandler(handledBy);
        } else if (usedBy != null) {
            transactions = service.getByStudent(usedBy);
        } else if (start != null && end != null) {
            transactions = service.getByDateRange(start, end);
        } else if (Boolean.TRUE.equals(activeOnly)) {
            transactions = service.getActiveBorrowings();
        } else if (studentId != null) {
            transactions = service.getStudentBorrowingHistory(studentId);
        } else if (page != null || size != null) {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            transactions = service.getAllWithPagination(p, s);
        } else {
            transactions = service.getAll();
        }

        String message = transactions.isEmpty()
                ? "Data transaksi tidak ditemukan"
                : "Data transaksi berhasil diambil";

        return ResponseEntity.ok(ApiResponse.success(message, transactions));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu transaksi",
        description = "Mengambil detail satu transaksi berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<InventoryTransaction>> get(@PathVariable String id) {
        InventoryTransaction transaction = service.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data transaksi berhasil ditemukan", transaction));
    }

    @PostMapping
    @Operation(
        summary = "Membuat transaksi baru",
        description = "Membuat satu transaksi baru ke dalam sistem. Otomatis memvalidasi kondisi alat dan memperbarui stok."
    )
    public ResponseEntity<ApiResponse<InventoryTransaction>> create(@RequestBody InventoryTransaction transaction) {
        InventoryTransaction created = service.save(transaction);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Membuat transaksi secara bulk",
        description = "Membuat banyak transaksi baru dalam satu operasi (maksimal 100). Setiap transaksi akan divalidasi."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> createBulk(@RequestBody List<InventoryTransaction> transactions) {
        List<InventoryTransaction> created = service.saveBulk(transactions);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bulk insert berhasil: " + created.size() + " transaksi dibuat", created));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data transaksi",
        description = "Memperbarui data transaksi berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<InventoryTransaction>> update(
            @PathVariable String id,
            @RequestBody InventoryTransaction transaction) {
        InventoryTransaction updated = service.update(id, transaction);
        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menghapus transaksi",
        description = "Menghapus satu transaksi berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @DeleteMapping("/bulk")
    @Operation(
        summary = "Menghapus transaksi secara bulk",
        description = "Menghapus banyak transaksi berdasarkan daftar ID (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<Void>> deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
        return ResponseEntity.ok(
            ApiResponse.success("Bulk delete berhasil: " + ids.size() + " transaksi dihapus", null)
        );
    }
}