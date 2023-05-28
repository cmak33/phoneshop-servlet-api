package com.es.phoneshop.model.product;

import com.es.phoneshop.model.entity.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class Product implements Entity<Long> {

    private Long id;
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceChange> priceHistory;

    public Product() {
        id = generateId();
    }

    private Product(ProductBuilder productBuilder) {
        id = generateId();
        this.code = productBuilder.code;
        this.description = productBuilder.description;
        this.price = productBuilder.price;
        this.currency = productBuilder.currency;
        this.stock = productBuilder.stock;
        this.imageUrl = productBuilder.imageUrl;
        this.priceHistory = productBuilder.priceHistory;
    }

    public static class ProductBuilder {

        private String code;
        private String description;
        private BigDecimal price;
        private Currency currency;
        private int stock;
        private String imageUrl;
        private List<PriceChange> priceHistory;

        public ProductBuilder setCode(String code) {
            this.code = code;
            return this;
        }

        public ProductBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder setCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public ProductBuilder setStock(int stock) {
            this.stock = stock;
            return this;
        }

        public ProductBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductBuilder setPriceHistory(List<PriceChange> priceHistory) {
            this.priceHistory = priceHistory;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    private Long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}