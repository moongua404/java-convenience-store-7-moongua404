package store.model;

import store.model.dto.ProductDto;

public class Product {
    private String name;
    private int price;
    private int amount;
    private String promotion;

    public Product(ProductDto productDto) {
        this.name = productDto.getName();
        this.price = productDto.getPrice();
        this.amount = productDto.getAmount();
        this.promotion = productDto.getPromotion();
    }

    public Product(String name, int price, int amount, String promotion) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String getPromotion() {
        return promotion;
    }
}
