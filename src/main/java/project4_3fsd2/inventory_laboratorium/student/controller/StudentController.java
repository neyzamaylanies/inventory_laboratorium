package project4_3fsd2.inventory_laboratorium.student.controller;

import project4_3fsd2.inventory_laboratorium.student.model.Student;
import project4_3fsd2.inventory_laboratorium.student.view.StudentService;
import project4_3fsd2.inventory_laboratorium.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student", description = "API untuk mengelola data mahasiswa")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "Mengambil daftar semua mahasiswa", 
        description = "Mengambil seluruh data mahasiswa yang tersedia di sistem. Mendukung pagination opsional melalui parameter page dan size."
    )
    public ResponseEntity<ApiResponse<List<Student>>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        List<Student> students;
        if (page == null && size == null) {
            students = service.getAll();
        } else {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            students = service.getAllWithPagination(p, s);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Data mahasiswa berhasil diambil", students));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail satu mahasiswa", 
        description = "Mengambil detail satu mahasiswa berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Student>> get(@PathVariable String id) {
        Student student = service.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data mahasiswa berhasil ditemukan", student));
    }

    @GetMapping("/nim/{nim}")
    @Operation(
        summary = "Mengambil mahasiswa berdasarkan NIM", 
        description = "Mengambil detail mahasiswa berdasarkan NIM."
    )
    public ResponseEntity<ApiResponse<Student>> getByNim(@PathVariable String nim) {
        Student student = service.getByNim(nim);
        return ResponseEntity.ok(ApiResponse.success("Data mahasiswa berhasil ditemukan", student));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Mencari mahasiswa berdasarkan nama", 
        description = "Mencari mahasiswa berdasarkan kata kunci pada nama (case insensitive)."
    )
    public ResponseEntity<ApiResponse<List<Student>>> search(@RequestParam String q) {
        List<Student> students = service.searchByName(q);
        return ResponseEntity.ok(ApiResponse.success("Pencarian berhasil", students));
    }

    @GetMapping("/search/program")
    @Operation(
        summary = "Mencari mahasiswa berdasarkan program studi", 
        description = "Mencari mahasiswa berdasarkan program studi (case insensitive)."
    )
    public ResponseEntity<ApiResponse<List<Student>>> searchByProgram(@RequestParam String program) {
        List<Student> students = service.searchByStudyProgram(program);
        return ResponseEntity.ok(ApiResponse.success("Pencarian berhasil", students));
    }

    @PostMapping
    @Operation(
        summary = "Membuat data mahasiswa baru", 
        description = "Membuat satu data mahasiswa baru ke dalam sistem."
    )
    public ResponseEntity<ApiResponse<Student>> create(@RequestBody Student student) {
        Student created = service.save(student);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Membuat mahasiswa secara bulk", 
        description = "Membuat banyak data mahasiswa baru dalam satu transaksi (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<List<Student>>> createBulk(@RequestBody List<Student> students) {
        List<Student> created = service.saveBulk(students);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bulk insert berhasil: " + created.size() + " data dibuat", created));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data mahasiswa", 
        description = "Memperbarui data mahasiswa berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Student>> update(
            @PathVariable String id, 
            @RequestBody Student student) {
        Student updated = service.update(id, student);
        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menghapus mahasiswa", 
        description = "Menghapus satu data mahasiswa berdasarkan ID."
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @DeleteMapping("/bulk")
    @Operation(
        summary = "Menghapus mahasiswa secara bulk", 
        description = "Menghapus banyak data mahasiswa berdasarkan daftar ID (maksimal 100)."
    )
    public ResponseEntity<ApiResponse<Void>> deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
        return ResponseEntity.ok(
            ApiResponse.success("Bulk delete berhasil: " + ids.size() + " data dihapus", null)
        );
    }
}