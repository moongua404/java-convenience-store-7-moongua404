package store.constants;

public enum MessageConstants {
    ITEM_MESSAGE("%s %,d원 %d개 %s");

    private String message;

    MessageConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
