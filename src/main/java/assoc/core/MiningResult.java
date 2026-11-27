package assoc.core;

import java.util.List;
import java.util.Map;


public final class MiningResult {

    public final String algorithm;
    public final int numTransactions;
    public final int minCount;
    public final Map<ItemSet, Integer> itemsets;
    public final List<AssociationRule> rules;
    /** Tempo da busca de itemsets frequentes (a parte que difere entre os algoritmos). */
    public final long itemsetNanos;
    /** Tempo da geração de regras (código compartilhado). */
    public final long ruleNanos;

    public MiningResult(String algorithm, int numTransactions, int minCount, Map<ItemSet, Integer> itemsets,
                        List<AssociationRule> rules, long itemsetNanos, long ruleNanos) {
        this.algorithm = algorithm;
        this.numTransactions = numTransactions;
        this.minCount = minCount;
        this.itemsets = itemsets;
        this.rules = rules;
        this.itemsetNanos = itemsetNanos;
        this.ruleNanos = ruleNanos;
    }

    public long totalNanos() {
        return itemsetNanos + ruleNanos;
    }

    public double itemsetMillis() {
        return itemsetNanos / 1e6;
    }

    public double ruleMillis() {
        return ruleNanos / 1e6;
    }

    public double totalMillis() {
        return totalNanos() / 1e6;
    }
}
