package store;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.constants.FilePathConstants;
import store.model.Product;
import store.model.dto.ProductDto;

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
}
