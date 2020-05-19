package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Class to represent recommendation configuration.
 *
 */
public class RecConfig {

    @Valid
    @NotNull
    private List<Rec> recs;

    /**
     * Method to get the recs.
     *
     * @return the recs
     */
    public List<Rec> getRecs() {
        return recs;
    }

    /**
     * Method to set the recs.
     *
     * @param recs the recs
     */
    public void setRecs(List<Rec> recs) {
        this.recs = recs;
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

        RecConfig recConfig = (RecConfig) o;

        return recs != null ? recs.equals(recConfig.recs) : recConfig.recs == null;
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return recs != null ? recs.hashCode() : 0;
    }
}
