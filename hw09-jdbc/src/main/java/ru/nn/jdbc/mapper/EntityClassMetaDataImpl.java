package ru.nn.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> paramClass;

    public EntityClassMetaDataImpl(Class<T> tClass) {
        this.paramClass = tClass;
    }

    @Override
    public String getName() {
        return paramClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return paramClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(paramClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new RuntimeException("There is not field with Id annotation"));
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(paramClass.getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(paramClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .toList();
    }
}
