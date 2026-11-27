package assoc.core;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Primeira passada sobre a base: conta em quantas transações cada item aparece.
 */
public final class ItemCounter {

    private ItemCounter() {
    }

    public static int[] count(TransactionDB db, ForkJoinPool pool) {
        int[][] tx = db.transactions();
        if (tx.length == 0) return new int[db.numItems()];
        int threshold = Parallel.threshold(tx.length, pool, 4, 2_000);
        return pool.invoke(new CountTask(tx, 0, tx.length, db.numItems(), threshold));
    }

    private static final class CountTask extends RecursiveTask<int[]> {
        private final int[][] tx;
        private final int lo, hi, numItems, threshold;

        CountTask(int[][] tx, int lo, int hi, int numItems, int threshold) {
            this.tx = tx;
            this.lo = lo;
            this.hi = hi;
            this.numItems = numItems;
            this.threshold = threshold;
        }

        @Override
        protected int[] compute() {
            if (hi - lo <= threshold) {
                int[] counts = new int[numItems];
                for (int i = lo; i < hi; i++) {
                    for (int item : tx[i]) counts[item]++;
                }
                return counts;
            }
            int mid = (lo + hi) >>> 1;
            CountTask left = new CountTask(tx, lo, mid, numItems, threshold);
            CountTask right = new CountTask(tx, mid, hi, numItems, threshold);
            left.fork();                       // metade esquerda vai para a fila (pode ser roubada)
            int[] r = right.compute();         // metade direita roda nesta thread
            int[] l = left.join();
            for (int i = 0; i < numItems; i++) l[i] += r[i];
            return l;
        }
    }
}
