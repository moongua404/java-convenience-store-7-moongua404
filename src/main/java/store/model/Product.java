package store.model;

import store.model.dto.ProductDto;

public class Product {
    private final String name;
    private final int price;
    private int amount;
    private int promotionAmount;
    private final Promotion promotion;

    public Product(ProductDto productDto, Promotion promotion) {
        this.name = productDto.getName();
        this.price = productDto.getPrice();
        this.promotionAmount = productDto.getAmount();
        this.promotion = promotion;
        if (promotion == null) {
            this.promotionAmount = 0;
            this.amount = productDto.getAmount();
        }
    }

    public Product(String name, int price, int amount, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.promotionAmount = amount;
        this.promotion = promotion;
        if (promotion == null) {
            this.promotionAmount = 0;
            this.amount = amount;
        }
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

    public int getTotalAmount() {
        return amount + promotionAmount;
    }

    public int getPromotionAmount() {
        return promotionAmount;
    }

    public void addAmount(int amount, boolean isPromotion) {
        if (isPromotion) {
            promotionAmount += amount;
            return;
        }
        this.amount += amount;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
