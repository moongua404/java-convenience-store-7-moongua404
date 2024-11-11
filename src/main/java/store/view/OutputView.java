package store.view;

import store.constants.MessageConstants;
import store.model.Product;
import store.model.Promotion;

public class OutputView {
    private final String EMPTY_MARK = "";
    private static final String AMOUNT_UNIT = "개";
    private static final String PRICE_UNIT = "원";
    private static final String EMPTY_AMOUNT = "재고 없음";
    private static final String PRICE_FORMAT = "%,d";
    private static final String STORE_NAME = "W 편의점";
    private static final String PROMOTION_HEADER = "증\t정";
    private static final String ITEM_LABEL = "상품명";
    private static final String AMOUNT_LABEL = "수량";
    private static final String PRICE_LABEL = "금액";
    private static final String RECEIPT_SEPARATOR = "=====";
    private static final String TOTAL_PURCHASE_LABEL = "총구매액";
    private static final String PROMOTION_DISCOUNT_LABEL = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT_LABEL = "멤버십할인";
    private static final String FINAL_PAYMENT_LABEL = "내실돈";

    public void printGuide(MessageConstants messageConstants) {
        System.out.println(messageConstants.getMessage());
    }

    public void printProducts(Product product, boolean hasPromotion) {
        String price = String.format(PRICE_FORMAT, product.getPrice()) + PRICE_UNIT;
        if (hasPromotion) {
            printProductWithPromotion(product, price);
            return;
        }
        printProductWithoutPromotion(product, price);
    }

    private void printProductWithPromotion(Product product, String price) {
        String amount = formatAmount(product.getPromotionAmount());
        String promotion = formatPromotion(product.getPromotion());
        System.out.println(String.format(
                MessageConstants.ITEM_MESSAGE.getMessage(),
                product.getName(), price, amount, promotion
        ));
        printProducts(product, false);
    }

    private void printProductWithoutPromotion(Product product, String price) {
        String amount = formatAmount(product.getAmount());
        String promotion = formatPromotion(null);

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

    public void printReceiptStart() {
        printReceiptHeader(STORE_NAME);
        printReceiptItem(ITEM_LABEL, AMOUNT_LABEL, PRICE_LABEL);
    }

    public void printReceiptPromotionHeader() {
        printReceiptHeader(PROMOTION_HEADER);
    }

    private void printReceiptHeader(String header) {
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

    public void printReceiptResult(int totalPrice, int totalCount, int promotePrice, int membershipPrice) {
        printReceiptHeader(RECEIPT_SEPARATOR);
        printReceiptItem(TOTAL_PURCHASE_LABEL, Integer.toString(totalCount), Integer.toString(totalPrice));
        printReceiptItem(PROMOTION_DISCOUNT_LABEL, EMPTY_MARK, Integer.toString(-promotePrice));
        printReceiptItem(MEMBERSHIP_DISCOUNT_LABEL, EMPTY_MARK, Integer.toString(-membershipPrice));
        printReceiptItem(FINAL_PAYMENT_LABEL, EMPTY_MARK,
                String.format(PRICE_FORMAT, totalPrice - promotePrice - membershipPrice));
    }

    private String formatAmount(int amount) {
        if (amount == 0) {
            return EMPTY_AMOUNT;
        }
        return amount + AMOUNT_UNIT;
    }

    private String formatPromotion(Promotion promotion) {
        if (promotion == null) {
            return EMPTY_MARK;
        }
        return promotion.getName();
    }
}
