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
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal price; // Changed from Float to BigDecimal

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoffeeSize size; // Ensure matching enum

    @Column(nullable = false, updatable = false, insertable = false)
    private Timestamp createdAt;

    @Column(nullable = false, insertable = false)
    private Timestamp updatedAt;

    @Column(nullable = false)
    private Integer quantity; // Ensure it matches int(11)

    @Column(name = "imageUrl", length = 255) // Nullable removed for default handling
    private String imageUrl;

    public static final Finder<Integer, Coffee> find = new Finder<>(Coffee.class);

    // Getters and Setters with Null Safety
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return (name != null && !name.isEmpty()) ? name : "Unknown"; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return (description != null && !description.isEmpty()) ? description : "No description available"; }
    public void setDescription(String description) { this.description = description; }

    public String getPrice() { return (price != null) ? price.toString() : "0.00"; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public CoffeeSize getSize() { return size; }
    public void setSize(CoffeeSize size) { this.size = size; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Integer getQuantity() { return (quantity != null) ? quantity : 0; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getImageUrl() { return (imageUrl != null && !imageUrl.isEmpty()) ? imageUrl : "/assets/images/default.jpg"; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
