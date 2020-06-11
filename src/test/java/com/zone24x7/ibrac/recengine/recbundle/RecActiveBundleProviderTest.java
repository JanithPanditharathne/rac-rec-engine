package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.exceptions.SetupException;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Rec;
import com.zone24x7.ibrac.recengine.pojo.recbundle.ActiveBundleProviderConfig;
import com.zone24x7.ibrac.recengine.rules.recrules.executors.RecRuleExecutor;
import com.zone24x7.ibrac.recengine.util.JsonPojoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test the RecActiveBundleProvider.
 */
public class RecActiveBundleProviderTest {
    private static final String VALID_REC_SLOT_CONFIG = "{\"recSlots\":[{\"channel\":\"TCom\",\"page\":\"PDP\",\"placeholder\":\"Grid\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"400\",\"401\",\"402\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"403\",\"404\",\"405\"]},{\"channel\":\"WebStore\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[]},{\"channel\":\"TCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"444\",\"445\"]}]}";
    private static final String VALID_BUNDLE_CONFIG = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":8,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":true,\"combineDisplayText\":\"ABC\"}},{\"id\":\"2\",\"name\":\"Bundle 2\",\"type\":\"FLAT\",\"defaultLimit\":10,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"102\",\"name\":\"TopTrendingByAttributes\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending attr\",\"customDisplayText\":\"Top Trending Products attr\"}},{\"rank\":1,\"algorithm\":{\"id\":\"103\",\"name\":\"BestSellersByAttributes\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers attr\",\"customDisplayText\":\"Best Selling Products attr\"}}],\"algoCombineInfo\":{\"enableCombine\":true,\"combineDisplayText\":\"Combined Products\"}},{\"id\":\"3\",\"name\":\"Bundle 3\",\"type\":\"FLAT\",\"defaultLimit\":9,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"104\",\"name\":\"View View \",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"View view\",\"customDisplayText\":\"View viewed products\"}}],\"algoCombineInfo\":{\"enableCombine\":true,\"combineDisplayText\":\"Combined Products\"}}]}";

    private static final String REC_CONFIG_400 = "{\"id\":\"400\",\"name\":\"Sample Rec Config 1\",\"type\":\"REGULAR\",\"matchingCondition\":\"department == \\\"shoes\\\"\",\"regularConfig\":{\"bundleId\":\"1\"},\"testConfig\":null}";
    private static final String REC_CONFIG_402 = "{\"id\":\"402\",\"name\":\"Sample Rec Config 3\",\"type\":\"REGULAR\",\"matchingCondition\":\"department == \\\"shoes\\\"\",\"regularConfig\":{\"bundleId\":\"3\"},\"testConfig\":null}";
    private static final String REC_CONFIG_403 = "{\"id\":\"403\",\"name\":\"Sample Rec Config 4\",\"type\":\"INVALID\",\"matchingCondition\":\"department == \\\"shoes\\\"\",\"regularConfig\":{\"bundleId\":\"3\"},\"testConfig\":null}";
    private static final String REC_CONFIG_404 = "{\"id\":\"404\",\"name\":\"Sample Rec Config 5\",\"type\":\"REGULAR\",\"matchingCondition\":\"department == \\\"shoes\\\"\",\"regularConfig\":null,\"testConfig\":null}";
    private static final String REC_CONFIG_405 = "{\"id\":\"405\",\"name\":\"Sample Rec Config 6\",\"type\":\"REGULAR\",\"matchingCondition\":\"department == \\\"shoes\\\"\",\"regularConfig\":{\"bundleId\":\"91\"},\"testConfig\":null}";

    private Logger logger;
    private RecRuleExecutor recRuleExecutor;
    private AlgoParamsValidator algoParamsValidator;
    private RecCycleStatus recCycleStatus;
    private Map<String, String> ccp;
    private ActiveBundleProviderConfig activeBundleProviderConfig;
    private RecActiveBundleProvider recActiveBundleProvider;

