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
        summary = "Mengambil daftar alat laboratorium",
        description = "Mengambil data alat dengan optional filtering (name, categoryId, condition, location) dan pagination"
    )
    public ResponseEntity<ApiResponse<List<LaboratoryEquipment>>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) LaboratoryEquipment.ConditionStatus condition,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        List<LaboratoryEquipment> equipments;

        if (name != null) {
            equipments = service.searchByName(name);
        } else if (categoryId != null) {
            equipments = service.searchByCategory(categoryId);
        } else if (condition != null) {
            equipments = service.searchByCondition(condition);
        } else if (location != null) {
            equipments = service.searchByLocation(location);
        } else if (page != null || size != null) {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            equipments = service.getAllWithPagination(p, s);
        } else {
            equipments = service.getAll();
        }

        String message = equipments.isEmpty()
                ? "Data alat laboratorium tidak ditemukan"
                : "Data alat laboratorium berhasil diambil";

        return ResponseEntity.ok(ApiResponse.success(message, equipments));
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