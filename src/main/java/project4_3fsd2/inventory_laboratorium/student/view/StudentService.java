package project4_3fsd2.inventory_laboratorium.student.view;

import project4_3fsd2.inventory_laboratorium.student.model.Student;
import project4_3fsd2.inventory_laboratorium.student.model.StudentRepository;
import project4_3fsd2.inventory_laboratorium.DataAlreadyExistsException;
import project4_3fsd2.inventory_laboratorium.DataNotFoundException;
import project4_3fsd2.inventory_laboratorium.InvalidDataException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public List<Student> getAllWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Student getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Student", id));
    }

    public Student getByNim(String nim) {
        return repository.findByNim(nim)
                .orElseThrow(() -> new DataNotFoundException("Student dengan NIM", nim));
    }

    public List<Student> searchByName(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Student> searchByStudyProgram(String studyProgram) {
        return repository.findByStudyProgramContainingIgnoreCase(studyProgram);
    }

    public Student save(Student student) {
        if (student.getId() == null || student.getId().isBlank()) {
            throw new InvalidDataException("Student", "id", "wajib diisi");
        }

        if (repository.existsById(student.getId())) {
            throw new DataAlreadyExistsException("Student", student.getId());
        }

        if (student.getNim() == null || student.getNim().isBlank()) {
            throw new InvalidDataException("Student", "nim", "wajib diisi");
        }

        if (repository.existsByNim(student.getNim())) {
            throw new InvalidDataException("Student", "nim", "'" + student.getNim() + "' sudah digunakan");
        }

        if (student.getName() == null || student.getName().isBlank()) {
            throw new InvalidDataException("Student", "name", "wajib diisi");
        }

        if (student.getStudyProgram() == null || student.getStudyProgram().isBlank()) {
            throw new InvalidDataException("Student", "studyProgram", "wajib diisi");
        }

        return repository.save(student);
    }

    @Transactional
    public List<Student> saveBulk(List<Student> students) {
        if (students == null || students.isEmpty()) {
            throw new InvalidDataException("Student", "list tidak boleh kosong");
        }

        if (students.size() > 100) {
            throw new InvalidDataException("Student", "maksimal 100 data per bulk insert");
        }

        for (Student student : students) {
            if (student.getId() == null || student.getId().isBlank()) {
                throw new InvalidDataException("Student", "id", "wajib diisi untuk setiap data");
            }

            if (repository.existsById(student.getId())) {
                throw new DataAlreadyExistsException("Student", student.getId());
            }

            if (student.getNim() == null || student.getNim().isBlank()) {
                throw new InvalidDataException("Student", "nim", "wajib diisi untuk setiap data");
            }

            if (repository.existsByNim(student.getNim())) {
                throw new InvalidDataException("Student", "nim", "'" + student.getNim() + "' sudah digunakan");
            }
        }

        return repository.saveAll(students);
    }

    public Student update(String id, Student updated) {
        Student existing = getById(id);

        // Update NIM hanya jika berbeda dan belum digunakan
        if (!existing.getNim().equals(updated.getNim())) {
            if (repository.existsByNim(updated.getNim())) {
                throw new InvalidDataException("Student", "nim", "'" + updated.getNim() + "' sudah digunakan");
            }
            existing.setNim(updated.getNim());
        }

        existing.setName(updated.getName());
        existing.setStudyProgram(updated.getStudyProgram());
        existing.setPhone(updated.getPhone());

        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new DataNotFoundException("Student", id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("Student", "list ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("Student", "maksimal 100 data per bulk delete");
        }

        long existingCount = repository.countByIdIn(ids);
        if (existingCount != ids.size()) {
            throw new InvalidDataException("Student", "sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        repository.deleteAllById(ids);
    }
}