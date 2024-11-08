package store.view;

import store.constants.MessageConstants;
import store.model.Product;

public class OutputView {
    public void printGuide(MessageConstants messageConstants) {
        System.out.println(messageConstants.getMessage());
    }

    public void printProducts(Product product) {
        System.out.println(product.toString());
        // ...
    }

    public void newLine() {
        System.out.println();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
