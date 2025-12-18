package project4_3fsd2.inventory_laboratorium.conditionlog.controller;

import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLog;
import project4_3fsd2.inventory_laboratorium.conditionlog.model.EquipmentConditionLog.ConditionStatus;
import project4_3fsd2.inventory_laboratorium.conditionlog.view.EquipmentConditionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public List<EquipmentConditionLog> list(
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
        summary = "Mengambil detail satu log kondisi", 
        description = "Mengambil detail satu log kondisi berdasarkan ID."
    )
    public EquipmentConditionLog get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/equipment/{equipmentId}")
    @Operation(
        summary = "Mengambil history kondisi alat", 
        description = "Mengambil seluruh riwayat kondisi untuk satu alat tertentu (urut dari terbaru)."
    )
    public List<EquipmentConditionLog> getByEquipment(@PathVariable String equipmentId) {
        return service.getByEquipmentId(equipmentId);
    }

    @GetMapping("/checker/{checkedBy}")
    @Operation(
        summary = "Mengambil log berdasarkan petugas", 
        description = "Mengambil log kondisi yang dicek oleh user tertentu."
    )
    public List<EquipmentConditionLog> getByChecker(@PathVariable String checkedBy) {
        return service.getByCheckedBy(checkedBy);
    }

    @GetMapping("/condition/{status}")
    @Operation(
        summary = "Mengambil log berdasarkan kondisi akhir", 
        description = "Mengambil log berdasarkan kondisi setelah pengecekan."
    )
    public List<EquipmentConditionLog> getByCondition(@PathVariable ConditionStatus status) {
        return service.getByConditionAfter(status);
    }

    @GetMapping("/date-range")
    @Operation(
        summary = "Mengambil log berdasarkan rentang tanggal", 
        description = "Mengambil log dalam rentang tanggal tertentu."
    )
    public List<EquipmentConditionLog> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return service.getByDateRange(startDate, endDate);
    }

    @GetMapping("/degraded")
    @Operation(
        summary = "Mengambil log kondisi yang memburuk", 
        description = "Mengambil log dimana kondisi alat memburuk (BAIK → RUSAK, dll)."
    )
    public List<EquipmentConditionLog> getConditionDegraded() {
        return service.getConditionDegraded();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat log kondisi baru", 
        description = "Membuat satu data log kondisi baru ke dalam sistem."
    )
    public EquipmentConditionLog create(@RequestBody EquipmentConditionLog log) {
        return service.save(log);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat log kondisi secara bulk", 
        description = "Membuat banyak log kondisi baru dalam satu transaksi (maksimal 100)."
    )
    public List<EquipmentConditionLog> createBulk(@RequestBody List<EquipmentConditionLog> logs) {
        return service.saveBulk(logs);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data log kondisi", 
        description = "Memperbarui data log kondisi berdasarkan ID."
    )
    public EquipmentConditionLog update(
            @PathVariable String id, 
            @RequestBody EquipmentConditionLog log) {
        return service.update(id, log);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus log kondisi", 
        description = "Menghapus satu log kondisi berdasarkan ID."
    )
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus log kondisi secara bulk", 
        description = "Menghapus banyak log kondisi berdasarkan daftar ID (maksimal 100)."
    )
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }
}