package com.zone24x7.ibrac.recengine.pojo.tableconfigs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test TableConfigInfo.
 */
public class TableConfigInfoTest {
    private TableConfigInfo tableConfigInfo;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        tableConfigInfo = new TableConfigInfo();
        tableConfigInfo.setTableName("Recommendations");
        tableConfigInfo.setColumnFamily("Rec");
        tableConfigInfo.setQualifier("100");
        tableConfigInfo.setDescription("Algo 100");
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(tableConfigInfo.getTableName(), is(equalTo("Recommendations")));
        assertThat(tableConfigInfo.getColumnFamily(), is(equalTo("Rec")));
        assertThat(tableConfigInfo.getQualifier(), is(equalTo("100")));
        assertThat(tableConfigInfo.getDescription(), is(equalTo("Algo 100")));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(tableConfigInfo.equals(new Integer(3)), is(equalTo(false)));

        TableConfigInfo tableConfigInfo2 = null;
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));

        tableConfigInfo2 = new TableConfigInfo();
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setTableName("Invalid table");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setTableName("Recommendations");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setColumnFamily("Invalid CF");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setColumnFamily("Rec");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setQualifier("Invalid Q");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setQualifier("100");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setDescription("Invalid Algo");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(false)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(false)));

        tableConfigInfo2.setDescription("Algo 100");
        assertThat(tableConfigInfo.equals(tableConfigInfo2), is(equalTo(true)));
        assertThat(tableConfigInfo2.equals(tableConfigInfo), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(tableConfigInfo.hashCode(), is(equalTo(1185261929)));
    }
}