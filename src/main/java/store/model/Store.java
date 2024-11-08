package store.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import store.constants.ExceptionConstants;

public class Store {
    private final List<Product> remain = new ArrayList<Product>();
    private final List<Promotion> promotions = new ArrayList<Promotion>();

    public void receiveRemain(List<Product> remain) {
        this.remain.addAll(remain);
    }

    public void validatePurchase(Purchase purchase) throws Exception {
        List<Product> targets = remain.stream()
                .filter((product) -> Objects.equals(product.getName(), purchase.getName()))
                .toList();
        if (targets.isEmpty()) {
            throw ExceptionConstants.UNAVAILABLE_PRODUCT.getException();
        }
        if (purchase.getAmount() <= 0) {
            throw ExceptionConstants.INVALID_INPUT.getException();
        }
        long remainCount = targets.stream()
                .map(Product::getAmount)
                .reduce(0, Integer::sum);
        if (remainCount < purchase.getAmount()) {
            throw ExceptionConstants.EXCEEDED_AMOUNT.getException();
        }
    }
}
