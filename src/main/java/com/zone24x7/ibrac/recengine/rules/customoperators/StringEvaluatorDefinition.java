package com.zone24x7.ibrac.recengine.rules.customoperators;

import org.drools.core.base.ValueType;
import org.drools.core.base.evaluators.EvaluatorCache;
import org.drools.core.base.evaluators.EvaluatorDefinition;
import org.drools.core.base.evaluators.Operator;
import org.drools.core.spi.Evaluator;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Custom string evaluator definitions class used by Drools to compare strings in a case ignored manner
 */
public class StringEvaluatorDefinition implements EvaluatorDefinition {
    private static final Operator STR_COMPARE = Operator.addOperatorToRegistry("equalsIgnoreCase", false);
    private static final Operator NOT_STR_COMPARE = Operator.addOperatorToRegistry("equalsIgnoreCase", true);
    private static final String[] SUPPORTED_IDS = {STR_COMPARE.getOperatorString()};
    private EvaluatorCache evaluators;

    /**
     * Creates an instance of the StringEvaluatorDefinition
     */
    public StringEvaluatorDefinition() {
        evaluators = new EvaluatorCache();
        evaluators.addEvaluator(ValueType.OBJECT_TYPE, STR_COMPARE, new StringEvaluator(ValueType.OBJECT_TYPE, STR_COMPARE));
        evaluators.addEvaluator(ValueType.OBJECT_TYPE, NOT_STR_COMPARE, new StringEvaluator(ValueType.OBJECT_TYPE, NOT_STR_COMPARE));
    }

    /**
     * Gets an evaluator
     *
     * @param type     the value type of the evaluator
     * @param operator the operator of the evaluator
     * @return the evaluator
     */
    @Override
    public Evaluator getEvaluator(ValueType type, Operator operator) {
        return this.getEvaluator(type, operator.getOperatorString(), operator.isNegated(), null);
    }

    /**
     * Gets an evaluator
     *
     * @param type          the value type of the evaluator
     * @param operator      the operator of the evaluator
     * @param parameterText the parameter test of the evaluator
     * @return the evaluator
     */
    @Override
    public Evaluator getEvaluator(ValueType type, Operator operator, String parameterText) {
        return this.getEvaluator(type, operator.getOperatorString(), operator.isNegated(), parameterText);
    }

    /**
     * Gets an evaluator
     *
     * @param type          the value type of the evaluator
     * @param operatorId    the id of the operator for the evaluator
     * @param isNegated     the status of isNegated
     * @param parameterText the parameter text of the evaluator
     * @return the evaluator
     */
    @Override
    public Evaluator getEvaluator(ValueType type, String operatorId, boolean isNegated, String parameterText) {
        return getEvaluator(type, operatorId, isNegated, parameterText, Target.FACT, Target.FACT);
    }

    /**
     * Gets an evaluator
     *
     * @param type          the value type of the evaluator
     * @param operatorId    the id of the operator for the evaluator
     * @param isNegated     the status of isNegated
     * @param parameterText the parameter text of the evaluator
     * @param leftTarget    the LHS target for the evaluator
     * @param rightTarget   the RHS target for the evaluator
     * @return the evaluator
     */
    @Override
    public Evaluator getEvaluator(ValueType type, String operatorId, boolean isNegated, String parameterText, Target leftTarget, Target rightTarget) {
        return new StringEvaluator(type, isNegated ? NOT_STR_COMPARE : STR_COMPARE);
    }

    /**
     * Gets the evaluator ids
     *
     * @return the evaluator ids
     */
    @Override
    public String[] getEvaluatorIds() {
        return SUPPORTED_IDS;
    }

    /**
     * Gets the target of the evaluator
     *
     * @return the target of the evaluator
     */
    @Override
    public Target getTarget() {
        return Target.FACT;
    }

    /**
     * Gets the status of isNegatable
     *
     * @return this will always return true
     */
    @Override
    public boolean isNegatable() {
        return true;
    }

    /**
     * Gets the status of whether value type is supported
     *
     * @param type the value type
     * @return this will always return true
     */
    @Override
    public boolean supportsType(ValueType type) {
        return true;
    }

    /**
     * Reads the external evaluator
     *
     * @param in input to get the external evaluator
     * @throws IOException            if an IO exception generated when reading the input
     * @throws ClassNotFoundException if the specified class was not found when reading the input
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        evaluators = (EvaluatorCache) in.readObject();
    }

    /**
     * Writes the external evaluator
     *
     * @param out output to write the evaluator to
     * @throws IOException if writing to the output failed due to IO issues
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(evaluators);
    }
}
