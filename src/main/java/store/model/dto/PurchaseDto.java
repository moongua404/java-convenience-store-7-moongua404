package store.model.dto;

public class PurchaseDto {
    private final String name;
    private final int amount;

    public PurchaseDto(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void validation() throws Exception {

    }
}
