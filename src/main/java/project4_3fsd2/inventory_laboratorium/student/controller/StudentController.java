package project4_3fsd2.inventory_laboratorium.student.controller;

import project4_3fsd2.inventory_laboratorium.student.model.Student;
import project4_3fsd2.inventory_laboratorium.student.view.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
    public List<Student> list(
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
        summary = "Mengambil detail satu mahasiswa", 
        description = "Mengambil detail satu mahasiswa berdasarkan ID."
    )
    public Student get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/nim/{nim}")
    @Operation(
        summary = "Mengambil mahasiswa berdasarkan NIM", 
        description = "Mengambil detail mahasiswa berdasarkan NIM."
    )
    public Student getByNim(@PathVariable String nim) {
        return service.getByNim(nim);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Mencari mahasiswa berdasarkan nama", 
        description = "Mencari mahasiswa berdasarkan kata kunci pada nama (case insensitive)."
    )
    public List<Student> search(@RequestParam String q) {
        return service.searchByName(q);
    }

    @GetMapping("/search/program")
    @Operation(
        summary = "Mencari mahasiswa berdasarkan program studi", 
        description = "Mencari mahasiswa berdasarkan program studi (case insensitive)."
    )
    public List<Student> searchByProgram(@RequestParam String program) {
        return service.searchByStudyProgram(program);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat data mahasiswa baru", 
        description = "Membuat satu data mahasiswa baru ke dalam sistem."
    )
    public Student create(@RequestBody Student student) {
        return service.save(student);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Membuat mahasiswa secara bulk", 
        description = "Membuat banyak data mahasiswa baru dalam satu transaksi (maksimal 100)."
    )
    public List<Student> createBulk(@RequestBody List<Student> students) {
        return service.saveBulk(students);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Memperbarui data mahasiswa", 
        description = "Memperbarui data mahasiswa berdasarkan ID."
    )
    public Student update(
            @PathVariable String id, 
            @RequestBody Student student) {
        return service.update(id, student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus mahasiswa", 
        description = "Menghapus satu data mahasiswa berdasarkan ID."
    )
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Menghapus mahasiswa secara bulk", 
        description = "Menghapus banyak data mahasiswa berdasarkan daftar ID (maksimal 100)."
    )
    public void deleteBulk(@RequestBody List<String> ids) {
        service.deleteBulk(ids);
    }
}