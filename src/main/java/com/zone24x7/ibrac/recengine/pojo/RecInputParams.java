package com.zone24x7.ibrac.recengine.pojo;

import java.util.Map;

/**
 * Class to represent recommendation input parameters.
 */
public class RecInputParams {
    private String channel;
    private String page;
    private String placeholder;
    private Map<String, String> ccp;
    private Integer limit;

    /**
     * Method to get the channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Method to set the channel.
     *
     * @param channel the channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Method to get the page.
     *
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * Method to set the page.
     *
     * @param page the page
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Method to get the limit.
     *
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Method to set the limit.
     *
     * @param limit the limit to set
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Method to get the placeholder.
     *
     * @return the placeholder
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Method to set the placeholder.
     *
     * @param placeholder the placeholder
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Method to get the channel context parameters.
     *
     * @return the channel context parameters
     */
    public Map<String, String> getCcp() {
        return ccp;
    }

    /**
     * Method to set the channel context parameter.
     *
     * @param ccp the channel context parameters
     */
    public void setCcp(Map<String, String> ccp) {
        this.ccp = ccp;
    }
}
