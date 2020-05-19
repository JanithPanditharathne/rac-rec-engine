package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Class to represent bundle algorithm container.
 */
public class BundleAlgorithmContainer implements Comparable<BundleAlgorithmContainer> {
    private int rank;

    @Valid
    @NotNull
    private BundleAlgorithm algorithm;

    private static final int HASH_SEED = 31;

    /**
     * Method to get the rank.
     *
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Method to set the rank.
     *
     * @param rank the rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Method to get the algorithm.
     *
     * @return the algorithm
     */
    public BundleAlgorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Method to set the algorithm.
     *
     * @param algorithm the algorithm
     */
    public void setAlgorithm(BundleAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Overridden equals method
     *
     * @param o object to compare
     * @return true if equal and false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BundleAlgorithmContainer that = (BundleAlgorithmContainer) o;

        if (rank != that.rank) {
            return false;
        }

        return algorithm != null ? algorithm.equals(that.algorithm) : that.algorithm == null;
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = rank;
        result = HASH_SEED * result + (algorithm != null ? algorithm.hashCode() : 0);
        return result;
    }

    /**
     * Compares this BundleAlgorithmContainer object with the specified BundleAlgorithmContainer object for order based on the rank.
     *
     * @param compareToBundleAlgorithmContainer the other BundleAlgorithmContainer object to be compared.
     * @return a negative integer, zero, or a positive integer as this BundleAlgorithmContainer object is less than, equal to,
     * or greater than the specified BundleAlgorithmContainer object in terms of the BundleAlgorithmContainer object's rank attribute.
     */
    @Override
    public int compareTo(BundleAlgorithmContainer compareToBundleAlgorithmContainer) {
        int compareToBundleAlgorithmRank = compareToBundleAlgorithmContainer.getRank();

        return this.rank - compareToBundleAlgorithmRank;
    }
}
