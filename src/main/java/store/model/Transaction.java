package store.model;

public class Transaction {
    private Product target;
    private int amount;
    private TransactionType type;

    public Transaction(Product target, int amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
        this.target = target;
    }

    public Product getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}
