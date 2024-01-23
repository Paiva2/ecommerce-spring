package ecommerce.http.dtos.category;

import ecommerce.http.entities.Category;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewCategoryDto {
    @NotBlank(message = "name can't be empty")
    @NotNull(message = "name can't be null")
    @Length(min = 3, message = "Category name must have at least 3 characters")
    private String name;

    public Category toCategory() {
        return new Category(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
