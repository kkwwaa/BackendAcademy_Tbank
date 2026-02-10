package academy.services;

import academy.config.ExitCode;
import academy.exception.ClassInspectorException;
import academy.exception.ErrorMessages;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ObjectCreatorService {

    private static final int MAX_DEPTH = 3;
    private static final int MAX_COLLECTION_SIZE = 5;
    private static final Random RANDOM = new Random();

    private static final int MIN_STRING_LENGTH = 5;
    private static final int MAX_STRING_EXTRA = 10;
    private static final int ALPHABET_SIZE = 26;
    private static final char FIRST_LOWERCASE_CHAR = 'a';

    public <T> T create(Class<T> clazz) {
        return create(clazz, 0, new IdentityHashMap<>());
    }

    @SuppressWarnings("unchecked")
    private <T> T create(Class<T> clazz, int depth, Map<Class<?>, Object> cache) {

        if (depth > MAX_DEPTH) {
            return null;
        }

        // предотвращаем циклы
        if (cache.containsKey(clazz)) {
            return (T) cache.get(clazz);
        }

        // примитивы и обёртки
        if (clazz.isPrimitive() || isWrapper(clazz)) {
            return createPrimitive(clazz);
        }

        // строки
        if (clazz == String.class) {
            return (T) randomString();
        }

        // enum
        if (clazz.isEnum()) {
            return createEnum(clazz);
        }

        // массивы
        if (clazz.isArray()) {
            return createArray(clazz, depth, cache);
        }

        // коллекции
        if (Collection.class.isAssignableFrom(clazz)) {
            return (T) createCollection(clazz);
        }

        // Map
        if (Map.class.isAssignableFrom(clazz)) {
            return (T) createMap();
        }

        // даты
        if (clazz == Date.class) return (T) new Date();
        if (clazz == LocalDate.class) return (T) LocalDate.now();
        if (clazz == LocalDateTime.class) return (T) LocalDateTime.now();

        // обычный объект
        return createObject(clazz, depth, cache);
    }

    @SuppressWarnings("unchecked")
    private <T> T createPrimitive(Class<T> clazz) {
        if (clazz == boolean.class || clazz == Boolean.class) return (T) Boolean.valueOf(RANDOM.nextBoolean());
        if (clazz == byte.class || clazz == Byte.class) return (T) Byte.valueOf((byte) RANDOM.nextInt());
        if (clazz == short.class || clazz == Short.class) return (T) Short.valueOf((short) RANDOM.nextInt());
        if (clazz == int.class || clazz == Integer.class) return (T) Integer.valueOf(RANDOM.nextInt(1000));
        if (clazz == long.class || clazz == Long.class) return (T) Long.valueOf(RANDOM.nextLong());
        if (clazz == float.class || clazz == Float.class) return (T) Float.valueOf(RANDOM.nextFloat());
        if (clazz == double.class || clazz == Double.class) return (T) Double.valueOf(RANDOM.nextDouble());
        if (clazz == char.class || clazz == Character.class)
            return (T) Character.valueOf((char) (FIRST_LOWERCASE_CHAR + RANDOM.nextInt(ALPHABET_SIZE)));
        return null;
    }

    private boolean isWrapper(Class<?> clazz) {
        return clazz == Boolean.class
                || clazz == Byte.class
                || clazz == Short.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Float.class
                || clazz == Double.class
                || clazz == Character.class;
    }

    private String randomString() {
        int len = RANDOM.nextInt(MAX_STRING_EXTRA) + MIN_STRING_LENGTH;
        StringBuilder string = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            string.append((char) (FIRST_LOWERCASE_CHAR + RANDOM.nextInt(ALPHABET_SIZE)));
        }
        return string.toString();
    }

    private <T> T createEnum(Class<T> clazz) {
        T[] constants = clazz.getEnumConstants();
        if (constants == null || constants.length == 0) return null;
        return constants[RANDOM.nextInt(constants.length)];
    }

    @SuppressWarnings("unchecked")
    private <T> T createArray(Class<T> clazz, int depth, Map<Class<?>, Object> cache) {
        Class<?> component = clazz.getComponentType();
        int length = RANDOM.nextInt(MAX_COLLECTION_SIZE) + 1;

        Object array = Array.newInstance(component, length);

        for (int index = 0; index < length; index++) {
            Array.set(array, index, create(component, depth + 1, cache));
        }

        return (T) array;
    }

    private Collection<Object> createCollection(Class<?> clazz) {
        Collection<Object> collection;

        if (clazz.isInterface()) {
            if (List.class.isAssignableFrom(clazz)) collection = new ArrayList<>();
            else if (Set.class.isAssignableFrom(clazz)) collection = new HashSet<>();
            else collection = new ArrayList<>();
        } else {
            try {
                collection = (Collection<Object>) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception exception) {
                collection = new ArrayList<>();
            }
        }

        int size = RANDOM.nextInt(MAX_COLLECTION_SIZE) + 1;
        for (int i = 0; i < size; i++) {
            collection.add(randomString());
        }

        return collection;
    }

    private Map<Object, Object> createMap() {
        Map<Object, Object> map = new HashMap<>();
        int size = RANDOM.nextInt(MAX_COLLECTION_SIZE) + 1;

        for (int number = 0; number < size; number++) {
            map.put("key" + number, randomString());
        }

        return map;
    }

    private <T> T createObject(Class<T> clazz, int depth, Map<Class<?>, Object> cache) {
        try {
            Constructor<T> constructor = findConstructor(clazz);
            Object[] params = createConstructorParams(constructor, depth, cache);

            T instance = constructor.newInstance(params);

            if (depth == 0) {
                cache.put(clazz, instance);
            }

            populateFields(instance, depth, cache);

            return instance;

        } catch (ReflectiveOperationException exception) {
            throw new ClassInspectorException(
                    ErrorMessages.CREATE_OBJECT_ERROR + clazz.getName(), ExitCode.INVALID_USAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> findConstructor(Class<T> clazz) throws NoSuchMethodException {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException exception) {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            if (constructors.length == 0) throw exception;

            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);
            return (Constructor<T>) constructor;
        }
    }

    private Object[] createConstructorParams(Constructor<?> constructor, int depth, Map<Class<?>, Object> cache) {
        Class<?>[] types = constructor.getParameterTypes();
        Object[] params = new Object[types.length];

        for (int i = 0; i < types.length; i++) {
            params[i] = create(types[i], depth + 1, cache);
        }

        return params;
    }

    private void populateFields(Object instance, int depth, Map<Class<?>, Object> cache) {
        Class<?> clazz = instance.getClass();

        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {

                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    Object value = create(field.getType(), depth + 1, cache);
                    field.set(instance, value);
                } catch (Exception ignored) {
                    // intentionally ignored — поле невозможно установить, пропускаем
                }
            }

            clazz = clazz.getSuperclass();
        }
    }
}
