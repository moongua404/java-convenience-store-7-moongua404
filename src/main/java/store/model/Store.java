package store.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import store.Utils;
import store.constants.ExceptionConstants;

public class Store {
    private final Utils utils;

    private final Map<String, Product> remain = new HashMap<>();

    private final int ZERO = 0;

    public Store() {
        utils = new Utils();
    }

    public void receiveRemain(List<Product> remain) {
        for (Product item : remain) {
            if (this.remain.containsKey(item.getName())) {
                this.remain.get(item.getName())
                        .addAmount(item.getAmount(), item.getPromotion() == null);
            } else {
                this.remain.put(item.getName(), item);
            }
        }
    }

    public List<Product> getRemains() {
        return remain.values().stream().toList();
    }

    public void validatePurchase(List<Purchase> purchases) throws Exception {
        for (Purchase item : purchases) {
            validatePurchase(item);
        }
        try {
            utils.validateDuplicate(purchases, Purchase::getName);
        } catch (Exception exception) {
            throw ExceptionConstants.DUPLICATED_PRODUCT.getException();
        }
    }

    public void validatePurchase(Purchase purchase) throws Exception {
        Product target = remain.get(purchase.getName());
        utils.validateNull(target, ExceptionConstants.UNAVAILABLE_PRODUCT.getException());
        utils.validatePositiveNumber(purchase.getAmount(), ExceptionConstants.INVALID_INPUT.getException());

        if (target.getTotalAmount() < purchase.getAmount()) {
            throw ExceptionConstants.EXCEEDED_AMOUNT.getException();
        }
    }

    // 일단 여기까지 검수함

    public List<Transaction> makeTransaction(List<Purchase> purchases) {
        return purchases.stream()
                .map(this::makeTransaction)
                .filter(Objects::nonNull)
                .toList();
    }

    public Transaction makeTransaction(Purchase purchased) {
        Product target = remain.get(purchased.getName());
        if (target == null) {
            return null;
        }
        if (target.getPromotion() != null && target.getPromotion().isValidOn(utils.getToday())) {
            int bundle = target.getPromotion().getBundle();
            int promotionAvailable = (target.getPromotionAmount() / bundle) * bundle;
            if (purchased.getAmount() > promotionAvailable) {
                return new Transaction(target, purchased.getAmount() - promotionAvailable, TransactionType.SUB);
            }
            if (promotionAvailable >= purchased.getAmount() + target.getPromotion().getGet()
                    && purchased.getAmount() % bundle == target.getPromotion().getBuy()) {
                return new Transaction(target, target.getPromotion().getGet(), TransactionType.ADD);
            }
        }
        return null;
    }
}
