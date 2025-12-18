package project4_3fsd2.inventory_laboratorium.category.view;

public class CategoryNotFoundException extends RuntimeException {
    
    public CategoryNotFoundException(String id) {
        super("Kategori dengan ID " + id + " tidak ditemukan");
    }
}