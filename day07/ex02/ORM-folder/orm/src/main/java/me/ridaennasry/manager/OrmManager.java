
package me.ridaennasry.orm.manager;

import java.sql.Connection;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import me.ridaennasry.orm.annotations.OrmEntity;
import me.ridaennasry.orm.annotations.OrmColumn;
import me.ridaennasry.orm.annotations.OrmColumnId;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;

public class OrmManager {
    HikariDataSource ds = new HikariDataSource();
    Connection connection = null;

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignored
            }
        }
        if (ds != null) {
            ds.close();
        }
    }

    private String convertJavaTypeToSqlType(Class<?> javaType) {
        switch (javaType.getSimpleName()) {
            case "String":
                return "VARCHAR";
            case "Integer":
                return "INTEGER";
            case "Double":
                return "BIGINT";
            case "Boolean":
                return "BOOLEAN";
            case "Long":
                return "BIGINT";
            default:
                return "VARCHAR";
        }
    }

    public void initSchema(Class<?>... classes) throws SQLException {


        for (Class<?> clazz : classes) {
            // drop table if exists
            if (clazz.isAnnotationPresent(OrmEntity.class)) {
                OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
                String tableName = ormEntity.table();
                dropTable(tableName);
            }
            
            // if annotated with @OrmEntity
            if (clazz.isAnnotationPresent(OrmEntity.class)) {
                OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
                String tableName = ormEntity.table();
                // storing the colmins in arraylist
                List<String> columns = new ArrayList<>();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {

                    if (field.isAnnotationPresent(OrmColumn.class) || field.isAnnotationPresent(OrmColumnId.class)) {
                        // meaning ColumnId is the one present, we get the name from class attribute
                        // itself
                        String columnDefinition = null;
                        String columnName = field.getName();
                        String columnType = convertJavaTypeToSqlType(field.getType());
                        int columnLength = 0;
                        if (field.isAnnotationPresent(OrmColumn.class)) {
                            OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                            if (!ormColumn.name().isEmpty()) {
                                columnName = ormColumn.name();
                            }
                            // if column type is varchar, we get the length from ormColumn
                            if (columnType.equals("VARCHAR")) {
                                columnLength = ormColumn.length();
                            }
                            columnDefinition = columnName + " " + columnType;
                        }
                        // if we have a length/varchar, we add it to the column definition
                        if (columnLength > 0)
                            columnDefinition += "(" + columnLength + ")";
                        // else if columnId is the one present
                        if (field.isAnnotationPresent(OrmColumnId.class)) {
                            columnDefinition = columnName + " SERIAL PRIMARY KEY";
                            // columnDefinition += " SERIAL PRIMARY KEY";
                        }
                        columns.add(columnDefinition);
                    }
                }
                createTable(tableName, columns);
            }
        }
    }

    // a method to create tables
    private void createTable(String tableName, List<String> columns) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
        for (int i = 0; i < columns.size(); i++) {
            sql += columns.get(i);
            if (i < columns.size() - 1) {
                sql += ", ";
            }
        }
        sql += ");";
        System.out.println("--------------------");
        System.out.println("Executing SQL: " + sql);
        try {
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            System.out.println("Error executing SQL: " + sql);
            throw e;
        }
    }

    // a method to drop tables in initialization
    private void dropTable(String tableName) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + tableName + ";";
        System.out.println("--------------------");
        System.out.println("Executing SQL: " + sql);
        try {
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            System.out.println("Error executing SQL: " + sql);
            throw e;
        }
    }

    public OrmManager(String url, String userName, String password) throws SQLException {
        ds.setJdbcUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        connection = ds.getConnection();
        if (connection == null) {
            throw new SQLException("Connection was null");
        }
    }

    private Long executeQuery(String sql, List<Object> values) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < values.size(); i++) {
            preparedStatement.setObject(i + 1, values.get(i));
        }
        System.out.println("--------------------");
        System.out.println("Executing SQL: " + sql);
        for (int i = 0; i < values.size(); i++) {
            System.out.println("Param " + (i + 1) + ": " + values.get(i));
        }

        // execute and return the result
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getLong(1);
        }
        return null;
    }

    public void save(Object o) throws Exception {
        Class<?> clazz = o.getClass();
        if (clazz.isAnnotationPresent(OrmEntity.class)) {
            OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
            String tableName = ormEntity.table();
            List<String> columnNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // if annotated with @OrmColumn
                if (field.isAnnotationPresent(OrmColumn.class)) {
                    String columnName = field.getName();
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    if (!ormColumn.name().isEmpty()) {
                        columnName = ormColumn.name();
                    }
                    columnNames.add(columnName);
                    field.setAccessible(true);
                    Object value = field.get(o);
                    values.add(value);
                }
            }
            String columnNamesString = String.join(", ", columnNames);
            String sql = "INSERT INTO " + tableName + " (" + columnNamesString + ") VALUES (";
            for (int i = 0; i < values.size(); i++) {
                sql += "?";
                if (i < values.size() - 1) {
                    sql += ", ";
                }
            }
            sql += ");";
            Long id = executeQuery(sql, values);
            System.out.println("Generated id: " + id);
            if (id != null) {
                Field[] fields2 = clazz.getDeclaredFields();
                for (Field field : fields2) {
                    if (field.isAnnotationPresent(OrmColumnId.class)) {
                        field.setAccessible(true);
                        field.set(o, id);
                    }
                }
            }

        }
    }

    public void update(Object o) throws SQLException, IllegalAccessException {
        Class<?> clazz = o.getClass();
        if (clazz.isAnnotationPresent(OrmEntity.class)) {
            OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
            String tableName = ormEntity.table();
            List<String> columnNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            Object id = null;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(OrmColumn.class)
                        || field.isAnnotationPresent(OrmColumnId.class)) {
                    String columnName = field.getName();
                    if (field.isAnnotationPresent(OrmColumn.class)) {
                        OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                        if (!ormColumn.name().isEmpty()) {
                            columnName = ormColumn.name();
                        }
                    }
                    field.setAccessible(true);
                    Object value = field.get(o);
                    if (field.isAnnotationPresent(OrmColumnId.class)) {
                        // get id from database
                        id = value;
                    } else {
                        columnNames.add(columnName);
                        values.add(value);
                    }
                }
            }

            List<String> columnValuePairs = new ArrayList<>();
            for (int i = 0; i < columnNames.size(); i++) {
                columnValuePairs.add(columnNames.get(i) + " = ?");
            }
            String columnValuePairsString = String.join(", ", columnValuePairs);
            String sql = "UPDATE " + tableName + " SET " + columnValuePairsString + " WHERE id = ?;";
            values.add(id);
            executeQuery(sql, values);
        }
    }

    public <T> T findById(Long id, Class<T> c) throws Exception {
        if (c.isAnnotationPresent(OrmEntity.class)) {
            OrmEntity ormEntity = c.getAnnotation(OrmEntity.class);
            String tableName = ormEntity.table();
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            System.out.println("--------------------");
            System.out.println("Executing SQL: " + sql);
            System.out.println("Param 1: " + id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                T object = c.getDeclaredConstructor().newInstance();
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(OrmColumn.class) || field.isAnnotationPresent(OrmColumnId.class)) {
                        String columnName = field.getName();
                        if (field.isAnnotationPresent(OrmColumn.class)) {
                            OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                            if (!ormColumn.name().isEmpty()) {
                                columnName = ormColumn.name();
                            }
                        }
                        Object value = resultSet.getObject(columnName);
                        field.setAccessible(true);
                        // if its id convert to long
                        if (field.isAnnotationPresent(OrmColumnId.class)) {
                            value = Long.valueOf(value.toString());
                        }
                        field.set(object, value);
                    }
                }
                return object;
            }
        }
        return null;
    }

}
