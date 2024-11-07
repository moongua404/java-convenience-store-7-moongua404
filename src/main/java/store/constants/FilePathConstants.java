package store.constants;

public enum FilePathConstants {
    ITEM_FILE_PATH("src/main/resources/products.md"),
    EVENT_FILE_PATH("src/main/resources/promotions.md");

    final String path;

    FilePathConstants(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
