package com.rotem.graphsync.graph.common.Annotations;

import com.rotem.graphsync.graph.common.validators.UniqueNodeIdsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueNodeIdsValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNodeIds {
    String message() default "Nodes IDs must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
