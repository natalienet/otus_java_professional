package ru.nn.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> paramClass;

    public EntityClassMetaDataImpl() {
        ParameterizedType type = (ParameterizedType) EntityClassMetaDataImpl.class.getGenericSuperclass();
        paramClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public String getName() {
        return paramClass.getName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return paramClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        //try {
//            return (Constructor<T>) Arrays.stream(paramClass.getDeclaredConstructors())
//                    .max(Comparator.comparing(Constructor::getParameterCount)).get();
                    //.orElseThrow(() -> new RuntimeException());
                    //.findFirst().orElseThrow(() -> new RuntimeException());
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(paramClass.getFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new RuntimeException("There is not field with Id annotation"));
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(paramClass.getFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(paramClass.getFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .toList();
    }
}
