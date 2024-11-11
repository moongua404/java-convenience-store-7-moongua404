package store.model;

public class Transaction {
    private final Purchase target;
    private int amount;
    private final TransactionType type;

    public Transaction(Purchase target, int amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
        this.target = target;
    }

    public Purchase getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void commit() {
        if (type == TransactionType.ADD) {
            target.addAmount(amount);
        }
        if (type == TransactionType.SUB) {
            target.addAmount(-amount);
        }
    }
}
