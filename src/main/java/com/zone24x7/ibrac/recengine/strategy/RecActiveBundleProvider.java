package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import org.springframework.stereotype.Component;

@Component
public class RecActiveBundleProvider implements ActiveBundleProvider {
    @Override
    public ActiveBundle getActiveBundle(RecInputParams recInputParams) {
        //TODO: Write logic
        ActiveBundle activeBundle = new ActiveBundle();
        activeBundle.setType("flat");
        return activeBundle;
    }
}
