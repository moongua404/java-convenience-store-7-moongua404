package store.constants;

public enum MessageConstants {
    START_GUIDE_MESSAGE("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다."),
    ITEM_MESSAGE("- %s %s %s %s"),
    PROMOTE_MESSAGE("이름: %s, 구매: %d, 증정: %d + 행사 시작: %s, 행사 종료: %s"),
    PURCHASE_GUIDE_MESSAGE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    GET_PROMOTION_PRODUCT_REQUEST("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    FULL_PRICE_PAYMENT_REQUEST("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    MEMBERSHIP_DISCOUNT_REQUEST("\n멤버십 할인을 받으시겠습니까? (Y/N)"),
    RECEIPT_DIVIDE_LINE("==============%s================"),
    RECEIPT_ITEM("%-15s %-10s %-15s"),
    SHOPPING_CONTINUE("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");

    private final String message;

    MessageConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
