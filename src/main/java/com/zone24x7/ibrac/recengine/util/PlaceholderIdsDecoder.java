package com.zone24x7.ibrac.recengine.util;

import com.zone24x7.ibrac.recengine.pojo.PlaceholderId;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to decode the placeholders.
 */
public final class PlaceholderIdsDecoder {
    //Splitting character of placeholder ids list
    private static final String PLACEHOLDER_IDS_SPLITTER = ",";

    //Splitting character of a single placeholder id
    private static final String PLACEHOLDER_ID_SPLITTER = "\\|";

    //Length of a complete decoded placeholder
    private static final int LENGTH_OF_A_COMPLETE_DECODED_PLACEHOLDER = 2;

    //Length of a decoded placeholder only including a placeholder id (i.e. no limit specified
    private static final int LENGTH_OF_A_DECODED_PLACEHOLDER_ONLY_INCLUDING_PLACEHOLDER_ID = 1;

    /**
     * Private constructor to prevent instantiating instances of this final class
     */
    private PlaceholderIdsDecoder() {

    }

    /**
     * Decode the placeholder ids parameter in the RTIS request
     *
     * @param placeholderIds the placeholder IDs passed in the RTIS request
     * @return the decoded list of placeholder IDs
     * <p>
     * Special notes on the returned list
     * - If non-numeric limits are specified, such placeholders will be ignored (div1|4,div2|2t3,div5|5).
     * - If limit component is empty or not specified (e.g. div1|,div2|3 OR div1|5,div2,div3|2)
     * for any specified placeholder, a default limit will be applied as Null for such placeholders.
     * - Empty placeholder id components (e.g. div1|10,|2,div5|5) will be ignored.
     * - If same placeholder id is repeated more than once, the first occurrence will be considered.
     * - A complete null or empty placeholder ids input parameter will return an empty list
     */
    public static List<PlaceholderId> decodePlaceholderIds(String placeholderIds) {
        return decodePlaceholderIds(placeholderIds, null);
    }

    /**
     * Decode the placeholder ids parameter in the Rec Engine request
     *
     * @param placeholderIds the placeholder IDs passed in the rec request
     * @param defaultLimit   the default limit to set if the placeholder does not have a limit
     * @return the decoded list of placeholder IDs
     * <p>
     * Special notes on the returned list
     * - If non-numeric limits are specified, such placeholders will be ignored (div1|4,div2|2t3,div5|5).
     * - If limit component is empty or not specified (e.g. div1|,div2|3 OR div1|5,div2,div3|2)
     * for any specified placeholders, limit the default limit will be applied for such placeholders.
     * - Empty placeholder id components (e.g. div1|10,|2,div5|5) will be ignored.
     * - If same placeholder id is repeated more than once, the first occurrence will be considered.
     * - A complete null or empty placeholder ids input parameter will return an empty list
     */
    public static List<PlaceholderId> decodePlaceholderIds(String placeholderIds, Integer defaultLimit) {
        List<PlaceholderId> decodedPlaceholderIds = new ArrayList<>();

        if (StringUtils.isNotEmpty(placeholderIds)) {
            String[] placeholders = placeholderIds.split(PLACEHOLDER_IDS_SPLITTER);

            for (String placeholder : placeholders) {
                PlaceholderId currentPlaceholderId = decodePlaceholderId(placeholder, defaultLimit);
                //Ignore if one occurrence of same placeholder is already in list
                if (currentPlaceholderId != null && !decodedPlaceholderIds.contains(currentPlaceholderId)) {
                    decodedPlaceholderIds.add(currentPlaceholderId);
                }
            }
        }

        return decodedPlaceholderIds;
    }

    /**
     * Decode a single placeholder id parameter in the Rec Engine request
     *
     * @param placeholder  a single placeholder in the rec request
     * @param defaultLimit the default limit to set if the placeholder does not have a limit
     * @return the decoded placeholder id
     * <p>
     * Special notes on the returned list
     * - If non-numeric limits are specified, such placeholders will be ignored (div1|4,div2|2t3,div5|5).
     * - If limit component is empty or not specified (e.g. div1|,div2|3 OR div1|5,div2,div3|2)
     * for any specified placeholders, limit the default limit will be applied for such placeholders.
     * - Empty placeholder id components (e.g. div1|10,|2,div5|5) will be ignored.
     * - If same placeholder id is repeated more than once, the first occurrence will be considered.
     * - A complete null or empty placeholder ids input parameter will return an empty list
     */
    private static PlaceholderId decodePlaceholderId(String placeholder, Integer defaultLimit) {
        String[] decodedPlaceholder = placeholder.split(PLACEHOLDER_ID_SPLITTER);

        if (decodedPlaceholder.length == LENGTH_OF_A_COMPLETE_DECODED_PLACEHOLDER
                && StringUtils.isNotEmpty(decodedPlaceholder[0])
                && NumberUtils.isCreatable(decodedPlaceholder[1])) {
            //Both placeholder id and limit is provided and limit is a number
            return new PlaceholderId.PlaceholderIdBuilder()
                    .id(decodedPlaceholder[0])
                    .recommendationLimit(Integer.parseInt(decodedPlaceholder[1]))
                    .build();
        } else if (decodedPlaceholder.length == LENGTH_OF_A_DECODED_PLACEHOLDER_ONLY_INCLUDING_PLACEHOLDER_ID
                && StringUtils.isNotEmpty(decodedPlaceholder[0])) {
            //Limit is not provided, thus apply the default limit
            return new PlaceholderId.PlaceholderIdBuilder()
                    .id(decodedPlaceholder[0])
                    .recommendationLimit(defaultLimit)
                    .build();
        }

        return null;
    }
}
