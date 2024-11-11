package store.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import store.constants.MessageConstants;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;
import store.model.Stores;
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
    Stores store;

    private final float MEMBERSHIP_DISCOUNT_RATE = 0.3f;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;
    private static final String PRICE_FORMAT = "%,d";

    public StoreController(
            InputView inputView,
            OutputView outputView,
            StoreService storeService
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
        store = new Stores();
    }

    public void run() throws Exception {
        setup();
        boolean shoppingContinue = true;
        while (shoppingContinue) {
            businessLogic();
            shoppingContinue = getShoppingContinue();
            outputView.newLine();
        }
    }

    private void businessLogic() throws Exception {
        printGuide();
        List<Purchase> purchased = purchase();
        adjustPurchase(purchased);
        boolean applyPromotion = getMembershipResponse();
        List<ReceiptDto> receiptDto = dealCompletion(purchased);
        getReceipt(receiptDto, applyPromotion);
    }

    private void setup() throws Exception {
        Map<String, Promotion> promotions = new HashMap<>();
        storeService.loadPromotion().forEach((dto) -> promotions.put(dto.getName(), new Promotion(dto)));
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
                handleInputExcept(exception);
            }
        }
    }

    private void handleInputExcept(Exception exception) throws Exception {
        if (exception.getClass() == NoSuchElementException.class) {
            throw exception;
        }
        outputView.printMessage(exception.getMessage());
    }

    private List<Purchase> getPurchaseInput() throws Exception {
        String response = inputView.readItem();
        if (Objects.equals(response, "")) {
            throw new NoSuchElementException();
        }
        List<PurchaseDto> parsedPurchase = storeService.parsePurchaseDto(response);
        List<Purchase> purchased = convertDtoToPurchase(parsedPurchase);
        store.validatePurchase(purchased);
        return purchased;
    }

    List<Purchase> convertDtoToPurchase(List<PurchaseDto> parsedPurchase) {
        return parsedPurchase.stream()
                .map((dto) -> new Purchase(dto, store.getItem(dto.getName())))
                .toList();
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
                handleInputExcept(exception);
            }
        }
    }

    private boolean getMembershipResponse() throws Exception {
        while (true) {
            try {
                return inputView.readYorN(MessageConstants.MEMBERSHIP_DISCOUNT_REQUEST);
            } catch (Exception exception) {
                handleInputExcept(exception);
                outputView.printMessage(exception.getMessage());
            }
        }
    }

    private boolean getShoppingContinue() throws Exception {
        while (true) {
            try {
                return inputView.readYorN(MessageConstants.SHOPPING_CONTINUE);
            } catch (Exception exception) {
                handleInputExcept(exception);
                outputView.printMessage(exception.getMessage());
            }
        }
    }

    private List<ReceiptDto> dealCompletion(List<Purchase> purchases) {
        List<ReceiptDto> receiptDto = new ArrayList<>();
        purchases.forEach(purchase -> receiptDto.add(purchase.commit()));
        return receiptDto;
    }

    private void getReceipt(List<ReceiptDto> receiptDto, boolean applyPromotion) {
        showReceiptItems(receiptDto);
        showReceiptPromotion(receiptDto);
        int totalPrice = calculateTotalPrice(receiptDto);
        int totalCount = calculateTotalCount(receiptDto);
        int promotePrice = calculatePromotionPrice(receiptDto);
        int membershipPrice = adjustMembershipPrice(calculateMembershipPrice(receiptDto), applyPromotion);
        outputView.printReceiptResult(totalPrice, totalCount, promotePrice, membershipPrice);
    }

    private int adjustMembershipPrice(int membershipPrice, boolean applyPromotion) {
        if (membershipPrice > MAX_MEMBERSHIP_DISCOUNT) {
            membershipPrice = MAX_MEMBERSHIP_DISCOUNT;
        }
        if (!applyPromotion) {
            membershipPrice = 0;
        }
        return membershipPrice;
    }

    private int calculateTotalPrice(List<ReceiptDto> receiptDto) {
        return receiptDto.stream()
                .mapToInt(dto -> dto.getPrice() * dto.getAmount())
                .sum();
    }

    private int calculateTotalCount(List<ReceiptDto> receiptDto) {
        return receiptDto.stream()
                .mapToInt(ReceiptDto::getAmount)
                .sum();
    }

    private int calculatePromotionPrice(List<ReceiptDto> receiptDto) {
        return receiptDto.stream()
                .filter(dto -> dto.getType() == PurchaseType.PROMOTION)
                .mapToInt(dto -> dto.getPrice() * dto.getBonus())
                .sum();
    }

    private int calculateMembershipPrice(List<ReceiptDto> receiptDto) {
        return receiptDto.stream()
                .filter(dto -> dto.getType() == PurchaseType.MEMBERSHIP)
                .mapToInt(dto -> (int) (dto.getPrice() * dto.getAmount() * MEMBERSHIP_DISCOUNT_RATE))
                .sum();
    }


    private void showReceiptItems(List<ReceiptDto> receiptDto) {
        outputView.printReceiptStart();
        for (ReceiptDto dto : receiptDto) {
            outputView.printReceiptItem(dto.getName(), Integer.toString(dto.getAmount()),
                    String.format(PRICE_FORMAT, dto.getPrice() * dto.getAmount()));
        }
    }

    private void showReceiptPromotion(List<ReceiptDto> receiptDto) {
        outputView.printReceiptPromotionHeader();
        for (ReceiptDto dto : receiptDto) {
            if (dto.getType() == PurchaseType.PROMOTION) {
                outputView.printReceiptItem(dto.getName(), Integer.toString(dto.getBonus()), "");
            }
        }
    }
}
