package store.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import store.Utils;
import store.constants.ExceptionConstants;
import store.model.dto.ProductModifyDto;
import store.model.dto.ProductModifyDto.ModifyingType;

public class Store {
    private final Utils utils;

    private final List<Product> remain = new ArrayList<Product>();
    private final List<Promotion> promotions = new ArrayList<Promotion>();

    private final int ZERO = 0;

    public Store() {
        utils = new Utils();
    }

    public void receiveRemain(List<Product> remain) {
        this.remain.addAll(remain);
    }

    public void receivePromotion(List<Promotion> promotions) {
        this.promotions.addAll(promotions);
    }

    public List<Product> getRemains() {
        return remain;
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
        List<Product> targets = findItem(purchase.getName());
        utils.validateEmpty(targets, ExceptionConstants.UNAVAILABLE_PRODUCT.getException());
        utils.validatePositiveNumber(purchase.getAmount(), ExceptionConstants.INVALID_INPUT.getException());

        long remainCount = targets.stream()
                .map(Product::getAmount)
                .reduce(0, Integer::sum);
        if (remainCount < purchase.getAmount()) {
            throw ExceptionConstants.EXCEEDED_AMOUNT.getException();
        }
    }

    public List<ProductModifyDto> getAdjustRequests(List<Purchase> purchases) {
        return purchases.stream()
                .map(this::getAdjustRequests)
                .filter(Objects::nonNull)
                .toList();
    }

    public ProductModifyDto getAdjustRequests(Purchase purchase) {
        System.out.println(purchase);
        Product remain = findPromotionItem(purchase.getName());
        if (remain == null) {
            return null;
        }
        Promotion promotion = findPromotion(remain.getPromotion());
        if (promotion != null && promotion.isValidOn(utils.getToday())) {
            int unit = promotion.getBundle();
            int available = (remain.getAmount() / unit) * unit;
            if (purchase.getAmount() > available) {
                return new ProductModifyDto(purchase.getName(), purchase.getAmount() - available, ModifyingType.SUB);
            }
            if (available >= purchase.getAmount() + promotion.getGet()
                    && purchase.getAmount() % unit == promotion.getBuy()) {
                return new ProductModifyDto(purchase.getName(), promotion.getGet(), ModifyingType.ADD);
            }
        }
        return null;
    }

    private List<Product> findItem(String name) {
        return this.remain.stream()
                .filter((item) -> Objects.equals(item.getName(), name))
                .toList();
    }

    private Product findPromotionItem(String name) {
        return this.remain.stream()
                .filter((item) -> Objects.equals(item.getName(), name))
                .filter((item) -> item.getPromotion() != null)
                .findFirst()
                .orElse(null);
    }

    private Promotion findPromotion(String name) {
        return promotions.stream()
                .filter((promotion -> Objects.equals(promotion.getName(), name)))
                .findFirst()
                .orElse(null);
    }
}
