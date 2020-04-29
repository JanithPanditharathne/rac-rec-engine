package com.zone24x7.ibrac.recengine.pojo;

import java.util.Map;

/**
 * Class to represent recommendation input parameters.
 */
public class RecInputParams {
    private String channelId;
    private String pageId;
    private PlaceholderId placeholderId;
    private Map<String, String> ccp;
    private int limit;

    /**
     * Method to get the channel id.
     *
     * @return id of the channel.
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Method to set the channel id.
     *
     * @param channelId id of the channel.
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * Method to get the page id.
     *
     * @return id of the page.
     */
    public String getPageId() {
        return pageId;
    }

    /**
     * Method to set the page id.
     *
     * @param pageId id of the page.
     */
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    /**
     * Method to get the placeholder id.
     *
     * @return id of the placeholder.
     */
    public PlaceholderId getPlaceholderId() {
        return placeholderId;
    }

    /**
     * Method to set the placeholder id.
     *
     * @param placeholderId id of the placeholder.
     */
    public void setPlaceholderId(PlaceholderId placeholderId) {
        this.placeholderId = placeholderId;
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

    /**
     * Method to get the limit.
     *
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Method to set the limit.
     *
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }
}
