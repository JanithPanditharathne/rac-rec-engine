package com.zone24x7.ibrac.recengine.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test rule utils.
 */
public final class RuleUtilsTest {

    /**
     * Test to verify that double is converted correctly.
     */
    @Test
    public void should_convert_string_to_double() {
        assertThat(RuleUtils.toDouble("5.99"), is(equalTo(5.99)));
    }

    /**
     * Test to verify that integer is converted correctly.
     */
    @Test
    public void should_convert_string_to_int() {
        assertThat(RuleUtils.toInteger("5"), is(equalTo(5)));
    }

    /**
     * Test to verify that null is returned if value is null when converting to double.
     */
    @Test
    public void should_return_null_if_value_is_null_when_converting_to_double() {
        assertThat(RuleUtils.toDouble(null), is(equalTo(null)));
    }

    /**
     * Test to verify that null is returned if value is empty when converting to double.
     */
    @Test
    public void should_return_null_if_value_is_empty_when_converting_to_double() {
        assertThat(RuleUtils.toDouble(""), is(equalTo(null)));
    }

    /**
     * Test to verify that null is returned if value is null when converting to integer.
     */
    @Test
    public void should_return_null_if_value_is_null_when_converting_to_integer() {
        assertThat(RuleUtils.toInteger(null), is(equalTo(null)));
    }

    /**
     * Test to verify that null is returned if value is empty when converting to integer.
     */
    @Test
    public void should_return_null_if_value_is_empty_when_converting_to_integer() {
        assertThat(RuleUtils.toInteger(""), is(equalTo(null)));
    }

    /**
     * Test to verify that float is converted correctly.
     */
    @Test
    public void should_convert_string_to_float() {
        assertThat(RuleUtils.toFloat("5.1"), is(equalTo(5.1f)));
    }

    /**
     * Test to verify that boolean is converted correctly.
     */
    @Test
    public void should_convert_string_to_boolean() {
        assertThat(RuleUtils.toBoolean("true"), is(equalTo(true)));
    }

    /**
     * Test to verify that null is returned if value is null when converting to float.
     */
    @Test
    public void should_return_null_if_value_is_null_when_converting_to_float() {
        assertThat(RuleUtils.toFloat(null), is(equalTo(null)));
    }

    /**
     * Test to verify that null is returned if value is empty when converting to float.
     */
    @Test
    public void should_return_null_if_value_is_empty_when_converting_to_float() {
        assertThat(RuleUtils.toFloat(""), is(equalTo(null)));
    }

    /**
     * Test to verify that null is returned if value is null when converting to boolean.
     */
    @Test
    public void should_return_null_if_value_is_null_when_converting_to_boolean() {
        assertThat(RuleUtils.toBoolean(null), is(equalTo(false)));
    }

    /**
     * Test to verify that null is returned if value is empty when converting to boolean.
     */
    @Test
    public void should_return_null_if_value_is_empty_when_converting_to_boolean() {
        assertThat(RuleUtils.toBoolean(""), is(equalTo(false)));
    }
}