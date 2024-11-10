package store.controller;

import java.util.List;
import store.constants.MessageConstants;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;
import store.model.Store;
import store.model.dto.ProductModifyDto;
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
        printGuide();
        while (true) {
            purchase();
        }
    }

    private void setup() throws Exception {
        store.receiveRemain(storeService.loadItem().stream()
                .map(Product::new)
                .toList()
        );
        store.receivePromotion(storeService.loadPromotion().stream()
                .map(Promotion::new)
                .toList()
        );
    }

    private void printGuide() {
        outputView.printGuide(MessageConstants.START_GUIDE_MESSAGE);
        outputView.newLine();
        store.getRemains().forEach((product) -> outputView.printProducts(product));
        outputView.newLine();
    }

    private void purchase() throws Exception {
        while (true) {
            try {
                List<Purchase> purchased = getPurchaseInput();
                store.validatePurchase(purchased);
                adjustPurchase(purchased);

                //제로부터 시작하는...
                break;
            } catch (Exception exception) {
                outputView.printMessage(exception.getMessage());
            }
        }
    }

    private List<Purchase> getPurchaseInput() throws Exception {
        String response = inputView.readItem();
        List<PurchaseDto> parsedPurchase = storeService.parsePurchaseDto(response);
        return parsedPurchase.stream().map((dto) -> new Purchase(dto)).toList();
    }

    private void adjustPurchase(List<Purchase> purchases) {
        List<ProductModifyDto> requests = store.getAdjustRequests(purchases);
        requests.forEach((request) -> {

        });
    }
}
