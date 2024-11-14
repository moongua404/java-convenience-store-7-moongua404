package store.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import store.Utils;
import store.constants.ExceptionConstants;

public class Stores {
    private final Utils utils;

    private final Map<String, Product> remain = new HashMap<>();

    public Stores() {
        utils = new Utils();
    }

    public void receiveRemain(List<Product> remain) {
        for (Product item : remain) {
            if (this.remain.containsKey(item.getName())) {
                this.remain.get(item.getName())
                        .addAmount(item.getAmount(), item.getPromotion() != null);
                continue;
            }
            this.remain.put(item.getName(), item);
        }
    }

    public List<Product> getRemains() {
        return remain.values().stream().toList();
    }

    public Product getItem(String name) {
        return remain.get(name);
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
        if (isPromotionValid(target)) {
            return composeTransaction(purchased, target, target.getPromotion().getBundle());
        }
        return null;
    }

    private Transaction composeTransaction(Purchase purchased, Product target, int bundle) {
        int promotionAvailable = (target.getPromotionAmount() / bundle) * bundle;
        if (purchased.getAmount() > promotionAvailable) {
            return new Transaction(purchased, purchased.getAmount() - promotionAvailable, TransactionType.SUB);
        }
        if (isAddCondition(target, purchased, promotionAvailable, bundle)) {
            return new Transaction(purchased, target.getPromotion().getGet(), TransactionType.ADD);
        }
        return null;
    }

    private boolean isAddCondition(Product target, Purchase purchased, int promotionAvailable, int bundle) {
        return promotionAvailable >= purchased.getAmount() + target.getPromotion().getGet()
                && purchased.getAmount() % bundle == target.getPromotion().getBuy();
    }

    private boolean isPromotionValid(Product target) {
        return target.getPromotion() != null && target.getPromotion().isValidOn(Utils.getToday());
    }
}
