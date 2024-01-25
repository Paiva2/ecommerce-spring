package ecommerce.http.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicUpdate(true)
public class Product {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column()
    private String name;

    @Column()
    private Double price;

    @Column(nullable = true, name = "price_on_sale")
    private Double priceOnSale;

    @Column()
    private String description;

    @Column()
    private String colors;

    @Column()
    private String sizes;

    @Column(name = "is_on_sale")
    private Boolean isOnSale = false;

    @Column(name = "active")
    private Boolean active = true;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category")
    private Category category;

    @Transient
    private String categoryId;

    @Transient
    private String categoryName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product() {}

    // Repository
    public Product(UUID id, String name, Double price, Double priceOnSale, String description,
            String colors, String sizes, Boolean isOnSale, Boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.priceOnSale = priceOnSale;
        this.description = description;
        this.colors = colors;
        this.sizes = sizes;
        this.isOnSale = isOnSale;
        this.active = active;
    }

    // Creation default
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

    // Creation DTO
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

    // Update DTO
    public Product(UUID id, String name, Double price, Double priceOnSale, String description,
            String colors, String sizes, Boolean isOnSale, String categoryId, Boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.priceOnSale = priceOnSale;
        this.description = description;
        this.colors = colors;
        this.sizes = sizes;
        this.isOnSale = isOnSale;
        this.categoryId = categoryId;
        this.active = active;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
