package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import org.springframework.stereotype.Component;

/**
 * Responsible for generating an active bundle object considering input parameters.
 */
@Component
public class RecActiveBundleProvider implements ActiveBundleProvider {

    /**
     * Generates an active bundle object execution details according to the given input parameters.
     *
     * @param recInputParams input parameters received.
     * @return an active bundle.
     */
    @Override
    public ActiveBundle getActiveBundle(RecInputParams recInputParams) {
        //TODO: Write logic
        ActiveBundle activeBundle = new ActiveBundle();
        activeBundle.setType("flat");
        return activeBundle;
    }
}
