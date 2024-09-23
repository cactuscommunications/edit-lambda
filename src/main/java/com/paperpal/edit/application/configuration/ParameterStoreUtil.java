package com.paperpal.edit.application.configuration;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.paperpal.edit.citation.CitationCacheCleaner;

import java.util.logging.Logger;

public class ParameterStoreUtil {
    private static final Logger logger = Logger.getLogger(CitationCacheCleaner.class.getName());

    private static final String PASSWORD_PARAMETER_PATH = "/config/service-edit/spring/datasource/password";
    private static final String USERNAME_PARAMETER_PATH = "/config/service-edit/spring/datasource/username";

    public static String getParameter(String name) {
        logger.info("fetching parameter value");
        AWSSimpleSystemsManagement ssmClient = AWSSimpleSystemsManagementClientBuilder.standard().build();
        GetParameterRequest request = new GetParameterRequest().withName(name).withWithDecryption(true);
        GetParameterResult result = ssmClient.getParameter(request);
        return result.getParameter().getValue();
    }

    public static String getDbUser() {
        return getParameter(USERNAME_PARAMETER_PATH);

    }

    public static String getDbPassword() {
        return getParameter(PASSWORD_PARAMETER_PATH);

    }
}
