package assoc.core;

public final class AssociationRule implements Comparable<AssociationRule> {

    public final ItemSet antecedent;
    public final ItemSet consequent;
    public final int supportCount;
    public final double support;
    public final double confidence;
    public final double lift;

    public AssociationRule(ItemSet antecedent, ItemSet consequent, int supportCount,
                           double support, double confidence, double lift) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.supportCount = supportCount;
        this.support = support;
        this.confidence = confidence;
        this.lift = lift;
    }

    /** Ordem fixa (antecedente, depois consequente) só para gravar saídas determinísticas. */
    @Override
    public int compareTo(AssociationRule o) {
        int c = antecedent.compareTo(o.antecedent);
        return c != 0 ? c : consequent.compareTo(o.consequent);
    }
}
