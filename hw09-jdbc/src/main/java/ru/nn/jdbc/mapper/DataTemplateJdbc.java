package ru.nn.jdbc.mapper;

import ru.nn.core.repository.DataTemplate;
import ru.nn.core.repository.DataTemplateException;
import ru.nn.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    private final Map<Long, T> cache;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
        cache = new HashMap<>();
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        if (cache.containsKey(id)) {
            return Optional.of(cache.get(id));
        }

        Optional<T> object = dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createObject(rs);
                }
                return null;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });

        object.ifPresent(t -> cache.put(id, t));

        return object;
    }

    @Override
    public List<T> findAll(Connection connection) {
        List<T> objects = dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var objectList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            T object = createObject(rs);
                            objectList.add(object);
                        }
                        return objectList;
                    } catch (SQLException | InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));

        try {
            cache.clear();
            for (T object : objects) {
                long id = getObjectId(object);
                cache.put(id, object);
            }
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
        return objects;
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            List<Object> fieldValues = getValuesOfObjectFields(object);
            long objectId = dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), fieldValues);

            cache.put(objectId, object);
            return objectId;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            List<Object> fieldValues = getValuesOfObjectFields(object);
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), fieldValues);

            long objectId = getObjectId(object);
            if (cache.containsKey(objectId)) {
                cache.put(objectId, object);
            }
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createObject(ResultSet rs) throws InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        T object = entityClassMetaData.getConstructor().newInstance();
        List<Field> fields = entityClassMetaData.getAllFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(object, rs.getObject(field.getName()));
        }
        return object;
    }

    private List<Object> getValuesOfObjectFields(T object) throws IllegalAccessException {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        List<Object> fieldValues = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            fieldValues.add(field.get(object));
        }
        return fieldValues;
    }

    private long getObjectId(T object) throws IllegalAccessException {
        return (long) entityClassMetaData.getIdField().get(object);
    }
}
