package com.zone24x7.ibrac.recengine.rules.recrules.knowledgebase;


import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Rec;
import com.zone24x7.ibrac.recengine.pojo.csconfig.RecConfig;
import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.recrules.rulegenerators.RecRuleGenerator;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.customoperators.StringEvaluatorDefinition;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.knowledgebase.KnowledgeBaseGenerator;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//TODO: Singleton and does not support multi config threads at the moment. Need to change
/**
 * KnowledgeBaseGenerator for rec rules
 */
@Component
public class RecRuleKnowledgeBaseGenerator implements KnowledgeBaseGenerator<String, RecRuleKnowledgeBaseInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecRuleKnowledgeBaseGenerator.class);

    private RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo;
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private RecRuleGenerator recRuleGenerator;

    /**
     * Converts the input configuration to knowledge base and sets recRuleKnowledgeBaseInfo
     *
     * @param inputConfigurations the configurations to set
     * @throws MalformedConfigurationException if any issue found with the configurations
     */
    @Override
    public void setConfigurations(String inputConfigurations) throws MalformedConfigurationException {
        if (StringUtils.isEmpty(inputConfigurations)) {
            throw new MalformedConfigurationException("Rec configurations cannot be empty or null.");
        }

        RecConfig recConfig;

        try {
            recConfig = JsonPojoConverter.toPojo(inputConfigurations, RecConfig.class);
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error occurred when converting rule config string to pojo.", e);
        }

        if (recConfig == null || recConfig.getRecs() == null) {
            throw new MalformedConfigurationException("RecConfig or recs inside recConfig is null ");
        }

        Set<ConstraintViolation<RecConfig>> constraintViolations;

        try {
            constraintViolations = validator.validate(recConfig);
        } catch (ValidationException | IllegalArgumentException e) {
            throw new MalformedConfigurationException("Exception thrown in rec slot configuration validation.", e);
        }

        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            throw new MalformedConfigurationException("Error in rec slot configurations. Violations: " + constraintViolations);
        }

        KnowledgeBuilderConfiguration builderConf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
        builderConf.setOption(EvaluatorOption.get("equalsIgnoreCase", new StringEvaluatorDefinition()));

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(builderConf);

        if (CollectionUtils.isNotEmpty(recConfig.getRecs())) {
            for (Rec rec : recConfig.getRecs()) {
                try {
                    String drl = recRuleGenerator.generateRecRule(rec.getId(), rec.getMatchingCondition());
                    knowledgeBuilder.add(ResourceFactory.newReaderResource(new StringReader(drl)), ResourceType.DRL);
                } catch (RuleGeneratorException e) {
                    throw new MalformedConfigurationException("Error occurred when generating rules. Rec Id: " + rec.getId(), e);
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

        Map<String, Rec> recMap = new HashMap<>();
        recConfig.getRecs().stream().forEach(rec -> recMap.put(rec.getId(), rec));

        recRuleKnowledgeBaseInfo = new RecRuleKnowledgeBaseInfo();
        recRuleKnowledgeBaseInfo.setKnowledgeBase(knowledgeBase);
        recRuleKnowledgeBaseInfo.setRecIdToRecMap(recMap);
    }

    /**
     * Method to return the RecRuleKnowledgeBaseInfo
     *
     * @return RecRuleKnowledgeBaseInfo
     */
    @Override
    public RecRuleKnowledgeBaseInfo getKnowledgeBaseInfo() {
        return recRuleKnowledgeBaseInfo;
    }
}
