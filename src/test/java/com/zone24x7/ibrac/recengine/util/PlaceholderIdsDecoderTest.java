package com.zone24x7.ibrac.recengine.util;

import com.zone24x7.ibrac.recengine.pojo.PlaceholderId;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

/**
 * Class to test PlaceholderIdsDecoder Util.
 */
public class PlaceholderIdsDecoderTest {

    private static final String HORIZONTAL_1 = "horizontal1";
    private static final String HORIZONTAL_2 = "horizontal2";
    private static final String PLACEHOLDER_SEPARATOR = "|";

    /**
     * Test to verify an empty placeholder list is returned if the limit is not numeric.
     */
    @Test
    public void should_return_an_empty_placeholder_list_if_the_limit_is_non_numeric() {
        String placeHolderString = HORIZONTAL_1 + PLACEHOLDER_SEPARATOR + "abc";
        List<PlaceholderId> decodedPlaceholders = PlaceholderIdsDecoder.decodePlaceholderIds(placeHolderString);
        assertThat(decodedPlaceholders, Matchers.is(empty()));
    }

    /**
     * Test to verify an empty placeholder list is returned if only limit is specified.
     */
    @Test
    public void should_send_an_empty_placeholder_list_when_only_limit_is_specified() {
        String placeHolderString = StringUtils.EMPTY + PLACEHOLDER_SEPARATOR + "4";
        List<PlaceholderId> decodedPlaceholders = PlaceholderIdsDecoder.decodePlaceholderIds(placeHolderString);
        assertThat(decodedPlaceholders, empty());
    }

    /**
     * Test to verify a placeholder list is returned when the specified limit is blank.
     */
    @Test
    public void should_return_placeholder_list_when_the_specified_limit_is_empty() {
        String placeHolderString = HORIZONTAL_1 + PLACEHOLDER_SEPARATOR + "";
        List<PlaceholderId> decodedPlaceholders = PlaceholderIdsDecoder.decodePlaceholderIds(placeHolderString);
        assertThat(decodedPlaceholders, hasSize(1));
        assertThat(decodedPlaceholders.get(0).getId(), is(HORIZONTAL_1));
    }

    /**
     * Test to verify a placeholder list is returned as expected.
     */
    @Test
    public void should_return_a_placeholder_list_as_expected() {
        String placeHolderString = HORIZONTAL_1 + PLACEHOLDER_SEPARATOR + "10";
        List<PlaceholderId> decodedPlaceholders = PlaceholderIdsDecoder.decodePlaceholderIds(placeHolderString);
        assertThat(decodedPlaceholders, hasSize(1));
        assertThat(decodedPlaceholders.get(0).getId(), is(HORIZONTAL_1));
        assertThat(decodedPlaceholders.get(0).getRecommendationLimit(), is(10));
    }

    /**
     * Test to verify an empty placeholder list will be returned when multiple placeholders are not separated by comma.
     */
    @Test
    public void should_send_an_empty_placeholder_list_when_multiple_placeholders_are_not_separated_by_comma() {
        String placeHolderString = HORIZONTAL_1 + PLACEHOLDER_SEPARATOR + "10" + HORIZONTAL_2 + PLACEHOLDER_SEPARATOR + "3";
        List<PlaceholderId> decodedPlaceholders = PlaceholderIdsDecoder.decodePlaceholderIds(placeHolderString);

        assertThat(decodedPlaceholders, empty());
    }

    /**
     * Test to verify only one placeholder is returned when multiple placeholders passed for the same placeholder.
     */
    @Test
    public void should_return_the_first_placeholder_when_multiple_values_passed_for_the_same_placeholder() {
        String placeHolderString = HORIZONTAL_1 + PLACEHOLDER_SEPARATOR + "2" + "," + HORIZONTAL_1 + PLACEHOLDER_SEPARATOR + "3";
        List<PlaceholderId> decodedPlaceholders = PlaceholderIdsDecoder.decodePlaceholderIds(placeHolderString);

        assertThat(decodedPlaceholders, hasSize(1));
        assertThat(decodedPlaceholders.get(0).getId(), is(HORIZONTAL_1));
        assertThat(decodedPlaceholders.get(0).getRecommendationLimit(), is(2));
    }

    /**
     * Test to verify a placeholder is returned without a limit, when the limit is not specified.
     */
    @Test
    public void should_return_the_placeholder_without_a_limit_when_the_limit_is_not_specified() {
        List<PlaceholderId> decodedPlaceholders = PlaceholderIdsDecoder.decodePlaceholderIds(HORIZONTAL_1);

        assertThat(decodedPlaceholders, hasSize(1));
        assertThat(decodedPlaceholders.get(0).getId(), is(HORIZONTAL_1));
        assertThat(decodedPlaceholders.get(0).getRecommendationLimit(), nullValue());
    }
}