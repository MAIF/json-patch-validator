package fr.maif.json.patch.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = JsonPatchPermittedOperationsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermittedOperations {
    String message() default "operation {0} is not allowed on path {1}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Operation[] value();
}