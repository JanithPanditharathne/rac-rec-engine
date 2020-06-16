package com.zone24x7.ibrac.recengine.rules.merchandisingrules.knowledgebase;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;
import com.zone24x7.ibrac.recengine.pojo.rules.MerchandisingRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Rule;
import com.zone24x7.ibrac.recengine.pojo.csconfig.RuleConfig;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.customoperators.StringEvaluatorDefinition;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.rulegenerators.MerchandisingRuleGenerator;
import com.zone24x7.ibrac.recengine.util.JsonPojoConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderConfiguration;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.builder.conf.EvaluatorOption;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class to represent merchandising rule knowledge base generator.
 */
@Component
public class MerchandisingRuleKnowledgeBaseGenerator implements KnowledgeBaseGenerator<String, MerchandisingRuleKnowledgeBaseInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchandisingRuleKnowledgeBaseGenerator.class);

    @Autowired
    private MerchandisingRuleGenerator ruleGenerator;

    private MerchandisingRuleKnowledgeBaseInfo knowledgeBaseInfo;

    private static final String BOOST = "BOOST";
    private static final String BURY = "BURY";
    private static final String ONLY_RECOMMEND = "ONLY_RECOMMEND";
    private static final String DO_NOT_RECOMMEND = "DO_NOT_RECOMMEND";

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Sets configuration.
     *
     * @param inputConfigurations the configurations to set
     * @throws MalformedConfigurationException malformed configuration
     */
    @Override
    public void setConfigurations(String inputConfigurations) throws MalformedConfigurationException {
        if (StringUtils.isEmpty(inputConfigurations)) {
            throw new MalformedConfigurationException("Rule configurations cannot be empty or null.");
        }

        RuleConfig ruleConfig;

        try {
            ruleConfig = JsonPojoConverter.toPojo(inputConfigurations, RuleConfig.class);
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error occurred when converting rule config string to pojo.", e);
        }

        Set<ConstraintViolation<RuleConfig>> constraintViolations;

        try {
            constraintViolations = validator.validate(ruleConfig);
        } catch (ValidationException | IllegalArgumentException e) {
            throw new MalformedConfigurationException("Exception thrown in rule configuration validation.", e);
        }

        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            throw new MalformedConfigurationException("Error in rule configurations. Violations: " + constraintViolations);
        }

        KnowledgeBuilderConfiguration builderConf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
        builderConf.setOption(EvaluatorOption.get("equalsIgnoreCase", new StringEvaluatorDefinition()));
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(builderConf);

        Set<String> globalFilteringRuleIds = new LinkedHashSet<>();

        if (ruleConfig != null && CollectionUtils.isNotEmpty(ruleConfig.getRules())) {
            for (Rule rule : ruleConfig.getRules()) {
                try {
                    String drl = generateRuleStringFromRule(rule);
                    knowledgeBuilder.add(ResourceFactory.newReaderResource(new StringReader(drl)), ResourceType.DRL);
                } catch (RuleGeneratorException e) {
                    throw new MalformedConfigurationException("Error occurred when generating rules. Rule Id: " + rule.getId(), e);
                }

                if (isRuleAGlobalFilteringRule(rule)) {
                    globalFilteringRuleIds.add(rule.getId());
                }
            }
        }

        if (knowledgeBuilder.hasErrors()) {
            LOGGER.error("Knowledge build has errors. {}", knowledgeBuilder.getErrors());
            throw new MalformedConfigurationException("Error in building Rules Knowledge base.");
        }

        Collection<KiePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();
        InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addPackages(knowledgePackages);

        knowledgeBaseInfo = new MerchandisingRuleKnowledgeBaseInfo();
        knowledgeBaseInfo.setKnowledgeBase(knowledgeBase);
        knowledgeBaseInfo.addGlobalFilteringRuleIds(globalFilteringRuleIds);
    }

    /**
     * Method to get the merchandising rule knowledge base info.
     *
     * @return the merchandising rule knowledge base info
     */
    @Override
    public MerchandisingRuleKnowledgeBaseInfo getKnowledgeBaseInfo() {
        return knowledgeBaseInfo;
    }

    /**
     * Method to generate drools rule string from a rule.
     *
     * @param rule the rule string
     * @return the drools rule string
     * @throws RuleGeneratorException if an error occurs when generating the rule
     */
    private String generateRuleStringFromRule(Rule rule) throws RuleGeneratorException {
        switch (rule.getType()) {
            case BOOST:
                return ruleGenerator.generateBoostRule(rule.getId(), rule.getMatchingCondition(), rule.getActionCondition(), null);
            case BURY:
                return ruleGenerator.generateBuryRule(rule.getId(), rule.getMatchingCondition(), rule.getActionCondition());
            case ONLY_RECOMMEND:
                return ruleGenerator.generateOnlyRecommendRule(rule.getId(), rule.getMatchingCondition(), rule.getActionCondition());
            case DO_NOT_RECOMMEND:
                return ruleGenerator.generateDoNotRecommendRule(rule.getId(), rule.getMatchingCondition(), rule.getActionCondition());
            default:
                return "";
        }
    }

    /**
     * Method to check whether a rule is a global filtering rule.
     *
     * @param rule the rule to check
     * @return true if a global filtering rule, else false
     */
    private static boolean isRuleAGlobalFilteringRule(Rule rule) {
        return (rule.isGlobal() && Arrays.asList(BOOST, BURY, ONLY_RECOMMEND, DO_NOT_RECOMMEND).contains(rule.getType()));
    }
}
