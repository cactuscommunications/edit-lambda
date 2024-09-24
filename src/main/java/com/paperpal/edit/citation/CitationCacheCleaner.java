package com.paperpal.edit.citation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.paperpal.edit.application.configuration.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class CitationCacheCleaner implements RequestHandler<Object, String> {

    private static final Logger logger = Logger.getLogger(CitationCacheCleaner.class.getName());
    private static final String DB_URL = "DB_URL";

    @Override
    public String handleRequest(Object o, Context context) {
        logger.info("Stating cleaning cache");
        try (Connection conn = DatabaseConfiguration.getConnection(System.getenv(DB_URL))) {
            // Fetch records from the last 15 minutes
            String selectQuery = "SELECT * FROM citation_style_cache where created_at < ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusMinutes(15)));
                ResultSet rs = selectStmt.executeQuery();

                // Collect IDs of records to delete
                StringBuilder idsToDelete = new StringBuilder();
                while (rs.next()) {
                    if (idsToDelete.length() > 0) {
                        idsToDelete.append(",");
                    }
                    idsToDelete.append(rs.getLong("id"));
                }

                // Delete records
                if (idsToDelete.length() > 0) {
                    String deleteQuery = "DELETE FROM citation_style_cache WHERE id IN (" + idsToDelete + ");";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                        int rowsAffected = deleteStmt.executeUpdate();
                        logger.info("Deleted " + rowsAffected + " records.");
                    }
                } else {
                    logger.info("No records found to delete.");
                }
            }
        } catch (SQLException e) {
            logger.info("Database error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return "Data cleanup completed successfully.";
    }
}