package Utils.Enums;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public final class ObjectUtils {
    public static <T> T tableMapToClass(Map<String, String> tableMap, Class<T> objClass) {
        T instance = null;
        try {
            instance = objClass.newInstance();
            return tableMapToClass(tableMap, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(instance, String.format("Failed instantiate class=%s. Check that default parameter should exist", objClass));
    }


    public static <T> T tableMapToClass(Map<String, String> tableMap, T instance) {
        Class<?> someClass = instance.getClass();
        for (String columnName : tableMap.keySet()) {
            String nameBody = columnName.toLowerCase().replaceAll(" ", "");
            //check if setMethod present
            Optional<Method> setMethodOptional = Arrays.stream(someClass.getMethods())
                    .filter(method -> {
                        String name = method.getName().toLowerCase();
                        boolean startsWithSet = name.startsWith("set");
                        name = name.replaceAll("^set", "");
                        return name.equals(nameBody) && startsWithSet;
                    })
                    .filter(method -> method.getParameterTypes()[0].equals(String.class))
                    .findFirst();
            String value = UtilsTest.defaultIfEmpty(tableMap.get(columnName), null);
            if (setMethodOptional.isPresent() && value != null) {
                //if setMethod present invoke it
                Method setMethod = setMethodOptional.get();
                try {
                    try {
                        setMethod.invoke(instance, value);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                //check if field with such name present
                Optional<Field> fieldOptional = Arrays.stream(someClass.getFields())
                        .filter(field -> {
                            String name = field.getName().toLowerCase();
                            return name.equals(nameBody);
                        })
                        .findFirst();
                if (!fieldOptional.isPresent())
                    continue;

                Field field = fieldOptional.get();
                //skip if field has wrong type
                if (!field.getType().getTypeName().equals(String.class.getTypeName()))
                    continue;

                field.setAccessible(true);
                try {
                    field.set(instance, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return instance;
    }

    /**
     * Expand initial object's properties like: String name, String email, with additional object of the same type,
     * if they are not set, but expanded obj has this props.
     *
     * @param initialObj
     * @param expandObj
     * @param <T>
     * @return
     */
    public static <T> T expandObjectProperties(T initialObj, T expandObj) {
        List<Field> expandFields = fields(expandObj);

        for (Field expandField : expandFields) {
            expandField.setAccessible(true);
            try {
                Object expandProperty = expandField.get(expandObj);
                Optional<Field> initialOptionalField = FunctionalUtils.filterOneOptional(fields(initialObj), expandField::equals);

                if (initialOptionalField.isPresent()) {
                    Field initialField = initialOptionalField.get();
                    initialField.setAccessible(true);

                    Object initialProperty = initialField.get(initialObj);
                    if (initialProperty == null || String.valueOf(initialProperty).isEmpty()
                            || (UtilsTest.hasDigit(initialProperty) && UtilsTest.isZero(initialProperty))) {
                        initialField.set(initialObj, expandProperty);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return initialObj;
    }

    private static <T> List<Field> fields(T obj) {
        List<Field> expandFields = new ArrayList<>();
        expandFields.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
        expandFields.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
        return expandFields;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericClass(Object genericObj) {
        return (Class<T>) ((ParameterizedType) genericObj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public static String createToString(Object... properties) {
        return "[" + Arrays.stream(properties)
                .map(data -> Optional.ofNullable(data).orElse(""))
                .map(String::valueOf)
                .filter(UtilsTest::nonBlank)
                .collect(Collectors.joining("/")) + "]";
    }
}
