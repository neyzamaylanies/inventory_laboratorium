package project4_3fsd2.inventory_laboratorium.equipment.controller;

import project4_3fsd2.inventory_laboratorium.equipment.model.LaboratoryEquipment;
import project4_3fsd2.inventory_laboratorium.equipment.view.LaboratoryEquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
    public List<LaboratoryEquipment> list(
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
        summary = "Mengambil detail satu alat", 
        description = "Mengambil detail satu alat laboratorium berdasarkan ID."
    )
    public LaboratoryEquipment get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Mencari alat berdasarkan nama", 
        description = "Mencari alat berdasarkan kata kunci pada nama alat (case insensitive)."
    )
    public List<LaboratoryEquipment> search(@RequestParam String q) {
        return service.searchByName(q);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(
        summary = "Mencari alat berdasarkan kategori", 
        description = "Mengambil semua alat yang termasuk dalam kategori tertentu."
    )
    public List<LaboratoryEquipment> searchByCategory(@PathVariable String categoryId) {
        return service.searchByCategory(categoryId);
    }

    @GetMapping("/condition/{status}")
    @Operation(
        summary = "Mencari alat berdasarkan kondisi", 
        description = "Mengambil semua alat dengan status kondisi tertentu (BAIK, RUSAK_RINGAN, RUSAK_BERAT, DALAM_PERBAIKAN)."
    )
    public List<LaboratoryEquipment> searchByCondition(@PathVariable LaboratoryEquipment.ConditionStatus status) {
        return service.searchByCondition(status);
    }

    @GetMapping("/location")
    @Operation(
        summary = "Mencari alat berdasarkan lokasi", 
        description = "Mencari alat berdasarkan lokasi penyimpanan (case insensitive)."
    )
    public List<LaboratoryEquipment> searchByLocation(@RequestParam String location) {
        return service.searchByLocation(location);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat data alat baru", 
        description = "Membuat satu data alat laboratorium baru ke dalam sistem."
    )
    public LaboratoryEquipment create(@RequestBody LaboratoryEquipment equipment) {
        return service.save(equipment);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat alat secara bulk", 
        description = "Membuat banyak data alat baru dalam satu transaksi (maksimal 100)."
    )
    public List<LaboratoryEquipment> createBulk(@RequestBody List<LaboratoryEquipment> equipments) {
        return service.saveBulk(equipments);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data alat", 
        description = "Memperbarui data alat laboratorium berdasarkan ID."
    )
    public LaboratoryEquipment update(
            @PathVariable String id, 
            @RequestBody LaboratoryEquipment equipment) {
        return service.update(id, equipment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus alat", 
        description = "Menghapus satu data alat laboratorium berdasarkan ID."
    )
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus alat secara bulk", 
        description = "Menghapus banyak data alat berdasarkan daftar ID (maksimal 100)."
    )
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }
}