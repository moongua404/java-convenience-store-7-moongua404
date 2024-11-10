package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.constants.ExceptionConstants;
import store.model.dto.ProductModifyDto;
import store.model.dto.ProductModifyDto.ModifyingType;

public class StoreTest {
    private Store store;

    @BeforeEach
    public void setUp() throws Exception {
        store = new Store();
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10));
        Product product1 = new Product("콜라", 2100, 10, promotion.getName());
        Product product2 = new Product("사이다", 750, 5, promotion.getName());
        Product product3 = new Product("집가고싶다", 1000, 4, null);
        store.receiveRemain(Arrays.asList(product1, product2));
        store.receivePromotion(List.of(promotion));
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

    @Test
    public void testGetAdjustRequests_Add() throws Exception {
        // 2개 구매 시 1개 추가되는 프로모션
        Purchase purchase = new Purchase("콜라", 2);
        List<ProductModifyDto> result = store.getAdjustRequests(List.of(purchase));

        assertEquals(1, result.size());
        ProductModifyDto dto = result.get(0);
        assertEquals("콜라", dto.getName());
        assertEquals(1, dto.getAmount());
        assertEquals(ModifyingType.ADD, dto.getType());
    }

    @Test
    public void testGetAdjustRequests_Sub() {
        Purchase purchase = new Purchase("콜라", 15);
        List<ProductModifyDto> result = store.getAdjustRequests(List.of(purchase));

        assertEquals(1, result.size());
        ProductModifyDto dto = result.getFirst();
        assertEquals("콜라", dto.getName());
        assertEquals(6, dto.getAmount());
        assertEquals(ModifyingType.SUB, dto.getType());
    }


    @Test
    public void testGetAdjustRequests_InvalidProduct() {
        // 잘못된 제품이 요청된 경우
        Purchase purchase = new Purchase("잘못된 제품", 2);
        List<ProductModifyDto> result = store.getAdjustRequests(Arrays.asList(purchase));

        assertTrue(result.isEmpty()); // 잘못된 제품은 Adjust 요청이 없음
    }

    @Test
    public void testGetAdjustRequests_PromotionInvalid() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(10);
        Promotion expiredPromotion = new Promotion("집가고싶은행사", 2, 1, startDate, endDate);

        assertFalse(expiredPromotion.isValidOn(LocalDateTime.now()));
        store.receivePromotion(List.of(expiredPromotion));

        Purchase purchase = new Purchase("집가고싶다", 2);
        List<ProductModifyDto> result = store.getAdjustRequests(Arrays.asList(purchase));

        assertTrue(result.isEmpty()); // 유효하지 않은 프로모션에는 Adjust 요청이 없음
    }

}
