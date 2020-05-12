package com.zone24x7.ibrac.recengine.pojo.csconfig;

/**
 * Class to represent algorithm combine information.
 */
public class AlgoCombineInfo {
    private boolean enableCombine;
    private String combineDisplayText;

    private static final int HASH_SEED = 31;

    /**
     * Method to check whether combine is enabled.
     *
     * @return true if combined is enabled, false if not
     */
    public boolean isEnableCombine() {
        return enableCombine;
    }

    /**
     * Method to set combine enabled state.
     *
     * @param enableCombine true to enable combine and false to disable
     */
    public void setEnableCombine(boolean enableCombine) {
        this.enableCombine = enableCombine;
    }

    /**
     * Method to get the combined display text.
     *
     * @return the combined display text
     */
    public String getCombineDisplayText() {
        return combineDisplayText;
    }

    /**
     * Method to set the combined display text.
     *
     * @param combineDisplayText the combined display text
     */
    public void setCombineDisplayText(String combineDisplayText) {
        this.combineDisplayText = combineDisplayText;
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

        AlgoCombineInfo that = (AlgoCombineInfo) o;

        if (enableCombine != that.enableCombine) {
            return false;
        }

        return combineDisplayText != null ? combineDisplayText.equals(that.combineDisplayText) : that.combineDisplayText == null;
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = (enableCombine ? 1 : 0);
        result = HASH_SEED * result + (combineDisplayText != null ? combineDisplayText.hashCode() : 0);
        return result;
    }
}
