package org.processmining.localityawareplacenetoracle.help;

public class Pair {
    // Pair attributes
    private final int index1;
    private final int index2;

    // Constructor to initialize the pair
    public Pair(int index1, int index2) {
        this.index1 = index1;
        this.index2 = index2;
    }

    // Getters
    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + index1;
        hash = 31 * hash + index2;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        return this.index1 == other.index1 && this.index2 == other.index2;
    }

    @Override
    public String toString() {
        return "Pair{" + "index1=" + index1 + ", index2=" + index2 + '}';
    }
}
