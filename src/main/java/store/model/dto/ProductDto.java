package store.model.dto;

import store.constants.ErrorConstants;
import store.constants.MessageConstants;

public class ProductDto {
    private String name;
    private int price;
    private int amount;
    private String promotion;
    private String EMPTY_MESSAGE;

    public ProductDto(String name, int price, int amount, String promotion) throws Exception {
        if (name == null || price <= 0 || amount <= 0) {
            throw ErrorConstants.INVALID_ITEM.getException();
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
                this.name,
                this.price,
                this.amount,
                promotionMessage
        );
    }
}
