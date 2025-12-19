package project4_3fsd2.inventory_laboratorium.equipment.controller;

import project4_3fsd2.inventory_laboratorium.equipment.model.LaboratoryEquipment;
import project4_3fsd2.inventory_laboratorium.equipment.view.LaboratoryEquipmentService;
import project4_3fsd2.inventory_laboratorium.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
@Tag(name = "Laboratory Equipment", description = "API untuk mengelola alat laboratorium")
public class LaboratoryEquipmentController {

    private final LaboratoryEquipmentService service;

    public LaboratoryEquipmentController(LaboratoryEquipmentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Mengambil daftar semua alat laboratorium", 
        description = "Mengambil seluruh data alat laboratorium yang tersedia di sistem. Mendukung pagination opsional melalui parameter page dan size."
    )
    public ResponseEntity<ApiResponse<List<LaboratoryEquipment>>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        List<LaboratoryEquipment> equipments;
        if (page == null && size == null) {
            equipments = service.getAll();
        } else {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            equipments = service.getAllWithPagination(p, s);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Data alat laboratorium berhasil diambil", equipments));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu alat", 
        description = "Mengambil detail satu alat laboratorium berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<LaboratoryEquipment>> get(@PathVariable String id) {
        LaboratoryEquipment equipment = service.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data alat berhasil ditemukan", equipment));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Mencari alat berdasarkan nama", 
        description = "Mencari alat berdasarkan kata kunci pada nama alat (case insensitive)."
    )
    public ResponseEntity<ApiResponse<List<LaboratoryEquipment>>> search(@RequestParam String q) {
        List<LaboratoryEquipment> equipments = service.searchByName(q);
        return ResponseEntity.ok(ApiResponse.success("Pencarian berhasil", equipments));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(
        summary = "Mencari alat berdasarkan kategori", 
        description = "Mengambil semua alat yang termasuk dalam kategori tertentu."
    )
    public ResponseEntity<ApiResponse<List<LaboratoryEquipment>>> searchByCategory(@PathVariable String categoryId) {
        List<LaboratoryEquipment> equipments = service.searchByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Data alat berhasil ditemukan", equipments));
    }

    @GetMapping("/condition/{status}")
    @Operation(
        summary = "Mencari alat berdasarkan kondisi", 
        description = "Mengambil semua alat dengan status kondisi tertentu (BAIK, RUSAK_RINGAN, RUSAK_BERAT, DALAM_PERBAIKAN)."
    )
    public ResponseEntity<ApiResponse<List<LaboratoryEquipment>>> searchByCondition(@PathVariable LaboratoryEquipment.ConditionStatus status) {
        List<LaboratoryEquipment> equipments = service.searchByCondition(status);
        return ResponseEntity.ok(ApiResponse.success("Data alat berhasil ditemukan", equipments));
    }

    @GetMapping("/location")
    @Operation(
        summary = "Mencari alat berdasarkan lokasi", 
        description = "Mencari alat berdasarkan lokasi penyimpanan (case insensitive)."
    )
    public ResponseEntity<ApiResponse<List<LaboratoryEquipment>>> searchByLocation(@RequestParam String location) {
        List<LaboratoryEquipment> equipments = service.searchByLocation(location);
        return ResponseEntity.ok(ApiResponse.success("Pencarian berhasil", equipments));
    }

    @PostMapping
    @Operation(
        summary = "Membuat data alat baru", 
        description = "Membuat satu data alat laboratorium baru ke dalam sistem."
    )
    public ResponseEntity<ApiResponse<LaboratoryEquipment>> create(@RequestBody LaboratoryEquipment equipment) {
        LaboratoryEquipment created = service.save(equipment);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Membuat alat secara bulk", 
        description = "Membuat banyak data alat baru dalam satu transaksi (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<List<LaboratoryEquipment>>> createBulk(@RequestBody List<LaboratoryEquipment> equipments) {
        List<LaboratoryEquipment> created = service.saveBulk(equipments);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bulk insert berhasil: " + created.size() + " data dibuat", created));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data alat", 
        description = "Memperbarui data alat laboratorium berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<LaboratoryEquipment>> update(
            @PathVariable String id, 
            @RequestBody LaboratoryEquipment equipment) {
        LaboratoryEquipment updated = service.update(id, equipment);
        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menghapus alat", 
        description = "Menghapus satu data alat laboratorium berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @DeleteMapping("/bulk")
    @Operation(
        summary = "Menghapus alat secara bulk", 
        description = "Menghapus banyak data alat berdasarkan daftar ID (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<Void>> deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
        return ResponseEntity.ok(
            ApiResponse.success("Bulk delete berhasil: " + ids.size() + " data dihapus", null)
        );
    }
}