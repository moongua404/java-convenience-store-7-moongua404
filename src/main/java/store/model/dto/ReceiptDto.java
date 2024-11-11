package store.model.dto;

public class ReceiptDto {
    private String name;
    private int amount;
    private int bonus;
    private int price;
    private PurchaseType type;

    public enum PurchaseType {
        FULL_PRICE,
        PROMOTION,
        MEMBERSHIP
    }

    public ReceiptDto(PurchaseType type, String name, int amount, int price, int bonus) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public PurchaseType getType() {
        return type;
    }

    public int getBonus() {
        return bonus;
    }
}