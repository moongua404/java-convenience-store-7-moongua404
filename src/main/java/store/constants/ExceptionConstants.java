package store.constants;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public enum ExceptionConstants {
    INVALID_FILE_IO(IOException.class, "파일 입출력 과정에서 문제가 생겼습니다."),
    INVALID_ITEM(IllegalStateException.class, "상품을 등록할 수 없습니다."),
    INVALID_PROMOTION(IllegalStateException.class, "프로모션을 등록할 수 없습니다."),
    DUPLICATED_PRODUCT(IllegalArgumentException.class, "중복된 상품은 입력될 수 없습니다."),
    DUPLICATED_ITEM(IllegalStateException.class, "중복된 아이템이 있습니다."),

    INVALID_INPUT(IllegalArgumentException.class, "올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_NUMBER_FORMAT(NumberFormatException.class, "잘못된 숫자 입력 형식입니다."),
    INVALID_DATE_FORMAT(DateTimeParseException.class, "잘못된 날짜 입력 형식입니다."),
    UNAVAILABLE_PRODUCT(IllegalArgumentException.class, "존재하지 않는 상품입니다. 다시 입력해 주세요."),
    EXCEEDED_AMOUNT(IllegalArgumentException.class, "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    private final Class<? extends Exception> exception;
    private final String message;

    ExceptionConstants(Class<? extends Exception> exception, String message) {
        this.exception = exception;
        this.message = "[ERROR]" + message;
    }

    public Exception getException() {
        try {
            return this.exception.getConstructor(String.class).newInstance(message);
        } catch (Exception exception) {
            throw new RuntimeException("[ERROR] 예외처리 과정에서 문제가 생겼습니다. ");
        }
    }
}
