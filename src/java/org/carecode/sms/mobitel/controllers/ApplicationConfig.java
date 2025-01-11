package org.carecode.sms.mobitel.controllers;

import jakarta.ws.rs.core.Application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Dr M H B Ariyaratne <buddhika.ari@gmail.com>
 */
@jakarta.ws.rs.ApplicationPath("ws")
public class ApplicationConfig extends Application {
    private static final Logger logger = Logger.getLogger(ApplicationConfig.class.getName());

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> props = super.getProperties();
        loadPropertiesFromFile();

        return props;
    }

    private void loadPropertiesFromFile() {
        Properties props = System.getProperties();

        final String configFilePath = "config.properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            props.load(inputStream);
        } catch (IOException e) {
            logger.severe("Error loading config.properties: " + e.getMessage());
        }
    }
}
