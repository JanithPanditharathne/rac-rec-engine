package com.zone24x7.ibrac.recengine.logging;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for mark the loggers in classes
 */
@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface Log {
}
