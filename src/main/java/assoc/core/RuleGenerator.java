package assoc.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class RuleGenerator {

    private RuleGenerator() {
    }

    public static List<AssociationRule> generate(Map<ItemSet, Integer> frequent, int numTransactions,
                                                 double minConfidence, ForkJoinPool pool) {
        List<ItemSet> candidates = new ArrayList<>();
        for (ItemSet s : frequent.keySet()) {
            if (s.size() >= 2) candidates.add(s);
        }
        if (candidates.isEmpty()) return new ArrayList<>();
        ItemSet[] sets = candidates.toArray(new ItemSet[0]);
        int threshold = Parallel.threshold(sets.length, pool, 8, 256);
        return pool.invoke(new RuleTask(sets, 0, sets.length, frequent, numTransactions, minConfidence, threshold));
    }

    private static final class RuleTask extends RecursiveTask<List<AssociationRule>> {
        private final ItemSet[] sets;
        private final int lo, hi, n, threshold;
        private final Map<ItemSet, Integer> frequent;
        private final double minConfidence;

        RuleTask(ItemSet[] sets, int lo, int hi, Map<ItemSet, Integer> frequent, int n,
                 double minConfidence, int threshold) {
            this.sets = sets;
            this.lo = lo;
            this.hi = hi;
            this.frequent = frequent;
            this.n = n;
            this.minConfidence = minConfidence;
            this.threshold = threshold;
        }

        @Override
        protected List<AssociationRule> compute() {
            if (hi - lo <= threshold) {
                List<AssociationRule> out = new ArrayList<>();
                for (int i = lo; i < hi; i++) rulesOf(sets[i], out);
                return out;
            }
            int mid = (lo + hi) >>> 1;
            RuleTask left = new RuleTask(sets, lo, mid, frequent, n, minConfidence, threshold);
            RuleTask right = new RuleTask(sets, mid, hi, frequent, n, minConfidence, threshold);
            left.fork();
            List<AssociationRule> r = right.compute();
            List<AssociationRule> l = left.join();
            l.addAll(r);
            return l;
        }

        /** Enumera os subconjuntos próprios de x por máscara de bits. */
        private void rulesOf(ItemSet x, List<AssociationRule> out) {
            int k = x.size();
            int supX = frequent.get(x);
            int full = (1 << k) - 1;
            for (int mask = 1; mask < full; mask++) {
                int aSize = Integer.bitCount(mask);
                int[] a = new int[aSize];
                int[] c = new int[k - aSize];
                int ai = 0, ci = 0;
                for (int b = 0; b < k; b++) {
                    if ((mask & (1 << b)) != 0) a[ai++] = x.get(b);
                    else c[ci++] = x.get(b);
                }
                // Todo subconjunto de um itemset frequente é frequente, então está no mapa.
                ItemSet antecedent = ItemSet.ofSorted(a);
                int supA = frequent.get(antecedent);
                double confidence = (double) supX / supA;
                if (confidence >= minConfidence) {
                    ItemSet consequent = ItemSet.ofSorted(c);
                    int supC = frequent.get(consequent);
                    double lift = confidence / ((double) supC / n);
                    out.add(new AssociationRule(antecedent, consequent, supX, (double) supX / n, confidence, lift));
                }
            }
        }
    }
}
