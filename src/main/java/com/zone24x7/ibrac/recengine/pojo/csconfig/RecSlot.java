package com.zone24x7.ibrac.recengine.pojo.csconfig;

import java.util.List;

/**
 * Pojo for Recslots
 */
public class RecSlot {
    private String channel;
    private String page;
    private String placeholder;
    private List<String> recIds;
    private List<String> ruleIds;

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
}
