package store.model;

import store.model.dto.PurchaseDto;
import store.model.dto.ReceiptDto;
import store.model.dto.ReceiptDto.PurchaseType;

public class Purchase {
    private String name;
    private int amount;
    private Product product;

    private final int ZERO = 0;

    public Purchase(String name, int amount, Product product) {
        this.name = name;
        this.amount = amount;
        this.product = product;
    }

    public Purchase(PurchaseDto purchaseDto, Product product) {
        this.name = purchaseDto.getName();
        this.amount = purchaseDto.getAmount();
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public ReceiptDto commit() {
        Promotion promotion = product.getPromotion();
        if (promotion != null) {
            return subOnPromotion(promotion);
        }
        return subOnMembership();
    }

    public void addAmount(int value) {
        this.amount += value;
    }

    private ReceiptDto subOnPromotion(Promotion promotion) {
        int bonus = countPromotionGet(product.getPromotionAmount(), this.amount, promotion.getBundle());
        if (product.getPromotionAmount() <= this.amount) {
            int amount = product.getPromotionAmount();
            product.addAmount(-amount, true);
            product.addAmount(-(product.getAmount() - amount), false);
            return makeReceiptDto(this.getName(), this.product.getPrice(), this.amount, bonus, false);
        }
        product.addAmount(-bonus * promotion.getBundle(), true);
        product.addAmount(-(this.amount - bonus * promotion.getBundle()), false);
        return makeReceiptDto(this.getName(), this.product.getPrice(), this.amount, bonus, false);
    }

    private ReceiptDto subOnMembership() {
        product.addAmount(-this.amount, false);
        return makeReceiptDto(this.getName(), this.product.getPrice(), this.amount, ZERO, true);
    }

    private ReceiptDto makeReceiptDto(String name, int price, int amount, int bonus, boolean isMembership) {
        if (isMembership) {
            return new ReceiptDto(PurchaseType.MEMBERSHIP, name, amount, price, ZERO);
        }
        if (bonus == ZERO) {
            return new ReceiptDto(PurchaseType.FULL_PRICE, name, amount, price, ZERO);
        }
        return new ReceiptDto(PurchaseType.PROMOTION, name, amount, price, bonus);
    }

    private int countPromotionGet(int promotionAmount, int amount, int bundle) {
        if (promotionAmount < amount) {
            return promotionAmount / bundle;
        }
        return amount / bundle;
    }
}
