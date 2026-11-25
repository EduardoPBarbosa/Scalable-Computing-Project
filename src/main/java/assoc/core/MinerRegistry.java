package assoc.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Catálogo dos algoritmos disponíveis.
 */
public final class MinerRegistry {

    private static final Map<String, String> CLASSES = new LinkedHashMap<>();

    static {
        CLASSES.put("apriori", "assoc.apriori.AprioriMiner");
        CLASSES.put("fpgrowth", "assoc.fpgrowth.FPGrowthMiner");
    }

    private MinerRegistry() {
    }

    public static List<String> names() {
        return new ArrayList<>(CLASSES.keySet());
    }

    public static AssociationMiner create(String name) {
        String className = CLASSES.get(name.toLowerCase());
        if (className == null) {
            throw new IllegalArgumentException("Algoritmo desconhecido: '" + name + "'. Opcoes: " + names());
        }
        try {
            return (AssociationMiner) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("O algoritmo '" + name + "' ainda nao foi implementado neste build ("
                    + className + " nao encontrada).", e);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Nao foi possivel instanciar " + className, e);
        }
    }

    public static List<AssociationMiner> createAll(String spec) {
        List<AssociationMiner> miners = new ArrayList<>();
        if (spec.equalsIgnoreCase("todos")) {
            for (String n : names()) miners.add(create(n));
        } else {
            for (String n : spec.split(",")) miners.add(create(n.trim()));
        }
        return miners;
    }
}
