package com.zone24x7.ibrac.recengine.controller;

import com.zone24x7.ibrac.recengine.exception.ErrorCode;
import com.zone24x7.ibrac.recengine.exception.InputValidationException;
import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.strategy.PlacementTask;
import com.zone24x7.ibrac.recengine.strategy.PlacementTaskFactory;
import com.zone24x7.ibrac.recengine.util.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;

/**
 * Rest Controller class for serving recommendations.
 */
@RestController
public class RecController {

    @Autowired
    @Qualifier("cachedThreadPoolTaskExecutor")
    private ExecutorService cachedTaskExecutorService;

    @Autowired
    private PlacementTaskFactory placementTaskFactory;

    @Autowired
    private ChannelContextParamsDecoder channelContextParamsDecoder;

    @Autowired
    @Qualifier("inputParamValidationPattern")
    private Pattern inputParamValidationPattern;

    @Autowired
    @Qualifier("inputParamValidationPatternExtraChars")
    private Pattern inputParamValidationPatternExtraChars;

    private static final Logger logger = LoggerFactory.getLogger(RecController.class);

    /**
     * Controller method to get recommendations.
     *
     * @param channelId                channel id.
     * @param pageId                   page id.
     * @param placeholderIds           placeholder id.
     * @param channelContextParameters channel context parameters.
     * @return the recommendations json as a http 200 ok response.
     */
    @GetMapping(path = "/recengine/v1/recommendations", produces = "application/json")
    public ResponseEntity<Object> getRecommendation(@RequestParam(value = "cid", required = false) String channelId,
                                                    @RequestParam(value = "pgid", required = false) String pageId,
                                                    @RequestParam(value = "plids", required = false) String placeholderIds,
                                                    @RequestParam(value = "ccp", required = false) String channelContextParameters) {

        //todo: get request id
        String requestId = "REQUEST_ID";

        //validate mandatory input parameters
        validateInputParameters(channelId, pageId, placeholderIds, requestId);
        // Decode the placement ids
        List<PlaceholderId> decodedPlacementIds = PlaceholderIdsDecoder.decodePlaceholderIds(placeholderIds);

        if (decodedPlacementIds.isEmpty()) {
            throw new InputValidationException(ErrorCode.RE1006.toString());
        }

        Map<String, String> channelContextParamsMap = generateFilteredChannelContextParamsMapFromBase64String(channelContextParameters, requestId);

        if (channelContextParamsMap == null) {
            throw new InputValidationException(ErrorCode.RE1007.toString());
        }

        return getRecommendationResult(channelId, pageId, decodedPlacementIds, channelContextParamsMap, requestId);
    }

    /**
     * Method to validate the input params for rec generation.
     *
     * @param channelId      id of the channel.
     * @param pageId         id of the page.
     * @param placeholderIds ids of the placeholders.
     * @param requestId      id of the request.
     */
    private void validateInputParameters(String channelId, String pageId, String placeholderIds, String requestId) {
        //Check if the required parameters are all provided
        if (channelId == null) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Missing mandatory parameter cid: {}", requestId, channelId);
            throw new InputValidationException(ErrorCode.RE1000.toString());
        }

