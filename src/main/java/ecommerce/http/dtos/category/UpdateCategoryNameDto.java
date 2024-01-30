package ecommerce.http.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import ecommerce.http.entities.Category;

public class UpdateCategoryNameDto {
    @NotBlank(message = "id can't be empty.")
    @NotNull(message = "id can't be null")
    @org.hibernate.validator.constraints.UUID(message = "id must be an valid UUID")
    private String id;

    @NotNull(message = "newName can't be null")
    @NotBlank(message = "newName can't be empty")
    private String newName;

    public UpdateCategoryNameDto() {}

    public Category toCategory() {
        Category category = new Category();

        category.setId(UUID.fromString(id));
        category.setName(newName);

        return category;
    }



    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
