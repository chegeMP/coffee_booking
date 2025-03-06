package models;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "coffee") // Ensure lowercase if your table name is lowercase
public class Coffee extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false, length = 100)
    public String name;

    @Column(columnDefinition = "TEXT")
    public String description;

    @Column(nullable = false, precision = 6, scale = 2)
    public BigDecimal price; // Changed from Float to BigDecimal

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public CoffeeSize size; // Ensure matching enum

    @Column(nullable = false, updatable = false, insertable = false)
    public Timestamp createdAt;

    @Column(nullable = false, insertable = false)
    public Timestamp updatedAt;

    @Column(nullable = false)
    public Integer quantity; // Ensure it matches int(11)


    @Column(name = "imageUrl", nullable = true, length = 255) // New column for image URL
    public String imageUrl;

    public static final Finder<Integer, Coffee> find = new Finder<>(Coffee.class);

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public CoffeeSize getSize() { return size; }
    public void setSize(CoffeeSize size) { this.size = size; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }


    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