        if (StringUtils.isBlank(channelId) || !ValidationUtilities.matchesPattern(inputParamValidationPattern, channelId)) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Invalid mandatory parameter cid: {}", requestId, channelId);
            throw new InputValidationException(ErrorCode.RE1003.toString());
        }

        if (pageId == null) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Missing mandatory parameter pgid: {}", requestId, pageId);
            throw new InputValidationException(ErrorCode.RE1001.toString());
        }

        if (StringUtils.isBlank(pageId) || !ValidationUtilities.matchesPattern(inputParamValidationPattern, pageId)) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Invalid mandatory parameter pgid: {}", requestId, pageId);
            throw new InputValidationException(ErrorCode.RE1004.toString());
        }

        if (placeholderIds == null) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Missing mandatory parameter plids: {}", requestId, placeholderIds);
            throw new InputValidationException(ErrorCode.RE1002.toString());
        }

        if (StringUtils.isBlank(placeholderIds) || !ValidationUtilities.matchesPattern(inputParamValidationPatternExtraChars, placeholderIds)) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Invalid mandatory parameter plids: {}", requestId, placeholderIds);
            throw new InputValidationException(ErrorCode.RE1005.toString());
        }
    }

    /**
     * Method to generate the filtered channel context parameter map from the base 64 encoded context param string.
     *
     * @param channelContextParameters the channel context parameter string.
     * @param requestId                id of the request.
     * @return the generated channel context parameter map
     */
    private Map<String, String> generateFilteredChannelContextParamsMapFromBase64String(String channelContextParameters, String requestId) {
        if (StringUtils.isEmpty(channelContextParameters)) {
            return Collections.emptyMap();
        }

        String urlDecodedString = channelContextParamsDecoder.urlDecode(channelContextParameters);
        Map<String, String> urlDecodedCcpMap = channelContextParamsDecoder.deserializeFromBase64StringToMap(urlDecodedString);

        // filter the ccp map by the whitelisted ccp keys
        if (MapUtils.isNotEmpty(urlDecodedCcpMap)) {
            logger.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Filtering decoded ccp map: {}", requestId, urlDecodedCcpMap);
            return CcpProcessorUtilities.filterChannelContextParamMap(urlDecodedCcpMap);
        }

        return urlDecodedCcpMap;
    }

    /**
     * Method to get the recommendation result.
     *
     * @param channelId               the channel Id
     * @param pageId                  the page Id
     * @param placeholderIds          the placement Ids
     * @param channelContextParamsMap the channel context parameters map
     * @param requestId               the request Id
     * @return generated recommendation result
     */
    private ResponseEntity<Object> getRecommendationResult(String channelId, String pageId, List<PlaceholderId> placeholderIds, Map<String, String> channelContextParamsMap, String requestId) {
        logger.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Received ccp: {}", requestId, channelContextParamsMap);

        RecResult recResult = getRecResultForPlaceholder(channelId, pageId, placeholderIds.get(0), channelContextParamsMap, requestId);

        if (recResult != null) {
            RecResponsePayload recResponsePayload = new RecResponsePayload();
            recResponsePayload.setCid(channelId);
            recResponsePayload.setPgId(pageId);
            recResponsePayload.setRecommendations(Collections.singletonList(recResult));

            return ResponseEntity.ok()
                                 .header(StringConstants.REC_BUNDLE_PARAMS, "REC_BUNDLE_PARAMS") //todo: modify header as per the format
                                 .body(new RecResponse(recResponsePayload));
        }

        return null;
    }

    /**
     * Method to get placeholder based rec result.
     *
     * @param channelId               id of the channel.
     * @param pageId                  id of the page.
     * @param placeholderId           placeholder id.
     * @param channelContextParamsMap channel context params map.
     * @param requestId               id of the request.
     * @return {@link RecResult} instance.
     */
    private RecResult getRecResultForPlaceholder(String channelId, String pageId, PlaceholderId placeholderId, Map<String, String> channelContextParamsMap, String requestId) {
        RecCycleStatus recCycleStatus = new RecCycleStatus(requestId);
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannelId(channelId);
        recInputParams.setPageId(pageId);
        recInputParams.setCcp(new HashMap<>(channelContextParamsMap));
        recInputParams.setPlaceholderId(placeholderId);

        //Submit the placement task for the given channel, page & placeholder to get rec results.
        PlacementTask placementTask = placementTaskFactory.create(recInputParams, recCycleStatus);

        try {
            return cachedTaskExecutorService.submit(placementTask).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Placement task had an error. Channel ID: {}, Page ID: {}, Placeholder ID: {}",
                         requestId,
                         channelId,
                         pageId,
                         placeholderId,
                         e);
        }

        return null;
    }
}
