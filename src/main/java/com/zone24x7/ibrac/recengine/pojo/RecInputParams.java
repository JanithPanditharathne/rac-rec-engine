package com.zone24x7.ibrac.recengine.pojo;

import java.util.Map;

/**
 * Class to represent recommendation input parameters.
 */
public class RecInputParams {
    private Map<String, String> ccp;
    private int limit;

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
