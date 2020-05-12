package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Bundle;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleConfig;
import com.zone24x7.ibrac.recengine.pojo.csconfig.RecSlot;
import com.zone24x7.ibrac.recengine.pojo.csconfig.RecSlotConfig;
import com.zone24x7.ibrac.recengine.pojo.recbundle.ActiveBundleProviderConfig;
import com.zone24x7.ibrac.recengine.util.JsonPojoConverter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation class for active bundle config generator.
 */
@Component
public class RecActiveBundleConfigGenerator implements ActiveBundleConfigGenerator {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Method to generate the active bundle provider configuration.
     *
     * @param recSlotConfiguration the rec slots configuration string
     * @param bundleConfiguration  the bundle configuration string
     * @return the active bundle provider configuration
     * @throws MalformedConfigurationException if the configuration are malformed
     */
    @Override
    public ActiveBundleProviderConfig generateConfiguration(String recSlotConfiguration, String bundleConfiguration) throws MalformedConfigurationException {
        RecSlotConfig recSlotConfig;
        BundleConfig bundleConfig;

        try {
            recSlotConfig = JsonPojoConverter.toPojo(recSlotConfiguration, RecSlotConfig.class);
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error reading rec slot configurations.", e);
        }

        try {
            bundleConfig = JsonPojoConverter.toPojo(bundleConfiguration, BundleConfig.class);
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error reading bundle configurations.", e);
        }

        Set<ConstraintViolation<RecSlotConfig>> recSlotConfigConstraintViolations;

        try {
            recSlotConfigConstraintViolations = validator.validate(recSlotConfig);
        } catch (ValidationException | IllegalArgumentException e) {
            throw new MalformedConfigurationException("Validator error occurred when validating rec slot configurations.", e);
        }

        // If rec slot configurations have constraint violation throw malformed configuration exception.
        if (CollectionUtils.isNotEmpty(recSlotConfigConstraintViolations)) {
            throw new MalformedConfigurationException("Error in rec slot configurations. Violations: " + recSlotConfigConstraintViolations);
        }

        Set<ConstraintViolation<BundleConfig>> bundleConfigConstraintValidations;

        try {
            bundleConfigConstraintValidations = validator.validate(bundleConfig);
        } catch (ValidationException | IllegalArgumentException e) {
            throw new MalformedConfigurationException("Validator error occurred when validating bundle configurations.", e);
        }

        // If bundle configurations have constraint violation throw malformed configuration exception.
        if (CollectionUtils.isNotEmpty(bundleConfigConstraintValidations)) {
            throw new MalformedConfigurationException("Error in bundle configurations. Violations: " + bundleConfigConstraintValidations);
        }

        // Create active bundle provider config and include the config maps.
        ActiveBundleProviderConfig activeBundleProviderConfig = new ActiveBundleProviderConfig();

        if (recSlotConfig != null && CollectionUtils.isNotEmpty(recSlotConfig.getRecSlots())) {
            Map<String, RecSlot> recSlotMap = recSlotConfig.getRecSlots().stream().collect(Collectors.toMap(RecSlot::toString, recSlot -> recSlot));
            activeBundleProviderConfig.setRecSlotMap(recSlotMap);
        }

        if (bundleConfig != null && CollectionUtils.isNotEmpty(bundleConfig.getBundles())) {
            Map<String, Bundle> bundleMap = bundleConfig.getBundles().stream().collect(Collectors.toMap(Bundle::getId, bundle -> bundle));
            activeBundleProviderConfig.setBundleMap(bundleMap);
        }

        return activeBundleProviderConfig;
    }
}
