package project4_3fsd2.inventory_laboratorium.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import project4_3fsd2.inventory_laboratorium.ApiResponse;
import project4_3fsd2.inventory_laboratorium.auth.model.LoginRequest;
import project4_3fsd2.inventory_laboratorium.auth.model.LoginResponse;
import project4_3fsd2.inventory_laboratorium.security.JwtUtil;
import project4_3fsd2.inventory_laboratorium.user.model.User;
import project4_3fsd2.inventory_laboratorium.user.model.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Email atau password salah", null));
        }

        String rawPassword = request.getPassword();
        String dbPassword = user.getPassword();

        boolean isMatch;

        // 🔥 CASE 1: PASSWORD MASIH PLAINTEXT
        if (!dbPassword.startsWith("$2")) {
            isMatch = rawPassword.equals(dbPassword);

            if (isMatch) {
                // 🔥 AUTO HASH (MIGRATION)
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
            }

        } else {
            // 🔐 CASE 2: PASSWORD SUDAH HASH
            isMatch = passwordEncoder.matches(rawPassword, dbPassword);
        }

        if (!isMatch) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Email atau password salah", null));
        }

        String roleStr = user.getRole().name();
        String token = jwtUtil.generateToken(user.getEmail(), roleStr);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login berhasil",
                        new LoginResponse(
                                token,
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                roleStr
                        )
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Logout berhasil", null)
        );
    }
}