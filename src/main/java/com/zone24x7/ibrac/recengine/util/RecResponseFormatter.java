package com.zone24x7.ibrac.recengine.util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zone24x7.ibrac.recengine.pojo.FlatRecPayload;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import com.zone24x7.ibrac.recengine.pojo.controller.ResponseFormatterConfig;

import java.util.List;

/**
 * Class to format recommendation responses.
 */
public final class RecResponseFormatter {
    /**
     * Default constructor.
     */
    private RecResponseFormatter() {
        // Private constructor
    }

    /**
     * Method to format the recommendation response.
     *
     * @param channel       the channel
     * @param page          the page
     * @param recResultList the result list
     * @param config        the configuration object
     * @return formatted recommendation result as a Json node for the input recommendation result
     */
    public static ObjectNode format(String channel, String page, List<RecResult> recResultList, ResponseFormatterConfig config) {
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
        ObjectNode payloadNode = JsonNodeFactory.instance.objectNode();
        payloadNode.put("cid", channel);
        payloadNode.put("pgid", page);
        rootNode.set("payload", payloadNode);

        ArrayNode recArrayNode = JsonNodeFactory.instance.arrayNode();

        for (RecResult recResult : recResultList) {
            if (recResult != null) {
                ObjectNode recNode = formatRecResult(recResult, config);
                recArrayNode.add(recNode);
            }
        }

        payloadNode.set("recommendations", recArrayNode);
        return rootNode;
    }

    /**
     * Method to format the an individual rec result.
     *
     * @param recResult the rec result to format
     * @param config    the configuration object
     * @return the formatted rec result as a json node
     */
    private static ObjectNode formatRecResult(RecResult recResult, ResponseFormatterConfig config) {
        ObjectNode recNode = JsonNodeFactory.instance.objectNode();
        recNode.put("plid", recResult.getPlaceHolder());

        ObjectNode recMetaNode = JsonNodeFactory.instance.objectNode();
        String recType = recResult.getRecMetaInfo().getType();
        recMetaNode.put("type", recType);
        recNode.set("recMeta", recMetaNode);

        ObjectNode payloadNode = null;

        // NOTE : In future if more types are added, the processing should be moved to difference processors according to type.
        if ("FlatRec".equalsIgnoreCase(recType) && recResult.getRecPayload() != null) {
            payloadNode = processFlatPayload((FlatRecPayload) recResult.getRecPayload(), config);
        }

        recNode.set("recPayload", payloadNode);

        return recNode;
    }

    /**
     * Method to process flat rec payload.
     *
     * @param flatRecPayload the flat rec payload
     * @param config the configuration object
     * @return the formatted payload node
     */
    private static ObjectNode processFlatPayload(FlatRecPayload flatRecPayload, ResponseFormatterConfig config) {
        ObjectNode recPayloadNode = JsonNodeFactory.instance.objectNode();
        recPayloadNode.put("displayText", flatRecPayload.getDisplayText());

        ArrayNode productArrayNode = JsonNodeFactory.instance.arrayNode();
        List<Product> productList = flatRecPayload.getProducts();

        int i = 1;

        for (Product product : productList) {
            ObjectNode productNode = formatRecProduct(product, i, config);
            productArrayNode.add(productNode);
            i++;
        }

        recPayloadNode.set("products", productArrayNode);
        return recPayloadNode;
    }

    /**
     * Method to format a recommendation product.
     *
     * @param product the product to format
     * @param rank    the rank of the product
     * @param config  the configuration object
     * @return the formatted recommendation product as a json node
     */
    private static ObjectNode formatRecProduct(Product product, int rank, ResponseFormatterConfig config) {
        ObjectNode productNode = JsonNodeFactory.instance.objectNode();

        productNode.put("id", product.getAttributesMap().get("productId"));
        productNode.put("rank", rank);
        productNode.put("productTitle", product.getAttributesMap().get("productTitle"));

        ObjectNode imageNode = JsonNodeFactory.instance.objectNode();
        imageNode.put("url", product.getAttributesMap().get("imageUrl"));
        imageNode.put("height", (config != null ? config.getImageHeight() : null));
        imageNode.put("width", (config != null ? config.getImageWidth() : null));
        productNode.set("image", imageNode);

        productNode.put("productUrl", product.getAttributesMap().get("productUrl"));

        ArrayNode pricesArrayNode = JsonNodeFactory.instance.arrayNode();

        String regularPrice = product.getAttributesMap().get("regularPrice");
        String validStartDate = product.getAttributesMap().get("validStartDate");
        String validEndDate = product.getAttributesMap().get("validEndDate");

        if (regularPrice != null) {
            ObjectNode priceNode = JsonNodeFactory.instance.objectNode();
            priceNode.put("currency", (config != null ? config.getCurrency() : null));
            priceNode.put("regularPrice", Double.valueOf(regularPrice));
            priceNode.put("validityStartTime", validStartDate);
            priceNode.put("validityEndTime", validEndDate);
            pricesArrayNode.add(priceNode);
        }

        productNode.set("prices", pricesArrayNode);

        return productNode;
    }
}
