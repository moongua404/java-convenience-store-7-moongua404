package store.model.dto;

public class ProductDto {
    private String name;
    private int price;
    private int amount;
    private String promotion;

    public ProductDto(String name, int price, int amount, String promotion) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.promotion = promotion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPromotion(String promotion) {
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
