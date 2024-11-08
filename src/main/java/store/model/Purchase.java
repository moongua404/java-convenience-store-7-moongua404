package store.model;

import store.model.dto.PurchaseDto;

public class Purchase {
    private String name;
    private int amount;

    public Purchase(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public Purchase(PurchaseDto purchaseDto) {
        this.name = purchaseDto.getName();
        this.amount = purchaseDto.getAmount();
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
