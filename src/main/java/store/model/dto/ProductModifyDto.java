package store.model.dto;

import store.model.Product;

public class ProductModifyDto {
    private String name;
    private int amount;
    private ModifyingType type;
    private Product target;

    public ProductModifyDto(String name, int amount, ModifyingType type, Product target) {
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.target = target;
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
