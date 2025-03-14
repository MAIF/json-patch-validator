package fr.maif.json.patch.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.TextNode;
import com.gravity9.jsonpatch.AddOperation;
import com.gravity9.jsonpatch.ReplaceOperation;

class JsonPatchPermittedOperationsValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidationOk() {
        TestWrapped testWrapped = new TestWrapped(
                List.of(new AddOperation("/test", new TextNode("claude")),
                        new ReplaceOperation("/bob", new TextNode("toto"))));
        Set<ConstraintViolation<TestWrapped>> violations = validator.validate(testWrapped);
        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    @Test
    void testValidationFails() {
        TestWrapped testWrapped = new TestWrapped(
                List.of(new AddOperation("invalid", new TextNode("claude")),
                        new ReplaceOperation("bob", new TextNode("toto"))));

        Set<ConstraintViolation<TestWrapped>> violations = validator.validate(testWrapped);
        assertFalse(violations.isEmpty(), "There should be validation errors");

        for (ConstraintViolation<TestWrapped> violation : violations) {
            System.out.println(violation.getMessage());
            assertTrue(violation.getMessage().contains("operation"), "Violation message should contain 'operation'");
            assertTrue(violation.getMessage().contains("is not allowed on path "),
                    "Violation message should contain 'is not allowed on path '");
        }
    }

}
