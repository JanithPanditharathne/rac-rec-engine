package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleConfig;
import com.zone24x7.ibrac.recengine.pojo.csconfig.RecSlotConfig;
import com.zone24x7.ibrac.recengine.pojo.recbundle.ActiveBundleProviderConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ValidationException;
import javax.validation.Validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test RecActiveBundleConfigGenerator.
 */
public class RecActiveBundleConfigGeneratorTest {
    private static final String VALID_REC_SLOT_CONFIG = "{\"recSlots\":[{\"channel\":\"TCom\",\"page\":\"PDP\",\"placeholder\":\"Grid\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"101\"]}]}";
    private static final String VALID_BUNDLE_CONFIG = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}},{\"id\":\"2\",\"name\":\"Bundle 2\",\"type\":\"FLAT\",\"defaultLimit\":10,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"102\",\"name\":\"TopTrendingByAttributes\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending attr\",\"customDisplayText\":\"Top Trending Products attr\"}},{\"rank\":1,\"algorithm\":{\"id\":\"103\",\"name\":\"BestSellersByAttributes\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers attr\",\"customDisplayText\":\"Best Selling Products attr\"}}],\"algoCombineInfo\":{\"enableCombine\":true,\"combineDisplayText\":\"Combined Products\"}}]}";

    private static final String INVALID_REC_SLOT_CONFIG_WITH_FORMAT_ERROR = "abc:{{\"channel\":\"TCom\",\"page\":\"PDP\",\"placeholder\":\"Grid\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]}}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_FORMAT_ERROR = "abc:{{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}}";

    private static final String INVALID_REC_SLOT_CONFIG_WITH_CHANNEL_EMPTY = "{\"recSlots\":[{\"channel\":\"\",\"page\":\"PDP\",\"placeholder\":\"Grid\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"101\"]}]}";
    private static final String INVALID_REC_SLOT_CONFIG_WITH_CHANNEL_NULL = "{\"recSlots\":[{\"channel\":null,\"page\":\"PDP\",\"placeholder\":\"Grid\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"101\"]}]}";
    private static final String INVALID_REC_SLOT_CONFIG_WITH_PAGE_EMPTY = "{\"recSlots\":[{\"channel\":\"TCom\",\"page\":\"\",\"placeholder\":\"Grid\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"101\"]}]}";
    private static final String INVALID_REC_SLOT_CONFIG_WITH_PAGE_NULL = "{\"recSlots\":[{\"channel\":\"TCom\",\"page\":null,\"placeholder\":\"Grid\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"101\"]}]}";
    private static final String INVALID_REC_SLOT_CONFIG_WITH_PLACEHOLDER_EMPTY = "{\"recSlots\":[{\"channel\":\"TCom\",\"page\":\"PDP\",\"placeholder\":\"\",\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"101\"]}]}";
    private static final String INVALID_REC_SLOT_CONFIG_WITH_PLACEHOLDER_NULL = "{\"recSlots\":[{\"channel\":\"TCom\",\"page\":\"PDP\",\"placeholder\":null,\"ruleIds\":[\"700\",\"701\"],\"recIds\":[\"100\"]},{\"channel\":\"MCom\",\"page\":\"Home\",\"placeholder\":\"Horizontal\",\"ruleIds\":[\"708\",\"709\"],\"recIds\":[\"101\"]}]}";

    private static final String INVALID_BUNDLE_CONFIG_WITH_BUNDLE_ID_EMPTY = "{\"bundles\":[{\"id\":\"\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_BUNDLE_ID_NULL = "{\"bundles\":[{\"id\":null,\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_BUNDLE_NAME_EMPTY = "{\"bundles\":[{\"id\":\"1\",\"name\":\"\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_BUNDLE_NAME_NULL = "{\"bundles\":[{\"id\":\"1\",\"name\":null,\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_DEFAULT_LIMIT_NULL = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":null,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHMS_LIST_EMPTY = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHMS_LIST_NULL = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":null,\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_COMBINED_INFO_NULL = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":null}]}";

    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_OBJECT_NULL = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":null},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_ID_EMPTY = "{\"bundles\":[{\"id\":\"\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_ID_NULL = "{\"bundles\":[{\"id\":\"\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":null,\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_NAME_EMPTY = "{\"bundles\":[{\"id\":\"\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_NAME_NULL = "{\"bundles\":[{\"id\":\"\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":null,\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Best Sellers\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_DEFAULT_DISPLAY_TEXT_EMPTY = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"\",\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";
    private static final String INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_DEFAULT_DISPLAY_TEXT_NULL = "{\"bundles\":[{\"id\":\"1\",\"name\":\"Bundle 1\",\"type\":\"FLAT\",\"defaultLimit\":5,\"algorithms\":[{\"rank\":0,\"algorithm\":{\"id\":\"100\",\"name\":\"TopTrending\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":\"Top Trending\",\"customDisplayText\":\"Top Trending Products\"}},{\"rank\":1,\"algorithm\":{\"id\":\"101\",\"name\":\"BestSellers\",\"type\":\"FLAT_ALGO\",\"defaultDisplayText\":null,\"customDisplayText\":\"Best Selling Products\"}}],\"algoCombineInfo\":{\"enableCombine\":false,\"combineDisplayText\":null}}]}";

    private RecActiveBundleConfigGenerator recActiveBundleConfigGenerator;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        recActiveBundleConfigGenerator = new RecActiveBundleConfigGenerator();
    }

    /**
     * Test to verify that active bundle config was generated correctly for a valid config.
     *
     * @throws MalformedConfigurationException if a configuration is malformed
     */
    @Test
    public void should_generate_active_bundle_config_correctly_for_valid_configs() throws MalformedConfigurationException {
        ActiveBundleProviderConfig resultConfig = recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, VALID_BUNDLE_CONFIG);

        assertThat(resultConfig, is(notNullValue()));

        assertThat(resultConfig.getRecSlotMap(), aMapWithSize(equalTo(2)));
        assertThat(resultConfig.getRecSlotMap().keySet(), containsInAnyOrder("TCom_PDP_Grid", "MCom_Home_Horizontal"));
        assertThat(resultConfig.getRecSlotMap().get("TCom_PDP_Grid").getChannel(), is(equalTo("TCom")));
        assertThat(resultConfig.getRecSlotMap().get("TCom_PDP_Grid").getPage(), is(equalTo("PDP")));
        assertThat(resultConfig.getRecSlotMap().get("TCom_PDP_Grid").getPlaceholder(), is(equalTo("Grid")));
        assertThat(resultConfig.getRecSlotMap().get("TCom_PDP_Grid").getRuleIds(), containsInAnyOrder("700", "701"));
        assertThat(resultConfig.getRecSlotMap().get("TCom_PDP_Grid").getRecIds(), contains("100"));

        assertThat(resultConfig.getRecSlotMap().get("MCom_Home_Horizontal").getChannel(), is(equalTo("MCom")));
        assertThat(resultConfig.getRecSlotMap().get("MCom_Home_Horizontal").getPage(), is(equalTo("Home")));
        assertThat(resultConfig.getRecSlotMap().get("MCom_Home_Horizontal").getPlaceholder(), is(equalTo("Horizontal")));
        assertThat(resultConfig.getRecSlotMap().get("MCom_Home_Horizontal").getRuleIds(), containsInAnyOrder("708", "709"));
        assertThat(resultConfig.getRecSlotMap().get("MCom_Home_Horizontal").getRecIds(), contains("101"));

        assertThat(resultConfig.getBundleMap(), aMapWithSize(equalTo(2)));
        assertThat(resultConfig.getBundleMap().keySet(), containsInAnyOrder("1", "2"));

        assertThat(resultConfig.getBundleMap().get("1").getId(), is(equalTo("1")));
        assertThat(resultConfig.getBundleMap().get("1").getName(), is(equalTo("Bundle 1")));
        assertThat(resultConfig.getBundleMap().get("1").getType(), is(equalTo("FLAT")));
        assertThat(resultConfig.getBundleMap().get("1").getDefaultLimit(), is(equalTo(5)));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(0).getRank(), is(equalTo(0)));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(0).getAlgorithm().getId(), is(equalTo("100")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(0).getAlgorithm().getName(), is(equalTo("TopTrending")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(0).getAlgorithm().getType(), is(equalTo("FLAT_ALGO")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(0).getAlgorithm().getDefaultDisplayText(), is(equalTo("Top Trending")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(0).getAlgorithm().getCustomDisplayText(), is(equalTo("Top Trending Products")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(1).getRank(), is(equalTo(1)));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(1).getAlgorithm().getId(), is(equalTo("101")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(1).getAlgorithm().getName(), is(equalTo("BestSellers")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(1).getAlgorithm().getType(), is(equalTo("FLAT_ALGO")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(1).getAlgorithm().getDefaultDisplayText(), is(equalTo("Best Sellers")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgorithms().get(1).getAlgorithm().getCustomDisplayText(), is(equalTo("Best Selling Products")));
        assertThat(resultConfig.getBundleMap().get("1").getAlgoCombineInfo().isEnableCombine(), is(equalTo(false)));
        assertThat(resultConfig.getBundleMap().get("1").getAlgoCombineInfo().getCombineDisplayText(), is(nullValue()));

        assertThat(resultConfig.getBundleMap().get("2").getId(), is(equalTo("2")));
        assertThat(resultConfig.getBundleMap().get("2").getName(), is(equalTo("Bundle 2")));
        assertThat(resultConfig.getBundleMap().get("2").getType(), is(equalTo("FLAT")));
        assertThat(resultConfig.getBundleMap().get("2").getDefaultLimit(), is(equalTo(10)));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(0).getRank(), is(equalTo(0)));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(0).getAlgorithm().getId(), is(equalTo("102")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(0).getAlgorithm().getName(), is(equalTo("TopTrendingByAttributes")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(0).getAlgorithm().getType(), is(equalTo("FLAT_ALGO")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(0).getAlgorithm().getDefaultDisplayText(), is(equalTo("Top Trending attr")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(0).getAlgorithm().getCustomDisplayText(), is(equalTo("Top Trending Products attr")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(1).getRank(), is(equalTo(1)));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(1).getAlgorithm().getId(), is(equalTo("103")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(1).getAlgorithm().getName(), is(equalTo("BestSellersByAttributes")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(1).getAlgorithm().getType(), is(equalTo("FLAT_ALGO")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(1).getAlgorithm().getDefaultDisplayText(), is(equalTo("Best Sellers attr")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgorithms().get(1).getAlgorithm().getCustomDisplayText(), is(equalTo("Best Selling Products attr")));
        assertThat(resultConfig.getBundleMap().get("2").getAlgoCombineInfo().isEnableCombine(), is(equalTo(true)));
        assertThat(resultConfig.getBundleMap().get("2").getAlgoCombineInfo().getCombineDisplayText(), is(equalTo("Combined Products")));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when invalid format errors occur in configs.
     */
    @Test
    public void should_throw_malformed_exception_when_configs_have_invalid_format_errors() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(INVALID_REC_SLOT_CONFIG_WITH_FORMAT_ERROR, VALID_BUNDLE_CONFIG));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_FORMAT_ERROR));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when rec slot channel is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_rec_slot_channel_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(INVALID_REC_SLOT_CONFIG_WITH_CHANNEL_EMPTY, VALID_BUNDLE_CONFIG));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(INVALID_REC_SLOT_CONFIG_WITH_CHANNEL_NULL, VALID_BUNDLE_CONFIG));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when rec slot page is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_rec_slot_page_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(INVALID_REC_SLOT_CONFIG_WITH_PAGE_EMPTY, VALID_BUNDLE_CONFIG));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(INVALID_REC_SLOT_CONFIG_WITH_PAGE_NULL, VALID_BUNDLE_CONFIG));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when rec slot placeholder is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_rec_slot_placeholder_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(INVALID_REC_SLOT_CONFIG_WITH_PLACEHOLDER_EMPTY, VALID_BUNDLE_CONFIG));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(INVALID_REC_SLOT_CONFIG_WITH_PLACEHOLDER_NULL, VALID_BUNDLE_CONFIG));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config id is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_id_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_BUNDLE_ID_EMPTY));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_BUNDLE_ID_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config name is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_name_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_BUNDLE_NAME_EMPTY));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_BUNDLE_NAME_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config default limit is null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_default_limit_is_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_DEFAULT_LIMIT_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config algorithm list is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_algorithm_list_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHMS_LIST_EMPTY));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHMS_LIST_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config combine info is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_combine_info_is_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_COMBINED_INFO_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config algorithm object is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_algorithm_object_is_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_OBJECT_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config algorithm id is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_algorithm_id_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_ID_EMPTY));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_ID_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config algorithm name is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_algorithm_name_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_NAME_EMPTY));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_NAME_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when bundle config default display text is empty or null.
     */
    @Test
    public void should_throw_malformed_exception_when_bundle_config_algorithm_default_display_text_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_DEFAULT_DISPLAY_TEXT_EMPTY));
        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, INVALID_BUNDLE_CONFIG_WITH_ALGORITHM_DEFAULT_DISPLAY_TEXT_NULL));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when an error occurs in rec slot config validation.
     */
    @Test
    public void should_throw_malformed_exception_when_validator_error_occurs_in_rec_slot_config_validations() {
        Validator validator = mock(Validator.class);
        when(validator.validate(ArgumentMatchers.any(RecSlotConfig.class))).thenThrow(ValidationException.class);
        ReflectionTestUtils.setField(recActiveBundleConfigGenerator, "validator", validator);

        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, VALID_BUNDLE_CONFIG));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when an error occurs in bundle config validation.
     */
    @Test
    public void should_throw_malformed_exception_when_validator_error_occurs_in_bundle_config_validations() {
        Validator validator = mock(Validator.class);
        when(validator.validate(ArgumentMatchers.any(BundleConfig.class))).thenThrow(ValidationException.class);
        ReflectionTestUtils.setField(recActiveBundleConfigGenerator, "validator", validator);

        assertThrows(MalformedConfigurationException.class, () -> recActiveBundleConfigGenerator.generateConfiguration(VALID_REC_SLOT_CONFIG, VALID_BUNDLE_CONFIG));
    }
}