package com.paperpal.edit.application.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {

    public static Connection getConnection(String dbUrl) throws SQLException {
        return DriverManager.getConnection(dbUrl,
                ParameterStoreUtil.getDbUser(), ParameterStoreUtil.getDbPassword());
    }

}
