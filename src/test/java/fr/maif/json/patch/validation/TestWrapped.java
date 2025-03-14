package fr.maif.json.patch.validation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gravity9.jsonpatch.JsonPatchOperation;

import fr.maif.json.patch.JsonPatchOperationWrapper;

@PermittedOperations(
        value = {
                @Operation(name = "add", path = "/test"),
                @Operation(name = "remove", path = "/test"),
                @Operation(name = "replace", path = "/bob")
        }
)
public class TestWrapped extends JsonPatchOperationWrapper {

    @JsonCreator
    public TestWrapped(final List<JsonPatchOperation> ops) {
        super(ops);
    }
}
