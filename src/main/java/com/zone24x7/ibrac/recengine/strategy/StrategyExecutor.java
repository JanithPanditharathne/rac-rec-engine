package com.zone24x7.ibrac.recengine.strategy;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;

/**
 * Class to execute strategies.
 *
 */
public abstract class StrategyExecutor {
    protected StrategyExecutor nextExecutor;

    /**
     * Method to set the next strategy executor.
     *
     * @param nextExecutor Next strategy executor.
     */
    public void setNextExecutor(StrategyExecutor nextExecutor) {
        this.nextExecutor = nextExecutor;
    }

    /**
     * Method to execute a strategy.
     *
     * @param inputParams  wrapper object for inputs
     * @return Experience generation strategy.
     */
    public abstract RecResult<?,?> execute(InputParams inputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus);
}
