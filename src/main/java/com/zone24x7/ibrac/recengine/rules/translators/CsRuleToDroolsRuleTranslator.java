package com.zone24x7.ibrac.recengine.rules.translators;

import com.zone24x7.ibrac.recengine.exceptions.InvalidRuleException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class to represent configuration service rule to drools rule translator.
 */
@Component
public class CsRuleToDroolsRuleTranslator implements RuleTranslator {
    private static final String DELIMITERS = "=()&&||<>!#";
    private static final String AND_OPERATOR = "&";
    private static final String OR_OPERATOR = "|";

    private static final String EQUAL_SYMBOL = "=";
    private static final String HASH_SYMBOL = "#";
    private static final String NOT_SYMBOL = "!";
    private static final String GREATER_THAN_SYMBOL = ">";
    private static final String LESS_THAN_SYMBOL = "<";
    private static final String DOUBLE_EQUAL_SYMBOL = "==";
    private static final String HASH_EQUAL_SYMBOL = "#=";
    private static final String NOT_EQUAL_SYMBOL = "!=";
    private static final String GREATER_THAN_OR_EQUAL_SYMBOL = ">=";
    private static final String LESS_THAN_OR_EQUAL_SYMBOL = "<=";

    private static final String EQUALS_IGNORE_CASE = "equalsIgnoreCase";

    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";

    private static final String MATCHING_CONDITION_START = "MatchingCondition(";
    private static final String MATCHING_CONDITION_END = ")";

    private static final String ACTION_CONDITION_START = "Product(";
    private static final String ACTION_CONDITION_END = "))";

    private static final String INVALID_RULE_EXC_MSG = "Error parsing input rule string";
    private static final String INVALID_RULE_FORMAT_ERROR_MSG = "Rule provided has an incorrect format.";

    private static final Set<String> simpleComparatorTokens = new HashSet<>(Arrays.asList(EQUAL_SYMBOL, NOT_SYMBOL, LESS_THAN_SYMBOL, GREATER_THAN_SYMBOL, HASH_SYMBOL));
    private static final Set<String> complexComparatorTokens = new HashSet<>(Arrays.asList(DOUBLE_EQUAL_SYMBOL, NOT_EQUAL_SYMBOL, LESS_THAN_SYMBOL, GREATER_THAN_SYMBOL, LESS_THAN_OR_EQUAL_SYMBOL, GREATER_THAN_OR_EQUAL_SYMBOL, EQUALS_IGNORE_CASE));

    private Map<String, String> attributeTypeMappingInfo = new HashMap<>();

    /**
     * Class to instantiate CsRuleToDroolsRuleTranslator
     *
     * @param mappingInfo mapping information of attribute types
     */
    //TODO: Remove this attribute mapping to the configuration constants class
    public CsRuleToDroolsRuleTranslator(@Value("#{${ruleTranslator.attributeMapping}}") Map<String, String> mappingInfo) {
        if (MapUtils.isNotEmpty(mappingInfo)) {
            this.attributeTypeMappingInfo.putAll(mappingInfo);
        }
    }

