package project4_3fsd2.inventory_laboratorium;

import java.time.LocalDateTime;

/**
 * Wrapper response untuk API
 * Digunakan untuk memberikan format response yang konsisten
 */
public class ApiResponse<T> {
    
    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;
    
    // Constructor untuk success response
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    // Static factory methods untuk kemudahan
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operasi berhasil", data);
    }
    
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, "Data berhasil dibuat", data);
    }
    
    public static <T> ApiResponse<T> updated(T data) {
        return new ApiResponse<>(true, "Data berhasil diperbarui", data);
    }
    
    public static ApiResponse<Void> deleted() {
        return new ApiResponse<>(true, "Data berhasil dihapus", null);
    }
    
    // Getters
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public T getData() {
        return data;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}