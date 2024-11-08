package store.controller;

import java.util.List;
import store.constants.MessageConstants;
import store.model.Product;
import store.model.Purchase;
import store.model.Store;
import store.model.dto.PurchaseDto;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    InputView inputView;
    OutputView outputView;
    StoreService storeService;

    Store store = new Store();

    public StoreController(
            InputView inputView,
            OutputView outputView,
            StoreService storeService
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
    }

    public void run() throws Exception {
        setup();
        while (true) {
            purchase();
        }
    }

    private void setup() throws Exception {
        List<Product> products = storeService.loadItem()
                .stream()
                .map((dto) -> new Product(dto))
                .toList();
        store.receiveRemain(products);

        outputView.printGuide(MessageConstants.START_GUIDE_MESSAGE);
        outputView.newLine();
        products.forEach((product) -> outputView.printProducts(product));
        outputView.newLine();
    }

    private void purchase() throws Exception {
        while (true) {
            try {
                String response = inputView.readItem();
                List<PurchaseDto> parsedPurchase = storeService.parsePurchaseDto(response);
                List<Purchase> purchased = parsedPurchase.stream().map((dto) -> new Purchase(dto)).toList();
                for (Purchase item : purchased) {
                    store.validatePurchase(item);
                }
                break;
            } catch (Exception exception) {
                outputView.printMessage(exception.getMessage());
            }
        }
    }
}