    /**
     * Setup method
     *
     * @throws MalformedConfigurationException if the configuration is malformed
     * @throws IOException                     if an error occurs while converting configuration to pojo
     */
    @BeforeEach
    public void setup() throws MalformedConfigurationException, IOException {
        logger = mock(Logger.class);
        recRuleExecutor = mock(RecRuleExecutor.class);
        algoParamsValidator = mock(AlgoParamsValidator.class);
        recCycleStatus = mock(RecCycleStatus.class);

        ccp = new HashMap<>();
        ccp.put("department", "shoes");

        RecActiveBundleConfigGenerator configGenerator = new RecActiveBundleConfigGenerator();

        activeBundleProviderConfig = configGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, VALID_BUNDLE_CONFIG);

        recActiveBundleProvider = new RecActiveBundleProvider();
        recActiveBundleProvider.setConfig(activeBundleProviderConfig);

        List<Rec> matchingRecs = new LinkedList<>();
        matchingRecs.add(JsonPojoConverter.toPojo(REC_CONFIG_400, Rec.class));
        matchingRecs.add(JsonPojoConverter.toPojo(REC_CONFIG_402, Rec.class));

        when(recRuleExecutor.getMatchingRecs(new HashSet<>(Arrays.asList("400", "401", "402")), ccp, recCycleStatus)).thenReturn(matchingRecs);
        when(recRuleExecutor.getMatchingRecs(new HashSet<>(Arrays.asList("444", "445")), ccp, recCycleStatus)).thenReturn(Collections.emptyList());

        when(algoParamsValidator.isValidForIncomingContext("100", ccp)).thenReturn(true);
        when(algoParamsValidator.isValidForIncomingContext("101", ccp)).thenReturn(false);

