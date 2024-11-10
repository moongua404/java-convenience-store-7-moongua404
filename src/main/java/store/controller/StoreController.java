package store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.constants.MessageConstants;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;
import store.model.Store;
import store.model.Transaction;
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
        Map<String, Promotion> promotions = new HashMap<>();
        storeService.loadPromotion().forEach((dto) -> {
            promotions.put(dto.getName(), new Promotion(dto));
        });
        store.receiveRemain(storeService.loadItem().stream()
                .map((dto) -> new Product(dto, promotions.get(dto.getPromotion())))
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
        List<Transaction> requests = store.makeTransaction(purchases);
        requests.forEach((request) -> {

        });
    }
}
