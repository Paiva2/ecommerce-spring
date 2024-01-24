package ecommerce.http.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.CascadeType;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true, name = "price_on_sale")
    private Double priceOnSale;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String colors;

    @Column(nullable = false)
    private String sizes;

    @Column(nullable = false, name = "is_on_sale")
    private Boolean isOnSale = false;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(nullable = false, name = "category")
    private Category category;

    @Transient
    private String categoryId;

    @Transient
    private String categoryName;

    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    public Product() {}

    // Repository
    public Product(UUID id, String name, Double price, Double priceOnSale, String description,
            String colors, String sizes, Boolean isOnSale) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.priceOnSale = priceOnSale;
        this.description = description;
        this.colors = colors;
        this.sizes = sizes;
        this.isOnSale = isOnSale;
    }

    // Creation
    public Product(String name, Double price, Double priceOnSale, String description, String colors,
            String sizes, Boolean isOnSale, Category category) {
        this.name = name;
        this.price = price;
        this.priceOnSale = priceOnSale;
        this.description = description;
        this.colors = colors;
        this.sizes = sizes;
        this.isOnSale = isOnSale;
        this.category = category;
    }

    // Dto
    public Product(String name, Double price, Double priceOnSale, String description, String colors,
            String sizes, Boolean isOnSale, String categoryId) {
        this.name = name;
        this.price = price;
        this.priceOnSale = priceOnSale;
        this.description = description;
        this.colors = colors;
        this.sizes = sizes;
        this.isOnSale = isOnSale;
        this.categoryId = categoryId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getPriceOnSale() {
        return priceOnSale;
    }

    public void setPriceOnSale(Double priceOnSale) {
        this.priceOnSale = priceOnSale;
    }

    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Boolean isOnSale) {
        this.isOnSale = isOnSale;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
