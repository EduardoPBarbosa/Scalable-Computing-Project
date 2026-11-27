package assoc.core;

import java.util.Arrays;

public final class TransactionDB {

    private final int[][] transactions;
    private final String[] itemNames;

    public TransactionDB(int[][] transactions, String[] itemNames) {
        this.transactions = transactions;
        this.itemNames = itemNames;
    }

    public int size() {
        return transactions.length;
    }

    public int numItems() {
        return itemNames.length;
    }

    public int[][] transactions() {
        return transactions;
    }

    public String itemName(int id) {
        return itemNames[id];
    }

    public TransactionDB prefix(int n) {
        if (n > transactions.length) {
            throw new IllegalArgumentException("O dataset tem apenas " + transactions.length
                    + " transacoes; foram pedidas " + n);
        }
        return new TransactionDB(Arrays.copyOf(transactions, n), itemNames);
    }
}
