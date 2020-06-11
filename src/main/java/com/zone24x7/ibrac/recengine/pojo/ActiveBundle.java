package com.zone24x7.ibrac.recengine.pojo;

import com.zone24x7.ibrac.recengine.pojo.csconfig.AlgoCombineInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleAlgorithm;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Set;

/**
 * Class to represent the active bundle.
 */
public class ActiveBundle {
    private final String id;
    private final String name;
    private final String type;
    private final String recId;
    private final Integer limitToApply;
    private final List<BundleAlgorithm> validAlgorithmListToExecute;
    private final AlgoCombineInfo algoCombineInfo;
    private final Set<String> placementFilteringRuleIds;

    private static final int HASH_SEED1 = 17;
    private static final int HASH_SEED2 = 37;

    /**
     * Constructor to instantiate ActiveBundle.
     *
     * @param bundleInfo                  the bundle info
     * @param recId                       the recommendation id
     * @param limitToApply                the limit to apply
     * @param validAlgorithmListToExecute the valid algorithm list to execute
     * @param algoCombineInfo             the algorithm combine information
     * @param placementFilteringRuleIds   the placement filtering rule ids
     */
    public ActiveBundle(BundleInfo bundleInfo,
                        String recId,
                        Integer limitToApply,
                        List<BundleAlgorithm> validAlgorithmListToExecute,
                        AlgoCombineInfo algoCombineInfo,
                        Set<String> placementFilteringRuleIds) {
        this.id = bundleInfo.getBundleId();
        this.name = bundleInfo.getBundleName();
        this.type = bundleInfo.getBundleType();
        this.recId = recId;
        this.limitToApply = limitToApply;
        this.validAlgorithmListToExecute = validAlgorithmListToExecute;
        this.algoCombineInfo = algoCombineInfo;
        this.placementFilteringRuleIds = placementFilteringRuleIds;
    }

    /**
     * Method to get the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Method to get the rec id.
     *
     * @return the rec id
     */
    public String getRecId() {
        return recId;
    }

    /**
     * Method to get the limit to apply.
     *
     * @return the limit to apply
     */
    public Integer getLimitToApply() {
        return limitToApply;
    }

    /**
     * Method to get the valid algorithm list to execute.
     *
     * @return the valid algorithm list to execute
     */
    public List<BundleAlgorithm> getValidAlgorithmListToExecute() {
        return validAlgorithmListToExecute;
    }

    /**
     * Method to get the algorithm combine information.
     *
     * @return the algorithm combine information
     */
    public AlgoCombineInfo getAlgoCombineInfo() {
        return algoCombineInfo;
    }

    /**
     * Method to get the placement filtering rule ids.
     *
     * @return the placement filtering rule ids
     */
    public Set<String> getPlacementFilteringRuleIds() {
        return placementFilteringRuleIds;
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

        ActiveBundle that = (ActiveBundle) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(type, that.type)
                .append(recId, that.recId)
                .append(limitToApply, that.limitToApply)
                .append(validAlgorithmListToExecute, that.validAlgorithmListToExecute)
                .append(algoCombineInfo, that.algoCombineInfo)
                .append(placementFilteringRuleIds, that.placementFilteringRuleIds)
                .isEquals();
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(HASH_SEED1, HASH_SEED2)
                .append(id)
                .append(name)
                .append(type)
                .append(recId)
                .append(limitToApply)
                .append(validAlgorithmListToExecute)
                .append(algoCombineInfo)
                .append(placementFilteringRuleIds)
                .toHashCode();
    }

    /**
     * Class to represent bundle information.
     */
    public static class BundleInfo {
        private final String bundleId;
        private final String bundleName;
        private final String bundleType;

        /**
         * Constructor to instantiate BundleInfo.
         *
         * @param bundleId   the bundle id
         * @param bundleName the bundle name
         * @param bundleType the bundle type
         */
        public BundleInfo(String bundleId, String bundleName, String bundleType) {
            this.bundleId = bundleId;
            this.bundleName = bundleName;
            this.bundleType = bundleType;
        }

        /**
         * Method to get the bundle id.
         *
         * @return the bundle id
         */
        private String getBundleId() {
            return bundleId;
        }

        /**
         * Method to get the bundle name.
         *
         * @return the bundle name
         */
        private String getBundleName() {
            return bundleName;
        }

        /**
         * Method to get the bundle type.
         *
         * @return the bundle type
         */
        private String getBundleType() {
            return bundleType;
        }
    }
}
