package ru.nn.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.nn.core.repository.DataTemplate;
import ru.nn.core.repository.DataTemplateException;
import ru.nn.core.repository.executor.DbExecutor;
import ru.nn.crm.model.Client;

/** Сохратяет объект в базу, читает объект из базы */
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
                    T obj = entityClassMetaData.getConstructor().newInstance();
                    List<Field> fields = entityClassMetaData.getAllFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        field.set(obj, rs.getObject(field.getName()));
                    }
                    return obj;
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
                    var tList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            T obj = entityClassMetaData.getConstructor().newInstance();
                            List<Field> fields = entityClassMetaData.getAllFields();
                            for (Field field : fields) {
                                field.setAccessible(true);
                                field.set(obj, rs.getObject(field.getName()));
                            }
                            tList.add(obj);
                            //tList.add(new T(rs.getLong("id"), rs.getString("name")));
                        }
                        return tList;
                    } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Field> fields = entityClassMetaData.getFieldsWithoutId();
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : fields) {
                fieldValues.add(field.get(client));
            }
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Field> fields = entityClassMetaData.getFieldsWithoutId();
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : fields) {
                fieldValues.add(field.get(client));
            }
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
