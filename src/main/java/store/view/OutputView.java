package store.view;

import store.constants.MessageConstants;
import store.model.Product;

public class OutputView {
    String EMPTY_MARK = "";
    String AMOUNT_UNIT = "개";
    String PRICE_UNIT = "원";
    String EMPTY_AMOUNT = "재고 없음";

    public void printGuide(MessageConstants messageConstants) {
        System.out.println(messageConstants.getMessage());
    }

    public void printProducts(Product product) {
        String name = product.getName();
        String price = String.format("%,d", product.getPrice()) + PRICE_UNIT;
        boolean isAmountNull = (product.getAmount() == 0);
        String amount = replaceOnCondition(
                isAmountNull, String.valueOf(product.getAmount()) + AMOUNT_UNIT, EMPTY_AMOUNT);
        boolean isPromotionNull = (product.getPromotion() == null);
        String promotion = replaceOnCondition(isPromotionNull, product.getPromotion(), EMPTY_MARK);

        System.out.println(String.format(
                MessageConstants.ITEM_MESSAGE.getMessage(),
                name, price, amount, promotion
        ));
    }

    public void newLine() {
        System.out.println();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    private String replaceOnCondition(Boolean condition, String targetSentence, String replaceSentence) {
        if (condition) {
            return replaceSentence;
        }
        return targetSentence;
    }
}
