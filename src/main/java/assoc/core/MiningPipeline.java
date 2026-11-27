package assoc.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public final class MiningPipeline {

    private MiningPipeline() {
    }

    /** Converte o suporte mínimo relativo (ex.: 0.01 = 1%) em número de transações. */
    public static int minCount(double minSupport, int numTransactions) {
        return Math.max(1, (int) Math.ceil(minSupport * numTransactions - 1e-9));
    }

    public static MiningResult run(AssociationMiner miner, TransactionDB db, double minSupport,
                                   double minConfidence, ForkJoinPool pool) {
        int minCount = minCount(minSupport, db.size());

        long t0 = System.nanoTime();
        Map<ItemSet, Integer> itemsets = miner.mine(db, minCount, pool);
        long t1 = System.nanoTime();
        List<AssociationRule> rules = RuleGenerator.generate(itemsets, db.size(), minConfidence, pool);
        long t2 = System.nanoTime();

        return new MiningResult(miner.name(), db.size(), minCount, itemsets, rules, t1 - t0, t2 - t1);
    }
}
