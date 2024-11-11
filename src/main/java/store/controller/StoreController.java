package store.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.constants.MessageConstants;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;
import store.model.Store;
import store.model.Transaction;
import store.model.TransactionType;
import store.model.dto.PurchaseDto;
import store.model.dto.ReceiptDto;
import store.model.dto.ReceiptDto.PurchaseType;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    InputView inputView;
    OutputView outputView;
    StoreService storeService;
    Store store;

    private final float MEMBERSHIP_DISCOUNT_RATE = 0.3f;
    private final int MAX_MEMBERSHIP_DISCOUNT = 8000;

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
        while (true) {
            printGuide();
            List<Purchase> purchased = purchase();
            adjustPurchase(purchased);
            List<ReceiptDto> receiptDto = dealCompletion(purchased);
            getReceipt(receiptDto);
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
        store.getRemains().forEach((product) -> outputView.printProducts(product, product.getPromotion() != null));
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
        List<Purchase> purchased = parsedPurchase.stream()
                .map((dto) -> new Purchase(dto, store.getItem(dto.getName())))
                .toList();
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
        boolean response = getRequestResponse(request.getType().getMessageConstants(),
                request.getTarget().getName(), request.getAmount());
        if (request.getType() == TransactionType.SUB && !response) {
            request.commit();
        }
        if (request.getType() == TransactionType.ADD && response) {
            request.commit();
        }
    }

    private boolean getRequestResponse(MessageConstants message, String name, int price) throws Exception {
        while (true) {
            try {
                return inputView.readYorN(message, name, price);
            } catch (Exception exception) {
                outputView.printMessage(exception.getMessage());
            }
        }
    }

    private List<ReceiptDto> dealCompletion(List<Purchase> purchases) {
        List<ReceiptDto> receiptDto = new ArrayList<>();
        purchases.forEach(purchase -> {
            receiptDto.add(purchase.commit());
        });
        return receiptDto;
    }

    private void getReceipt(List<ReceiptDto> receiptDto) {
        int totalPrice = 0;
        int totalCount = 0;
        int promotePrice = 0;
        int membershipPrice = 0;
        outputView.printReceiptHeader("W 편의점");
        outputView.printReceiptItem("상품명", "수량", "금액");
        for (ReceiptDto dto : receiptDto) {
            outputView.printReceiptItem(dto.getName(), Integer.toString(dto.getAmount()),
                    Integer.toString(dto.getPrice()));
        }
        outputView.printReceiptHeader("증\t정");
        for (ReceiptDto dto : receiptDto) {
            if (dto.getType() == PurchaseType.PROMOTION) {
                outputView.printReceiptItem(dto.getName(), Integer.toString(dto.getBonus()), "");
            }
        }
        outputView.printReceiptHeader("=====");
        for (ReceiptDto dto : receiptDto) {
            totalPrice += dto.getPrice() * dto.getAmount();
            totalCount += dto.getAmount();
            if (dto.getType() == PurchaseType.PROMOTION) {
                promotePrice += dto.getPrice() * dto.getBonus();//??????
            }
            if (dto.getType() == PurchaseType.MEMBERSHIP) {
                membershipPrice += (int) (dto.getPrice() * dto.getAmount() * MEMBERSHIP_DISCOUNT_RATE);
            }
        }
        if (membershipPrice > MAX_MEMBERSHIP_DISCOUNT) {
            membershipPrice = MAX_MEMBERSHIP_DISCOUNT;
        }
        outputView.printReceiptItem("총구매액", Integer.toString(totalCount), Integer.toString(totalPrice));
        outputView.printReceiptItem("행사할인", "", Integer.toString(-promotePrice));
        outputView.printReceiptItem("멤버십할인", "", Integer.toString(-membershipPrice));
        outputView.printReceiptItem("내실돈", "", Integer.toString(totalPrice - promotePrice - membershipPrice));
    }
}
