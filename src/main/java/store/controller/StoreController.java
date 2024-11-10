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
    Store store;

    public StoreController(
            InputView inputView,
            OutputView outputView,
            StoreService storeService
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
        store = new Store();
    }

    public void run() throws Exception {
        setup();
        printGuide();
        while (true) {
            List<Purchase> purchased = purchase();
            adjustPurchase(purchased);
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

    private List<Purchase> purchase() throws Exception {
        while (true) {
            try {
                return getPurchaseInput();
            } catch (Exception exception) {
                outputView.printMessage(exception.getMessage());
            }
        }
    }

    private List<Purchase> getPurchaseInput() throws Exception {
        String response = inputView.readItem();
        List<PurchaseDto> parsedPurchase = storeService.parsePurchaseDto(response);
        List<Purchase> purchased = parsedPurchase.stream().map((dto) -> new Purchase(dto)).toList();
        store.validatePurchase(purchased);
        return purchased;
    }

    private void adjustPurchase(List<Purchase> purchases) throws Exception {
        List<Transaction> requests = store.makeTransaction(purchases);
        for (Transaction request : requests) {
            dealTransaction(request);
        }
    }

    private void dealTransaction(Transaction request) throws Exception {
        boolean willCommit = getRequestResponse(request.getType().getMessageConstants());
        if (willCommit) {
            request.commit();
        }
    }

    private boolean getRequestResponse(MessageConstants message) throws Exception {
        while (true) {
            try {
                return inputView.readYorN(message);
            } catch (Exception exception) {
                outputView.printMessage(exception.getMessage());
            }
        }
    }
}
