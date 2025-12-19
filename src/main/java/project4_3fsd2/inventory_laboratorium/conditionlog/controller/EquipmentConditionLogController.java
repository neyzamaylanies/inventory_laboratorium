package project4_3fsd2.inventory_laboratorium.conditionlog.controller;

import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLog;
import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLog.ConditionStatus;
import project4_3fsd2.inventory_laboratorium.conditionlog.view.EquipmentConditionLogService;
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
@RequestMapping("/api/condition-logs")
@Tag(name = "Equipment Condition Log", description = "API untuk mengelola log kondisi alat laboratorium")
public class EquipmentConditionLogController {

    private final EquipmentConditionLogService service;

    public EquipmentConditionLogController(EquipmentConditionLogService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Mengambil daftar semua log kondisi", 
        description = "Mengambil seluruh data log kondisi alat yang tersedia di sistem. Mendukung pagination opsional."
    )
    public ResponseEntity<ApiResponse<List<EquipmentConditionLog>>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        List<EquipmentConditionLog> logs;
        if (page == null && size == null) {
            logs = service.getAll();
        } else {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            logs = service.getAllWithPagination(p, s);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Data log kondisi berhasil diambil", logs));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu log kondisi", 
        description = "Mengambil detail satu log kondisi berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<EquipmentConditionLog>> get(@PathVariable String id) {
        EquipmentConditionLog log = service.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data log kondisi berhasil ditemukan", log));
    }

    @GetMapping("/equipment/{equipmentId}")
    @Operation(
        summary = "Mengambil history kondisi alat", 
        description = "Mengambil seluruh riwayat kondisi untuk satu alat tertentu (urut dari terbaru)."
    )
    public ResponseEntity<ApiResponse<List<EquipmentConditionLog>>> getByEquipment(@PathVariable String equipmentId) {
        List<EquipmentConditionLog> logs = service.getByEquipmentId(equipmentId);
        return ResponseEntity.ok(ApiResponse.success("Riwayat kondisi alat berhasil ditemukan", logs));
    }

    @GetMapping("/checker/{checkedBy}")
    @Operation(
        summary = "Mengambil log berdasarkan petugas", 
        description = "Mengambil log kondisi yang dicek oleh user tertentu."
    )
    public ResponseEntity<ApiResponse<List<EquipmentConditionLog>>> getByChecker(@PathVariable String checkedBy) {
        List<EquipmentConditionLog> logs = service.getByCheckedBy(checkedBy);
        return ResponseEntity.ok(ApiResponse.success("Data log berhasil ditemukan", logs));
    }

    @GetMapping("/condition/{status}")
    @Operation(
        summary = "Mengambil log berdasarkan kondisi akhir", 
        description = "Mengambil log berdasarkan kondisi setelah pengecekan."
    )
    public ResponseEntity<ApiResponse<List<EquipmentConditionLog>>> getByCondition(@PathVariable ConditionStatus status) {
        List<EquipmentConditionLog> logs = service.getByConditionAfter(status);
        return ResponseEntity.ok(ApiResponse.success("Data log berhasil ditemukan", logs));
    }

    @GetMapping("/date-range")
    @Operation(
        summary = "Mengambil log berdasarkan rentang tanggal", 
        description = "Mengambil log dalam rentang tanggal tertentu."
    )
    public ResponseEntity<ApiResponse<List<EquipmentConditionLog>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<EquipmentConditionLog> logs = service.getByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Data log berhasil ditemukan", logs));
    }

    @GetMapping("/degraded")
    @Operation(
        summary = "Mengambil log kondisi yang memburuk", 
        description = "Mengambil log dimana kondisi alat memburuk (BAIK → RUSAK, dll)."
    )
    public ResponseEntity<ApiResponse<List<EquipmentConditionLog>>> getConditionDegraded() {
        List<EquipmentConditionLog> logs = service.getConditionDegraded();
        return ResponseEntity.ok(ApiResponse.success("Data kondisi yang memburuk berhasil ditemukan", logs));
    }

    @PostMapping
    @Operation(
        summary = "Membuat log kondisi baru", 
        description = "Membuat satu data log kondisi baru ke dalam sistem."
    )
    public ResponseEntity<ApiResponse<EquipmentConditionLog>> create(@RequestBody EquipmentConditionLog log) {
        EquipmentConditionLog created = service.save(log);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Membuat log kondisi secara bulk", 
        description = "Membuat banyak log kondisi baru dalam satu transaksi (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<List<EquipmentConditionLog>>> createBulk(@RequestBody List<EquipmentConditionLog> logs) {
        List<EquipmentConditionLog> created = service.saveBulk(logs);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bulk insert berhasil: " + created.size() + " data dibuat", created));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data log kondisi", 
        description = "Memperbarui data log kondisi berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<EquipmentConditionLog>> update(
            @PathVariable String id, 
            @RequestBody EquipmentConditionLog log) {
        EquipmentConditionLog updated = service.update(id, log);
        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menghapus log kondisi", 
        description = "Menghapus satu log kondisi berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @DeleteMapping("/bulk")
    @Operation(
        summary = "Menghapus log kondisi secara bulk", 
        description = "Menghapus banyak log kondisi berdasarkan daftar ID (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<Void>> deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
        return ResponseEntity.ok(
            ApiResponse.success("Bulk delete berhasil: " + ids.size() + " data dihapus", null)
        );
    }
}