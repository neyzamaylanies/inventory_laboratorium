package project4_3fsd2.inventory_laboratorium.user.controller;

import project4_3fsd2.inventory_laboratorium.user.model.User;
import project4_3fsd2.inventory_laboratorium.user.model.User.UserRole;
import project4_3fsd2.inventory_laboratorium.user.view.UserService;
import project4_3fsd2.inventory_laboratorium.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "API untuk mengelola user (Admin & Petugas)")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Mengambil daftar user",
        description = "Mengambil data user dengan optional filtering (name, role, email) dan pagination"
    )
    public ResponseEntity<ApiResponse<List<User>>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        List<User> users;

        if (name != null) {
            users = service.searchByName(name);
        } else if (role != null) {
            users = service.getByRole(role);
        } else if (email != null) {
            User user = service.getByEmail(email);
            users = user != null ? List.of(user) : List.of();
        } else if (page != null || size != null) {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            users = service.getAllWithPagination(p, s);
        } else {
            users = service.getAll();
        }

        String message = users.isEmpty()
                ? "Data user tidak ditemukan"
                : "Data user berhasil diambil";

        return ResponseEntity.ok(ApiResponse.success(message, users));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu user",
        description = "Mengambil detail satu user berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<User>> get(@PathVariable String id) {
        User user = service.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data user berhasil ditemukan", user));
    }

    @PostMapping
    @Operation(
        summary = "Membuat user baru",
        description = "Membuat satu data user baru ke dalam sistem."
    )
    public ResponseEntity<ApiResponse<User>> create(@RequestBody User user) {
        User created = service.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Membuat user secara bulk",
        description = "Membuat banyak user baru dalam satu transaksi (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<List<User>>> createBulk(@RequestBody List<User> users) {
        List<User> created = service.saveBulk(users);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bulk insert berhasil: " + created.size() + " data dibuat", created));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data user",
        description = "Memperbarui data user berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<User>> update(
            @PathVariable String id,
            @RequestBody User user) {
        User updated = service.update(id, user);
        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menghapus user",
        description = "Menghapus satu user berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @DeleteMapping("/bulk")
    @Operation(
        summary = "Menghapus user secara bulk",
        description = "Menghapus banyak user berdasarkan daftar ID (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<Void>> deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
        return ResponseEntity.ok(
            ApiResponse.success("Bulk delete berhasil: " + ids.size() + " data dihapus", null)
        );
    }
}