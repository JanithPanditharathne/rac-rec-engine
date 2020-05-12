package com.zone24x7.ibrac.recengine.rules.knowledgebase;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;

/**
 * Class to generate knowledge base information
 *
 * @param <I> input configurations
 * @param <O> knowledge base information
 */
public interface KnowledgeBaseGenerator<I, O> {

    /**
     * Method to set the configurations.
     *
     * @param inputConfigurations the configurations to set
     */
    void setConfigurations(I inputConfigurations) throws MalformedConfigurationException;

    /**
     * Method to get the knowledge base information.
     *
     * @return the knowledge base information
     */
    O getKnowledgeBaseInfo();
}
