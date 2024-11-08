package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.constants.ExceptionConstants;

public class StoreTest {
    private Store store;

    @BeforeEach
    public void setUp() {
        store = new Store();
        // 테스트를 위한 제품 초기화
        Product product1 = new Product("콜라", 2100, 10, null);
        Product product2 = new Product("사이다", 750, 5, null);
        List<Product> products = Arrays.asList(product1, product2);
        store.receiveRemain(products);
    }

    @Test
    public void testValidatePurchase_Success() throws Exception {
        Purchase purchase = new Purchase("콜라", 5);
        store.validatePurchase(purchase); // 예외가 발생하지 않아야 함
    }

    @Test
    public void testValidatePurchase_ProductUnavailable() {
        Purchase purchase = new Purchase("곰탕", 1);
        Exception exception = assertThrows(Exception.class, () -> {
            store.validatePurchase(purchase);
        });

        assertEquals(ExceptionConstants.UNAVAILABLE_PRODUCT.getException().getMessage(), exception.getMessage());
    }

    @Test
    public void testValidatePurchase_ExceededAmount() {
        Purchase purchase = new Purchase("사이다", 6);
        Exception exception = assertThrows(Exception.class, () -> {
            store.validatePurchase(purchase);
        });

        assertEquals(ExceptionConstants.EXCEEDED_AMOUNT.getException().getMessage(), exception.getMessage());
    }

    @Test
    public void testValidatePurchase_NegativeAmount() {
        Purchase purchase = new Purchase("콜라", -1);
        Exception exception = assertThrows(Exception.class, () -> {
            store.validatePurchase(purchase);
        });

        assertEquals(ExceptionConstants.INVALID_INPUT.getException().getMessage(), exception.getMessage());
    }
}
