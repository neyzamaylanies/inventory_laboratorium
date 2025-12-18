package project4_3fsd2.inventory_laboratorium.category.controller;

import project4_3fsd2.inventory_laboratorium.category.model.EquipmentCategory;
import project4_3fsd2.inventory_laboratorium.category.view.EquipmentCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Equipment Category", description = "API untuk mengelola kategori alat laboratorium")
public class EquipmentCategoryController {

    private final EquipmentCategoryService service;

    public EquipmentCategoryController(EquipmentCategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Mengambil daftar semua kategori", 
        description = "Mengambil seluruh data kategori alat laboratorium yang tersedia di sistem. Mendukung pagination opsional melalui parameter page dan size."
    )
    public List<EquipmentCategory> list(
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
        summary = "Mengambil detail satu kategori", 
        description = "Mengambil detail satu kategori berdasarkan ID."
    )
    public EquipmentCategory get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Mencari kategori berdasarkan nama", 
        description = "Mencari kategori berdasarkan kata kunci pada nama kategori (case insensitive)."
    )
    public List<EquipmentCategory> search(@RequestParam String q) {
        return service.searchByName(q);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat kategori baru", 
        description = "Membuat satu data kategori baru ke dalam sistem."
    )
    public EquipmentCategory create(@RequestBody EquipmentCategory category) {
        return service.save(category);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat kategori secara bulk", 
        description = "Membuat banyak kategori baru dalam satu transaksi (maksimal 100)."
    )
    public List<EquipmentCategory> createBulk(@RequestBody List<EquipmentCategory> categories) {
        return service.saveBulk(categories);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data kategori", 
        description = "Memperbarui data kategori berdasarkan ID."
    )
    public EquipmentCategory update(
            @PathVariable String id, 
            @RequestBody EquipmentCategory category) {
        return service.update(id, category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus kategori", 
        description = "Menghapus satu kategori berdasarkan ID."
    )
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus kategori secara bulk", 
        description = "Menghapus banyak kategori berdasarkan daftar ID (maksimal 100)."
    )
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }
}