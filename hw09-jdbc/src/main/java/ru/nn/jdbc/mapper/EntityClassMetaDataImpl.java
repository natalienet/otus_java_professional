package ru.nn.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> paramClass;
    private String className;
    private Constructor<T> constructor;
    private Field idField;
    private List<Field> allFields;
    private List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> tClass) {
        this.paramClass = tClass;
    }

    @Override
    public String getName() {
        if (className == null) {
            className = paramClass.getSimpleName();
        }
        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        if (constructor == null) {
            try {
                constructor = paramClass.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            idField = Arrays.stream(paramClass.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst().orElseThrow(() -> new RuntimeException("There is not field with Id annotation"));
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (allFields == null) {
            allFields = Arrays.stream(paramClass.getDeclaredFields()).toList();
        }
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = Arrays.stream(paramClass.getDeclaredFields())
                    .filter(f -> !f.isAnnotationPresent(Id.class))
                    .toList();
        }
        return fieldsWithoutId;
    }
}
