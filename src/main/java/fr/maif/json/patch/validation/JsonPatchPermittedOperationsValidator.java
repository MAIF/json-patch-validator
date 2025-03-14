package fr.maif.json.patch.validation;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Predicate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.gravity9.jsonpatch.JsonPatchOperation;

import fr.maif.json.patch.JsonPatchOperationWrapper;

public class JsonPatchPermittedOperationsValidator implements
        ConstraintValidator<PermittedOperations, JsonPatchOperationWrapper> {
    private PermittedOperations permittedOperations;

    @Override
    public void initialize(PermittedOperations permittedOperations) {
        this.permittedOperations = permittedOperations;
    }

    @Override
    public boolean isValid(JsonPatchOperationWrapper operations, ConstraintValidatorContext cxt) {
        return operations.getOperations().stream().filter(operation -> !isValid(operation, cxt)).toList().isEmpty();
    }

    public boolean isValid(JsonPatchOperation operation, ConstraintValidatorContext cxt) {
        boolean valid = Arrays.stream(permittedOperations.value()).anyMatch(isEqualAPermittedOperation(operation));
        if (!valid) {
            cxt.disableDefaultConstraintViolation();

            cxt.buildConstraintViolationWithTemplate(
                    MessageFormat.format(permittedOperations.message(),
                            operation.getOp(),
                            operation.getPath())
            ).addConstraintViolation();
        }
        return valid;
    }

    private Predicate<Operation> isEqualAPermittedOperation(JsonPatchOperation operation) {
        return permittedOperation -> permittedOperation.name().equals(operation.getOp()) &&
                permittedOperation.path().equals(operation.getPath());
    }

}
