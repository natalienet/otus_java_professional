package ru.nn.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }
    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?",
                entityClassMetaData.getName().toLowerCase(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        String fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName).collect(Collectors.joining(", "));
        return String.format("insert into %s(%s) values(?)",
                entityClassMetaData.getName().toLowerCase(), fields);
    }

    @Override
    public String getUpdateSql() {
        String fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> f.getName() + " = ?").collect(Collectors.joining(", "));
        return String.format("update %s set %s where %s = ?",
                entityClassMetaData.getName().toLowerCase(), fields, entityClassMetaData.getIdField().getName());
    }
}
