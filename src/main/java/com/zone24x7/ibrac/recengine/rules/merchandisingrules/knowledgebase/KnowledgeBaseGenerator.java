package com.zone24x7.ibrac.recengine.rules.merchandisingrules.knowledgebase;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;

/**
 * Class to generate knowledge base information
 *
 * @param <I> input configurations
 * @param <O> knowledge base information
 */
public interface KnowledgeBaseGenerator<I, O> {

    /**
     * Method to generate the knowledge base for a given configuration
     *
     * @param inputConfigurations the configurations to set
     * @return the knowledge base information
     */
    O generate(I inputConfigurations) throws MalformedConfigurationException;
}
