package store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.constants.ExceptionConstants;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;
import store.model.Stores;
import store.model.Transaction;
import store.model.TransactionType;

public class StoreTest {
    private Stores store;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    public void setUp() throws Exception {
        store = new Stores();
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10));
        product1 = new Product("콜라", 2100, 10, promotion);
        product2 = new Product("사이다", 750, 5, promotion);
        product3 = new Product("집가고싶다", 1000, 4, null);
        store.receiveRemain(List.of(product1, product2, product3));
    }

    @Test
    void testReceiveRemain_Success() {
        Product product1 = new Product("콜라", 1000, 17, null);
        Product product2 = new Product("밥", 1000, 8, null);

        store.receiveRemain(List.of(product1, product2));
        List<Product> remains = store.getRemains();

        assertThat(remains).hasSize(4);
        assertThat(remains).extracting(Product::getName).contains("콜라", "사이다");

        Product coke = remains.stream()
                .filter(product -> product.getName().equals("콜라"))
                .findFirst()
                .orElseThrow();

        assertThat(coke.getAmount() == 10);
        assertThat(coke.getPromotionAmount() == 17);
    }

    @Test
    void testValidatePurchase_Success() throws Exception {
        Purchase purchase = new Purchase("콜라", 5, product1);
        store.validatePurchase(purchase); // 예외가 발생하지 않아야 함
    }

    @Test
    void testValidatePurchase_ProductUnavailable() {
        Purchase purchase = new Purchase("곰탕", 1, null);
        Exception exception = assertThrows(Exception.class, () -> {
            store.validatePurchase(purchase);
        });

        assertThat(exception.getMessage()).isEqualTo(
                ExceptionConstants.UNAVAILABLE_PRODUCT.getException().getMessage());
    }

    @Test
    void testValidatePurchase_ExceededAmount() {
        Purchase purchase = new Purchase("사이다", 6, product2);
        Exception exception = assertThrows(Exception.class, () -> {
            store.validatePurchase(purchase);
        });

        assertThat(exception.getMessage()).isEqualTo(ExceptionConstants.EXCEEDED_AMOUNT.getException().getMessage());
    }

    @Test
    void testValidatePurchase_NegativeAmount() {
        Purchase purchase = new Purchase("콜라", -1, product1);
        Exception exception = assertThrows(Exception.class, () -> {
            store.validatePurchase(purchase);
        });

        assertThat(exception.getMessage()).isEqualTo(ExceptionConstants.INVALID_INPUT.getException().getMessage());
    }

    @Test
    void testMakeTransaction_Add() throws Exception {
        Purchase purchase = new Purchase("콜라", 2, product1);
        Transaction result = store.makeTransaction(purchase);

        assertThat(result.getTarget().getName()).isEqualTo("콜라");
        assertThat(result.getAmount()).isEqualTo(1);
        assertThat(result.getType()).isEqualTo(TransactionType.ADD);
    }

    @Test
    void testGetAdjustRequests_Sub() {
        Purchase purchase = new Purchase("콜라", 15, product1);
        Transaction result = store.makeTransaction(purchase);

        assertThat(result.getTarget().getName()).isEqualTo("콜라");
        assertThat(result.getAmount()).isEqualTo(6);
        assertThat(result.getType()).isEqualTo(TransactionType.SUB);
    }

    @Test
    void testGetAdjustRequests_InvalidProduct() {
        Purchase purchase = new Purchase("나v자바v바라", 2, null);
        List<Transaction> result = store.makeTransaction(List.of(purchase));

        assertThat(result).isEmpty();
    }

    @Test
    void testGetAdjustRequests_PromotionInvalid() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(10);
        Promotion expiredPromotion = new Promotion("집가고싶은행사", 2, 1, startDate, endDate);

        assertThat(expiredPromotion.isValidOn(LocalDateTime.now())).isFalse();
        Product product = new Product("집가고싶다", 1000, 4, expiredPromotion);
        store.receiveRemain(List.of(product));

        Purchase purchase = new Purchase("집가고싶다", 2, product3);
        List<Transaction> result = store.makeTransaction(List.of(purchase));

        assertThat(result).isEmpty();
    }
}
