package fr.maif.json.patch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gravity9.jsonpatch.JsonPatch;
import com.gravity9.jsonpatch.JsonPatchOperation;

public class JsonPatchOperationWrapper {
    private final List<JsonPatchOperation> ops;

    @JsonCreator
    public JsonPatchOperationWrapper(final List<JsonPatchOperation> ops) {
        this.ops = Collections.unmodifiableList(new ArrayList<>(ops));
    }

    public JsonPatch toJsonPatch() {
        return new JsonPatch(ops);
    }

    public List<JsonPatchOperation> getOperations() {
        return ops;
    }
}
