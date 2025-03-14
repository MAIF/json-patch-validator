# json-patch-validator [![License ASL 2.0][ASL 2.0 badge]][ASL 2.0] [![Build Status][github-action-badge]][github-action] [![Maven Central][Maven Central badge]][Maven]

A Validator, allowing to declare the permitted operations, for a json-patch.


### Compatibility

| Library Version            | Java Version | Json-patch Dependencies                      
|----------------------------|--------------|--------------------------------------------|  
| 1.0.0 and later | 17+          | com.gravity9:json-patch-path:2.0.2 |

> This library use Jakarta Validation, it doesn't support older javax.validation.

## Technical Dependencies of the Library

### Runtime Dependencies

- [com.gravity9:json-patch-path] : for Json Patch
- jakarta.validation:jakarta.validation-api : as validation api

### Test Dependencies 

- org.junit.jupiter:junit-jupiter-api : test framework
- org.hibernate.validator:hibernate-validator : validation-api hibernate implementation for test

## Usage

### Installation

```xml
<dependency>
    <groupId>fr.maif.json.validation</groupId>
    <artifactId>json-patch-validator</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Usage Example

#### Declaration of a DTO for Json Patch with allowed operations

```java

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.fge.jsonpatch.JsonPatchOperation;

import fr.maif.json.patch.JsonPatchOperationWrapper;
import fr.maif.json.validation.Operation;
import fr.maif.json.validation.PermittedOperations;

@PermittedOperations(
        value = {
                @Operation(name = "add", path = "/test"),
                @Operation(name = "remove", path = "/test"),
                @Operation(name = "replace", path = "/element"),
        }
)
public class EntityPatchOperationsDto extends JsonPatchOperationWrapper {

    @JsonCreator
    public EntityPatchOperationsDto(final List<JsonPatchOperation> ops) {
        super(ops);
    }
}
```

This will create a Class, with a List of JsonPatch Operation, with the permitted operations.
This class allow,
`add` operation on the `test` field, 
`remove` operations on the `test` field
and `replace` operations on the `element` field.

Validation with an operation other than the above in the operation list, will result in a ConstraintViolation. 

#### Controller Usage Example

```java

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
public class EntityController {

    private final EntityService entityService;
    private final Validator validator;

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Object> patch(
            @PathVariable("id") String id,
            @RequestBody @Valid EntityPatchOperationsDto patchOperation) {
        // Read the original entity to patch
        Object original = entityService.getEntity(id);

        // Patch the entity
        Object patched = patch.apply(patchOperation.asJsonPatch());

        // Optionally validate the patched entity
        Set<ConstraintViolation<Object>> violations = validator.validate(patched);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // Save the patched entity
        entityService.save(patched);
        
        // Return the patched entity
        return ResponseEntity.ok(patched).build();
    }
}
```

[ASL 2.0 badge]: https://img.shields.io/:license-Apache%202.0-blue.svg
[ASL 2.0]: http://www.apache.org/licenses/LICENSE-2.0.html
[github-action-badge]: https://github.com/MAIF/json-patch-validator/actions/workflows/build.yml/badge.svg?branch=main
[github-action]: https://github.com/MAIF/json-patch-validator/actions?query=workflow%3ABuild
[Maven Central badge]: https://img.shields.io/maven-central/v/fr.maif.json.validation/json-patch-validator.svg
[Maven]: https://search.maven.org/artifact/fr.maif.json.validation/json-patch-validator
[com.gravity9:json-patch-path]: https://github.com/gravity9-tech/json-patch-path