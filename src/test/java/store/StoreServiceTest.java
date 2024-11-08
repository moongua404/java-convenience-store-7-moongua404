package store;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.dto.ProductDto;
import store.model.dto.PromotionDto;

public class StoreServiceTest {
    private StoreService storeService;

    @BeforeEach
    void setUp() throws Exception {
        storeService = new StoreService();
    }

    @Test
    void ItemLoadTest() {
        assertSimpleTest(() -> {
            List<ProductDto> result = storeService.loadItem();
        });
    }

    @Test
    void PromotionLoadTest() {
        assertSimpleTest(() -> {
            List<PromotionDto> result = storeService.loadPromotion();
            result.forEach((item) -> System.out.println(item.toString()));
        });
    }
}
