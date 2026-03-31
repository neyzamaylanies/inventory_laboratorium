package project4_3fsd2.inventory_laboratorium.category.controller;

import project4_3fsd2.inventory_laboratorium.category.model.EquipmentCategory;
import project4_3fsd2.inventory_laboratorium.category.view.EquipmentCategoryService;
import project4_3fsd2.inventory_laboratorium.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        summary = "Mengambil daftar kategori",
        description = "Mengambil data kategori dengan optional filtering (name) dan pagination"
    )
    public ResponseEntity<ApiResponse<List<EquipmentCategory>>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        List<EquipmentCategory> categories;

        if (name != null) {
            categories = service.searchByName(name);
        } else if (page != null || size != null) {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            categories = service.getAllWithPagination(p, s);
        } else {
            categories = service.getAll();
        }

        String message = categories.isEmpty()
                ? "Data kategori tidak ditemukan"
                : "Data kategori berhasil diambil";

        return ResponseEntity.ok(ApiResponse.success(message, categories));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu kategori",
        description = "Mengambil detail satu kategori berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<EquipmentCategory>> get(@PathVariable String id) {
        EquipmentCategory category = service.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data kategori berhasil ditemukan", category));
    }

    @PostMapping
    @Operation(
        summary = "Membuat kategori baru",
        description = "Membuat satu data kategori baru ke dalam sistem."
    )
    public ResponseEntity<ApiResponse<EquipmentCategory>> create(@RequestBody EquipmentCategory category) {
        EquipmentCategory created = service.save(category);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Membuat kategori secara bulk",
        description = "Membuat banyak kategori baru dalam satu transaksi (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<List<EquipmentCategory>>> createBulk(@RequestBody List<EquipmentCategory> categories) {
        List<EquipmentCategory> created = service.saveBulk(categories);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bulk insert berhasil: " + created.size() + " data dibuat", created));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data kategori",
        description = "Memperbarui data kategori berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<EquipmentCategory>> update(
            @PathVariable String id,
            @RequestBody EquipmentCategory category) {
        EquipmentCategory updated = service.update(id, category);
        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menghapus kategori",
        description = "Menghapus satu kategori berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @DeleteMapping("/bulk")
    @Operation(
        summary = "Menghapus kategori secara bulk",
        description = "Menghapus banyak kategori berdasarkan daftar ID (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<Void>> deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
        return ResponseEntity.ok(
            ApiResponse.success("Bulk delete berhasil: " + ids.size() + " data dihapus", null)
        );
    }
}