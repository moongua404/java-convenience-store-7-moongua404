package store.model.dto;

import store.constants.ExceptionConstants;
import store.constants.MessageConstants;

public class ProductDto {
    private final String name;
    private final int price;
    private final int amount;
    private final String promotion;

    private static final String EMPTY_MESSAGE = "";

    public ProductDto(String name, int price, int amount, String promotion) throws Exception {
        if (name == null || price <= 0 || amount <= 0) {
            throw ExceptionConstants.INVALID_ITEM.getException();
        }
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        String promotionMessage = promotion;
        if (promotion == null) {
            promotionMessage = EMPTY_MESSAGE;
        }
        return String.format(
                MessageConstants.ITEM_MESSAGE.getMessage(),
                this.name, this.price, this.amount, promotionMessage
        );
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
