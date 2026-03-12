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
        summary = "Mengambil daftar mahasiswa",
        description = "Mengambil data mahasiswa dengan optional filtering (name, program, nim) dan pagination"
    )
    public ResponseEntity<ApiResponse<List<Student>>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String program,
            @RequestParam(required = false) String nim,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        List<Student> students;

        if (name != null) {
            students = service.searchByName(name);
        } else if (program != null) {
            students = service.searchByStudyProgram(program);
        } else if (nim != null) {
            Student student = service.getByNim(nim);
            students = student != null ? List.of(student) : List.of();
        } else if (page != null || size != null) {
            int p = (page != null && page >= 0) ? page : 0;
            int s = (size != null && size > 0) ? size : 10;
            students = service.getAllWithPagination(p, s);
        } else {
            students = service.getAll();
        }

        return ResponseEntity.ok(
            ApiResponse.success("Data mahasiswa berhasil diambil", students)
        );
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Mengambil detail mahasiswa",
        description = "Mengambil detail satu mahasiswa berdasarkan ID"
    )
    public ResponseEntity<ApiResponse<Student>> get(@PathVariable String id) {
        Student student = service.getById(id);
        return ResponseEntity.ok(
            ApiResponse.success("Data mahasiswa berhasil ditemukan", student)
        );
    }

    @PostMapping
    @Operation(
        summary = "Membuat mahasiswa baru",
        description = "Menambahkan satu data mahasiswa baru ke sistem"
    )
    public ResponseEntity<ApiResponse<Student>> create(@RequestBody Student student) {

        Student created = service.save(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Bulk insert mahasiswa",
        description = "Menambahkan banyak data mahasiswa sekaligus (maksimal 100)"
    )
    public ResponseEntity<ApiResponse<List<Student>>> createBulk(@RequestBody List<Student> students) {

        List<Student> created = service.saveBulk(students);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Bulk insert berhasil: " + created.size() + " data dibuat",
                        created
                ));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update mahasiswa",
        description = "Memperbarui data mahasiswa berdasarkan ID"
    )
    public ResponseEntity<ApiResponse<Student>> update(
            @PathVariable String id,
            @RequestBody Student student) {

        Student updated = service.update(id, student);

        return ResponseEntity.ok(
            ApiResponse.updated(updated)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Menghapus mahasiswa",
        description = "Menghapus satu data mahasiswa berdasarkan ID"
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {

        service.delete(id);

        return ResponseEntity.ok(
            ApiResponse.deleted()
        );
    }

    @DeleteMapping("/bulk")
    @Operation(
        summary = "Bulk delete mahasiswa",
        description = "Menghapus banyak mahasiswa sekaligus berdasarkan daftar ID"
    )
    public ResponseEntity<ApiResponse<Void>> deleteBulk(@RequestBody List<String> ids) {

        service.deleteBulk(ids);

        return ResponseEntity.ok(
            ApiResponse.success(
                    "Bulk delete berhasil: " + ids.size() + " data dihapus",
                    null
            )
        );
    }
}