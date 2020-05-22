package com.zone24x7.ibrac.recengine.pojo.controller;

/**
 * Class to represent the response formatter configuration.
 */
public class ResponseFormatterConfig {
    private String currency;
    private String imageWidth;
    private String imageHeight;

    /**
     * Constructor to instantiate ResponseFormatterConfig.
     *
     * @param currency the currency
     * @param imageWidth the image width
     * @param imageHeight the image height
     */
    public ResponseFormatterConfig(String currency, String imageWidth, String imageHeight) {
        this.currency = currency;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    /**
     * Method to get the currency.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Method to get the image width.
     *
     * @return the image width
     */
    public String getImageWidth() {
        return imageWidth;
    }

    /**
     * Method to get the image height.
     *
     * @return the image height
     */
    public String getImageHeight() {
        return imageHeight;
    }
}
