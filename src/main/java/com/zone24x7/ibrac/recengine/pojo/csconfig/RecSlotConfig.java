package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.Valid;
import java.util.List;

/**
 * RecSlot Configs pojo class
 */
public class RecSlotConfig {
    @Valid
    private List<RecSlot> recSlots;

    /**
     * Get recSlot config
     *
     * @return recSlot config
     */
    public List<RecSlot> getRecSlots() {
        return recSlots;
    }

    /**
     * Set recSlots
     *
     * @param recSlots recSlots to set
     */
    public void setRecSlots(List<RecSlot> recSlots) {
        this.recSlots = recSlots;
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

        RecSlotConfig that = (RecSlotConfig) o;

        return recSlots != null ? recSlots.equals(that.recSlots) : that.recSlots == null;
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return recSlots != null ? recSlots.hashCode() : 0;
    }
}
