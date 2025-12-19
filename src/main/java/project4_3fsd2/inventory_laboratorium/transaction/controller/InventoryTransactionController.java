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
        summary = "Mengambil daftar semua transaksi", 
        description = "Mengambil seluruh data transaksi inventori yang tersedia di sistem. Mendukung pagination opsional melalui parameter page dan size."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        List<InventoryTransaction> transactions;
        if (page == null && size == null) {
            transactions = service.getAll();
        } else {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            transactions = service.getAllWithPagination(p, s);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Data transaksi berhasil diambil", transactions));
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

    @GetMapping("/equipment/{equipmentId}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan alat", 
        description = "Mengambil semua transaksi yang terkait dengan alat tertentu."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getByEquipment(@PathVariable String equipmentId) {
        List<InventoryTransaction> transactions = service.getByEquipment(equipmentId);
        return ResponseEntity.ok(ApiResponse.success("Data transaksi berhasil ditemukan", transactions));
    }

    @GetMapping("/type/{type}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan tipe", 
        description = "Mengambil semua transaksi dengan tipe tertentu (IN atau OUT)."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getByType(@PathVariable InventoryTransaction.TransactionType type) {
        List<InventoryTransaction> transactions = service.getByType(type);
        return ResponseEntity.ok(ApiResponse.success("Data transaksi berhasil ditemukan", transactions));
    }

    @GetMapping("/handler/{handledBy}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan petugas", 
        description = "Mengambil semua transaksi yang ditangani oleh petugas tertentu."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getByHandler(@PathVariable String handledBy) {
        List<InventoryTransaction> transactions = service.getByHandler(handledBy);
        return ResponseEntity.ok(ApiResponse.success("Data transaksi berhasil ditemukan", transactions));
    }

    @GetMapping("/student/{usedBy}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan mahasiswa", 
        description = "Mengambil semua transaksi yang dilakukan oleh mahasiswa tertentu."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getByStudent(@PathVariable String usedBy) {
        List<InventoryTransaction> transactions = service.getByStudent(usedBy);
        return ResponseEntity.ok(ApiResponse.success("Data transaksi berhasil ditemukan", transactions));
    }

    @GetMapping("/date-range")
    @Operation(
        summary = "Mengambil transaksi berdasarkan rentang tanggal", 
        description = "Mengambil semua transaksi dalam rentang tanggal tertentu. Format: yyyy-MM-dd'T'HH:mm:ss"
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<InventoryTransaction> transactions = service.getByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success("Data transaksi berhasil ditemukan", transactions));
    }

    @GetMapping("/active-borrowings")
    @Operation(
        summary = "Mengambil daftar peminjaman aktif",
        description = "Mengambil semua transaksi peminjaman yang belum dikembalikan."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getActiveBorrowings() {
        List<InventoryTransaction> activeBorrowings = service.getActiveBorrowings();
        return ResponseEntity.ok(ApiResponse.success("Data peminjaman aktif berhasil diambil", activeBorrowings));
    }

    @GetMapping("/student-history/{studentId}")
    @Operation(
        summary = "Mengambil riwayat peminjaman mahasiswa",
        description = "Mengambil semua riwayat peminjaman untuk mahasiswa tertentu."
    )
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getStudentHistory(@PathVariable String studentId) {
        List<InventoryTransaction> history = service.getStudentBorrowingHistory(studentId);
        return ResponseEntity.ok(ApiResponse.success("Riwayat peminjaman mahasiswa berhasil diambil", history));
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
        description = "Memperbarui data transaksi berdasarkan ID. Update terbatas pada field tertentu."
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