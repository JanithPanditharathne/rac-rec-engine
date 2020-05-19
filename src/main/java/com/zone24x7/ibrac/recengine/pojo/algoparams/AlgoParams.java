package com.zone24x7.ibrac.recengine.pojo.algoparams;

import java.util.List;

/**
 * Class to represent the algorithm params.
 */
public class AlgoParams {
    private String algoId;
    private String algoName;
    private boolean optionalCombEnabled;
    private boolean localization;
    private List<String> localizationParams;
    private List<String> mandatoryParams;
    private List<List<String>> conditionalMandatoryParams;
    private List<String> optionalParams;

    /**
     * Method to get the algo id.
     *
     * @return the algo id
     */
    public String getAlgoId() {
        return algoId;
    }

    /**
     * Method to set the algo id.
     *
     * @param algoId the algo id
     */
    public void setAlgoId(String algoId) {
        this.algoId = algoId;
    }

    /**
     * Method to get the algorithm name.
     *
     * @return the algorithm name
     */
    public String getAlgoName() {
        return algoName;
    }

    /**
     * Method to set the algorithm name.
     *
     * @param algoName the algorithm name
     */
    public void setAlgoName(String algoName) {
        this.algoName = algoName;
    }

    /**
     * Method to check whether the optional combination is enabled.
     *
     * @return true if optional combination enabled and false if not
     */
    public boolean isOptionalCombEnabled() {
        return optionalCombEnabled;
    }

    /**
     * Method to set optional combination enabled.
     *
     * @param optionalCombEnabled set true to enable and false to disable
     */
    public void setOptionalCombEnabled(boolean optionalCombEnabled) {
        this.optionalCombEnabled = optionalCombEnabled;
    }

    /**
     * Method to check whether localization is enabled.
     *
     * @return true if localization enabled and false if not
     */
    public boolean isLocalization() {
        return localization;
    }

    /**
     * Method to set the location enabled flag.
     *
     * @param localization set true to enabled and false to disable
     */
    public void setLocalization(boolean localization) {
        this.localization = localization;
    }

    /**
     * Method to get the localization parameters.
     *
     * @return the localization parameters
     */
    public List<String> getLocalizationParams() {
        return localizationParams;
    }

    /**
     * Method to set the localization parameters.
     *
     * @param localizationParams the localization parameters
     */
    public void setLocalizationParams(List<String> localizationParams) {
        this.localizationParams = localizationParams;
    }

    /**
     * Method to get the mandatory parameters.
     *
     * @return the mandatory parameters
     */
    public List<String> getMandatoryParams() {
        return mandatoryParams;
    }

    /**
     * Method to set the mandatory parameters.
     *
     * @param mandatoryParams the mandatory parameters
     */
    public void setMandatoryParams(List<String> mandatoryParams) {
        this.mandatoryParams = mandatoryParams;
    }

    /**
     * Method to get the conditional mandatory parameters.
     *
     * @return the conditional mandatory parameters
     */
    public List<List<String>> getConditionalMandatoryParams() {
        return conditionalMandatoryParams;
    }

    /**
     * Method to set the conditional mandatory parameters.
     *
     * @param conditionalMandatoryParams the conditional mandatory parameters
     */
    public void setConditionalMandatoryParams(List<List<String>> conditionalMandatoryParams) {
        this.conditionalMandatoryParams = conditionalMandatoryParams;
    }

    /**
     * Method to get the optional parameters
     *
     * @return the optional parameters
     */
    public List<String> getOptionalParams() {
        return optionalParams;
    }

    /**
     * Method to set the optional parameters
     *
     * @param optionalParams the optional parameters
     */
    public void setOptionalParams(List<String> optionalParams) {
        this.optionalParams = optionalParams;
    }
}
