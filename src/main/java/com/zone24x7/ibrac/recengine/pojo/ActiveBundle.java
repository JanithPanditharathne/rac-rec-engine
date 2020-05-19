package com.zone24x7.ibrac.recengine.pojo;

import com.zone24x7.ibrac.recengine.pojo.csconfig.AlgoCombineInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleAlgorithm;

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

    private static final int HASH_SEED = 31;

    /**
     * Constructor to instantiate ActiveBundle.
     *
     * @param id the id
     * @param name the name
     * @param type the type
     * @param recId the rec id
     * @param limitToApply the limit to apply
     * @param validAlgorithmListToExecute the valid algorithm list to execute
     * @param algoCombineInfo the algorithm combine information
     * @param placementFilteringRuleIds the placement filtering rule ids
     */
    public ActiveBundle(String id,
                        String name,
                        String type,
                        String recId,
                        Integer limitToApply,
                        List<BundleAlgorithm> validAlgorithmListToExecute,
                        AlgoCombineInfo algoCombineInfo,
                        Set<String> placementFilteringRuleIds) {
        this.id = id;
        this.name = name;
        this.type = type;
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

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }

        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }

        if (recId != null ? !recId.equals(that.recId) : that.recId != null) {
            return false;
        }

        if (limitToApply != null ? !limitToApply.equals(that.limitToApply) : that.limitToApply != null) {
            return false;
        }

        if (validAlgorithmListToExecute != null ? !validAlgorithmListToExecute.equals(that.validAlgorithmListToExecute) : that.validAlgorithmListToExecute != null) {
            return false;
        }
        if (algoCombineInfo != null ? !algoCombineInfo.equals(that.algoCombineInfo) : that.algoCombineInfo != null) {
            return false;
        }
        return placementFilteringRuleIds != null ? placementFilteringRuleIds.equals(that.placementFilteringRuleIds) : that.placementFilteringRuleIds == null;
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = HASH_SEED * result + (name != null ? name.hashCode() : 0);
        result = HASH_SEED * result + (type != null ? type.hashCode() : 0);
        result = HASH_SEED * result + (recId != null ? recId.hashCode() : 0);
        result = HASH_SEED * result + (limitToApply != null ? limitToApply.hashCode() : 0);
        result = HASH_SEED * result + (validAlgorithmListToExecute != null ? validAlgorithmListToExecute.hashCode() : 0);
        result = HASH_SEED * result + (algoCombineInfo != null ? algoCombineInfo.hashCode() : 0);
        result = HASH_SEED * result + (placementFilteringRuleIds != null ? placementFilteringRuleIds.hashCode() : 0);
        return result;
    }
}