        ReflectionTestUtils.setField(recActiveBundleProvider, "recRuleExecutor", recRuleExecutor);
        ReflectionTestUtils.setField(recActiveBundleProvider, "algoParamsValidator", algoParamsValidator);
    }

    /**
     * Test to verify that the active bundle is generated correctly.
     *
     * @throws SetupException if the configurations are not setup
     */
    @Test
    public void should_generate_the_active_bundle_correctly() throws SetupException {
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("TCom");
        recInputParams.setPage("PDP");
        recInputParams.setPlaceholder("Grid");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);

        assertThat(activeBundle.isPresent(), is(equalTo(true)));
        assertThat(activeBundle.get().getId(), is(equalTo("1")));
        assertThat(activeBundle.get().getName(), is(equalTo("Bundle 1")));
        assertThat(activeBundle.get().getType(), is(equalTo("FLAT")));
        assertThat(activeBundle.get().getRecId(), is(equalTo("400")));
        assertThat(activeBundle.get().getLimitToApply(), is(equalTo(20)));
        assertThat(activeBundle.get().getPlacementFilteringRuleIds(), contains("700", "701"));
        assertThat(activeBundle.get().getAlgoCombineInfo().isEnableCombine(), is(equalTo(true)));
        assertThat(activeBundle.get().getAlgoCombineInfo().getCombineDisplayText(), is(equalTo("ABC")));
        assertThat(activeBundle.get().getValidAlgorithmListToExecute().size(), is(equalTo(1)));
        assertThat(activeBundle.get().getValidAlgorithmListToExecute().get(0).getId(), is(equalTo("100")));
    }

    /**
     * Test to verify that the active bundle is generated correctly with default limit.
     *
     * @throws SetupException if the configurations are not setup
     */
    @Test
    public void should_generate_the_active_bundle_correctly_with_default_limit() throws SetupException {
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("TCom");
        recInputParams.setPage("PDP");
        recInputParams.setPlaceholder("Grid");
        recInputParams.setLimit(null);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);

        assertThat(activeBundle.isPresent(), is(equalTo(true)));
        assertThat(activeBundle.get().getId(), is(equalTo("1")));
        assertThat(activeBundle.get().getName(), is(equalTo("Bundle 1")));
        assertThat(activeBundle.get().getType(), is(equalTo("FLAT")));
        assertThat(activeBundle.get().getRecId(), is(equalTo("400")));
        assertThat(activeBundle.get().getLimitToApply(), is(equalTo(8)));
        assertThat(activeBundle.get().getPlacementFilteringRuleIds(), contains("700", "701"));
        assertThat(activeBundle.get().getAlgoCombineInfo().isEnableCombine(), is(equalTo(true)));
        assertThat(activeBundle.get().getAlgoCombineInfo().getCombineDisplayText(), is(equalTo("ABC")));
        assertThat(activeBundle.get().getValidAlgorithmListToExecute().size(), is(equalTo(1)));
        assertThat(activeBundle.get().getValidAlgorithmListToExecute().get(0).getId(), is(equalTo("100")));
    }

    /**
     * Test to verify that the active bundle is generated correctly with backup default limit when default limit is null by any chance.
     *
     * @throws SetupException if the configurations are not setup
     */
    @Test
    public void should_generate_the_active_bundle_correctly_with_backup_default_limit() throws SetupException {
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("TCom");
        recInputParams.setPage("PDP");
        recInputParams.setPlaceholder("Grid");
        recInputParams.setLimit(null);
        recInputParams.setCcp(ccp);

        activeBundleProviderConfig.getBundleMap().get("1").setDefaultLimit(null);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);

        assertThat(activeBundle.isPresent(), is(equalTo(true)));
        assertThat(activeBundle.get().getId(), is(equalTo("1")));
        assertThat(activeBundle.get().getName(), is(equalTo("Bundle 1")));
        assertThat(activeBundle.get().getType(), is(equalTo("FLAT")));
        assertThat(activeBundle.get().getRecId(), is(equalTo("400")));
        assertThat(activeBundle.get().getLimitToApply(), is(equalTo(5)));
        assertThat(activeBundle.get().getPlacementFilteringRuleIds(), contains("700", "701"));
        assertThat(activeBundle.get().getAlgoCombineInfo().isEnableCombine(), is(equalTo(true)));
        assertThat(activeBundle.get().getAlgoCombineInfo().getCombineDisplayText(), is(equalTo("ABC")));
        assertThat(activeBundle.get().getValidAlgorithmListToExecute().size(), is(equalTo(1)));
        assertThat(activeBundle.get().getValidAlgorithmListToExecute().get(0).getId(), is(equalTo("100")));
    }

    /**
     * Test to verify whether setup exception is thrown when configurations are not set.
     */
    @Test
    public void should_throw_setup_exception_when_configuration_are_not_set() {
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("TCom");
        recInputParams.setPage("PDP");
        recInputParams.setPlaceholder("Grid");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        assertThrows(SetupException.class, () -> new RecActiveBundleProvider().getActiveBundle(recInputParams, recCycleStatus));
    }

    /**
     * Test to verify that optional empty is returned when rec slot is invalid.
     *
     * @throws SetupException if configurations are not set
     */
    @Test
    public void should_return_optional_empty_when_rec_slot_is_invalid() throws SetupException {
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("INVALID_CHANNEL");
        recInputParams.setPage("PDP");
        recInputParams.setPlaceholder("Grid");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        assertThat(activeBundle.isPresent(), is(equalTo(false)));
    }

    /**
     * Test to verify that optional empty is returned when rec ids are invalid in rec slot.
     *
     * @throws SetupException if configurations are not set
     */
    @Test
    public void should_return_optional_empty_when_rec_ids_are_invalid_in_rec_slot() throws SetupException {
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("WebStore");
        recInputParams.setPage("Home");
        recInputParams.setPlaceholder("Horizontal");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        assertThat(activeBundle.isPresent(), is(equalTo(false)));
    }

    /**
     * Test to verify that optional empty is returned when rule executor filters out all the recs.
     *
     * @throws SetupException if configurations are not set
     */
    @Test
    public void should_return_optional_empty_when_rec_rule_executor_filters_out_all_recs() throws SetupException {
        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("TCom");
        recInputParams.setPage("Home");
        recInputParams.setPlaceholder("Horizontal");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        assertThat(activeBundle.isPresent(), is(equalTo(false)));
    }

    /**
     * Test to verify that optional empty is returned when bundle has incorrect type.
     *
     * @throws SetupException if configurations are not set
     */
    @Test
    public void should_return_optional_empty_when_bundle_has_incorrect_type() throws SetupException, IOException {
        List<Rec> matchingRecs2 = new LinkedList<>();
        matchingRecs2.add(JsonPojoConverter.toPojo(REC_CONFIG_403, Rec.class));
        when(recRuleExecutor.getMatchingRecs(new HashSet<>(Arrays.asList("403", "404", "405")), ccp, recCycleStatus)).thenReturn(matchingRecs2);

        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("MCom");
        recInputParams.setPage("Home");
        recInputParams.setPlaceholder("Horizontal");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        assertThat(activeBundle.isPresent(), is(equalTo(false)));
    }

    /**
     * Test to verify that optional empty is returned when bundle type is regular and regular config is null.
     *
     * @throws SetupException if configurations are not set
     */
    @Test
    public void should_return_optional_empty_when_bundle_has_regular_type_and_regular_config_is_null() throws SetupException, IOException {
        List<Rec> matchingRecs2 = new LinkedList<>();
        matchingRecs2.add(JsonPojoConverter.toPojo(REC_CONFIG_404, Rec.class));
        when(recRuleExecutor.getMatchingRecs(new HashSet<>(Arrays.asList("403", "404", "405")), ccp, recCycleStatus)).thenReturn(matchingRecs2);

        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("MCom");
        recInputParams.setPage("Home");
        recInputParams.setPlaceholder("Horizontal");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        assertThat(activeBundle.isPresent(), is(equalTo(false)));
    }

    /**
     * Test to verify that optional empty is returned when bundle is not available in configurations.
     *
     * @throws SetupException if configurations are not set
     */
    @Test
    public void should_return_optional_empty_when_bundle_is_not_available_in_configurations() throws SetupException, IOException {
        List<Rec> matchingRecs2 = new LinkedList<>();
        matchingRecs2.add(JsonPojoConverter.toPojo(REC_CONFIG_405, Rec.class));
        when(recRuleExecutor.getMatchingRecs(new HashSet<>(Arrays.asList("403", "404", "405")), ccp, recCycleStatus)).thenReturn(matchingRecs2);

        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("MCom");
        recInputParams.setPage("Home");
        recInputParams.setPlaceholder("Horizontal");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        assertThat(activeBundle.isPresent(), is(equalTo(false)));
    }

    /**
     * Test to verify that optional empty is returned when bundle does not have valid algorithms for a given context.
     *
     * @throws SetupException if configurations are not set
     */
    @Test
    public void should_return_optional_empty_when_bundle_does_not_have_valid_algorithms_for_given_context() throws SetupException, IOException {
        when(algoParamsValidator.isValidForIncomingContext("100", ccp)).thenReturn(false);
        when(algoParamsValidator.isValidForIncomingContext("101", ccp)).thenReturn(false);

        RecInputParams recInputParams = new RecInputParams();
        recInputParams.setChannel("TCom");
        recInputParams.setPage("PDP");
        recInputParams.setPlaceholder("Grid");
        recInputParams.setLimit(20);
        recInputParams.setCcp(ccp);

        Optional<ActiveBundle> activeBundle = recActiveBundleProvider.getActiveBundle(recInputParams, recCycleStatus);
        assertThat(activeBundle.isPresent(), is(equalTo(false)));
    }
}