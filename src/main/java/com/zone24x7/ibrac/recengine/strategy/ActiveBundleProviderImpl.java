package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import org.springframework.stereotype.Component;

@Component
public class ActiveBundleProviderImpl implements ActiveBundleProvider {
    @Override
    public ActiveBundle getActiveBundle(InputParams inputParams) {
        //TODO: Write logic
        ActiveBundle activeBundle = new ActiveBundle();
        activeBundle.setType("flat");
        return activeBundle;
    }
}
