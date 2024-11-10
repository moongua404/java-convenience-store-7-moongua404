package store.model.dto;

public class ProductModifyDto {
    private String name;
    private int amount;
    private ModifyingType type;

    public ProductModifyDto(String name, int amount, ModifyingType type) {
        this.name = name;
        this.amount = amount;
        this.type = type;
    }

    public enum ModifyingType {
        ADD,
        SUB
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public ModifyingType getType() {
        return type;
    }
}
