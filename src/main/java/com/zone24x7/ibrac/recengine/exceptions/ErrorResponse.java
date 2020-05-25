package com.zone24x7.ibrac.recengine.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent error response.
 */
public class ErrorResponse {

    private List<Error> errors;

    /**
     * Default constructor to initialize error response
     */
    public ErrorResponse() {
        errors = new ArrayList<>();
    }

    /**
     * Method to add an error to the error response
     *
     * @param error Error to be added
     */
    public void addError(Error error) {
        errors.add(error);
    }

    /**
     * @return Boolean based on if the array is empty or not.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return errors == null || this.errors.isEmpty();
    }

    /**
     * Method to get the error list.
     *
     * @return list of {@link Error}
     */
    public List<Error> getErrors() {
        return errors;
    }
}
