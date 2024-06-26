package Model;

public class Category {
    private int category_id;
    private String category_name;
    private String username;

    public Category() {
    }

    public Category(int category_id, String category_name, String username) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.username = username;
    }
    public Category(int categoryId, String categoryName) {
        this.category_id = categoryId;
        this.category_name = categoryName;
    }

    public Category(int categoryId, Category categoryName) {
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
