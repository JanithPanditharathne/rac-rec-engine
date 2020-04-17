package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.InputParams;

public interface ActiveBundleProvider {
    ActiveBundle getActiveBundle(InputParams inputParams);
}

