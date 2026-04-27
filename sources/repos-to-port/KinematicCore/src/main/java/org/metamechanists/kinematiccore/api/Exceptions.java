package org.metamechanists.kinematiccore.api;

import java.util.Set;


@SuppressWarnings("EmptyClass")
public class Exceptions {
    public static class EntityMissingConstructorException extends RuntimeException {
        public EntityMissingConstructorException(String id) {
            super(id + " must implement a deserialization constructor, in the form: public SomeEntity(StateReader reader, Entity entity) { ... }");
        }
    }

    public static class BlockMissingConstructorException extends RuntimeException {
        public BlockMissingConstructorException(String id) {
            super(id + " must implement a deserialization constructor, in the form: public SomeBlock(StateReader reader) { ... }");
        }
    }

    public static class IdConflictException extends RuntimeException {
        public IdConflictException(String id) {
            super(id + " is already registered");
        }
    }

    public static class ValueNotFoundException extends RuntimeException {
        public ValueNotFoundException(String key, Set<String> available) {
            super("No value found at " + key + "; available values are: " + String.join(" ", available));
        }
    }

    public static class NotRegisteredException extends RuntimeException {
        public NotRegisteredException(String id) {
            super(id + " has not been registered");
        }
    }

    public static class ValueWrongTypeException extends RuntimeException {
        public ValueWrongTypeException(String key, String expected, String actual) {
            super("Expected value at " + key + " to be " + expected + " but got " + actual);
        }
    }
}
