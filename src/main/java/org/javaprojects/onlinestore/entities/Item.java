package org.javaprojects.onlinestore.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "img_path", nullable = false, length = 256)
    private String imgPath;


    private int count;

    // Constructors
    public Item() {}

    public Item(Long id, String title, String description, BigDecimal price, String imgPath, int count) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imgPath = imgPath;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public void decrementCount() {
        count = count - 1;
    }
    public void incrementCount() {
        count = count + 1;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int i) {
        this.count = i;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;

        if (!id.equals(item.id)) return false;
        if (!title.equals(item.title)) return false;
        if (!description.equals(item.description)) return false;
        if (!price.equals(item.price)) return false;
        return imgPath.equals(item.imgPath);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + imgPath.hashCode();
        return result;
    }


}
