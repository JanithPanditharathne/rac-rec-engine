package com.zone24x7.ibrac.recengine.util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import com.zone24x7.ibrac.recengine.pojo.controller.ResponseFormatterConfig;

import java.util.LinkedList;
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
        //TODO: Add placement.
        recNode.put("plid", "TODO: ADD PLACEMENT");

        //TODO: Populate rec meta from metadata in rec result.
        ObjectNode recMetaNode = JsonNodeFactory.instance.objectNode();
        recMetaNode.put("type", "TODO : ADD TYPE => FlatRec");
        recNode.set("recMeta", recMetaNode);

        //TODO: Populate rec payload from metadata in rec result.
        ObjectNode recPayloadNode = JsonNodeFactory.instance.objectNode();
        recPayloadNode.put("displayText", "TODO: ADD DISPLAY TEXT");

        ArrayNode productArrayNode = JsonNodeFactory.instance.arrayNode();

        List<Product> productList = new LinkedList<>(); //TODO : recResult.getRecPayload().getProducts();

        int i = 1;

        for (Product product : productList) {
            ObjectNode productNode = formatRecProduct(product, i, config);
            productArrayNode.add(productNode);
            i++;
        }

        recNode.set("recPayload", recPayloadNode);

        return recNode;
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

        ArrayNode pricesArrayNode = JsonNodeFactory.instance.arrayNode();

        String regularPrice = product.getAttributesMap().get("regularPrice");
        String validStartDate = product.getAttributesMap().get("validStartDate");
        String validEndDate = product.getAttributesMap().get("validEndDate");

        if (regularPrice != null) {
            ObjectNode priceNode = JsonNodeFactory.instance.objectNode();
            priceNode.put("currency", (config != null ? config.getCurrency() : null));
            priceNode.put("regularPrice", regularPrice);
            priceNode.put("validStartDate", validStartDate);
            priceNode.put("validEndDate", validEndDate);
            pricesArrayNode.add(priceNode);
        }

        productNode.set("prices", pricesArrayNode);

        return productNode;
    }
}
