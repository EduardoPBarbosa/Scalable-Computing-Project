package assoc.core;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;

/**
 * Interface de execução comum aos dois algoritmos do trabalho.
 */
public interface AssociationMiner {

    String name();

    /**
     * Encontra todos os itemsets frequentes.
     *
     * @param db       base de transações 
     * @param minCount suporte mínimo absoluto 
     * @param pool     pool ForkJoin onde todo o trabalho paralelo deve ser executado;
     *                 o número de threads do pool é o grau de paralelismo do experimento
     * @return mapa itemset -> número de transações que contêm o itemset
     */
    Map<ItemSet, Integer> mine(TransactionDB db, int minCount, ForkJoinPool pool);
}
