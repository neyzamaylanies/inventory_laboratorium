package project4_3fsd2.inventory_laboratorium.user.controller;

import project4_3fsd2.inventory_laboratorium.user.model.User;
import project4_3fsd2.inventory_laboratorium.user.model.User.UserRole;
import project4_3fsd2.inventory_laboratorium.user.view.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
        summary = "Mengambil daftar semua user", 
        description = "Mengambil seluruh data user yang tersedia di sistem. Mendukung pagination opsional melalui parameter page dan size."
    )
    public List<User> list(
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
        summary = "Mengambil detail satu user", 
        description = "Mengambil detail satu user berdasarkan ID."
    )
    public User get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/email/{email}")
    @Operation(
        summary = "Mencari user berdasarkan email", 
        description = "Mencari user berdasarkan alamat email."
    )
    public User getByEmail(@PathVariable String email) {
        return service.getByEmail(email);
    }

    @GetMapping("/role/{role}")
    @Operation(
        summary = "Mencari user berdasarkan role", 
        description = "Mencari user berdasarkan role (ADMIN atau PETUGAS)."
    )
    public List<User> getByRole(@PathVariable UserRole role) {
        return service.getByRole(role);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Mencari user berdasarkan nama", 
        description = "Mencari user berdasarkan kata kunci pada nama (case insensitive)."
    )
    public List<User> search(@RequestParam String q) {
        return service.searchByName(q);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat user baru", 
        description = "Membuat satu data user baru ke dalam sistem."
    )
    public User create(@RequestBody User user) {
        return service.save(user);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat user secara bulk", 
        description = "Membuat banyak user baru dalam satu transaksi (maksimal 100)."
    )
    public List<User> createBulk(@RequestBody List<User> users) {
        return service.saveBulk(users);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data user", 
        description = "Memperbarui data user berdasarkan ID."
    )
    public User update(
            @PathVariable String id, 
            @RequestBody User user) {
        return service.update(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus user", 
        description = "Menghapus satu user berdasarkan ID."
    )
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus user secara bulk", 
        description = "Menghapus banyak user berdasarkan daftar ID (maksimal 100)."
    )
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }
}