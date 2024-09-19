package com.paperpal.edit.application.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ParameterStoreUtil.getDbUrl(),
                ParameterStoreUtil.getDbUser(), ParameterStoreUtil.getDbPassword());
    }

}
