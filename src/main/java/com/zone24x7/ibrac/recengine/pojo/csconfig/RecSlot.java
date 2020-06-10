package com.zone24x7.ibrac.recengine.pojo.csconfig;

import com.zone24x7.ibrac.recengine.util.StringConstants;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Pojo for Recslots
 */
public class RecSlot {
    @NotEmpty
    private String channel;

    @NotEmpty
    private String page;

    @NotEmpty
    private String placeholder;

    private List<String> recIds;
    private List<String> ruleIds;

    private static final int HASH_SEED = 31;

    /**
     * Returns the channel.
     *
     * @return channel.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets channel.
     *
     * @param channel channel to set.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Returns the page.
     *
     * @return page.
     */
    public String getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page page to set.
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Returns the placeholder.
     *
     * @return placeholder.
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Sets placeholder.
     *
     * @param placeholder placeholder to set.
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Returns the list of recIds.
     *
     * @return list of recIds.
     */
    public List<String> getRecIds() {
        return recIds;
    }

    /**
     * Sets list og recIds.
     *
     * @param recIds recId list to set.
     */
    public void setRecIds(List<String> recIds) {
        this.recIds = recIds;
    }

    /**
     * Returns the list of ruleIds.
     *
     * @return list of ruleIds.
     */
    public List<String> getRuleIds() {
        return ruleIds;
    }

    /**
     * Sets list og ruleIds.
     *
     * @param ruleIds ruleId list to set.
     */
    public void setRuleIds(List<String> ruleIds) {
        this.ruleIds = ruleIds;
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

        RecSlot recSlot = (RecSlot) o;

        if (channel != null ? !channel.equals(recSlot.channel) : (recSlot.channel != null)) {
            return false;
        }

        if (page != null ? !page.equals(recSlot.page) : (recSlot.page != null)) {
            return false;
        }

        if (placeholder != null ? !placeholder.equals(recSlot.placeholder) : (recSlot.placeholder != null)) {
            return false;
        }

        if (recIds != null ? !recIds.equals(recSlot.recIds) : (recSlot.recIds != null)) {
            return false;
        }

        return ruleIds != null ? ruleIds.equals(recSlot.ruleIds) : (recSlot.ruleIds == null);
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = channel != null ? channel.hashCode() : 0;
        result = HASH_SEED * result + (page != null ? page.hashCode() : 0);
        result = HASH_SEED * result + (placeholder != null ? placeholder.hashCode() : 0);
        result = HASH_SEED * result + (recIds != null ? recIds.hashCode() : 0);
        result = HASH_SEED * result + (ruleIds != null ? ruleIds.hashCode() : 0);
        return result;
    }

    /**
     * Method to get the placement information as a string.
     * NOTE: Modifying this method will affect active bundle config generator and active bundle provider.
     *
     * @return string representation of the placement information
     */
    public String getPlacementInfoAsString() {
        return channel + StringConstants.RECSLOT_GENERATED_ID_SEPARATOR +
                page + StringConstants.RECSLOT_GENERATED_ID_SEPARATOR +
                placeholder;
    }
}
