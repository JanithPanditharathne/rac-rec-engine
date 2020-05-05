package com.zone24x7.ibrac.recengine.configuration.sync;

/**
 * The enum of the configurations types.
 */
public enum CsConfigurationTypes {
    /**
     * Rule configurations type flag.
     */
    RULE_CONFIG("ruleConfig"),

    /**
     * Bundle configurations type flag.
     */
    BUNDLE_CONFIG("bundleConfig"),

    /**
     * Rec configurations type flag.
     */
    REC_CONFIG("recConfig"),

    /**
     * Rec slot configurations type flag.
     */
    REC_SLOT_CONFIG("recSlotConfig");

    private String confType;

    /**
     * The default constructor.
     *
     * @param confType the configurations type in string.
     */
    CsConfigurationTypes(final String confType) {
        this.confType = confType;
    }

    /**
     * ToString override implementation of configurations type.
     *
     * @return the string out put of configuration type  value.
     */
    @Override
    public String toString() {
        return confType;
    }
}
