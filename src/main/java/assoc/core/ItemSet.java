package assoc.core;

import java.util.Arrays;

public final class ItemSet implements Comparable<ItemSet> {

    private final int[] items;
    private final int hash;

    private ItemSet(int[] sortedItems) {
        this.items = sortedItems;
        this.hash = Arrays.hashCode(sortedItems);
    }

    public static ItemSet ofSorted(int[] sortedItems) {
        return new ItemSet(sortedItems);
    }

    public static ItemSet of(int... items) {
        int[] copy = items.clone();
        Arrays.sort(copy);
        return new ItemSet(copy);
    }

    public int size() {
        return items.length;
    }

    public int get(int i) {
        return items[i];
    }

    public int[] toArray() {
        return items.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemSet)) return false;
        ItemSet other = (ItemSet) o;
        return hash == other.hash && Arrays.equals(items, other.items);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int compareTo(ItemSet o) {
        if (items.length != o.items.length) return Integer.compare(items.length, o.items.length);
        for (int i = 0; i < items.length; i++) {
            if (items[i] != o.items[i]) return Integer.compare(items[i], o.items[i]);
        }
        return 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(items);
    }
}
