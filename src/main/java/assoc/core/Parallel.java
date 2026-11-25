package assoc.core;

import java.util.concurrent.ForkJoinPool;

/** Utilitários de decomposição usados pelas tarefas ForkJoin. */
public final class Parallel {

    private Parallel() {
    }

    public static int threshold(int n, ForkJoinPool pool, int chunksPerThread, int minChunk) {
        int chunks = pool.getParallelism() * chunksPerThread;
        return Math.max(minChunk, (n + chunks - 1) / chunks);
    }
}