    /**
     * Method to convert a rule to a matching condition.
     *
     * @param rule the rule to be converted
     * @return the generated matching condition
     * @throws InvalidRuleException if a rule is invalid
     */
    @Override
    public String convertToMatchingCondition(String rule) throws InvalidRuleException {
        if (StringUtils.isEmpty(rule)) {
            throw new InvalidRuleException("Input rule condition cannot be null.");
        }

        List<String> tokenList = processRule(rule.trim());

        try {
            for (int i = 0; i < tokenList.size(); i++) {
                // Checks for property and add the matching condition part.
                if (isTokenAComplexComparator(tokenList.get(i))) {
                    String prevToken = tokenList.get(i - ONE);
                    tokenList.set(i - ONE, "matchingMap[\"" + prevToken + "\"]");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidRuleException(INVALID_RULE_EXC_MSG, e);
        }

        String newRuleString = joinTokensToRuleString(tokenList).trim();
        newRuleString = StringUtils.isEmpty(newRuleString) ? "" : MATCHING_CONDITION_START + newRuleString.trim() + MATCHING_CONDITION_END;
        return newRuleString;
    }

    /**
     * Method to convert a rule to a action condition.
     *
     * @param rule     the rule to be converted
     * @param uniqueId the unique id to be added for the condition of the rule
     * @return the generated action condition
     * @throws InvalidRuleException if a rule is invalid
     */
    @Override
    public String convertToActionCondition(String rule, String uniqueId) throws InvalidRuleException {
        if (StringUtils.isEmpty(rule)) {
            throw new InvalidRuleException("Input action rule condition cannot be null.");
        }

        List<String> tokenList = processRule(rule.trim());

        try {
            for (int i = 0; i < tokenList.size(); i++) {
                // Checks for property and add the action condition part.
                if (isTokenAComplexComparator(tokenList.get(i))) {
                    String prevToken = tokenList.get(i - ONE);
                    tokenList.set(i - ONE, generateActionConditionAttributeRetrievalPhrase(prevToken));
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidRuleException(INVALID_RULE_EXC_MSG, e);
        }

        String newRuleString = joinTokensToRuleString(tokenList).trim();
        newRuleString = StringUtils.isEmpty(newRuleString) ? "" : ACTION_CONDITION_START + generateUniqueIdCondition(uniqueId) + newRuleString.trim() + ACTION_CONDITION_END;
        return newRuleString;
    }

    /**
     * Method to process the rule and convert to a list of tokens.
     *
     * @param rule Rule to be converted.
     * @return The converted rule as a list of tokens.
     * @throws InvalidRuleException If the rule is in invalid format.
     */
    private static List<String> processRule(String rule) throws InvalidRuleException {
        try {
            List<String> tokenList = processValues(tokenizeAndCreateTokenList(rule));

            List<String> processedTokenList = new ArrayList<>();
            int i = 0;
            while (i < tokenList.size()) {
                String currToken = tokenList.get(i);
                String nextToken = (i >= tokenList.size() - 1) ? "" : tokenList.get(i + ONE);

                // Checks whether the current token and the next token are symbols. (Example, ! and =, = and <, = and >)
                // If so the symbols will be added together and set to a one token. ! and = will be !=, = and < will be =<
                if (checkAdjacentTokensAreSimpleComparators(currToken, nextToken)) {
                    currToken = currToken + nextToken;
                    nextToken = tokenList.get(i + TWO);
                    i++;
                }

                // Checks whether the current token and the next token are operators. (Example : & and &, | and |).
                // If so the operators will be added together and set to a one token. & and & will be &&, | and | will be ||.
                if (checkAdjacentTokensAreOperators(currToken, nextToken)) {
                    currToken = appendAdjacentOperatorTokens(currToken, nextToken, i, tokenList.size());
                    i++;
                }

                processedTokenList.add(currToken);
                i++;
            }

            return postProcessTokens(processedTokenList);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidRuleException(INVALID_RULE_FORMAT_ERROR_MSG, e);
        }
    }

    /**
     * Method to convert a string in to a set of tokens.
     *
     * @param rule Rule String to be tokenized.
     * @return A list of tokens.
     */
    private static List<String> tokenizeAndCreateTokenList(String rule) {
        List<String> tokenList = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(rule, DELIMITERS, true);

        // Adding tokenizer tokens into a string list.
        while (tokenizer.hasMoreTokens()) {
            String tk = tokenizer.nextToken();
            if (!tk.isEmpty()) {
                tokenList.add(tk);
            }
        }

        return tokenList;
    }

    /**
     * Method to process values which are inside of double quotes.
     * Example:
     * Input token list => department ,==, "clothing ,&, shoes"
     * Output token list => department ,==, clothing & shoes
     *
     * @param tokenList the input token list
     * @return the output token with processed values
     * @throws InvalidRuleException if the rule is not in correct format
     */
    private static List<String> processValues(List<String> tokenList) throws InvalidRuleException {
        List<String> resultTokens = new LinkedList<>();

        boolean isValue = false;
        StringBuilder stringBuilder = new StringBuilder();

        for (String token : tokenList) {
            if (token.trim().startsWith("\"") && token.trim().endsWith("\"")) {
                resultTokens.add(token);
            } else if (!isValue && token.trim().startsWith("\"")) {
                isValue = true;
                stringBuilder.append(token);
            } else if (isValue && token.trim().endsWith("\"")) {
                stringBuilder.append(token);
                resultTokens.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                isValue = false;
            } else if (isValue) {
                stringBuilder.append(token);
            } else {
                resultTokens.add(token);
            }
        }

        if (isValue) {
            throw new InvalidRuleException(INVALID_RULE_FORMAT_ERROR_MSG);
        }

        return resultTokens;
    }

    /**
     * Method to be post process the tokens.
     * This method will process the tokens and trim the unnecessary spaces and change the #= to equalsIgnoreCase.
     * example : department #= "shoes" -> department equalsIgnoreCase "shoes"
     *
     * @param tokenList Token list that has the processed tokens.
     * @return Newly processed tokens with condition values fixed.
     */
    private static List<String> postProcessTokens(List<String> tokenList) throws InvalidRuleException {
        List<String> processedTokenList = new ArrayList<>();

        int i = 0;
        while (i < tokenList.size()) {
            String currToken = tokenList.get(i);

            if (!currToken.trim().isEmpty()) {

                if (currToken.trim().equals(HASH_EQUAL_SYMBOL)) {
                    currToken = EQUALS_IGNORE_CASE;
                }

                validateToken(currToken.trim());

                processedTokenList.add(currToken.trim());
            }

            i++;
        }

        return processedTokenList;
    }

    /**
     * Method to check whether the current token and the next token are operators.
     *
     * @param currToken Current token.
     * @param nextToken Next token.
     * @return True if both are AND operators or both are OR operators, else returns False.
     */
    private static boolean checkAdjacentTokensAreOperators(String currToken, String nextToken) {
        return (currToken.equals(AND_OPERATOR) && nextToken.equals(AND_OPERATOR)) || (currToken.equals(OR_OPERATOR) && nextToken.equals(OR_OPERATOR));
    }

    /**
     * Method to check whether the current token and the next token are simple comparators.
     *
     * @param currToken Current token.
     * @param nextToken Next token.
     * @return True if both are symbols, else returns False.
     */
    private static boolean checkAdjacentTokensAreSimpleComparators(String currToken, String nextToken) {
        return isTokenASimpleComparator(currToken) && isTokenASimpleComparator(nextToken);
    }

    /**
     * Method to append adjacent tokens if both are operators.
     * Example: && , ||
     *
     * @param currentToken  Current token being processed.
     * @param nextToken     Next token to process.
     * @param currentIndex  Current index of the token list being processed.
     * @param tokenListSize Size of the token list.
     * @return Appended tokens.
     * @throws InvalidRuleException If a rule does not have any more tokens for processing. (currentIndex + 2 is like "B" in "A && B")
     */
    private static String appendAdjacentOperatorTokens(String currentToken, String nextToken, int currentIndex, int tokenListSize) throws InvalidRuleException {
        String newToken = currentToken + nextToken;
        // Checks whether the one after the next token is available if the adjacent tokens are operators.
        // Example : If the condition is 'A && B', Current and next tokens are '&' and '&'. The one after the next token is B. Checks whether B exists.
        // If B does not exists it means the condition is not complete. Therefore, throws InvalidRuleException.
        if (currentIndex + TWO >= tokenListSize) {
            throw new InvalidRuleException(INVALID_RULE_FORMAT_ERROR_MSG);
        }

        return newToken;
    }

    /**
     * Method to check whether a token is a symbol such as =, !, <, >, #
     *
     * @param token Tokenized string to be checked.
     * @return True if the token is equal to the above symbols, else returns False.
     */
    private static boolean isTokenASimpleComparator(String token) {
        return simpleComparatorTokens.contains(token);
    }

    /**
     * Method to check a token is a complex symbol such as ==, !=, <, >, <=, >=
     *
     * @param token Tokenized string to be checked.
     * @return True if the token is equal to the above symbols, else returns False.
     */
    private static boolean isTokenAComplexComparator(String token) {
        return complexComparatorTokens.contains(token);
    }

    /**
     * Method to generate the unique id condition.
     *
     * @param uniqueId the unique id
     * @return the generated unique id condition
     */
    private static String generateUniqueIdCondition(String uniqueId) {
        if (StringUtils.isEmpty(uniqueId)) {
            return "(";
        }

        return "(\"" + uniqueId + "\" == \"" + uniqueId + "\") && (";
    }

    /**
     * Method to generate the action condition attribute retrieval phrase.
     * <p>
     * Example 1: when attribute is department
     * Output => attributesMap["department]
     * <p>
     * Example 2: when attribute type mapping has price|double and attribute is price
     * Output => Double.valueOf(attributesMap["department])
     *
     * @param attribute the attribute
     * @return generate action condition attribute retrieval phrase
     */
    private String generateActionConditionAttributeRetrievalPhrase(String attribute) {
        String actionConditionAttributeRetrievalPhrase = "attributesMap[\"" + attribute + "\"]";

        if (attributeTypeMappingInfo.get(attribute) != null) {
            if ("double".equalsIgnoreCase(attributeTypeMappingInfo.get(attribute))) {
                actionConditionAttributeRetrievalPhrase = "Double.valueOf(" + actionConditionAttributeRetrievalPhrase + ")";
            } else if ("integer".equalsIgnoreCase(attributeTypeMappingInfo.get(attribute))) {
                actionConditionAttributeRetrievalPhrase = "Integer.valueOf(" + actionConditionAttributeRetrievalPhrase + ")";
            }
        }

        return actionConditionAttributeRetrievalPhrase;
    }

    /**
     * Method to validate whether the rule is in correct format.
     *
     * @param token the token to validate
     * @throws InvalidRuleException if the rule is in invalid format
     */
    private static void validateToken(String token) throws InvalidRuleException {
        if (EQUAL_SYMBOL.equals(token)) {
            throw new InvalidRuleException("A rule should indicate the equal sign as == instead of = symbol.");
        }

        if (HASH_SYMBOL.equals(token)) {
            throw new InvalidRuleException("A rule cannot have only # as a operator.");
        }

        if (AND_OPERATOR.equals(token)) {
            throw new InvalidRuleException("A rule cannot have a single & symbol as a comparator.");
        }

        if (OR_OPERATOR.equals(token)) {
            throw new InvalidRuleException("A rule cannot have a single | symbol as a comparator.");
        }
    }

    /**
     * Method to join the tokens to a rule string.
     *
     * @param tokenList the token list
     * @return the rule string
     */
    private String joinTokensToRuleString(List<String> tokenList) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (i < tokenList.size()) {
            String currentToken = tokenList.get(i);
            String nextToken = (i >= tokenList.size() - 1) ? "" : tokenList.get(i + ONE);

            if (OPEN_BRACKET.equals(currentToken)) {
                builder.append(currentToken);
            } else if (CLOSE_BRACKET.equals(nextToken)) {
                builder.append(currentToken);
            } else {
                builder.append(currentToken);
                builder.append(" ");
            }

            i++;
        }

        return builder.toString();
    }
}
