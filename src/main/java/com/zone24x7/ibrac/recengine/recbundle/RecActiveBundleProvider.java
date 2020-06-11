package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.exceptions.SetupException;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.csconfig.*;
import com.zone24x7.ibrac.recengine.pojo.recbundle.ActiveBundleProviderConfig;
import com.zone24x7.ibrac.recengine.rules.recrules.executors.RecRuleExecutor;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Implementation of active bundle provider for generating active bundle for the given input parameters.
 */
@Component
public class RecActiveBundleProvider implements ActiveBundleProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecActiveBundleProvider.class);

    private static final String BUNDLE_PROVIDER_NOT_CONFIGURED_ERROR_MSG = "Active bundle provider not configured yet.";
    private static final String REC_TYPE_REGULAR = "REGULAR";
    private static final Integer BACKUP_DEFAULT_LIMIT = 5;

    private Map<String, RecSlot> recSlotMap = new HashMap<>();
    private Map<String, Bundle> bundleMap = new HashMap<>();

    @Autowired
    private AlgoParamsValidator algoParamsValidator;

    @Autowired
    private RecRuleExecutor recRuleExecutor;

    /**
     * Method to generate an active bundle object execution details according to the given input parameters.
     *
     * @param recInputParams input parameters received
     * @param recCycleStatus the recommendation generation tracking object
     * @return an active bundle
     * @throws SetupException if rec slots and bundles are not setup
     */
    @Override
    public Optional<ActiveBundle> getActiveBundle(RecInputParams recInputParams, RecCycleStatus recCycleStatus) throws SetupException {
        if (MapUtils.isEmpty(recSlotMap) || MapUtils.isEmpty(bundleMap)) {
            throw new SetupException(BUNDLE_PROVIDER_NOT_CONFIGURED_ERROR_MSG);
        }

        RecSlot recSlotInfoForQuerying = new RecSlot();
        recSlotInfoForQuerying.setChannel(recInputParams.getChannel());
        recSlotInfoForQuerying.setPage(recInputParams.getPage());
        recSlotInfoForQuerying.setPlaceholder(recInputParams.getPlaceholder());

        RecSlot recSlot = recSlotMap.get(recSlotInfoForQuerying.getPlacementInfoAsString());

        if (recSlot == null) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Rec slot not found for placement : {}", recCycleStatus.getRequestId(), recSlotInfoForQuerying.getPlacementInfoAsString());
            return Optional.empty();
        }

        List<String> allRecIdsForRecSlot = recSlot.getRecIds();

        if (CollectionUtils.isEmpty(allRecIdsForRecSlot)) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Rec ids not found for Rec slot : {}", recCycleStatus.getRequestId(), recSlotInfoForQuerying.getPlacementInfoAsString());
            return Optional.empty();
        }

        List<Rec> recList = filterRecListForTheGivenContext(allRecIdsForRecSlot, recInputParams.getCcp(), recCycleStatus);

        if (CollectionUtils.isEmpty(recList)) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Matching Rec ids not found for given context. Rec slot : {}", recCycleStatus.getRequestId(), recSlotInfoForQuerying.getPlacementInfoAsString());
            return Optional.empty();
        }

        // currently zeroth rec id will be selected.
        // In future select rec id according to importance.
        Rec rec = recList.get(0);

        List<String> placementFilteringRuleIds = recSlot.getRuleIds();
        Bundle bundle = getBundleFromRec(rec, recCycleStatus);

        if (bundle == null) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Rec bundle not found for rec Id : {}", recCycleStatus.getRequestId(), rec.getId());
            return Optional.empty();
        }

        return generateActiveBundle(bundle, rec.getId(), recInputParams, placementFilteringRuleIds, recCycleStatus);
    }

    /**
     * Method to set the active bundle provider configurations.
     *
     * @param config the active bundle provider configurations
     */
    @Override
    public void setConfig(ActiveBundleProviderConfig config) {
        if (config.getRecSlotMap() != null && config.getBundleMap() != null) {
            this.recSlotMap = config.getRecSlotMap();
            this.bundleMap = config.getBundleMap();
        }
    }

    /**
     * Method to filter id rec list for the given context.
     *
     * @param recIdList      the rec id list
     * @param ccp            the channel context parameters
     * @param recCycleStatus the recommendation generation tracking object
     * @return the filtered rec list
     */
    private List<Rec> filterRecListForTheGivenContext(List<String> recIdList, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        return recRuleExecutor.getMatchingRecs(new HashSet<>(recIdList), ccp, recCycleStatus);
    }

    /**
     * Method to get the bundle from a given rec.
     *
     * @param rec            the rec
     * @param recCycleStatus the recommendation generation tracking object
     * @return the recommendation bundle
     */
    private Bundle getBundleFromRec(Rec rec, RecCycleStatus recCycleStatus) {
        String bundleId = null;

        // Currently default rec type is considered regular. If no type is sent, it is considered as regular.
        if (rec.getType() == null || rec.getType().equals(REC_TYPE_REGULAR)) {
            if (rec.getRegularConfig() == null) {
                LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Regular config null in Rec id : {}", recCycleStatus.getRequestId(), rec.getId());
                return null;
            }
            bundleId = rec.getRegularConfig().getBundleId();

        } else {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Unsupported rec type found. Rec id : {}", recCycleStatus.getRequestId(), rec.getId());
        }

        if (bundleId == null) {
            return null;
        }

        return bundleMap.get(bundleId);
    }

    /**
     * Method to generate the active bundle.
     *
     * @param bundle                    the recommendation bundle
     * @param recInputParams            the recommendation input parameters
     * @param placementFilteringRuleIds the placement filtering rule ids
     * @param recCycleStatus            the recommendation generation tracking object
     * @return the generated active bundle
     */
    private Optional<ActiveBundle> generateActiveBundle(Bundle bundle, String recId, RecInputParams recInputParams, List<String> placementFilteringRuleIds, RecCycleStatus recCycleStatus) {
        String id = bundle.getId();
        String name = bundle.getName();
        String type = bundle.getType();
        Integer limitToApply = getLimitToApply(recInputParams.getLimit(), bundle.getDefaultLimit());
        List<BundleAlgorithm> algorithms = filterAndGetValidAlgorithmsForTheContext(bundle.getAlgorithms(), recInputParams.getCcp());
        AlgoCombineInfo algoCombineInfo = bundle.getAlgoCombineInfo();

        if (CollectionUtils.isEmpty(algorithms)) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Valid algorithms not found for the given context. Bundle Id: {}", recCycleStatus.getRequestId(), bundle.getId());
            return Optional.empty();
        }

        return Optional.of(new ActiveBundle(id, name, type, recId, limitToApply, algorithms, algoCombineInfo, new HashSet<>(placementFilteringRuleIds)));
    }

    /**
     * Method to filter and get the valid algorithms for the given context.
     *
     * @param inputAlgorithms the input algorithms
     * @param ccp             the channel context parameters
     * @return the list of filtered algorithms
     */
    private List<BundleAlgorithm> filterAndGetValidAlgorithmsForTheContext(List<BundleAlgorithmContainer> inputAlgorithms, Map<String, String> ccp) {
        List<BundleAlgorithm> validAlgorithmList = new LinkedList<>();

        for (BundleAlgorithmContainer bundleAlgorithmContainer : inputAlgorithms) {
            BundleAlgorithm algorithm = bundleAlgorithmContainer.getAlgorithm();

            if (algoParamsValidator.isValidForIncomingContext(algorithm.getId(), ccp)) {
                validAlgorithmList.add(algorithm);
            }
        }

        return validAlgorithmList;
    }

    /**
     * Method to get the limit to apply.
     *
     * @param inputLimit   the input limit
     * @param defaultLimit the default limit
     * @return the limit to apply
     */
    private static Integer getLimitToApply(Integer inputLimit, Integer defaultLimit) {
        if (inputLimit != null && inputLimit > 0) {
            return inputLimit;
        } else if (defaultLimit != null && defaultLimit > 0) {
            return defaultLimit;
        } else {
            return BACKUP_DEFAULT_LIMIT;
        }
    }
}
