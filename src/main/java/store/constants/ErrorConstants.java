package store.constants;

import java.io.IOException;

public enum ErrorConstants {
    INVALID_FILE_IO(IOException.class, "파일 입출력 과정에서 문제가 생겼습니다. "),
    INVALID_INPUT(IllegalArgumentException.class, "올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    UNAVAILABLE_PRODUCT(IllegalArgumentException.class,"존재하지 않는 상품입니다. 다시 입력해 주세요.");

    private final Class<? extends Exception> exception;
    private final String message;

    ErrorConstants(Class<? extends Exception> exception, String message) {
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
