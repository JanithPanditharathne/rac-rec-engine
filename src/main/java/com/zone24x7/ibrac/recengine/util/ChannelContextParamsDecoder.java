package com.zone24x7.ibrac.recengine.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zone24x7.ibrac.recengine.logging.Log;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Utility class to decode the Channel Context Parameters (CCP) parameter sent in Rec request
 */
@Component
public class ChannelContextParamsDecoder {
    @Log
    private Logger logger;

    //URL encoding and decoding codec instance
    private static final URLCodec URLCODEC = new URLCodec();

    /**
     * Utility method that can be used to convert a given Map to a Base64 encoded string
     *
     * @param map to be serialized to Base64 string
     * @return Base64 string representation of the map if serialization succeeded, else null
     */
    public String serializeMapToBase64String(Map<String, String> map) {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        if (map != null) {
            try (Base64OutputStream base64Out = new Base64OutputStream(bytesOut)){
                base64Out.write(new Gson().toJson(map).getBytes(StandardCharsets.UTF_8.name()));
            } catch (Exception ex) {
                logger.error("Error in serializing Map to Base64", ex);
            }
        }

        return new String(bytesOut.toByteArray());
    }

    /**
     * Utility method that can be used to convert a Base64 string representing a Map to it's corresponding Map object
     *
     * @param base64String to be deserialized to the map
     * @return deserialized map if deserialization succeeded, else null
     */
    public Map<String, String> deserializeFromBase64StringToMap(String base64String) {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        Map<String, String> ccpParams = null;

        if (StringUtils.isNotEmpty(base64String)) {
            try (Base64InputStream base64In = new Base64InputStream(new ByteArrayInputStream(base64String.getBytes(StandardCharsets.UTF_8.name())));) {
                for (int data; (data = base64In.read()) > -1; ) {
                    bytesOut.write(data);
                }

                ccpParams = new Gson().fromJson(new String(bytesOut.toByteArray()), new TypeToken<Map<String, String>>() {
                }.getType());
            } catch (Exception ex) {
                logger.error("Error in deserializing from Base64 to Map", ex);
            }
        }

        return ccpParams;
    }

    /**
     * Deserialize from base64 string
     *
     * @param base64String the given base64 string
     * @return the base 64 de serialized string
     */
    public String deserializeFromBase64String(String base64String) {
        if (StringUtils.isNotEmpty(base64String)) {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            Base64InputStream base64In = null;

            try {
                base64In = new Base64InputStream(new ByteArrayInputStream(base64String.getBytes(StandardCharsets.UTF_8.name())));
                for (int data; (data = base64In.read()) > -1; ) {
                    bytesOut.write(data);
                }
                return bytesOut.toString(StandardCharsets.UTF_8.name());
            } catch (Exception ex) {
                logger.error("Error in deserializing from Base64 to Map", ex);
            }
        }

        return null;
    }

    /**
     * Utility method used to encode a given string to an URL friendly string
     *
     * @param messageToEncode the string to encode to URL friendly format
     * @return the URL friendly string if encoding succeeded, else null
     */
    public String urlEncode(String messageToEncode) {
        String urlEncodedBase64String = null;

        if (StringUtils.isNotEmpty(messageToEncode)) {
            try {
                urlEncodedBase64String = URLCODEC.encode(messageToEncode);
            } catch (Exception ex) {
                logger.error("Error in encoding Base64 string to URL healthy Base64 string", ex);
            }
        }

        return urlEncodedBase64String;
    }

    /**
     * Utility method used to decode a URL friendly string to a normal string
     *
     * @param messageToDecode the URL friendly string to decode to a normal string
     * @return the normal string if decoding succeeded, else null
     */
    public String urlDecode(String messageToDecode) {
        String urlDecodedBase64String = null;

        if (StringUtils.isNotEmpty(messageToDecode)) {
            try {
                urlDecodedBase64String = URLCODEC.decode(messageToDecode);
            } catch (Exception ex) {
                logger.error("Error in decoding URL healthy Base64 string to Base64 string", ex);
            }
        }

        return urlDecodedBase64String;
    }
}
