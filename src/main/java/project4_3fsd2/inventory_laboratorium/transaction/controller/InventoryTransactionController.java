package project4_3fsd2.inventory_laboratorium.transaction.controller;

import project4_3fsd2.inventory_laboratorium.transaction.model.InventoryTransaction;
import project4_3fsd2.inventory_laboratorium.transaction.view.InventoryTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public List<InventoryTransaction> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        if (page == null && size == null) {
            return service.getAll();
        }

        int p = (page != null && page >= 0) ? page : 0;
        int s = (size != null && size > 0) ? size : 10;
        return service.getAllWithPagination(p, s);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu transaksi", 
        description = "Mengambil detail satu transaksi berdasarkan ID."
    )
    public InventoryTransaction get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/equipment/{equipmentId}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan alat", 
        description = "Mengambil semua transaksi yang terkait dengan alat tertentu."
    )
    public List<InventoryTransaction> getByEquipment(@PathVariable String equipmentId) {
        return service.getByEquipment(equipmentId);
    }

    @GetMapping("/type/{type}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan tipe", 
        description = "Mengambil semua transaksi dengan tipe tertentu (IN atau OUT)."
    )
    public List<InventoryTransaction> getByType(@PathVariable InventoryTransaction.TransactionType type) {
        return service.getByType(type);
    }

    @GetMapping("/handler/{handledBy}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan petugas", 
        description = "Mengambil semua transaksi yang ditangani oleh petugas tertentu."
    )
    public List<InventoryTransaction> getByHandler(@PathVariable String handledBy) {
        return service.getByHandler(handledBy);
    }

    @GetMapping("/student/{usedBy}")
    @Operation(
        summary = "Mengambil transaksi berdasarkan mahasiswa", 
        description = "Mengambil semua transaksi yang dilakukan oleh mahasiswa tertentu."
    )
    public List<InventoryTransaction> getByStudent(@PathVariable String usedBy) {
        return service.getByStudent(usedBy);
    }

    @GetMapping("/date-range")
    @Operation(
        summary = "Mengambil transaksi berdasarkan rentang tanggal", 
        description = "Mengambil semua transaksi dalam rentang tanggal tertentu. Format: yyyy-MM-dd'T'HH:mm:ss"
    )
    public List<InventoryTransaction> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return service.getByDateRange(start, end);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat transaksi baru", 
        description = "Membuat satu transaksi baru ke dalam sistem. Otomatis memvalidasi kondisi alat dan memperbarui stok."
    )
    public InventoryTransaction create(@RequestBody InventoryTransaction transaction) {
        return service.save(transaction);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat transaksi secara bulk", 
        description = "Membuat banyak transaksi baru dalam satu operasi (maksimal 100). Setiap transaksi akan divalidasi."
    )
    public List<InventoryTransaction> createBulk(@RequestBody List<InventoryTransaction> transactions) {
        return service.saveBulk(transactions);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data transaksi", 
        description = "Memperbarui data transaksi berdasarkan ID. Update terbatas pada field tertentu."
    )
    public InventoryTransaction update(
            @PathVariable String id, 
            @RequestBody InventoryTransaction transaction) {
        return service.update(id, transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus transaksi", 
        description = "Menghapus satu transaksi berdasarkan ID."
    )
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus transaksi secara bulk", 
        description = "Menghapus banyak transaksi berdasarkan daftar ID (maksimal 100)."
    )
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }
}