package com.zone24x7.ibrac.recengine.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class of ChannelContextParamsDecoder.
 */
class ChannelContextParamsDecoderTest {
    private ChannelContextParamsDecoder channelContextParamsDecoder;
    private Logger logger;
    private URLCodec urlCodec;

    //Map containing a valid decoded Channel Context Parameters object
    private static Map<String, String> completeCcpMap;

    private static final String VALID_BASE64_ENCODED_CCP = "eyJ3ZWJJZCI6IjEyMzQ1IiwiemlwY29kZSI6Ijk1MDM1Iiwic3RvcmVOdW1iZXIiOiI1Njc4OSIs%0D%0AImlwQWRkcmVzcyI6IjE5Mi4xNjguMS4yIiwiY3VzdG9tZXJFbWFpbCI6ImFiY2RAZ21haWwuY29t%0D%0AIiwiZGV2aWNlSWQiOiJEZXYxIiwiYnJhbmQiOiJOaWtlIiwiZGVwYXJ0bWVudCI6Ik1lbnMiLCJj%0D%0AYXRlZ29yeSI6IlRyb3VzZXJzIiwic3ViQ2F0ZWdvcnkiOiJEZW5pbXMifQ%3D%3D%0D%0A";
    private static final String INVALID_BASE64_ENCODED_CCP = "eyJ3ZWJJZ6IjEyMzQ1IiwiemlwY29kZSI6Ijk1MDM1Iiwic3RvcmVOdW1iZXIiOiI1Njc4OSIs%0D%0AImlwQWRkcmVzcyI6IjE5Mi4xNjguMS4yIiwiY3VzdG9tZXFpbCI6ImFiY2RAZ21haWwuY29t%0D%0AIiwiZGV2aWNlSWQiOiJEZXYxIiwiYnJhbmQiOiJOaWtlIiwiZGVwYXJ0bWVudCI6Ik1lbnMiLCJj%0D%0AYXRlZ29yeSI6IlRyb3VzZXJzIiwic3ViQ2F0ZWdvcnkiOiJEZW5pbXMifQ%3D%3D%0D%0A";
    private static final String EMPTY_CCP = "";

    @BeforeEach
    void setup() throws Exception {
        logger = mock(Logger.class);
        urlCodec = mock(URLCodec.class);

        channelContextParamsDecoder = new ChannelContextParamsDecoder();

        Field loggerField = ChannelContextParamsDecoder.class.getDeclaredField("LOGGER");
        CustomReflectionTestUtils.setFinalStaticField(loggerField, this.logger);

        completeCcpMap = new HashMap<>();
        completeCcpMap.put("webId", "12345");
        completeCcpMap.put("zipcode", "95035");
        completeCcpMap.put("storeNumber", "56789");
        completeCcpMap.put("ipAddress", "192.168.1.2");
        completeCcpMap.put("customerEmail", "abcd@gmail.com");
        completeCcpMap.put("deviceId", "Dev1");
        completeCcpMap.put("brand", "Nike");
        completeCcpMap.put("department", "Mens");
        completeCcpMap.put("category", "Trousers");
        completeCcpMap.put("subCategory", "Denims");
    }

    /**
     * Test to check whether the utility methods to decode the Base64 encoded and URL encoded Channel Context Parameter map can
     * properly decode a correctly encoded CCP
     */
    @Test
    public void should_be_able_to_decode_a_ccp_with_properly_url_encoded_base64_string() {
        String urlDecodedString = channelContextParamsDecoder.urlDecode(VALID_BASE64_ENCODED_CCP);
        Map<String, String> decodedMap = channelContextParamsDecoder.deserializeFromBase64StringToMap(urlDecodedString);
        assertThat(decodedMap, aMapWithSize(10));
    }

    /**
     * Test to check whether the utility method to decode the Base64 encoded and URL encoded Channel Context Parameter map will
     * return null when given a malformatted CCP string
     */
    @Test
    public void should_return_null_for_a_ccp_with_malformed_url_encoded_base64_string() {
        String urlDecodedString = channelContextParamsDecoder.urlDecode(INVALID_BASE64_ENCODED_CCP);
        Map<String, String> decodedMap = channelContextParamsDecoder.deserializeFromBase64StringToMap(urlDecodedString);
        assertThat(decodedMap, is(nullValue()));
    }

    /**
     * Test to check whether the utility method to decode the Base64 encoded and URL encoded Channel Context Parameter map will
     * return null when given an empty CCP string
     */
    @Test
    public void should_return_null_for_an_empty_ccp() {
        String urlDecodedString = channelContextParamsDecoder.urlDecode(EMPTY_CCP);
        Map<String, String> decodedMap = channelContextParamsDecoder.deserializeFromBase64StringToMap(urlDecodedString);
        assertThat(decodedMap, is(nullValue()));
    }

