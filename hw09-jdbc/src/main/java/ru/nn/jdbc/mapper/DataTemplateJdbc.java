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

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createObject(rs);
                }
                return null;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
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
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            List<Object> fieldValues = getValuesOfObjectFields(object);
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), fieldValues);
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
}
