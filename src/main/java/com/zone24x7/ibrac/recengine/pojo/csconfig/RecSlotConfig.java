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
}