    /**
     * Test to check whether the utility method to decode the Base64 encoded and URL encoded Channel Context Parameter map will
     * return null when a null is given as the input CCP string
     */
    @Test
    public void should_return_null_when_a_null_is_specified_as_ccp() {
        String urlDecodedString = channelContextParamsDecoder.urlDecode(null);
        Map<String, String> decodedMap = channelContextParamsDecoder.deserializeFromBase64StringToMap(urlDecodedString);
        assertThat(decodedMap, is(nullValue()));
    }

    /**
     * Test to verify ccp encode returns an empty string if ccp is null when serializing ccp.
     */
    @Test
    public void should_return_an_empty_when_a_null_is_passed_as_ccp_to_encode() {
        String urlEncodedString = channelContextParamsDecoder.serializeMapToBase64String(null);
        assertThat(urlEncodedString, is(emptyOrNullString()));
    }

    /**
     * Test to verify url encode returns an empty string if url encode input is null.
     */
    @Test
    public void should_return_an_empty_when_a_null_string_is_passed_to_url_encode() {
        String urlEncodedString = channelContextParamsDecoder.urlEncode(null);
        assertThat(urlEncodedString, is(emptyOrNullString()));
    }

    /**
     * Test to verify url encode returns an empty string if url encode input is null.
     */
    @Test
    public void should_return_url_encoded_string_as_expected() {
        String urlEncodedString = channelContextParamsDecoder.urlEncode("cid=Web&pgid=Home&plids=Vertical");
        assertThat(urlEncodedString, is("cid%3DWeb%26pgid%3DHome%26plids%3DVertical"));
    }

    /**
     * Test to verify ccp map is serialized to a base 64 string as expected.
     */
    @Test
    public void should_return_encoded_ccp_map_to_base64_string_as_expected() {
        String urlEncodedString = channelContextParamsDecoder.serializeMapToBase64String(completeCcpMap);
        assertThat(urlEncodedString, is(not(emptyOrNullString())));
    }

    /**
     * Test to verify any exception is handled and a empty string is returned if an error occurs while url encode.
     */
    @Test
    public void should_handle_the_exception_and_return_empty_when_if_error_occurs_while_url_encode() throws Exception {
        Field urlCodecField = ChannelContextParamsDecoder.class.getDeclaredField("URLCODEC");
        CustomReflectionTestUtils.setFinalStaticField(urlCodecField, this.urlCodec);

        EncoderException mockedEncoderException = mock(EncoderException.class);
        when(urlCodec.encode(anyString())).thenThrow(mockedEncoderException);

        String urlEncodedString = channelContextParamsDecoder.urlEncode(INVALID_BASE64_ENCODED_CCP);
        assertThat(urlEncodedString, is(emptyOrNullString()));
        verify(logger, times(1)).error("Error in encoding Base64 string to URL healthy Base64 string", mockedEncoderException);
    }

    /**
     * Test to verify any exception is handled and a empty string is returned if an error occurs while url decode.
     */
    @Test
    public void should_handle_the_exception_and_return_empty_when_if_error_occurs_while_url_decode() throws Exception {
        Field urlCodecField = ChannelContextParamsDecoder.class.getDeclaredField("URLCODEC");
        CustomReflectionTestUtils.setFinalStaticField(urlCodecField, this.urlCodec);

        DecoderException mockedDecoderException = mock(DecoderException.class);
        when(urlCodec.decode(anyString())).thenThrow(mockedDecoderException);

        String urlEncodedString = channelContextParamsDecoder.urlDecode(INVALID_BASE64_ENCODED_CCP);
        assertThat(urlEncodedString, is(emptyOrNullString()));
        verify(logger, times(1)).error("Error in decoding URL healthy Base64 string to Base64 string", mockedDecoderException);
    }

    /**
     * Test to verify deserialize base64 string is  returned as expected.
     */
    @Test
    public void should_deserialize_base64_string_as_expected() {
        String base64SerializedString = "ZGVwYXJ0bWVudA==";
        String deserializeFromBase64String = channelContextParamsDecoder.deserializeFromBase64String(base64SerializedString);
        assertThat(deserializeFromBase64String, is(equalTo("department")));
    }

    /**
     * Test to verify null is returned when deserialize base64 string is empty..
     */
    @Test
    public void should_return_null_when_deserialize_base64_string_is_empty() {
        String deserializeFromBase64String = channelContextParamsDecoder.deserializeFromBase64String(StringUtils.EMPTY);
        assertThat(deserializeFromBase64String, is(nullValue()));
    }
}


