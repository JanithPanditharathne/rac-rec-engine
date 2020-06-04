package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.exceptions.SetupException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import com.zone24x7.ibrac.recengine.recbundle.ActiveBundleProvider;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Recommendation Generation strategy executor.
 */
@Component
public class RecGenerationStrategyExecutorEngine implements RecGenerationEngine {

    @Autowired
    @Qualifier("strategyExecutors")
    private StrategyExecutor strategyExecutor;

    @Autowired
    private ActiveBundleProvider activeBundleProvider;

    @Log
    private static Logger logger;

    /**
     * Returns the result provided by the implementation strategy based of the active bundle generated.
     *
     * @param recInputParams input parameters received.
     * @param recCycleStatus recCycle status.
     * @return result returned from the strategy.
     */
    public RecResult<?> getResult(RecInputParams recInputParams, RecCycleStatus recCycleStatus) {
        Optional<ActiveBundle> activeBundle = Optional.empty();

        try {
            activeBundle = activeBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        } catch (SetupException e) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Error occurred when trying to retrieve active bundle.", recCycleStatus.getRequestId(), e);
        }

        if (!activeBundle.isPresent()) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "No active bundle found for : Channel:{}, Page: {}, Placeholder: {}",
                    recCycleStatus.getRequestId(),
                    recInputParams.getChannel(),
                    recInputParams.getPage(),
                    recInputParams.getPlaceholder());
            return null;
        }

        return strategyExecutor.execute(recInputParams, activeBundle.get(), recCycleStatus);
    }
}