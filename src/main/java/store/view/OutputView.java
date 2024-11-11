package store.view;

import store.constants.MessageConstants;
import store.model.Product;
import store.model.Promotion;

public class OutputView {
    String EMPTY_MARK = "";
    String AMOUNT_UNIT = "개";
    String PRICE_UNIT = "원";
    String EMPTY_AMOUNT = "재고 없음";

    public void printGuide(MessageConstants messageConstants) {
        System.out.println(messageConstants.getMessage());
    }

    public void printProducts(Product product, boolean hasPromotion) {
        String price = String.format("%,d", product.getPrice()) + PRICE_UNIT;
        String amount = formatAmount(product.getAmount());
        String promotion = formatPromotion(null);
        if (hasPromotion) {
            amount = formatAmount(product.getPromotionAmount());
            promotion = formatPromotion(product.getPromotion());
        }
        System.out.println(String.format(
                MessageConstants.ITEM_MESSAGE.getMessage(),
                product.getName(), price, amount, promotion
        ));
        if (hasPromotion) {
            printProducts(product, false);
        }
    }

    public void newLine() {
        System.out.println();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printReceiptHeader(String header) {
        System.out.println(String.format(
                MessageConstants.RECEIPT_DIVIDE_LINE.getMessage(),
                header
        ));
    }

    public void printReceiptItem(String first, String second, String third) {
        System.out.println(String.format(
                MessageConstants.RECEIPT_ITEM.getMessage(),
                first, second, third
        ));
    }

    private String formatAmount(int amount) {
        if (amount == 0) {
            return EMPTY_AMOUNT;
        }
        return String.valueOf(amount) + AMOUNT_UNIT;
    }

    private String formatPromotion(Promotion promotion) {
        if (promotion == null) {
            return EMPTY_MARK;
        }
        return promotion.getName();
    }
}
