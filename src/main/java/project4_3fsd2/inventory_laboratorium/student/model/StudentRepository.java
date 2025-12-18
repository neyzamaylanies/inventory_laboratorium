package project4_3fsd2.inventory_laboratorium.student.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByNameContainingIgnoreCase(String keyword);

    List<Student> findByStudyProgramContainingIgnoreCase(String studyProgram);

    boolean existsByNim(String nim);

    Optional<Student> findByNim(String nim);
    
    long countByIdIn(List<String> ids);
}