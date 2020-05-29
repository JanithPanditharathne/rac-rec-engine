package com.zone24x7.ibrac.recengine.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.pojo.controller.ResponseFormatterConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * Class to test RecResponseFormatter.
 */
public class RecResponseFormatterTest {
    private static final String EXPECTED_JSON_OUTPUT = "{\"payload\":{\"cid\":\"WebStore\",\"pgid\":\"PDP\",\"recommendations\":[{\"plid\":\"Horizontal1\",\"recMeta\":{\"type\":\"FlatRec\"},\"recPayload\":{\"displayText\":\"People Who Viewed This Also Viewed\",\"products\":[{\"id\":\"2169013\",\"rank\":1,\"productTitle\":\"Waterproof Deep-Pocket Mattress Pad\",\"image\":{\"url\":\"https://media.test.com/image/2169013?w=180&h=180\",\"height\":\"180\",\"width\":\"180\"},\"productUrl\":\"mattress_2169013_white\",\"prices\":[{\"currency\":\"Rs\",\"regularPrice\":50.99,\"validityStartTime\":\"2020/04/22 04:39:13 -0500\",\"validityEndTime\":\"2020/04/23 04:39:13 -0500\"}]},{\"id\":\"2213222\",\"rank\":2,\"productTitle\":\"Nike Shoe\",\"image\":{\"url\":\"https://media.test.com/image/2169013?w=180&h=180\",\"height\":\"180\",\"width\":\"180\"},\"productUrl\":\"shoes_2213222_black\",\"prices\":[{\"currency\":\"Rs\",\"regularPrice\":50.0,\"validityStartTime\":\"2020/04/22 04:39:13 -0500\",\"validityEndTime\":\"2020/04/23 04:39:13 -0500\"}]}]}}]}}";
    private List<RecResult> recResultList = new LinkedList<>();

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        RecResult<FlatRecPayload> recResult = new RecResult<>();
        recResult.setPlaceHolder("Horizontal1");
        recResult.setRecCycleStatus(mock(RecCycleStatus.class));

        RecMetaInfo recMetaInfo = new RecMetaInfo();
        recMetaInfo.setBundleId("1");
        recMetaInfo.setType("FlatRec");
        recMetaInfo.setAlgoToProductsMap(new HashMap<>());
        recMetaInfo.setAlgoToUsedCcp(new HashMap<>());
        recMetaInfo.setExecutedFilteringRuleInfoList(new HashSet<>());
        recResult.setRecMetaInfo(recMetaInfo);

        FlatRecPayload flatRecPayload = new FlatRecPayload();
        flatRecPayload.setDisplayText("People Who Viewed This Also Viewed");

        Map<String, String> attributesMap1 = new HashMap<>();
        attributesMap1.put("productId", "2169013");
        attributesMap1.put("productTitle", "Waterproof Deep-Pocket Mattress Pad");
        attributesMap1.put("imageUrl", "https://media.test.com/image/2169013?w=180&h=180");
        attributesMap1.put("productUrl", "mattress_2169013_white");
        attributesMap1.put("regularPrice", "50.99");
        attributesMap1.put("validStartDate", "2020/04/22 04:39:13 -0500");
        attributesMap1.put("validEndDate", "2020/04/23 04:39:13 -0500");

        Product product1 = new Product();
        product1.setProductId("2169013");
        product1.setAttributesMap(attributesMap1);

        Map<String, String> attributesMap2 = new HashMap<>();
        attributesMap2.put("productId", "2213222");
        attributesMap2.put("productTitle", "Nike Shoe");
        attributesMap2.put("imageUrl", "https://media.test.com/image/2169013?w=180&h=180");
        attributesMap2.put("productUrl", "shoes_2213222_black");
        attributesMap2.put("regularPrice", "50.0");
        attributesMap2.put("validStartDate", "2020/04/22 04:39:13 -0500");
        attributesMap2.put("validEndDate", "2020/04/23 04:39:13 -0500");

        Product product2 = new Product();
        product2.setProductId("2213222");
        product2.setAttributesMap(attributesMap2);

        List<Product> productList = new LinkedList<>();
        productList.add(product1);
        productList.add(product2);

        flatRecPayload.setProducts(productList);
        recResult.setRecPayload(flatRecPayload);

        recResultList.add(recResult);
    }

    /**
     * Test to verify that the response is generated correctly for a given rec result.
     */
    @Test
    public void should_generate_the_response_correctly_for_given_rec_result() {
        ResponseFormatterConfig config = new ResponseFormatterConfig("Rs", "180", "180");
        ObjectNode formattedJsonNode = RecResponseFormatter.format("WebStore", "PDP", recResultList, config);
        assertThat(EXPECTED_JSON_OUTPUT, is(equalTo(formattedJsonNode.toString())));
    }
}