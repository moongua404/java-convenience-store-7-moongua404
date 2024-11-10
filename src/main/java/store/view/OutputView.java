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
        String price = String.format("%,d", product.getPrice()) + PRICE_UNIT;
        String amount = formatPrice(product);
        String promotion = formatPromotion(product);

        System.out.println(String.format(
                MessageConstants.ITEM_MESSAGE.getMessage(),
                product.getName(), price, amount, promotion
        ));
    }

    public void newLine() {
        System.out.println();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    private String formatPrice(Product product) {
        if (product.getAmount() == 0) {
            return EMPTY_AMOUNT;
        }
        return String.valueOf(product.getAmount()) + AMOUNT_UNIT;
    }

    private String formatPromotion(Product product) {
        if (product.getPromotion() == null) {
            return EMPTY_MARK;
        }
        return product.getPromotion().getName();
    }
}
