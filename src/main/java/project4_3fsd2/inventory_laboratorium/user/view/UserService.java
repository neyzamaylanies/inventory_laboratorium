package project4_3fsd2.inventory_laboratorium.user.view;

import project4_3fsd2.inventory_laboratorium.user.model.User;
import project4_3fsd2.inventory_laboratorium.user.model.User.UserRole;
import project4_3fsd2.inventory_laboratorium.user.model.UserRepository;
import project4_3fsd2.inventory_laboratorium.DataAlreadyExistsException;
import project4_3fsd2.inventory_laboratorium.DataNotFoundException;
import project4_3fsd2.inventory_laboratorium.InvalidDataException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public List<User> getAllWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }

    public User getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User", id));
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User", "email: " + email));
    }

    public List<User> getByRole(UserRole role) {
        return repository.findByRole(role);
    }

    public List<User> searchByName(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            throw new InvalidDataException("User", "id", "wajib diisi");
        }

        validateIdFormat(user.getId(), user.getRole());

        if (repository.existsById(user.getId())) {
            throw new DataAlreadyExistsException("User", user.getId());
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidDataException("User", "email", "wajib diisi");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new InvalidDataException("User", "email", "'" + user.getEmail() + "' sudah digunakan");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new InvalidDataException("User", "password", "wajib diisi");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidDataException("User", "name", "wajib diisi");
        }

        return repository.save(user);
    }

    @Transactional
    public List<User> saveBulk(List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new InvalidDataException("User", "list tidak boleh kosong");
        }

        if (users.size() > 100) {
            throw new InvalidDataException("User", "maksimal 100 data per bulk insert");
        }

        for (User user : users) {
            if (user.getId() == null || user.getId().isBlank()) {
                throw new InvalidDataException("User", "id", "wajib diisi untuk setiap data");
            }

            validateIdFormat(user.getId(), user.getRole());

            if (repository.existsById(user.getId())) {
                throw new DataAlreadyExistsException("User", user.getId());
            }

            if (repository.existsByEmail(user.getEmail())) {
                throw new InvalidDataException("User", "email", "'" + user.getEmail() + "' sudah digunakan");
            }
        }

        return repository.saveAll(users);
    }

    public User update(String id, User updated) {
        User existing = getById(id);

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword());
        }
        
        existing.setRole(updated.getRole());

        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new DataNotFoundException("User", id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidDataException("User", "list ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new InvalidDataException("User", "maksimal 100 data per bulk delete");
        }

        long existingCount = repository.countByIdIn(ids);
        if (existingCount != ids.size()) {
            throw new InvalidDataException("User", "sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        repository.deleteAllById(ids);
    }

    private void validateIdFormat(String id, UserRole role) {
        if (role == UserRole.ADMIN && !id.startsWith("ADM")) {
            throw new InvalidDataException("User", "id", "Admin harus dimulai dengan 'ADM' (contoh: ADM001)");
        }
        if (role == UserRole.PETUGAS && !id.startsWith("EMP")) {
            throw new InvalidDataException("User", "id", "Petugas harus dimulai dengan 'EMP' (contoh: EMP001)");
        }
    }
}