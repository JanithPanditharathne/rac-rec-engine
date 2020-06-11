package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.recommendation.curator.RecommendedProductsCurator;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Class for producing algorithm result
 */
@Component
public class RecAlgorithmResultGenerator implements AlgorithmResultGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecAlgorithmResultGenerator.class);

    @Autowired
    private DatasourceAdapter datasourceAdapter;

    @Autowired
    private RecommendedProductsCurator recommendedProductsCurator;

    @Autowired
    @Qualifier("timeZoneId")
    private ZoneId recEngineTimezone;

    private static final String BACKUP_ALGO_ID = "-1";

    /**
     * Method to get the an algorithm result for a given algorithm id and channel context parameters.
     * Calls datasource adapter to take products recommended.
     * Calls Product curator to curate recommended product.
     * Creates algorithm result object and returns
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context parameters
     * @return the generated algorithm result
     */
    public AlgorithmResult getAlgorithmResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        AlgorithmResult algorithmResult = new AlgorithmResult();
        algorithmResult.setUsedCcp(ccp);
        algorithmResult.setAlgorithmId(algorithmId);

        List<String> result;

        try {
            result = datasourceAdapter.getResult(algorithmId, ccp, recCycleStatus);
        } catch (BaseConnectionException e) {
            //TODO: If client requires back up. Add backup products here.
            // Currently to avoid multiple calls in disaster scenario when one exception happens returns with empty products with algoId = -1
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "HBase call failed. Algo:{}, ccp:{}",
                         recCycleStatus.getRequestId(),
                         algorithmId,
                         ccp,
                         e);
            algorithmResult.setAlgorithmId(BACKUP_ALGO_ID);
            recCycleStatus.indicateExceptionInCallingHBase();
            return algorithmResult;
        }

        List<Product> products = recommendedProductsCurator.getProducts(result, ZonedDateTime.now(recEngineTimezone), recCycleStatus);

        algorithmResult.setRecProducts(products);
        return algorithmResult;
    }
}
