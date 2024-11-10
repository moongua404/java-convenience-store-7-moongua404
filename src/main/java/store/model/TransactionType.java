package store.model;

import store.constants.MessageConstants;

public enum TransactionType {
    ADD(MessageConstants.GET_PROMOTION_PRODUCT_REQUEST),
    SUB(MessageConstants.FULL_PRICE_PAYMENT_REQUEST);

    MessageConstants messageConstants;

    TransactionType(MessageConstants messageConstants) {
        this.messageConstants = messageConstants;
    }

    public MessageConstants getMessageConstants() {
        return messageConstants;
    }
}