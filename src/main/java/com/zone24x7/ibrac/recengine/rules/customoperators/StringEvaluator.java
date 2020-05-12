package com.zone24x7.ibrac.recengine.rules.customoperators;

import org.drools.core.base.BaseEvaluator;
import org.drools.core.base.ValueType;
import org.drools.core.base.evaluators.Operator;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.rule.VariableRestriction;
import org.drools.core.spi.FieldValue;
import org.drools.core.spi.InternalReadAccessor;

/**
 * Custom string evaluators class used by Drools to compare strings in a case ignored manner
 */
public class StringEvaluator extends BaseEvaluator {

    /**
     * Creates and instance of the StringEvaluator
     *
     * @param type     the value type of the evaluator
     * @param operator the operator type of the evaluator
     */
    public StringEvaluator(final ValueType type, final Operator operator) {
        super(type, operator);
    }

    /**
     * Default Constructor
     */
    private StringEvaluator() {
        // Default constructor
    }

    /**
     * Evaluates strings
     *
     * @param internalWorkingMemory the internal working memory
     * @param internalReadAccessor  the internal read accessor
     * @param internalFactHandle    the internal fact handle
     * @param fieldValue            the filed value
     * @return the comparison status
     */
    @Override
    public boolean evaluate(InternalWorkingMemory internalWorkingMemory, InternalReadAccessor internalReadAccessor, InternalFactHandle internalFactHandle, FieldValue fieldValue) {
        final Object objectValue = internalReadAccessor.getValue(internalWorkingMemory, internalFactHandle.getObject());
        if (objectValue == null) {
            return false;
        }
        return this.getOperator().isNegated() ^ (((String) objectValue).equalsIgnoreCase((String) fieldValue.getValue()));
    }

    /**
     * Evaluates strings
     *
     * @param internalWorkingMemory the internal working memory
     * @param internalReadAccessor  the internal read accessor
     * @param internalFactHandle    the internal fact handle
     * @param internalReadAccessor2 the second internal read accessor
     * @param internalFactHandle2   the second internal fact handle
     * @return the comparison status
     */
    @Override
    public boolean evaluate(InternalWorkingMemory internalWorkingMemory, InternalReadAccessor internalReadAccessor, InternalFactHandle internalFactHandle, InternalReadAccessor internalReadAccessor2, InternalFactHandle internalFactHandle2) {
        final Object objectValue = internalReadAccessor.getValue(internalWorkingMemory, internalFactHandle.getObject());
        final Object objectValue2 = internalReadAccessor2.getValue(internalWorkingMemory, internalFactHandle2.getObject());
        if (objectValue == null || objectValue2 == null) {
            return false;
        }
        return this.getOperator().isNegated() ^ (((String) objectValue).equalsIgnoreCase((String) objectValue2));
    }

    /**
     * Evaluates the cached left operand
     *
     * @param internalWorkingMemory the internal working memory
     * @param variableContextEntry  the variable context entry
     * @param internalFactHandle    the internal fact handle
     * @return the comparison status
     */
    @Override
    public boolean evaluateCachedLeft(InternalWorkingMemory internalWorkingMemory, VariableRestriction.VariableContextEntry variableContextEntry, InternalFactHandle internalFactHandle) {
        final Object objectValue2 = ((VariableRestriction.ObjectVariableContextEntry) variableContextEntry).left;
        final Object objectValue = variableContextEntry.declaration.getExtractor().getValue(internalWorkingMemory, internalFactHandle.getObject());
        if (objectValue == null || objectValue2 == null) {
            return false;
        }
        return this.getOperator().isNegated() ^ (((String) objectValue).equalsIgnoreCase((String) objectValue2));
    }

    /**
     * Evaluates the cached right operand
     *
     * @param internalWorkingMemory the internal working memory
     * @param variableContextEntry  the variable context entry
     * @param internalFactHandle    the internal fact handle
     * @return the comparison status
     */
    @Override
    public boolean evaluateCachedRight(InternalWorkingMemory internalWorkingMemory, VariableRestriction.VariableContextEntry variableContextEntry, InternalFactHandle internalFactHandle) {
        final Object objectValue = variableContextEntry.declaration.getExtractor().getValue(internalWorkingMemory, internalFactHandle.getObject());
        final Object objectValue2 = ((VariableRestriction.ObjectVariableContextEntry) variableContextEntry).right;
        if (objectValue == null || objectValue2 == null) {
            return false;
        }
        return this.getOperator().isNegated() ^ (((String) objectValue).equalsIgnoreCase((String) objectValue2));
    }
}
