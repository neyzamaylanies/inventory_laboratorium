package project4_3fsd2.inventory_laboratorium.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import project4_3fsd2.inventory_laboratorium.user.model.User.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // Cari user berdasarkan email
    Optional<User> findByEmail(String email);

    // Cek apakah email sudah ada
    boolean existsByEmail(String email);

    // Cari user berdasarkan nama (case insensitive)
    List<User> findByNameContainingIgnoreCase(String keyword);

    // Cari user berdasarkan role
    List<User> findByRole(UserRole role);

    // Hitung user berdasarkan ID list
    long countByIdIn(List<String> ids);
}