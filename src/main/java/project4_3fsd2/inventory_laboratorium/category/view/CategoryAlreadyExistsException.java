package project4_3fsd2.inventory_laboratorium.category.view;

public class CategoryAlreadyExistsException extends RuntimeException {
    
    public CategoryAlreadyExistsException(String id) {
        super("Kategori dengan ID " + id + " sudah ada");
    }
}