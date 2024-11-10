package store.constants;

public enum MessageConstants {
    START_GUIDE_MESSAGE("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다."),
    ITEM_MESSAGE("- %s %s %s %s"),
    PROMOTE_MESSAGE("이름: %s, 구매: %d, 증정: %d + 행사 시작: %s, 행사 종료: %s"),
    PURCHASE_GUIDE_MESSAGE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    //"이름: " + name + ", 구매: " + buy + ", 증정: " + get + ", 행사 시작: " + startDate + ", 행사 종료: " + endDate;

    private String message;

    MessageConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
